package dokumentyPrawne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Parser {

    String filePath;

    int chapterNr;

    int articleNr;

    int fromArticle;
    int toArticle;

    FileReader fileReader;
    BufferedReader bufferedReader;

    ArrayList<Artykuł> articleList = new ArrayList<>();

    public Parser(int option, String filePath, int sectionNr){

        this.filePath = filePath;
        // jezeli rozdzial
        this.chapterNr = sectionNr;
        // jesli artykul
        this.articleNr = sectionNr;
    }
    public Parser(String filePath, int fromArticle, int toArticle){
        this.filePath = filePath;
        this.fromArticle = fromArticle;
        this.toArticle = toArticle;
    }

    public void openFile() throws IOException{
        /*
            jeżeli uda się otworzyć plik to wrzucamy go do bufferedReader
            i parsuj
            jeżeli nie to wyrzucamy wyjątek
         */
    }
    private void parse(){
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
}
