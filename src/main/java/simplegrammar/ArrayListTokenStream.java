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
