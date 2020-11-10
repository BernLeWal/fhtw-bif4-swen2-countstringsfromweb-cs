package at.fhtw.bif3.swe1.webcrawler;

import lombok.Getter;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebCrawler implements WebCrawlerInterface {

    @Getter private final String websiteDomain;
    private String contentString;
    private final MySocketInterface socket;

    public WebCrawler(String websiteDomain, MySocketInterface socket) {
        this.websiteDomain = websiteDomain;
        this.socket = socket;
    }

    public WebCrawler(String websiteDomain) {
        this( websiteDomain, new MySocket() );
    }

    @Override
    public String getContentString() {
        if( contentString!=null && !contentString.isBlank() )
            return contentString;

        try {
//            contentString = new BufferedReader(new InputStreamReader(new URL("https://" + webSiteDomain).openStream(), StandardCharsets.UTF_8))
//                    .lines()
//                    .collect(Collectors.joining("\n"));
            socket.connect( websiteDomain );
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) ) {
                writeHttpGetRequest( writer );
                int contentLength = readHttpHeader( reader );
                contentString = readHttpBody( reader, contentLength );
            }

            return contentString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHttpGetRequest(BufferedWriter writer) throws IOException {
        writer.write("GET / HTTP/1.1\r\n");
        writer.write("Host: " + websiteDomain + "\r\n");
        writer.write("Accept: */*\r\n" );
        writer.write("\r\n" );
        writer.flush();
    }

    private int readHttpHeader(BufferedReader reader) throws IOException {
        String line;
        int contentLength = 0;
        while ( (line=reader.readLine())!=null ) {
            if (line.isBlank() )
                break;
            if (line.toLowerCase().startsWith("content-length:") ) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }
        return contentLength;
    }

    private String readHttpBody(BufferedReader reader, int contentLength) throws IOException {
        StringBuilder sb = new StringBuilder(10000);
        char[] buf = new char[1024];
        int totalLen = 0;
        int len;
        while ((len = reader.read(buf)) != -1) {
            sb.append(buf, 0, len);
            totalLen += len;
            if( totalLen >= contentLength )
                break;
        }

        return sb.toString();
    }

    @Override
    public List<String> getHeadlines(String pattern, int group) {
        String content = getContentString();
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL )
                .matcher(content)
                .results()
                .map( m -> m.group(group).trim() )
                .collect(Collectors.toList());
    }

}
