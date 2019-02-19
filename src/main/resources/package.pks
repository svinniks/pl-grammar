PACKAGE plgen.test_package AUTHID CURRENT_USER IS

    function proc
    -- @json_data(validate={true, 1, "farfarewga", 3.9})
    -- @api(alias="docs", comment="Main table for storing document data.", @sequence(column="id", name="docs_seq"))
    return number;



    /*

    v_variable VARCHAR2(2000 BYTE) := 'abc' || (a.b.c and 5 =2) - 5 and (not 5>= proc) or true "abc" cde+"efg"-ghi;
    v_variable_with_value VARCHAR2(2000) := 'asd';

    v_nvarchar_variable NVARCHAR2(2000);
    v_char_variable CHAR(2000);
    v_nchar_variable NCHAR(2000);

    v_number_variable_1 NUMBER;
    v_number_variable_2 NUMBER(10);
    v_number_variable_3 NUMBER(10, 3);
    v_number_variable_4 NUMBER(10, 3) := 123.456;

    v_float_variable_1 FLOAT;
    v_float_variable_2 FLOAT(4);

    v_timestamp_variable_1 TIMESTAMP;
    v_timestamp_variable_2 TIMESTAMP(3);

    v_timestamp_variable_2 TIMESTAMP(3) WITH TIME ZONE;
    v_timestamp_variable_3 TIMESTAMP(3) WITH LOCAL TIME ZONE;

    v_interval_ym_variable_1 INTERVAL YEAR TO MONTH;
    v_interval_ym_variable_2 INTERVAL YEAR(2) TO MONTH;

    v_interval_ds_variable_1 INTERVAL DAY TO SECOND;
    v_interval_ds_variable_2 INTERVAL DAY(5) TO SECOND(5);

    v_raw_variable RAW(100);

    v_urowid_variable_1 UROWID;
    v_urowid_variable_2 UROWID(12);

    c_number_constant CONSTANT NUMBER(10) := 123;


    TYPE t_record IS
        RECORD (
            name VARCHAR2(4000),
            surname VARCHAR2(4000)
        );

    TYPE t_nested_table IS
        TABLE OF schema.table%ROWTYPE;

    TYPE t_associative_array IS
        TABLE OF NUMBER
        INDEX BY VARCHAR2(4000);

    TYPE t_varray IS
        VARRAY(2000) OF VARCHAR2(1000);

    TYPE t_ref_cursor_1 IS
        REF CURSOR;

    TYPE t_ref_cursor_2 IS
        REF CURSOR RETURN t_person;

    CURSOR c_cursor(p_param IN VARCHAR2) IS
        SELECT *
        FROM TABLE(get_rows(12 * (a + b.c.d*(5))));


    PROCEDURE proc1;

    PROCEDURE proc2 (
        p_parameter1 NUMBER
    );

    PROCEDURE proc2 (
        p_parameter1 IN NUMBER,
        p_parameter2 OUT NUMBER,
        p_parameter3 IN OUT VARCHAR2
    );

    FUNCTION func1
    RETURN TIMESTAMP WITH LOCAL TIME ZONE;

    FUNCTION func2 (
        p_parameter1 IN DATE,
        p_pls_integer IN PLS_INTEGER := 1 * 23 / (2 + a.b."c"-0) || '123''aaa' AND (NOT a >= 6) OR a.b.c!=false
    )
    RETURN schema.table%ROWTYPE;

    FUNCTION pipelined_func
    RETURN t_collection
    PIPELINED
    DETERMINISTIC
    RESULT_CACHE(RELIES ON aaa);

    e_exception EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_exception, -100);

    PRAGMA INLINE;
    PRAGMA RESTRICT_REFERENCES(func2, WNDS);

    */

END;
