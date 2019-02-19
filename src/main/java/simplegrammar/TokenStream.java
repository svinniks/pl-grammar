package simplegrammar;

public abstract class TokenStream {

    public abstract boolean hasNext(int depth);
    protected abstract Token doPeek(int depth);
    protected abstract Token doRead();
    
    public final boolean hasNext() {
        return hasNext(1);
    }
    
    public Token read() throws ParseException {

        if (hasNext(1))
            return doRead();
        else
            throw new ParseException("Attempt to read beyond token stream end!");

    }

    public final Token peek(int depth) throws ParseException {
        
        if (hasNext(depth))
            return doPeek(depth);
        else
            throw new ParseException("Attempt to read beyond token stream end!");

    }
    
}
