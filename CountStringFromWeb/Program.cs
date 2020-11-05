using System;
using System.Collections.Concurrent;
using System.IO;
using System.Net;
using System.Net.Security;
using System.Net.Sockets;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Text.RegularExpressions;

namespace CountStringFromWeb
{
    class Program
    {
        static void Main()
        {
            SecureWebContentReader client = new SecureWebContentReader("sport.orf.at");
            var results = client.GetContentStringsFromRegex(
                pattern: "ticker-story-headline.*?a href.*?>(.*?)<\\/a>");

            Console.WriteLine($"Pattern found: {results.Count}x");
            Console.WriteLine("_____________________________________________");
            Console.WriteLine();
            foreach (string result in results)
            {
                Console.WriteLine(result);
            }
            Console.WriteLine("_____________________________________________");
            Console.WriteLine();
            Console.WriteLine();
        }
    }
}

