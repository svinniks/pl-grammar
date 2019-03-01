package simplegrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

public class PatternTokenizer extends SimpleTokenizer {

    private static class TokenPattern {

        String name;
        Pattern pattern;

        public TokenPattern(String name, Pattern pattern) {
            this.name = name;
            this.pattern = pattern;
        }

    }

    private final List<TokenPattern> tokenPatterns;

    public PatternTokenizer() {
        tokenPatterns = new ArrayList<>();
    }

    public void addTokenPattern(String name, String pattern) {

        tokenPatterns.add(
                new TokenPattern(
                        name,
                        Pattern.compile("^" + pattern, DOTALL)
                )
        );

    }

    public void parse(CharSequence source) throws ParseException {
        parse(source, 1, 1);
    }

    public void parse(CharSequence source, int startLine, int startPosition) throws ParseException {

        List<Matcher> matchers = new ArrayList<>();

        for (TokenPattern tokenPattern : tokenPatterns)
            matchers.add(tokenPattern.pattern.matcher(source));

        int position = 0;
        int tokenLine = startLine;
        int tokenPosition = startPosition;

        do {

            String tokenSource = null;
            String tokenName = null;
            String tokenValue = null;

            for (int i = 0; i < matchers.size(); i++) {

                Matcher matcher = matchers.get(i);
                matcher.region(position, source.length());

                if (matcher.find()) {

                    String matchedSource;
                    String matchedValue = null;

                    if (matcher.groupCount() == 0)
                        matchedSource = matcher.group();
                    else if (matcher.groupCount() == 1) {
                        matchedSource = matcher.group();
                        matchedValue = matcher.group(1);
                    } else {
                        matchedSource = matcher.group(1);
                        matchedValue = matcher.group(2);
                    }

                    if (tokenName == null || matchedSource.length() > tokenSource.length()) {
                        tokenName = tokenPatterns.get(i).name;
                        tokenSource = matchedSource;
                        tokenValue = matchedValue;
                    }
                }

            }

            if (tokenName == null)
                throw new ParseException(String.format("Unexpected input at line %d, position %d!", tokenLine, tokenPosition));

            Token token = new Token(tokenName);
            token.setValue(tokenValue);
            token.setSource(tokenSource);
            token.setLine(tokenLine);
            token.setPosition(tokenPosition);

            addToken(token);

            for (int i = 1; i <= tokenSource.length(); i++)
                if (source.charAt(position++) == '\n') {
                    tokenLine++;
                    tokenPosition = 1;
                } else
                    tokenPosition++;

        } while (position < source.length());

    }

}
