using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;

namespace CountStringFromWeb.WebClient
{
    class Program
    {
        static void Main(string[] args)
        {
            new Regex("ticker-story-headline.*?a href.*?>(.*?)<\\/a>", RegexOptions.IgnoreCase | RegexOptions.Singleline)
                .Matches(new System.Net.WebClient().DownloadString("https://sport.orf.at"))
                .Select(match => match.Groups[1].ToString().Trim())
                .ToList()
                .ForEach(Console.WriteLine);
        }
    }
}
