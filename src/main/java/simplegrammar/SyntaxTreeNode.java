/*
    Copyright 2019 Sergejs Vinniks

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package simplegrammar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author s.vinniks
 */
public class SyntaxTreeNode {
    
    private final String value;
    private final List<SyntaxTreeNode> childNodes;
    
    public SyntaxTreeNode(String value) {
        this.value = value;
        childNodes = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<SyntaxTreeNode> getChildNodes() {
        return childNodes;
    }
    
    private void output(int depth) {
        
        for (int i = 1; i <= depth; i++)
            System.out.print("  ");
        
        System.out.println(value);
        
        for (SyntaxTreeNode node : childNodes)
            node.output(depth + 1);
        
    }
    
    public void output() {
        output(0);
    }
    
    public SyntaxTreeNode addChildNode(String value) {
        SyntaxTreeNode node = new SyntaxTreeNode(value);
        childNodes.add(node);
        return node;
    }
    
    public SyntaxTreeNode getChildNode(int i) {
        if (i >=0 && i < childNodes.size())
            return childNodes.get(i);
        else
            return null;
    }
    
    public SyntaxTreeNode getChildNode(String value) {
        
        for (SyntaxTreeNode childNode : childNodes)
            if (childNode.getValue().equals(value))
                return childNode;
        
        return null;
        
    }
    
    public List<SyntaxTreeNode> getChildNodes(String... values) {
        
        List<SyntaxTreeNode> matchingChildNodes  = new ArrayList<>(5);
        
        for (SyntaxTreeNode childNode : childNodes)
            for (String value : values)
                if (childNode.getValue().equals(value)) {
                    matchingChildNodes.add(childNode);
                    break;
                }
        
        return matchingChildNodes;
        
    }
    
    public String getChildValue(int i) {
        if (i >=0 && i < childNodes.size())
            return childNodes.get(i).getValue();
        else
            return null;
    }
    
}
