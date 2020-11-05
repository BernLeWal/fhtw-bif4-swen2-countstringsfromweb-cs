using System.IO;
using System.Net.Security;

namespace CountStringFromWeb
{
    public class TcpClient : ITcpClient
    {
        // The real implementation
        private readonly System.Net.Sockets.TcpClient _client;

        private SslStream _sslStream = null;

        public Stream GetSslStreamRead(string targetHost)
        {
            if (_sslStream != null)
            {
                return _sslStream;
            }

            _sslStream = new SslStream(this.GetStreamRead());
            _sslStream.AuthenticateAsClient(targetHost);
            return _sslStream;
        }

        public Stream GetSslStreamWrite(string targetHost) => GetSslStreamRead(targetHost);

        public TcpClient()
        {
            _client = new System.Net.Sockets.TcpClient();
        }

        public TcpClient(System.Net.Sockets.TcpClient client)
        {
            _client = client;
        }

        public Stream GetStreamRead() => _client.GetStream();
        public Stream GetStreamWrite() => _client.GetStream();
        public void Connect(string hostname, int port) => _client.Connect(hostname, port);
        
        public void Dispose()
        {
            _sslStream.Dispose();
            _client.Dispose();
        }
    }
}
