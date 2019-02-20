package simplegrammar;

import java.io.IOException;
import java.io.Reader;

import static simplegrammar.GrammarParser.State.*;

class GrammarParser {

    public enum State {
        LF_RULE,
        R_RULE,
        LF_RULE_OUTPUT,
        LF_ELEMENT,
        LF_COLON,
        R_RULE_ELEMENT,
        LF_RULE_ELEMENT_OUTPUT,
        LF_TOKEN_NAME,
        R_TOKEN_NAME,
        LF_COMMA,
        LF_TOKEN_VALUE,
        LF_TOKEN_NAME_OUTPUT,
        R_TOKEN_VALUE,
        LF_TOKEN_END,
        R_ESCAPED_CHAR,
        LF_TOKEN_VALUE_OUTPUT
    };

    private final Grammar target;

    private State state;
    private char ch;

    private StringBuilder nameBuilder;
    private boolean outputName;
    private StringBuilder valueBuilder;
    private boolean outputValue;
    private Option option;

    public GrammarParser(Grammar target) {
        this.target = target;
    }

    private void lfRule() throws GrammarParseException {

        if (Character.isLetter(ch)) {
            nameBuilder = new StringBuilder().append(ch);
            state = R_RULE;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void rRule() throws GrammarParseException {

        if (Character.isLetterOrDigit(ch) || ch == '_')
            nameBuilder.append(ch);
        else if (ch == ':') {
            option = target.createOption(nameBuilder.toString());
            state = LF_ELEMENT;
        } else if (ch == '+') {
            option = target.createOption(nameBuilder.toString(), true);
            state = LF_COLON;
        } else if (Character.isWhitespace(ch)) {
            state = LF_RULE_OUTPUT;
        } else
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfRuleOutput() throws GrammarParseException {

        if (ch == ':') {
            option = target.createOption(nameBuilder.toString());
            state = LF_ELEMENT;
        } else if (ch == '+') {
            option = target.createOption(nameBuilder.toString(), true);
            state = LF_COLON;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfColon() throws GrammarParseException {

        if (ch == ':')
            state = LF_ELEMENT;
        else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfElement() throws GrammarParseException {

        if (Character.isLetter(ch)) {
            nameBuilder = new StringBuilder().append(ch);
            state = R_RULE_ELEMENT;
        } else if (ch == ';')
            state = LF_RULE;
        else if (ch == '{') {
            nameBuilder = null;
            outputName = false;
            valueBuilder = null;
            outputValue = false;
            state = LF_TOKEN_NAME;
        } else if (ch == '^')
            option.addEmpty();
        else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void rRuleElement() throws GrammarParseException {

        if (Character.isLetterOrDigit(ch) || ch == '_')
            nameBuilder.append(ch);
        else if (ch == '+') {
            option.addRule(nameBuilder.toString(), true);
            state = LF_ELEMENT;
        } else if (ch == '-') {
            option.addRule(nameBuilder.toString(), false);
            state = LF_ELEMENT;
        } else if (ch == ';') {
            option.addRule(nameBuilder.toString());
            state = LF_RULE;
        } else if (Character.isWhitespace(ch))
            state =  LF_RULE_ELEMENT_OUTPUT;
        else
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfRuleElementOutput() throws GrammarParseException {

        if (ch == '+') {
            option.addRule(nameBuilder.toString(), true);
            state = LF_ELEMENT;
        } else if (ch == '-') {
            option.addRule(nameBuilder.toString(), false);
            state = LF_ELEMENT;
        } else if (ch == ';') {
            option.addRule(nameBuilder.toString());
            state = LF_RULE;
        } else if (Character.isLetter(ch)) {
            option.addRule(nameBuilder.toString());
            nameBuilder = new StringBuilder().append(ch);
            state = R_RULE_ELEMENT;
        } else if (ch == '{') {
            option.addRule(nameBuilder.toString());
            nameBuilder = null;
            outputName = false;
            valueBuilder = null;
            outputValue = false;
            state = LF_TOKEN_NAME;
        } else if (ch == '^')
            option.addEmpty();
        else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void addToken() {

        String name = nameBuilder == null
                ? null
                : nameBuilder.toString();

        String value = valueBuilder == null
                ? null
                : valueBuilder.toString();

        option.addToken(name, outputName, value, outputValue);

    }

    private void lfTokenName() throws GrammarParseException {

        if (Character.isLetter(ch)) {
            nameBuilder = new StringBuilder().append(ch);
            state = R_TOKEN_NAME;
        } else if (ch == '+') {
            outputName = true;
            state = LF_COMMA;
        } else if (ch == ',') {
            state = LF_TOKEN_VALUE;
        } else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void rTokenName() throws GrammarParseException {

        if (Character.isLetterOrDigit(ch) || ch == '_')
            nameBuilder.append(ch);
        else if (ch == '+') {
            outputName = true;
            state = LF_COMMA;
        } else if (ch == ',')
            state = LF_TOKEN_VALUE;
        else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (Character.isWhitespace(ch))
            state = LF_TOKEN_NAME_OUTPUT;
        else
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfComma() throws GrammarParseException {

        if (ch == ',')
            state = LF_TOKEN_VALUE;
        else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfTokenValue() throws GrammarParseException {

        if (ch == '"') {
            valueBuilder = new StringBuilder();
            state = R_TOKEN_VALUE;
        } else if (ch == '+') {
            outputValue = true;
            state = LF_TOKEN_END;
        } else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void lfTokenNameOutput() throws GrammarParseException {

        if (ch == '+') {
            outputName = true;
            state = LF_COMMA;
        } else if (ch == ',')
            state = LF_TOKEN_VALUE;
        else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void rTokenValue() {

        if (ch == '\\')
            state = R_ESCAPED_CHAR;
        else if (ch == '"') {
            state = LF_TOKEN_VALUE_OUTPUT;
        } else
            valueBuilder.append(ch);

    }

    private void lfTokenEnd() throws GrammarParseException {

        if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    private void rEscapedChar() {

        valueBuilder.append(ch);
        state = R_TOKEN_VALUE;

    }

    private void lfTokenValueOutput() throws GrammarParseException {

        if (ch == '+') {
            outputValue = true;
            state = LF_TOKEN_END;
        } else if (ch == '}') {
            addToken();
            state = LF_ELEMENT;
        } else if (!Character.isWhitespace(ch))
            throw new GrammarParseException(String.format("Unexpected character %c!", ch));

    }

    public void parse(Reader source) throws IOException, GrammarParseException {

        state = LF_RULE;
        int chInt = source.read();

        while (chInt != -1) {

            ch = (char)chInt;

            if (state == LF_RULE)
                lfRule();
            else if (state == R_RULE)
                rRule();
            else if (state == LF_RULE_OUTPUT)
                lfRuleOutput();
            else if (state == LF_COLON)
                lfColon();
            else if (state == LF_ELEMENT)
                lfElement();
            else if (state == R_RULE_ELEMENT)
                rRuleElement();
            else if (state == LF_RULE_ELEMENT_OUTPUT)
                lfRuleElementOutput();
            else if (state == LF_TOKEN_NAME)
                lfTokenName();
            else if (state == R_TOKEN_NAME)
                rTokenName();
            else if (state == LF_COMMA)
                lfComma();
            else if (state == LF_TOKEN_VALUE)
                lfTokenValue();
            else if (state == LF_TOKEN_NAME_OUTPUT)
                lfTokenNameOutput();
            else if (state == R_TOKEN_VALUE)
                rTokenValue();
            else if (state == LF_TOKEN_END)
                lfTokenEnd();
            else if (state == R_ESCAPED_CHAR)
                rEscapedChar();
            else if (state == LF_TOKEN_VALUE_OUTPUT)
                lfTokenValueOutput();

            chInt = source.read();

        }

        if (state != LF_RULE)
            throw new GrammarParseException("Unexpected end of the input!");

    }

}
