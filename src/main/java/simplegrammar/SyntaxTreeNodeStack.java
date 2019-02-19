/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegrammar;

import java.util.Stack;

/**
 *
 * @author s.vinniks
 */
public class SyntaxTreeNodeStack extends Stack<SyntaxTreeNode>{
    
    public void push(SyntaxTreeNode node, int count) {
        for (int i = 1; i <= count; i++)
            push(node);
    }
    
}
