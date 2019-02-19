package pldom;

import static pldom.DeclarationType.VARRAY_TYPE;

public class VArrayType extends NestedTableType {

    int size;

    public VArrayType() {
        type = VARRAY_TYPE;
    }

}
