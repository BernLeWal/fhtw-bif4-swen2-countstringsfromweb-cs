using System.Collections.Generic;

namespace CountStringFromWeb
{
    public interface ISecureWebContentReader
    {
        string WebsiteDomain { get; }
        int Port { get; }

        string GetHttpsContent();
        IList<string> GetContentStringsFromRegex(string pattern, int groupNr = 1);
    }
}
