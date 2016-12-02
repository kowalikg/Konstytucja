package dokumentyPrawne;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.ArrayList;

/**
 * Created by Gabrysia on 02.12.2016.
 */
public class Artykuł implements Podzielalne {

    private int articleNr;

    ArrayList<String> subsections = new ArrayList<>();

    public Artykuł(int articleNr){
        this.articleNr = articleNr;
    }

    @Override
    public void show() {

    }

    @Override
    public void push(Object subsection) {

    }
}
