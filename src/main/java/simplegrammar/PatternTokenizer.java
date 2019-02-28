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

        List<Matcher> matchers = new ArrayList<>();

        for (TokenPattern tokenPattern : tokenPatterns)
            matchers.add(tokenPattern.pattern.matcher(source));

        int position = 0;

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
                throw new ParseException("Unexpected input!");

            Token token = new Token(tokenName, tokenValue);
            token.setSource(tokenSource);

            addToken(token);

            position += tokenSource.length();

        } while (position < source.length());

    }

}
