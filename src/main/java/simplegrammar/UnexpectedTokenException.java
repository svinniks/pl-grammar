package simplegrammar;

public class UnexpectedTokenException extends ParseException {

    private static String formatMessage(Token token) {

        StringBuilder messageBuilder = new StringBuilder()
                .append("Unexpected ")
                .append(token.getName());

        if (token.getValue() != null)
            messageBuilder
                    .append(" \"")
                    .append(token.getValue())
                    .append("\"");

        if (token.getLine() != null) {

            messageBuilder
                    .append(" at line ")
                    .append(token.getLine());

            if (token.getPosition() != null)
                messageBuilder
                        .append(", position ")
                        .append(token.getPosition());

        }

        return messageBuilder.append('!').toString();

    }

    private final Token token;

    public UnexpectedTokenException(Token token) {
        super(formatMessage(token));
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

}
