package at.fhtw.bif3.swe1.webcrawler;

import at.fhtw.bif3.swe1.countstringfromweb.SecureWebContentReader;
import at.fhtw.bif3.swe1.countstringfromweb.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebCrawlerTest {

    public static final String URL = "www.example.com";
    private MySocketInterface socket;

    @BeforeEach
    void setUp() throws IOException {
        socket = mock(MySocketInterface.class);
        ByteArrayInputStream readStream;
        readStream = new ByteArrayInputStream("""
                HTTP/1.1 200 OK
                Content-Length: 10
                                
                1234567890""".getBytes(StandardCharsets.UTF_8) );
        var writeStream = new ByteArrayOutputStream();

        // mock the TCP client methods
        when(socket.getInputStream()).thenReturn(readStream);
        when(socket.getOutputStream()).thenReturn(writeStream);
    }

    @Test
    void getContentString() {
        // arrange
        WebCrawler crawler = new WebCrawler(URL, socket);

        // act
        var actual = crawler.getContentString();

        // assert
        assertEquals("1234567890", actual);
    }

    @Test
    public void testReceiveRightGroups() {
        // arrange
        String[] rightGroups = new String[] { "12345", "67890" };

        // act
        WebCrawler crawler = new WebCrawler(URL, socket);
        var actual = crawler.getHeadlines("([16].*?[50])",0);

        // assert
        assertNotNull(actual);
        assertTrue(actual.size()>0);
        assertArrayEquals(Arrays.stream(rightGroups).toArray(), actual.toArray());
    }

}