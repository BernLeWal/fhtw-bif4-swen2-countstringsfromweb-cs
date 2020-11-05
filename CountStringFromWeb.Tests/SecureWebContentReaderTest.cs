using System.IO;
using System.Linq;
using Moq;
using NUnit.Framework;

namespace CountStringFromWeb.Tests
{
    [TestFixture]
    public class SecureWebContentReaderTest
    {
        public static Mock<ITcpClient> GenerateSimpleTcpClientMock()
        {
            // set assembly reference
            var tcpClient = new Mock<ITcpClient>();

            var readStream= new MemoryStream();
            var writer = new StreamWriter(readStream) {AutoFlush = true};
            writer.Write(
                @"HTTP/1.1 200 OK
Content-Length: 10

1234567890");
            readStream.Position = 0;
            var writeStream= new MemoryStream();

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

        [TestFixture]
        public class GetHttpsContent
        {
            [Test]
            public void ReceiveRightContentTest()
            {
                // arrange
                var tcpClient = GenerateSimpleTcpClientMock();

                // act
                SecureWebContentReader reader = new SecureWebContentReader(tcpClient.Object, "www.example.com");
                var actual = reader.GetHttpsContent();

                // assert
                StringAssert.AreEqualIgnoringCase("1234567890", actual);
            }
        }

        public class GetContentStringsFromRegex
        {
            [Test]
            public void ReceiveRightGroupsTest()
            {
                // arrange
                var tcpClient = GenerateSimpleTcpClientMock();

                // act
                SecureWebContentReader reader = new SecureWebContentReader(tcpClient.Object, "www.example.com");
                var actual = reader.GetContentStringsFromRegex("([16].*?[50])");

                // assert
                CollectionAssert.AllItemsAreUnique(actual);
                CollectionAssert.AllItemsAreInstancesOfType(actual, typeof(string));
                CollectionAssert.AllItemsAreNotNull(actual);
                CollectionAssert.IsNotEmpty(actual);
                CollectionAssert.AreEqual(new string[]{"12345","67890"}.ToList(), actual);
            }
        }
    }
}