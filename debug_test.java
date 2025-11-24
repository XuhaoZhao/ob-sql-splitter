import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class debug_test {
    public static void main(String[] args) {
        // Test single '!' character
        System.out.println("=== Single '!' test ===");
        testTokens("!");

        // Test '!' in SQL statement
        System.out.println("\n=== '!' in SQL test ===");
        testTokens("SELECT * FROM table1 WHERE col1 = 'value'!SELECT * FROM table2;");
    }

    private static void testTokens(String input) {
        System.out.println("Input: " + input);

        CharStream charStream = CharStreams.fromString(input);
        Lexer lexer = new DB2zSQLLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();

        List<Token> tokens = tokenStream.getTokens();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            System.out.printf("Token[%d]: type=%d, text='%s', startIndex=%d, stopIndex=%d%n",
                i, token.getType(), token.getText(), token.getStartIndex(), token.getStopIndex());
        }
    }
}