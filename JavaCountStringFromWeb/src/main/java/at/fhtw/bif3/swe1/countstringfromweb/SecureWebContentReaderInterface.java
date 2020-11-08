package at.fhtw.bif3.swe1.countstringfromweb;

import java.io.IOException;
import java.util.List;

public interface SecureWebContentReaderInterface {
    String getWebsiteDomain();
    int getPort();

    String getHttpsContent();
    List<String> getContentStringsFromRegex(String pattern, int groupNr);
    List<String> getContentStringsFromRegex(String pattern) ;
}
