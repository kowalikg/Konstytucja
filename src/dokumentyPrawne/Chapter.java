package dokumentyPrawne;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Chapter implements Dividable{
    private String chapterNr;

    ArrayList<Article> articles = new ArrayList<>();

    public Chapter(String chapterNr){
        this.chapterNr = chapterNr;
    }
    @Override
    public void show() {

        System.out.println("Rozdział " + chapterNr);
        Iterator i = articles.iterator();
        while(i.hasNext()){
            Article article = (Article) i.next();
            article.show();
        }
    }

    @Override
    public void push(Object subsection) {
        if (subsection instanceof Article)
            if (!subsection.equals(""))
                articles.add((Article) subsection);
    }

}
