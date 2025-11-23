package debug;

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;
import org.antlr.v4.runtime.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PrintDb2Tokens {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: PrintDb2Tokens <sql-file>");
            System.exit(2);
        }
        String text = Files.readString(Paths.get(args[0]));
        CharStream input = CharStreams.fromString(text);
        DB2zSQLLexer lexer = new DB2zSQLLexer(input);
        CommonTokenStream ts = new CommonTokenStream(lexer);
        ts.fill();
        int i = 0;
        for (Token t : ts.getTokens()) {
            System.out.printf("%3d: type=%4d text='%s'\n", i++, t.getType(), t.getText().replace("\n", "\\n"));
        }
    }
}

