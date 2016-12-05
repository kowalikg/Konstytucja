package dokumentyPrawne;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Parser parser = null;
        Option option = Option.UNDEFINED;
        ArgumentParser argsParser = new ArgumentParser(args);
        try{
            option = argsParser.validateArgs();
        }
        catch(IllegalArgumentException | NullPointerException e){
            System.out.println(e);
            System.exit(0);
        }

        if (option == Option.CHAPTER){
             parser = new Parser(option, argsParser.getPath(), argsParser.getChapterNo());
        }
        else if (option == Option.SINGLE_ARTICLE){
            parser = new Parser(option, argsParser.getPath(), argsParser.getFromArticle());
        }
        else if (option == Option.MANY_ARTICLES){
            parser = new Parser(option, argsParser.getPath(), argsParser.getFromArticle(), argsParser.getToArticle());
        }

        try {
            parser.openFile();
        } catch (IOException e) {
            System.out.println(e);;
        }
        // write your code here
        /*
        przechwytujemy wyjatki w try-catch

        utwórz obiekt Argument parser
        sprawdz poprawnosc
        w zaleznosci od opcji uruchom parser z właściwymi argumentami

        przechwytujemy wyjatki w try-catch
        sprawdz czy parser jest w stanie otworzyc plik i sparsowac go

        wyswietl wynik

         */

    }
}
