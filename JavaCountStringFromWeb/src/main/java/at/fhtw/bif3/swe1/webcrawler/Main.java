package at.fhtw.bif3.swe1.webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main1(String[] args) throws IOException {
        Pattern.compile("ticker-story-headline.*?a href.*?>(.*?)<\\/a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL )
                .matcher( new BufferedReader( new InputStreamReader( new URL("https://sport.orf.at").openStream(), StandardCharsets.UTF_8 ))
                        .lines()
                        .collect(Collectors.joining("\n")) )
                .results()
                .map( m -> m.group(1).trim() )
                .forEach(System.out::println);
    }

    public static void main2(String[] args) throws IOException {
        String content = new BufferedReader(new InputStreamReader(new URL("https://" + "sport.orf.at").openStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        var headlines = Pattern.compile("ticker-story-headline.*?a href.*?>(.*?)<\\/a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL )
                .matcher(content)
                .results()
                .map( m -> m.group(1).trim() )
                .collect(Collectors.toList());
        headlines.forEach(System.out::println);
    }

    public static void main(String[] args) {
        new WebCrawler("news.orf.at")
                .getHeadlines("ticker-story-headline.*?a href.*?>(.*?)<\\/a>",1)
                .forEach(System.out::println);
    }

}
