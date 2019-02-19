/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegrammar;

/**
 *
 * @author s.vinniks
 */
public class GrammarParseException extends Exception {

    /**
     * Creates a new instance of <code>ParseException</code> without detail
     * message.
     */
    public GrammarParseException() {
    }

    /**
     * Constructs an instance of <code>ParseException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public GrammarParseException(String msg) {
        super(msg);
    }
}
