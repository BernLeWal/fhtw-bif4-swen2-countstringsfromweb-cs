package at.fhtw.bif3.swe1.countstringfromweb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHelper {
    public static final String URL = "www.example.com";

    public static TcpClientInterface generateSimpleTcpClientMock() {
        TcpClientInterface tcpClient = mock(TcpClientInterface.class);

        ByteArrayInputStream readStream;
        readStream = new ByteArrayInputStream("""
                HTTP/1.1 200 OK
                Content-Length: 10
                                
                1234567890""".getBytes(StandardCharsets.UTF_8) );
        var writeStream = new ByteArrayOutputStream();

        // mock the TCP client methods
        when(tcpClient.getStreamRead()).thenReturn(readStream);
        when(tcpClient.getStreamWrite()).thenReturn(writeStream);
        return tcpClient;
    }

}
