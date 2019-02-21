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

/**
 *
 * @author s.vinniks
 */
public class OptionToken extends AbstractOptionElement {
    
    public static enum Match {NONE, ANY, NAME, VALUE};
    
    private final String name;
    private final String value;
    private final boolean outputName;
    private final boolean outputValue;
    
    protected OptionToken(String name, boolean outputName, String value, boolean outputValue) {
        this.name = name;
        this.value = value;
        this.outputName = outputName;
        this.outputValue = outputValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isOutputName() {
        return outputName;
    }

    public boolean isOutputValue() {
        return outputValue;
    }
    
    public boolean matches(Token token) {
        return match(token) != Match.NONE;
    }
    
    public Match match(Token token) {
        
        if (token instanceof TokenStreamEnd)
            return Match.NONE;
        else if (name == null)
            return Match.ANY;
        else if (name.equals(token.getName())) {
            if (value == null)
                return Match.NAME;
            else if (value.equals(token.getValue()))
                return Match.VALUE;
            else
                return Match.NONE;
        } else
            return Match.NONE;
        
    }
    
}
