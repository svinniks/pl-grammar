package pldom;

import static pldom.DeclarationType.REF_CURSOR_TYPE;

public class RefCursorType extends Type {

    DataType returnType;

    public RefCursorType() {
        type = REF_CURSOR_TYPE;
    }

}
