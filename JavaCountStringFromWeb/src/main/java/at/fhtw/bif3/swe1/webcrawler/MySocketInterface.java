package at.fhtw.bif3.swe1.webcrawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MySocketInterface {
    void connect(String hostname) throws IOException;

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
