package pldom;

import static pldom.DeclarationType.CURSOR;

public class Cursor extends Subprogram {

    String queryStatement;

    public Cursor() {
        type = CURSOR;
    }

}
