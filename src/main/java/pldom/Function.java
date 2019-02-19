package pldom;

import java.util.List;

import static pldom.DeclarationType.FUNCTION;

public class Function extends Subprogram {

    DataType returnDataType;
    List<Annotation> returnAnnotations;
    FunctionDirectives directives;

    public Function() {
        type = FUNCTION;
    }

}
