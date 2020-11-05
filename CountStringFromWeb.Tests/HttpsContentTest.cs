using NUnit.Framework;

namespace CountStringFromWeb.Tests {
    [TestFixture]
    public class GetHttpsContent {
        [Test]
        public void ReceiveRightContentTest() {
            // arrange
            var tcpClient = TestHelper.GenerateSimpleTcpClientMock();

            // act
            SecureWebContentReader reader = new SecureWebContentReader(tcpClient.Object, TestHelper.Url);
            var actual = reader.GetHttpsContent();

            // assert
            StringAssert.AreEqualIgnoringCase("1234567890", actual);
        }
    }
}
