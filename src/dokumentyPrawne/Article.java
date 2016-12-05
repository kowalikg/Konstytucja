package dokumentyPrawne;

import java.util.ArrayList;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Article implements Podzielalne {

    private int articleNr;

    ArrayList<String> subsections = new ArrayList<>();

    public Article(int articleNr){
        this.articleNr = articleNr;
    }

    @Override
    public void show() {
        System.out.println("Art " + articleNr + ".");
        System.out.println(subsections.toString());
    }

    @Override
    public void push(Object subsection) {
        if (subsection instanceof String)
            if (!subsection.equals(""))
                subsections.add((String) subsection);
    }
}
