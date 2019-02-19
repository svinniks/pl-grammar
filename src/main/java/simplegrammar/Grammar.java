/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    public static Grammar parse(Reader source) throws IOException, GrammarParseException {
        return new GrammarParser().parse(source);
    }

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
    
    public boolean hasRule(String name) {
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
    
    private void pushOption(ElementStack elementStack, Option option) {
    
        for (int i = option.getElements().size() - 1; i >= 0; i--)
            elementStack.push(option.getElements().get(i));
        
    }
    
    private List<Option> getOptions(String ruleName) throws GrammarException {
        
        if (!hasRule(ruleName))
            throw new GrammarException("Rule \"" + ruleName + "\" does not exist!");
        
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
                    pushOption(innerElementStack, option);
                    
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

    public SyntaxTreeNode parse(TokenStream tokens) throws GrammarException, ParseException {
        return parse(tokens, false);
    }
    
    public SyntaxTreeNode parse(TokenStream tokens, boolean outputTrace) throws GrammarException, ParseException {
        
        ElementStack elementStack = new ElementStack();
        OptionStack expansionStack = new OptionStack();
        
        List<Option> rootOptions = getOptions(rootRuleName);
        
        if (rootOptions.size() > 1)
            throw new ParseException("Root rule \"" + rootRuleName + "\" has multiple options!");
        
        SyntaxTreeNode syntaxTree = new SyntaxTreeNode(rootRuleName);
        
        SyntaxTreeNodeStack treeNodeStack = new SyntaxTreeNodeStack();
        treeNodeStack.push(syntaxTree, rootOptions.get(0).getElementCount());
        
        pushOption(elementStack, rootOptions.get(0));
        
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
                    throw new ParseException("Unexpected " + token.getName() + " \"" + token.getValue()+ "\"!");
                
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
                    pushOption(elementStack, option);
                    
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
                            
                            throw new ParseException("Unexpected " + token.toString() + "!");
                        
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
            throw new ParseException("Unexpected " + tokens.peek(1).getName() + " \"" + tokens.peek(1).getValue()+ "\"!");
        
        if (!emptyPathExists(elementStack))
            throw new ParseException("Unexpected end of the input!");
        
        return syntaxTree;
        
    }
    
}
