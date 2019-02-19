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
public class GenericToken implements Token {

    private final String name;
    private final String value;
    
    public GenericToken(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public GenericToken(String name) {
        this(name, null);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return name + " \"" + value + "\"";
    }
    
}
