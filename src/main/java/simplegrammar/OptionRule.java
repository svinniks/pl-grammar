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
public class OptionRule extends AbstractOptionElement {
    
    private final String name;
    private final Boolean output;
    
    protected OptionRule(String name, Boolean output) {
        this.name = name;
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public Boolean getOutput() {
        return output;
    }
    
    
    
}
