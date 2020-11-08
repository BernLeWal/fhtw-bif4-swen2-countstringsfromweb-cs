package at.fhtw.bif3.swe1.countstringfromweb;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureWebContentReader implements SecureWebContentReaderInterface {

    @Getter private final String websiteDomain;
    @Getter private final int port;

    @Setter private String content;
    private final TcpClientInterface tcpClient;

    public SecureWebContentReader(TcpClientInterface tcpClient, String websiteDomain, int port) {
        this.tcpClient = tcpClient;
        this.websiteDomain = websiteDomain;
        this.port = port;
    }

    public SecureWebContentReader(TcpClientInterface tcpClient, String websiteDomain) {
        this(tcpClient, websiteDomain, 443);
    }

    public SecureWebContentReader(String websiteDomain, int port) {
        this.websiteDomain = websiteDomain;
        this.port = port;
        this.tcpClient = new TcpClient();
    }

    public SecureWebContentReader(String websiteDomain) {
        this(websiteDomain, 443);
    }


    public String getContent() {
        return (content!=null) ? content : getHttpsContent();
    }


    @Override
    public String getHttpsContent(){
        if( content!=null && !content.isBlank() ) {
            return content;
        }

        tcpClient.connectSsl(websiteDomain, port);
        try (
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(tcpClient.getStreamRead()));
            BufferedWriter streamWriter = new BufferedWriter(new OutputStreamWriter(tcpClient.getStreamWrite())) )
        {
            writeHttpGetRequest(streamWriter, websiteDomain);
            int contentLength = readHttpHeader(streamReader);
            return content = readHttpBody(streamReader, contentLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeHttpGetRequest(BufferedWriter streamWriter, String websiteDomain) throws IOException {
        streamWriter.write("GET / HTTP/1.1\r\n");
        streamWriter.write("Host: " + websiteDomain + "\r\n");
        streamWriter.write("Accept: */*\r\n" );
        streamWriter.write("\r\n" );
        streamWriter.flush();
    }

    private static int readHttpHeader(BufferedReader streamReader) throws IOException {
        String line;
        int contentLength = 0;
        while ( (line=streamReader.readLine())!=null ) {
            if (line.isBlank() )
                break;
            if (line.toLowerCase().startsWith("content-length:") ) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }
        return contentLength;
    }

    private static String readHttpBody(BufferedReader contentReader, int contentLength) throws IOException {
        StringBuilder sb = new StringBuilder(10000);
        char[] buf = new char[1024];
        int totalLen = 0;
        int len;
        while ((len = contentReader.read(buf)) != -1) {
            sb.append(buf, 0, len);
            totalLen += len;
            if( totalLen >= contentLength )
                break;
        }

        return sb.toString();
    }

    @Override
    public List<String> getContentStringsFromRegex(String pattern, int groupNr)  {
        // tested with https://regex101.com/
        Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
        final String httpsContent = getHttpsContent();
        Matcher matcher = regex.matcher(httpsContent);
        ArrayList<String> results = new ArrayList<>();
        while (matcher.find()) {
            results.add( matcher.group(groupNr).trim() );
        }
        return results;
    }

    @Override
    public List<String> getContentStringsFromRegex(String pattern) {
        return getContentStringsFromRegex(pattern, 1);
    }
}
