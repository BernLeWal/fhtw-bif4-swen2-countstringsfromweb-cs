package at.fhtw.bif3.swe1.countstringfromweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MySocketInterface {
    void connect(String hostname, int port);
    void connectSsl(String hostname, int port);
    InputStream getInputStream();
    OutputStream getOutputStream();
}
