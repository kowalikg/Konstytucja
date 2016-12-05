package dokumentyPrawne;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class ArgumentParser {
    String[] argsToValidate;

    String filePath;

    private int fromArticle;
    private int toArticle;

    private String romanianChapterNr = null;
    private int arabicChapterNr;

    private Option option;

    private int firstArticle = 1;
    private int lastArticle = 243;

    private HashMap<String, Integer> romanianMap = new HashMap<String, Integer>();

    public ArgumentParser(String[] args){
        this.argsToValidate = Arrays.copyOf(args, args.length);

        System.out.println(Arrays.toString(argsToValidate));
    }
    public Option validateArgs() throws IllegalArgumentException, NullPointerException{

        if (argsToValidate.length > 3 || argsToValidate.length < 1){
            throw new IllegalArgumentException("Arguments should be between 1 and 3");
        }

        if (validateFilePath() && (validateNr())){
            return option;
        }
        return Option.UNDEFINED;

        /*
        sprawdz poprawnosc argumentow (ilosc, typ, zakres)
        zrzutuj na int jeśli poprawne
        pokaz opcje (0,1,2,3)
         */
    }

    private boolean validateFilePath() throws IllegalArgumentException{
        filePath = argsToValidate[0];
        String pattern = "(\\S+).txt$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(filePath);

        if (m.find()){
            System.out.println(m.group(1));
            return true;
        }
        else
        {
            System.out.println("źle");
            throw new IllegalArgumentException("Zła ścieżka do pliku");
        }
    }
    private boolean validateNr() throws IllegalArgumentException, NullPointerException{

        if (argsToValidate.length > 1){
            try{
                fromArticle = Integer.parseInt(argsToValidate[1]);
                System.out.println(fromArticle);
                option = Option.SINGLE_ARTICLE;
            }
            catch (NumberFormatException e){
                System.out.println("rzymska");
                romanianChapterNr = argsToValidate[1];
                option = Option.CHAPTER;
                if (validateChapter())
                return true;
            }
        }
        else{
            option = Option.UNDEFINED;
            throw new IllegalArgumentException("No number.");
        }
        if (argsToValidate.length > 2){
            toArticle = Integer.parseInt(argsToValidate[2]);
            option = Option.MANY_ARTICLES;
            System.out.println(toArticle);
        }

        if (argsToValidate.length == 2){
            if (validateOneArticle())
                return true;
            else
                return false;
        }
        else if (argsToValidate.length == 3){
            if (validateMoreArticles())
                return true;
            else
                return false;
        }
        return false;
    }

    private boolean validateOneArticle() throws IllegalArgumentException{
        if (fromArticle >= firstArticle && fromArticle <= lastArticle)
        return true;
        return false;
    }
    private boolean validateMoreArticles() throws  IllegalArgumentException{
        if (fromArticle >= firstArticle && fromArticle <= lastArticle
                && toArticle >= firstArticle && toArticle <= lastArticle
                && fromArticle < toArticle
                )
            return true;
        return false;
    }

    private boolean validateChapter() throws NullPointerException{
        generateRomanianMap();

        arabicChapterNr = romanianMap.get(romanianChapterNr);
        System.out.println(arabicChapterNr);

        return true;

    }

    private void generateRomanianMap() {
        romanianMap.put("I", 1);
        romanianMap.put("II", 2);
        romanianMap.put("III", 3);
        romanianMap.put("IV", 4);
        romanianMap.put("V", 5);
        romanianMap.put("VI", 6);
        romanianMap.put("VII", 7);
        romanianMap.put("VIII", 8);
        romanianMap.put("IX", 9);
        romanianMap.put("X", 10);
        romanianMap.put("XI", 11);
        romanianMap.put("XII", 12);
        romanianMap.put("XIII", 13);
    }

    public String getPath(){
        return filePath;
    }
    public Option getOption(){
        return option;
    };
    public int getFromArticle(){
        return fromArticle;
    }
    public int getToArticle(){
        return toArticle;
    }
    public String getChapterNo(){
        return romanianChapterNr;
    }

}
