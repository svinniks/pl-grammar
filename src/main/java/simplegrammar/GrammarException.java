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
public class GrammarException extends Exception {

    /**
     * Creates a new instance of <code>GrammarException</code> without detail
     * message.
     */
    public GrammarException() {
    }

    /**
     * Constructs an instance of <code>GrammarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GrammarException(String msg) {
        super(msg);
    }
}
