package pldom;

import static pldom.DeclarationType.ASSOCIATIVE_ARRAY_TYPE;

public class AssociativeArrayType extends NestedTableType {

    DataType indexType;

    public AssociativeArrayType() {
        type = ASSOCIATIVE_ARRAY_TYPE;
    }

}
