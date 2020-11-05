using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace CountStringFromWeb
{
    public class SecureWebContentReader : ISecureWebContentReader
    {
        public string WebsiteDomain { get; }
        public int Port { get; }
        public string _content = null;

        public string Content
        {
            get
            {
                return _content ?? GetHttpsContent();
            }
            set
            {
                _content = value;
            }
        }

        private readonly ITcpClient _tcpClient;

        public SecureWebContentReader(ITcpClient tcpClient, string websiteDomain, int port = 443)
            : this(websiteDomain, port)
        {
            this._tcpClient = tcpClient;
        }

        public SecureWebContentReader(string websiteDomain, int port = 443)
        {
            this.WebsiteDomain = websiteDomain;
            this.Port = port;
            this._tcpClient = new TcpClient();
        }

        public string GetHttpsContent()
        {
            if (!string.IsNullOrWhiteSpace(_content))
            {
                return Content;
            }

            _tcpClient.Connect(this.WebsiteDomain, this.Port);
            using StreamWriter streamWriter = new StreamWriter(_tcpClient.GetSslStreamWrite(this.WebsiteDomain)) { AutoFlush = true };
            using StreamReader streamReader = new StreamReader(_tcpClient.GetSslStreamRead(this.WebsiteDomain));

            WriteHttpGetRequest(streamWriter, this.WebsiteDomain);
            ReadHttpHeader(streamReader, out var contentLength);
            return Content = ReadHttpBody(streamReader, contentLength);
        }

        protected static void WriteHttpGetRequest(StreamWriter streamWriter, string website)
        {
            streamWriter.WriteLine("GET / HTTP/1.1");
            streamWriter.WriteLine("Host: " + website);
            streamWriter.WriteLine("Accept: */*");
            streamWriter.WriteLine("");
        }

        protected static void ReadHttpHeader(StreamReader streamReader, out int contentLength)
        {
            string line;
            contentLength = 0;
            while (!string.IsNullOrWhiteSpace(line = streamReader.ReadLine()))
            {
                if (line.ToLower().StartsWith("content-length:"))
                {
                    contentLength = int.Parse(line.Substring(15).Trim());
                }
            }
        }

        protected static string ReadHttpBody(StreamReader streamReader, int contentLength)
        {
            StringBuilder contentStringBuilder = new StringBuilder(10000);
            char[] buffer = new char[1024];
            int bytesReadTotal = 0;
            while (bytesReadTotal < contentLength)
            {
                int bytesRead = streamReader.Read(buffer, 0, 1024);
                bytesReadTotal += bytesRead;
                if (bytesRead == 0)
                {
                    break;
                }
                contentStringBuilder.Append(buffer, 0, bytesRead);
            }

            return contentStringBuilder.ToString();
        }

        public IList<string> GetContentStringsFromRegex(string pattern, int groupNr = 1)
        {
            // tested with https://regex101.com/
            Regex regex = new Regex(pattern, RegexOptions.IgnoreCase | RegexOptions.Singleline);
            var matches = regex.Matches(this.GetHttpsContent());
            
            var results = new List<string>();
            foreach (Match match in matches)
            {
                results.Add(match.Groups[groupNr].ToString().Trim());
            }
            return results;

            // return matches.Select(match => match.Groups[groupNr].ToString().Trim()).ToList();
        }
    }
}
