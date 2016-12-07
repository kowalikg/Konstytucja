package dokumentyPrawne;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gabrysia on 07.12.2016.
 */
public class ArticleList implements Dividable {
    ArrayList<Article> articles = new ArrayList<>();

    @Override
    public void show() {
        Iterator i = articles.iterator();
        while(i.hasNext()){
            Article article = (Article) i.next();
            article.show();
        }

    }

    @Override
    public void push(Object subsection) {
        if (subsection instanceof Article){
            Article article = (Article) subsection;
            articles.add(article);
        }

    }
}
