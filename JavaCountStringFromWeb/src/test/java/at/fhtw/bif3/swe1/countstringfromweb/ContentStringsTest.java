package at.fhtw.bif3.swe1.countstringfromweb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ContentStringsTest {
    private static String[] rightGroups;

    @BeforeAll
    public static void setUp() {
        rightGroups = new String[] { "12345", "67890" };
    }

    @Test
    public void testReceiveRightGroups() {
        // arrange
        var tcpClient = TestHelper.generateSimpleTcpClientMock();

        // act
        SecureWebContentReader reader = new SecureWebContentReader(tcpClient, TestHelper.URL );
        var actual = reader.getContentStringsFromRegex("([16].*?[50])");

        // assert
        assertNotNull(actual);
        assertTrue(actual.size()>0);
        assertArrayEquals(Arrays.stream(rightGroups).toArray(), actual.toArray());
    }
}