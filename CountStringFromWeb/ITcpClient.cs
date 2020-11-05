using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace CountStringFromWeb
{
    public interface ITcpClient : IDisposable
    {
        Stream GetStreamRead();
        Stream GetStreamWrite();
        Stream GetSslStreamRead(string targetHost);
        Stream GetSslStreamWrite(string targetHost);
        void Connect(string hostname, int port);
    }
}
