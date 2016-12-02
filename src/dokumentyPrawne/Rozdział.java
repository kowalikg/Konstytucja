package dokumentyPrawne;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.ArrayList;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Rozdział implements Podzielalne {

    private int chapterNr;

    ArrayList<Artykuł> articles = new ArrayList<>();

    @Override
    public void show() {

    }

    @Override
    public void push(Object subsection) {

    }
}
