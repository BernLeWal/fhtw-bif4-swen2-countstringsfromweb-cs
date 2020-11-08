package at.fhtw.bif3.swe1.countstringfromweb;

public class Main {
    public static void main(String[] args) {
        SecureWebContentReader client = new SecureWebContentReader("sport.orf.at");
        var results = client.getContentStringsFromRegex("ticker-story-headline.*?a href.*?>(.*?)<\\/a>");

        System.out.printf("Pattern found: %d\n", results.size());
        System.out.println("_________________________________________________");
        System.out.println();
        for( String result : results ) {
            System.out.println(result);
        }
        System.out.println("_________________________________________________");
        System.out.println();
        System.out.println();
    }
}
