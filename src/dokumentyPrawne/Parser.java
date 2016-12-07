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

    private String currentLine = null;

    private Pattern articlePattern;
    private Matcher articleMatcher;

    private Pattern chapterPattern;
    private Matcher chapterMatcher;

    private Pattern sectionPattern;
    private Matcher sectionMatcher;

    private String result = "";

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

    private Article parseSingleArticle() throws IOException {
        currentLine = bufferedReader.readLine();
        switchToArticle(articleNr);

        return parseArticle();
    }

    private Chapter parseChapter() throws IOException{
        switchToStartChapter();

        Chapter chapter = new Chapter(chapterNr);

        setFirstArticleNr();

        do{
            Article article = parseArticle();
            chapter.push(article);
        }
        while(currentLine != null && !ifBeginOfChapter());

        return chapter;
    }

    private ArticleList parseManyArticles() throws IOException {

        currentLine = bufferedReader.readLine();
        switchToArticle(fromArticle);
        ArticleList list = new ArticleList();

        for (int i = fromArticle; i <= toArticle; i++){
            Article article = parseArticle();
            list.push(article);
            if (currentLine != null && !currentLine.equals("Art. " + articleNr + ".")){
                switchToArticle(articleNr);
            }
        }
        return list;
    }
    private Article parseArticle() throws IOException{

        Article article = new Article(articleNr++);

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

                // jezeli nie ma sekcji - nie pasuje do wzorca sekcji i nie zakonczyl sie jeszcze artykul
                if (!ifBeginOfChapter() && !sectionFound && !endOfArticleFound && ifAddLineToResult()){
                    if (giveEnter){
                        currentLine = currentLine.replaceFirst(" ", "\n");
                    }
                    giveEnter = ifParsedLineBreak();
                }
                if (!ifBeginOfChapter() && sectionFound && !endOfArticleFound && ifAddLineToResult()) {
                    article.push(result);
                    result = "";
                    giveEnter = ifParsedLineBreak();
                }
            }
        }
        while(currentLine != null && !endOfArticleFound && !ifBeginOfChapter());
        article.push(result);
        result = "";
        return article;
    }

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
        //uruchamiamy pattern do znalezienia pozadanego artykulu
        String startArticlePattern = articleStart + number + articleEnd;

        articlePattern = Pattern.compile(startArticlePattern);
        articleMatcher = articlePattern.matcher(currentLine);

        if (!articleMatcher.find()){
            // dopoki nie znajdzie pasujacego artykulu niech przelatuje po pliku
            do {
                currentLine = bufferedReader.readLine();
                articleMatcher = articlePattern.matcher(currentLine);
            }
            while (!articleMatcher.find());
        }
    }
    private void setFirstArticleNr() throws IOException {
        String nextArticlePattern = articleStart + "\\d+" + articleEnd;

        articlePattern = Pattern.compile(nextArticlePattern);

        do{
            currentLine = bufferedReader.readLine();
            articleMatcher = articlePattern.matcher(currentLine);
        }
        while (!articleMatcher.find());
        articleNr = Integer.parseInt(currentLine.substring(5, currentLine.length()-1));
    }

    private boolean ifBeginOfChapter(){
        String beginOfChapterPattern = chapterStart + "(.+)" + chapterEnd;
        chapterPattern = Pattern.compile(beginOfChapterPattern);
        chapterMatcher = chapterPattern.matcher(currentLine);

        return chapterMatcher.find();
    }

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
