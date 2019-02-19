package pldom;

import java.util.ArrayList;
import java.util.List;

import static pldom.Authid.DEFINER;

public class Package {

    String name;
    Authid authid;
    List<Declaration> declarations;

    public Package() {
        declarations = new ArrayList<>();
        authid = DEFINER;
    }

}
