package dokumentyPrawne;

import java.util.ArrayList;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Rozdział implements Podzielalne {

    private int chapterNr;

    ArrayList<Article> articles = new ArrayList<>();

    @Override
    public void show() {

    }

    @Override
    public void push(Object subsection) {

    }
}
