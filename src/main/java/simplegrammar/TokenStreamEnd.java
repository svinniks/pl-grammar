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
final class TokenStreamEnd implements Token {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }
    
    @Override
    public String toString() {
        return "end of the input";
    }
    
}
