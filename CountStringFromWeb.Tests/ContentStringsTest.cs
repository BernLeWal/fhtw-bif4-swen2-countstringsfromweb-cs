using NUnit.Framework;
using System.Linq;

namespace CountStringFromWeb.Tests {
    [TestFixture]
    public class ContentStringsTest {

        private string[] _rightGroups;

        [OneTimeSetUp]
        public void BeforeAll() {
            _rightGroups = new string[] { "12345", "67890" };
        }

        [Test]
        public void ReceiveRightGroupsTest() {
            // arrange
            var tcpClient = TestHelper.GenerateSimpleTcpClientMock();

            // act
            SecureWebContentReader reader = new SecureWebContentReader(tcpClient.Object, TestHelper.Url);
            var actual = reader.GetContentStringsFromRegex("([16].*?[50])");

            // assert
            CollectionAssert.AllItemsAreUnique(actual);
            CollectionAssert.AllItemsAreInstancesOfType(actual, typeof(string));
            CollectionAssert.AllItemsAreNotNull(actual);
            CollectionAssert.IsNotEmpty(actual);
            CollectionAssert.AreEqual(_rightGroups.ToList(), actual);
        }
    }
}
