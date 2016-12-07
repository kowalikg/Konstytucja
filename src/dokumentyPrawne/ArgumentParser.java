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
    }
    public Option validateArgs() throws IllegalArgumentException, NullPointerException{

        if (argsToValidate.length > 3 || argsToValidate.length < 1){
            throw new IllegalArgumentException("Arguments should be between 1 and 3");
        }
        if (validateFilePath() && (validateNr())){
            return option;
        }
        return Option.UNDEFINED;
    }

    private boolean validateFilePath() throws IllegalArgumentException{
        filePath = argsToValidate[0];
        String pattern = "(\\S+).txt$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(filePath);

        if (m.find()){
            return true;
        }
        throw new IllegalArgumentException("Zła ścieżka do pliku");
    }
    private boolean validateNr() throws IllegalArgumentException, NullPointerException{

        if (argsToValidate.length >= 2){
            try{
                fromArticle = Integer.parseInt(argsToValidate[1]);
            }
            catch (NumberFormatException e){
                romanianChapterNr = argsToValidate[1];

                if (validateChapter()){
                    option = Option.CHAPTER;
                }
                return true;
            }
        }
        if (argsToValidate.length == 2){

            if (validateOneArticle()){
                option = Option.SINGLE_ARTICLE;
                return true;
            }
        }
        else if (argsToValidate.length == 3){

            toArticle = Integer.parseInt(argsToValidate[2]);

            if (validateMoreArticles()){
                option = Option.MANY_ARTICLES;
                return true;
            }
        }
        option = Option.UNDEFINED;
        throw new IllegalArgumentException("No number.");
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
