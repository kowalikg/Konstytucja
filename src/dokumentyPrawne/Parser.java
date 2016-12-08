package dokumentyPrawne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Parser {

    private String filePath;

    private String chapterNr;

    private int articleNr;

    private int fromArticle;
    private int toArticle;

    private Option option;

    private FileReader fileReader;
    private BufferedReader bufferedReader;

    //parser musi wiedzieć w której lini jest obecnie
    private String currentLine = null;
    //wynik który będziemy wrzucać do artykułu
    private String result = "";

    //paterny do regexów
    private Pattern articlePattern;
    private Matcher articleMatcher;

    private Pattern chapterPattern;
    private Matcher chapterMatcher;

    private Pattern sectionPattern;
    private Matcher sectionMatcher;

    //regexy
    private String chapterStart = "^Rozdział ";
    private String articleStart = "^Art. ";

    private String articleEnd = ".$";
    private String chapterEnd = "$";

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
        this.articleNr = fromArticle;
    }
    //głowna metoda zwraca artykuł, rozdział bądź listę artykułów
    public Dividable parse() throws IOException {
        fileReader = new FileReader(filePath);
        bufferedReader = new BufferedReader(fileReader);
        
        if (option.equals(Option.SINGLE_ARTICLE))
            return parseSingleArticle();
        
        else if (option.equals(Option.CHAPTER))
            return parseChapter();
        
        else if (option.equals(Option.MANY_ARTICLES))
            return parseManyArticles();

        throw new IOException();
    }
    // sparsuj pojedynczy artykuł
    private Article parseSingleArticle() throws IOException {
        //ustaw currentLine na początek pliku
        currentLine = bufferedReader.readLine();
        //przenieś linię do pożądanego artykułu
        switchToArticle(articleNr);
        //przeparsuj go
        return parseArticle();
    }

    private Chapter parseChapter() throws IOException{
        //ustaw currentLine do żądanego rozdziału
        switchToStartChapter();

        Chapter chapter = new Chapter(chapterNr);

        //ustal numer pierwszego artykułu
        setFirstArticleNr();

        //parsuj artykuły dopóki nie skończy się konstytucja albo nie zacznie się nowy rozdział
        do{
            Article article = parseArticle();
            chapter.push(article);
        }
        while(currentLine != null && !ifBeginOfChapter());

        return chapter;
    }

    private ArticleList parseManyArticles() throws IOException {
        // to samo co przy pojedyńczym
        currentLine = bufferedReader.readLine();
        switchToArticle(fromArticle);

        ArticleList list = new ArticleList();

        for (int i = fromArticle; i <= toArticle; i++){
            Article article = parseArticle();
            list.push(article);
            //jeżeli konstytucja się nie skończyła a obecna linia nie równa się wzorcowi nowego artykułu
            // np. jest podtytułem to przesuń currentLine do nowego artykułu
            if (currentLine != null && !currentLine.equals("Art. " + articleNr + ".")){
                switchToArticle(articleNr);
            }
        }
        return list;
    }
    private Article parseArticle() throws IOException{
        //stwórz nowy artykuł i ustaw numer następnego artykułu
        Article article = new Article(articleNr++);

        //flagi do sprawdzenia końca artykułu/podpunktu
        boolean endOfArticleFound = false;
        boolean sectionFound = false;

        //flaga dla usuniecia podzielonych wyrazow i zastapienia ich znakiem nowej lini
        boolean giveEnter = false;

        //pattern do sprawdzenia czy artykul sie skonczyl
        String nextArticlePattern = articleStart + "\\d+" + articleEnd;
        articlePattern = Pattern.compile(nextArticlePattern);

        //pattern dla subsekcji
        sectionPattern = Pattern.compile(subsectionPattern);

        do {
            //wczytaj linie
            currentLine = bufferedReader.readLine();

            //jezeli nie dotarlismy do konca pliku

            if (currentLine != null){

                //sprawdz dopasowania
                articleMatcher = articlePattern.matcher(currentLine);
                sectionMatcher = sectionPattern.matcher(currentLine);

                endOfArticleFound = articleMatcher.find();
                sectionFound = sectionMatcher.find();

                // jezeli nie ma sekcji - nie pasuje do wzorca sekcji i nie zakonczyl sie jeszcze artykul ani rozdział
                if (!ifBeginOfChapter() && !sectionFound && !endOfArticleFound && ifAddLineToResult()){
                    if (giveEnter){
                        currentLine = currentLine.replaceFirst(" ", "\n");
                    }
                    giveEnter = ifParsedLineBreak();
                }
                // jeżeli znaleziono nową sekcję to wrzuć ją do artykułu
                if (!ifBeginOfChapter() && sectionFound && !endOfArticleFound && ifAddLineToResult()) {
                    article.push(result);
                    result = "";
                    giveEnter = ifParsedLineBreak();
                }
            }
        }//dopóki plik/artykuł/rozdział się nie skończy
        while(currentLine != null && !endOfArticleFound && !ifBeginOfChapter());
        article.push(result);
        result = "";
        return article;
    }
    //przenieś currentLine na żądany rozdział
    private void switchToStartChapter() throws IOException{
        String startChapterPattern = chapterStart + chapterNr + chapterEnd;
        chapterPattern = Pattern.compile(startChapterPattern);

        do{
            currentLine = bufferedReader.readLine();
            chapterMatcher = chapterPattern.matcher(currentLine);
        }
        while(!chapterMatcher.find());
    }

    private void switchToArticle(int number) throws IOException{
        /*uruchamiamy pattern do znalezienia pozadanego artykulu
        w głównych metodach parseSingle/ManyArticle[s] przełączamy currentLine na pierwszą linię pliku
        a nie tutaj ze względu na to że tej metody używamy również wewnątrz pliku
        */

        String startArticlePattern = articleStart + number + articleEnd;

        articlePattern = Pattern.compile(startArticlePattern);
        articleMatcher = articlePattern.matcher(currentLine);

        // dopoki nie znajdzie pasujacego artykulu niech przelatuje po pliku
        do {
            currentLine = bufferedReader.readLine();
            articleMatcher = articlePattern.matcher(currentLine);
        }
        while (!articleMatcher.find());
    }
    private void setFirstArticleNr() throws IOException {
        //ustal który artykuł jest pierwszym do wyświetlenia
        String nextArticlePattern = articleStart + "\\d+" + articleEnd;

        articlePattern = Pattern.compile(nextArticlePattern);

        do{
            currentLine = bufferedReader.readLine();
            articleMatcher = articlePattern.matcher(currentLine);
        }
        while (!articleMatcher.find());
        articleNr = Integer.parseInt(currentLine.substring(5, currentLine.length()-1));
    }
    //sprawdź czy nie currentLine nie jest na początku rozdziału
    private boolean ifBeginOfChapter(){
        String beginOfChapterPattern = chapterStart + "(.+)" + chapterEnd;
        chapterPattern = Pattern.compile(beginOfChapterPattern);
        chapterMatcher = chapterPattern.matcher(currentLine);

        return chapterMatcher.find();
    }
    //pomiń niepotrzebne tytuły
    private boolean ifAddLineToResult(){
        String date = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
        String goverment = "^©Kancelaria Sejmu$";
        String subTitle = "^[A-ZĄĆĘŁÓŃŚŹŻ ,]+$";

        Pattern subTitlePattern = Pattern.compile(subTitle);
        Matcher subTitleMatcher = subTitlePattern.matcher(currentLine);

        if(subTitleMatcher.find())
            return false;

        Pattern govPattern = Pattern.compile(goverment);
        Matcher govMatch = govPattern.matcher(currentLine);

        if (govMatch.find())
            return false;

        Pattern datePattern = Pattern.compile(date);
        Matcher dateMatch = datePattern.matcher(currentLine);

        if (dateMatch.find())
            return false;
        return true;
    }

    // patrzymy czy jest zalamane slowo
    private boolean ifParsedLineBreak(){
        if (currentLine.charAt(currentLine.length()-1) == '-'){

            result += currentLine.substring(0, currentLine.length()-1);
            return true;
        }
        result += currentLine + "\n";
        return false;
    }


}
