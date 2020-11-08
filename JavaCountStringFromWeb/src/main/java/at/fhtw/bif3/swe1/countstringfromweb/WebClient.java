package at.fhtw.bif3.swe1.countstringfromweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebClient {
    public static void main(String[] args) throws IOException {
        Pattern.compile("ticker-story-headline.*?a href.*?>(.*?)<\\/a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL )
            .matcher( new BufferedReader( new InputStreamReader( new URL("https://sport.orf.at").openStream(), StandardCharsets.UTF_8 ))
                            .lines()
                            .collect(Collectors.joining("\n")) )
            .results()
            .map(MatchResult::group)
            .forEach(System.out::println);         // or t -> System.out.println(t)

        //  .collect(Collectors.joining("\n"))  ... returns as String
    }
}
