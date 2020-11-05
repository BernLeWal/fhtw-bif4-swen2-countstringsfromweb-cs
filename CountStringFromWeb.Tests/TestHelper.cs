using Moq;
using System.IO;

namespace CountStringFromWeb.Tests {
    public class TestHelper {

        public static string Url = "www.example.com";

        public static Mock<ITcpClient> GenerateSimpleTcpClientMock() {
            // set assembly reference
            var tcpClient = new Mock<ITcpClient>();

            var readStream = new MemoryStream();
            var writer = new StreamWriter(readStream) { AutoFlush = true };
            writer.Write(
                @"HTTP/1.1 200 OK
Content-Length: 10

1234567890");
            readStream.Position = 0;
            var writeStream = new MemoryStream();

            // mock the TCP client methods
            tcpClient
                .Setup(c => c.GetSslStreamRead(It.IsAny<string>()))
                .Returns(readStream);
            tcpClient
                .Setup(c => c.GetSslStreamWrite(It.IsAny<string>()))
                .Returns(writeStream);
            tcpClient
                .Setup(c => c.GetStreamRead())
                .Returns(readStream);
            tcpClient
                .Setup(c => c.GetStreamWrite())
                .Returns(writeStream);

            return tcpClient;
        }
    }
}
