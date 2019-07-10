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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author s.vinniks
 */
public class Grammar {

    private final Map<String, List<Option>> rules;
    private String rootRuleName;

    public Grammar() {
        rules = new HashMap<>();
    }
    
    protected final Option createOption(String ruleName, boolean output) {
        
        Option option = new Option(this, ruleName, output);
        
        List<Option> options = rules.get(ruleName);
        
        if (options == null) {
            options = new ArrayList<>();
            rules.put(ruleName, options);
        }
        
        options.add(option);

        if (rootRuleName == null)
            rootRuleName = ruleName;

        return option;
        
    }
    
    protected final Option createOption(String ruleName) {
        return createOption(ruleName, false);
    }

    protected final void append(Reader source) throws IOException, GrammarParseException {
        new GrammarParser(this).parse(source);
    }

    public final boolean hasRule(String name) {
        return rules.containsKey(name);
    }
 
    public final void validate() throws GrammarException {
        
        for (List<Option> options : rules.values()) 
            for (Option option : options)
                for (AbstractOptionElement element : option.getElements())
                    if (element instanceof OptionRule)
                        if (!hasRule(((OptionRule) element).getName()))
                            throw new GrammarException("Rule \"" + ((OptionRule) element).getName() + "\" does not exist!");
        
    }

    private void checkRule(String ruleName) throws GrammarException {
        if (!hasRule(ruleName))
            throw new GrammarException("Rule \"" + ruleName + "\" does not exist!");
    }

    private List<Option> getOptions(String ruleName) throws GrammarException {
        checkRule(ruleName);
        return rules.get(ruleName);
    }
    
    private void findExpansionStacks(
            Token token, 
            ElementStack elementStack, 
            OptionStack expansionStack,
            MatchingElementStacks matchingElementStacks,
            List<OptionStack> matchingExpansionStacks
    ) throws GrammarException {
        
        while (!elementStack.empty()) {
            
            AbstractOptionElement element = elementStack.pop();
            
            if (element instanceof OptionToken) {
                
                OptionToken tokenElement = (OptionToken)element;
                
                if (tokenElement.matches(token)) {
                    
                    if (tokenElement.match(token).compareTo(matchingElementStacks.getBestMatch()) > 0) {
                            
                        matchingElementStacks.setBestMatch(tokenElement.match(token));

                        matchingElementStacks.clear();
                        matchingExpansionStacks.clear();

                    }
                       
                    if (tokenElement.match(token).compareTo(matchingElementStacks.getBestMatch()) == 0) {
                        matchingElementStacks.add((ElementStack)elementStack.clone());
                        matchingExpansionStacks.add((OptionStack)expansionStack.clone());
                    }
                    
                } 
                
                return;
                
            } else if (element instanceof OptionRule) {
              
                List<Option> options = getOptions(((OptionRule) element).getName());
                
                for (Option option : options) {
                    
                    ElementStack innerElementStack = (ElementStack)elementStack.clone();
                    innerElementStack.pushOption(option);
                    
                    expansionStack.push(option);
                    
                    findExpansionStacks(
                            token, 
                            innerElementStack, 
                            expansionStack, 
                            matchingElementStacks, 
                            matchingExpansionStacks); 
                    
                    expansionStack.pop();
                    
                }
                
                return;
                
            }
                
        }
        
        if (token instanceof TokenStreamEnd) {
            matchingElementStacks.add((ElementStack)elementStack.clone());
            matchingExpansionStacks.add((OptionStack)expansionStack.clone());
        }
        
    }
    
    private boolean emptyPathExists(List<AbstractOptionElement> optionElements) {
        
        for (AbstractOptionElement element : optionElements)
            if (element instanceof OptionToken)
                return false;
            else if (element instanceof OptionRule) {
                
                OptionRule optionRule = (OptionRule)element;
                boolean emptySubpathExists = false;
                
                for (Option option : rules.get(optionRule.getName()))
                    if (emptyPathExists(option.getElements())) {
                        emptySubpathExists = true;
                        break;
                    }
                
                if (!emptySubpathExists)
                    return false;
                
            }
        
        return true;
        
    }

    public String getRootRuleName() {
        return rootRuleName;
    }

    public void setRootRuleName(String rootRuleName) {
        this.rootRuleName = rootRuleName;
    }

    public SyntaxTreeNode parse(Tokenizer tokens) throws ParseException, GrammarException {
        return parse(tokens, rootRuleName, false);
    }

    public SyntaxTreeNode parse(Tokenizer tokens, String rootRoleName) throws GrammarException, ParseException {
        return parse(tokens, rootRoleName,false);
    }
    
