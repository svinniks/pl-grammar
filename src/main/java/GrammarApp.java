import com.google.gson.GsonBuilder;
import pldom.Package;
import pldom.DomBuilder;
import plsql.AnnotationTokenStream;
import plsql.PlsqlTokenStream;
import simplegrammar.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class GrammarApp {

    public final static Set<String> PLSQL_IGNORED_TOKENS;

    // Constructs an instance of PL/SQL grammar and exceluded tokens.
    static {

        PLSQL_IGNORED_TOKENS = new HashSet<>();
        PLSQL_IGNORED_TOKENS.add("SPACE");
        PLSQL_IGNORED_TOKENS.add("SHORT_COMMENT");
        PLSQL_IGNORED_TOKENS.add("LONG_COMMENT");

    }

    static private void parsePackage() throws IOException, GrammarParseException, ParseException, GrammarException {

        Reader grammarReader = new InputStreamReader(GrammarApp.class.getResourceAsStream("/plsql.grm"));
        Grammar grammar = Grammar.parse(grammarReader);

        Reader sourceReader = new InputStreamReader(GrammarApp.class.getResourceAsStream("/package.pks"));
        PlsqlTokenStream tokenStream = new PlsqlTokenStream(sourceReader, PLSQL_IGNORED_TOKENS);

        SyntaxTreeNode syntaxTree = grammar.parse(tokenStream, true);

        System.out.println(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create()
                        .toJson(syntaxTree)
        );

        Package package_ = DomBuilder.buildPackage(syntaxTree);

        System.out.println(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .create()
                        .toJson(package_)
        );

    }

    static private void parseAnnotations() throws IOException, ParseException {

        TokenStream tokenStream = new AnnotationTokenStream(new StringReader(" @api(alias=\"docs\", comment=\"Main table for storing document data.\", @sequence(column=\"id\", name=\"docs_seq\"))"));

        while (tokenStream.hasNext()) {
            Token token = tokenStream.read();
            System.out.println(String.format("%s %s", token.getName(), token.getValue()));
        }

    }

    static public void main(String[] args) throws ParseException, IOException, GrammarParseException, GrammarException {

        parsePackage();
        //parseAnnotations();

    }

}
