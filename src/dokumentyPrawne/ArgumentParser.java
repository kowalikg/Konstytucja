package dokumentyPrawne;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class ArgumentParser {
    String[] argsToValidate;

    String filePath;

    private int articleNr;
    private int fromArticle;
    private int toArticle;

    private int chapterNr;

    private int option;

    private int firstArticle = 1;
    private int lastArticle = 243;

    public ArgumentParser(String[] args){
        /*
        przekopiowywujemy tablice args
         */
    }
    public void validateArgs() throws IllegalArgumentException{
        /*
        sprawdz poprawnosc argumentow (ilosc, typ, zakres)
        zrzutuj na int jeśli poprawne
        pokaz opcje (0,1,2,3)
         */
    }

    public String getPath(){
        return filePath;
    }
    public int getOption(){
        return option;
    }
    public int getSingleArticleNr(){
        return articleNr;
    }
    public int getFromArticle(){
        return fromArticle;
    }
    public int getToArticle(){
        return toArticle;
    }
    public int getChapterNo(){
        return chapterNr;
    }

}
