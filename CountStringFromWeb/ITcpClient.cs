using System;
using System.IO;

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
