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

public abstract class Tokenizer {

    public abstract boolean hasNext(int depth);
    protected abstract Token doPeek(int depth);
    protected abstract Token doGet();
    
    public final boolean hasNext() {
        return hasNext(1);
    }
    
    public Token get() {
        if (hasNext(1))
            return doGet();
        else
            return new TokenStreamEnd();
    }

    public final Token peek(int depth) {
        if (hasNext(depth))
            return doPeek(depth);
        else
            return new TokenStreamEnd();
    }
    
}
