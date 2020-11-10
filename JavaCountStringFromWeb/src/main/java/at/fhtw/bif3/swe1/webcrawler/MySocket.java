package at.fhtw.bif3.swe1.webcrawler;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MySocket implements MySocketInterface {
    private Socket socket;

    @Override
    public void connect(String hostname) throws IOException {
        socket = SSLSocketFactory.getDefault().createSocket();
        socket.connect( new InetSocketAddress(hostname, 443 ));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
