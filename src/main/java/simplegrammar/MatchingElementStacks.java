/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegrammar;

import simplegrammar.OptionToken.Match;

import java.util.ArrayList;

/**
 *
 * @author s.vinniks
 */
public class MatchingElementStacks extends ArrayList<ElementStack> {
    
    private Match bestMatch;
    
    public MatchingElementStacks() {
        bestMatch = Match.NONE;
    }

    public Match getBestMatch() {
        return bestMatch;
    }

    public void setBestMatch(Match match) {
        this.bestMatch = match;
    }
    
}
