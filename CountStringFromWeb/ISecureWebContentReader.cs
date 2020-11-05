using System;
using System.Collections.Generic;
using System.Text;

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
