package dokumentyPrawne;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Article implements Dividable {

    private int articleNr;

    ArrayList<String> subsections = new ArrayList<>();

    public Article(int articleNr){
        this.articleNr = articleNr;
    }

    @Override
    public void show() {
        System.out.println("Art " + articleNr + ".");
        Iterator i = subsections.iterator();
        while(i.hasNext()){
            String s = (String) i.next();
            System.out.println(s);
        }
    }

    @Override
    public void push(Object subsection) {
        if (subsection instanceof String)
            if (!subsection.equals(""))
                subsections.add((String) subsection);
    }
}
