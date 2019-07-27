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

public class Option {
   
    private final Grammar grammar;
    private final String ruleName;
    private final boolean output;
    private final List<AbstractOptionElement> elements;
    
    protected Option(Grammar grammar, String ruleName, boolean output) {
        this.grammar = grammar;
        this.ruleName = ruleName;
        this.output = output;
        this.elements = new ArrayList<>();
    }

    public String getRuleName() {
        return ruleName;
    }

    public boolean isOutput() {
        return output;
    }
    
    public Option addToken(String name, boolean outputName, String value, boolean outputValue) {
        elements.add(new OptionToken(name, outputName, value, outputValue));
        return this;
    }
    
    public Option addToken(String name, boolean outputName, boolean outputValue) {
        elements.add(new OptionToken(name, outputName, null, outputValue));
        return this;
    }
    
    public Option addToken(String name, String value) {
        elements.add(new OptionToken(name, false, value, false));
        return this;
    }
    
    public Option addToken(String name) {
        elements.add(new OptionToken(name, false, null, false));
        return this;
    }
    
    public Option addToken() {
        elements.add(new OptionToken(null, false, null, false));
        return this;
    }
    
    public Option addToken(boolean outputName, boolean outputValue) {
        elements.add(new OptionToken(null, outputName, null, outputValue));
        return this;
    }
    
    public Option addRule(String name, boolean output) {
        elements.add(new OptionRule(name, output));
        return this;
    }
    
    public Option addRule(String name) {
        elements.add(new OptionRule(name, null));
        return this;
    }
 
    public Option addEmpty() {
        elements.add(new EmptyElement());
        return this;
    }

    public List<AbstractOptionElement> getElements() {
        return elements;
    }
 
    public int getElementCount() {
        return elements.size();
    }
    
}