    public SyntaxTreeNode parse(Tokenizer tokens, String rootRuleName, boolean outputTrace) throws GrammarException, ParseException {

        checkRule(rootRuleName);

        SyntaxTreeNode syntaxTree = new SyntaxTreeNode(rootRuleName);
        
        SyntaxTreeNodeStack treeNodeStack = new SyntaxTreeNodeStack();
        treeNodeStack.push(syntaxTree);

        ElementStack elementStack = new ElementStack();
        elementStack.push(new OptionRule(rootRuleName, false));

        OptionStack expansionStack = new OptionStack();

        while (!elementStack.empty() && tokens.hasNext()) {
            
            if (outputTrace) {
            
                System.out.println(tokens.peek(1).getName() + ":" + tokens.peek(1).getValue() + " - ");

                for (AbstractOptionElement element : elementStack) {

                    if (element instanceof OptionToken) {
                        OptionToken token = (OptionToken)element;
                        System.out.print(token.getName()+":"+token.getValue());
                    } else if (element instanceof OptionRule) {
                        OptionRule token = (OptionRule)element;
                        System.out.print("{" + token.getName()+"}");
                    } else {
                        System.out.print(element.getClass().getName());
                    }
                    System.out.print("  ");
                }

                System.out.println(); 
                
            }
            
            AbstractOptionElement element = elementStack.peek();
            
            if (element instanceof EmptyElement) {
                
                elementStack.pop();
                treeNodeStack.pop();
                
            } else if (element instanceof OptionToken) {
                
                Token token = tokens.read();
                OptionToken optionToken = (OptionToken)element;
                
                if (optionToken.matches(token)) {
                    
                    if (optionToken.isOutputName())
                        treeNodeStack.push(treeNodeStack.peek().addChildNode(token.getName()));
                    
                    if (optionToken.isOutputValue())
                        treeNodeStack.peek().addChildNode(token.getValue());
                    
                    if (optionToken.isOutputName())
                        treeNodeStack.pop();
                        
                } else
                    throw new UnexpectedTokenException(token);

                treeNodeStack.pop();
                elementStack.pop();
                
            } else if (element instanceof OptionRule) {
                
                if (!expansionStack.empty()) {
                    
                    OptionRule optionRule = (OptionRule)element;
                    Option option = expansionStack.pop();
                    
                    SyntaxTreeNode parentNode = treeNodeStack.pop();
                    
                    if (optionRule.getOutput() == null ? option.isOutput() : optionRule.getOutput())
                        parentNode = parentNode.addChildNode(optionRule.getName());
                    
                    treeNodeStack.push(parentNode, option.getElementCount());
                    
                    elementStack.pop();
                    elementStack.pushOption(option);
                    
                } else {
                    
                    int peekDepth = 1;
                    
                    List<ElementStack> initialElementStacks = new ArrayList<>();
                    initialElementStacks.add((ElementStack)elementStack.clone());
                    
                    List<OptionStack> initialExpansionStacks = new ArrayList<>();
                    initialExpansionStacks.add(new OptionStack());
                    
                    while (expansionStack.empty()) {
                        
                        Token token;
                        
                        if (tokens.hasNext(peekDepth))
                            token = tokens.peek(peekDepth);
                        else
                            token = new TokenStreamEnd();
                        
                        MatchingElementStacks matchingElementStacks = new MatchingElementStacks();
                        List<OptionStack> matchingExpansionStacks = new ArrayList<>();
                        
                        for (int i = 0; i < initialElementStacks.size(); i++) 
                            findExpansionStacks(
                                    token, 
                                    initialElementStacks.get(i), 
                                    initialExpansionStacks.get(i), 
                                    matchingElementStacks, 
                                    matchingExpansionStacks
                            );

                        /*System.out.println(token.toString());
                        System.out.println("------------------------------------------------");
                        int i = 0;
                        for (ElementStack stack : matchingElementStacks) {
                            System.out.print(++i);
                            System.out.print(' ');
                            for (AbstractOptionElement e : stack) {
                                if (e instanceof OptionToken) {
                                    OptionToken t = (OptionToken)e;
                                    System.out.print(t.getName()+":"+t.getValue());
                                } else if (e instanceof OptionRule) {
                                    OptionRule r = (OptionRule)e;
                                    System.out.print("{" + r.getName()+"}");
                                } else {
                                    System.out.print(e.getClass().getName());
                                }
                                System.out.print("  ");
                            }
                            System.out.println();
                        }
                        
                        System.out.println();*/
                        
                        if (matchingExpansionStacks.isEmpty())

                            throw new UnexpectedTokenException(token);

                        else if (matchingExpansionStacks.size() == 1) 
                            
                            while (!matchingExpansionStacks.get(0).empty())
                                expansionStack.push(matchingExpansionStacks.get(0).pop());
                        
                        else if (token instanceof TokenStreamEnd)

                            throw new ParseException("Unexpected end of the input!");
                            
                        else {
                            
                            peekDepth++;
                        
                            initialElementStacks = matchingElementStacks;
                            initialExpansionStacks = matchingExpansionStacks;
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        if (tokens.hasNext())
            throw new UnexpectedTokenException(tokens.peek(1));

        if (!emptyPathExists(elementStack))
            throw new ParseException("Unexpected end of the input!");
        
        return syntaxTree;
        
    }
    
}
