package dokumentyPrawne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Parser {

    String filePath;

    Option option;

    String chapterNr;

    int articleNr;

    int fromArticle;
    int toArticle;

    FileReader fileReader;
    BufferedReader bufferedReader;

    Pattern endOfSectionPattern;
    Pattern sectionPattern;


    Matcher endOfSectionMatcher;
    Matcher sectionMatcher;
    Matcher nextSectionMatcher;

    ArrayList<Article> articleList = new ArrayList<>();

    private String currentLine = null;
    private String result = "";

    private String chapterPattern = "^Rozdział ";
    private String articlePattern = "^Art. ";

    private String articleEnd = ".$";
    private String endOfPattern = "$";

    private String subsectionPattern = "^(\\d+)[\\.|\\)]";

    public Parser(Option option, String filePath, int articleNr){
        this.option = option;
        this.filePath = filePath;
        this.articleNr = articleNr;
    }
    public Parser(Option option, String filePath, String chapterNr){
        this.option = option;
        this.filePath = filePath;
        this.chapterNr = chapterNr;
    }
    public Parser(Option option, String filePath, int fromArticle, int toArticle){
        this.option = option;
        this.filePath = filePath;
        this.fromArticle = fromArticle;
        this.toArticle = toArticle;
    }

    public void openFile() throws IOException{
        fileReader = new FileReader(filePath);
        parse();
    }
    private void parse() throws IOException {
        bufferedReader = new BufferedReader(fileReader);
        parseSingleArticle();

        /*
        wykorzystując klasę Pattern (wyrażenia regularne), przechodzimy po bufferedReader
        mamy kilka wzorców do pomijania dat itp.

        główne szukanie po końcach lini \n

        jeżeli artykuł się zaczął to tworzymy obiekt artykułu
        jeżeli artykuł ma punkty to wrzucamy je na listę w obiekcie Artykuł
        jeżeli artykuł się skończył to wrzucamy go na listę articleList

         */
    }
    public void show(){
        /*
        wyświetlamy przetworzoną listę artykułów
         */
    }
    private void switchToStartArticle() throws IOException{
        //uruchamiamy pattern do znalezienia pozadanego artykulu
        String startArticlePattern = articlePattern + articleNr + articleEnd;
        endOfSectionPattern = Pattern.compile(startArticlePattern);

        // dopoki nie znajdzie pasujacego artykulu niech przelatuje po pliku
        do {
            currentLine = bufferedReader.readLine();
            endOfSectionMatcher = endOfSectionPattern.matcher(currentLine);
        }
        while (!endOfSectionMatcher.find());

    }
    // patrzymy czy jest zalamane slowo
    private boolean ifParsedLineBreak(){
        if (currentLine.charAt(currentLine.length()-1) == '-'){

            result += currentLine.substring(0, currentLine.length()-1);
            return true;
        }

        result += currentLine;
        return false;
    }
    private void parseSingleArticle() throws IOException{

        switchToStartArticle();

        //tworzymy nowy artykul
        Article article = new Article(articleNr);

        //flagi dla funkcji find
        boolean endOfArticleFound = false;
        boolean sectionFound = false;
        boolean nextSectionFound = false;

        //flaga dla usuniecia podzielonych wyrazow i zastapienia ich znakiem nowej lini
        boolean giveEnter = false;

        //pattern do sprawdzenia czy artykul sie skonczyl
        String nextArticlePattern = articlePattern + (articleNr+1) + articleEnd;
        endOfSectionPattern = Pattern.compile(nextArticlePattern);

        //pattern dla subsekcji
        sectionPattern = Pattern.compile(subsectionPattern);

        do {
            //wczytaj linie
            currentLine = bufferedReader.readLine();
            System.out.println(currentLine);

            //jezeli nie dotarlismy do konca pliku

            if (currentLine != null){

                //sprawdz dopasowania
                endOfSectionMatcher = endOfSectionPattern.matcher(currentLine);
                sectionMatcher = sectionPattern.matcher(currentLine);

                endOfArticleFound = endOfSectionMatcher.find();
                sectionFound = sectionMatcher.find();

                // jezeli nie ma sekcji - nie pasuje do wzorca sekcji i nie zakonczyl sie jeszcze artykul
                if (!sectionFound && !endOfArticleFound){
                    System.out.println("to nie jest sekcja");
                    if (giveEnter){
                        currentLine = currentLine.replaceFirst(" ", "\n");
                    }
                    giveEnter = ifParsedLineBreak();
                    System.out.println("giv: "+ result);
                }
                if (sectionFound && !endOfArticleFound) {
                    System.out.println("sekcja nowa");
                    System.out.println("pushujemy: " + result);
                    article.push(result);
                    result = "";

                    giveEnter = ifParsedLineBreak();

                    System.out.println("Result: " + result);

                }
            }
        }
        while(currentLine != null && !endOfArticleFound);
        System.out.println("koniec");
        article.push(result);
        articleList.add(article);
        System.out.println("Result: " + result);
        articleList.get(0).show();
    }

}
