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

public class ArrayListTokenStream extends TokenStream {

    private final List<Token> tokens;
    private int position;

    public ArrayListTokenStream() {
        tokens = new ArrayList<>();
        position = -1;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public void addAll(TokenStream tokenStream) throws ParseException {

        while (tokenStream.hasNext())
            addToken(tokenStream.read());

    }

    @Override
    public boolean hasNext(int depth) {
        return tokens.size() > position + depth;
    }

    @Override
    protected Token doPeek(int depth) {
        return tokens.get(position + depth);
    }

    @Override
    protected Token doRead() {
        position++;
        return doPeek(0);
    }

}
