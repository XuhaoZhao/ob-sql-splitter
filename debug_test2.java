import com.oceanbase.odc.core.sql.split.Db2StandaloneSqlSplitter;

import java.util.List;

public class debug_test2 {
    public static void main(String[] args) {
        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();

        // Test single '!' character
        System.out.println("=== Single '!' test ===");
        testTokens(splitter, "!");

        // Test '!' in SQL statement
        System.out.println("\n=== '!' in SQL test ===");
        testTokens(splitter, "SELECT 1!SELECT 2");
    }

    private static void testTokens(Db2StandaloneSqlSplitter splitter, String input) {
        System.out.println("Input: " + input);

        // Use reflection to access the private tokensWithoutEOF method
        try {
            java.lang.reflect.Method method = Db2StandaloneSqlSplitter.class.getDeclaredClass("DB2InnerUtils")
                .getDeclaredMethod("tokensWithoutEOF", String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<org.antlr.v4.runtime.Token> tokens = (List<org.antlr.v4.runtime.Token>) method.invoke(splitter.innerUtils, input);

            for (int i = 0; i < tokens.size(); i++) {
                org.antlr.v4.runtime.Token token = tokens.get(i);
                System.out.printf("Token[%d]: type=%d, text='%s', startIndex=%d, stopIndex=%d%n",
                    i, token.getType(), token.getText(), token.getStartIndex(), token.getStopIndex());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}