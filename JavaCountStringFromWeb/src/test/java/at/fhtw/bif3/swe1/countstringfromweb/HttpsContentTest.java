package at.fhtw.bif3.swe1.countstringfromweb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpsContentTest {

    @Test
    void testReceiveRightContent() {
        // arrange
        var tcpClient = TestHelper.generateSimpleTcpClientMock();

        // act
        SecureWebContentReader reader = new SecureWebContentReader(tcpClient, TestHelper.URL);
        var actual = reader.getHttpsContent();

        // assert
        assertEquals("1234567890", actual);
    }
}