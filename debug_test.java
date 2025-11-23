import com.oceanbase.odc.core.sql.split.*;
import java.util.List;

public class DebugTest {
    public static void main(String[] args) {
        String sql = "--#SET TERMINATOR !\n\nCREATE PROCEDURE TACHI.ROCINANTE.HOLDEN\n  DYNAMIC RESULT SETS 15\n  RETURN 7\n;\n!\nSELECT C1\nFROM T1\nWHERE C2 != 'X'\n;";

        System.out.println("=== Using Original SqlSplitter ===");
        SqlSplitter originalSplitter = new SqlSplitter(com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer.class);
        List<OffsetString> originalStmts = originalSplitter.split(sql);
        for (int i = 0; i < originalStmts.size(); i++) {
            System.out.println("--- Statement " + i + " ---");
            System.out.println("'" + originalStmts.get(i).getStr() + "'");
        }
        System.out.println("Delimiter: " + originalSplitter.getDelimiter());

        System.out.println("\n=== Using Db2StandaloneSqlSplitter ===");
        Db2StandaloneSqlSplitter standaloneSplitter = new Db2StandaloneSqlSplitter();
        List<OffsetString> standaloneStmts = standaloneSplitter.split(sql);
        for (int i = 0; i < standaloneStmts.size(); i++) {
            System.out.println("--- Statement " + i + " ---");
            System.out.println("'" + standaloneStmts.get(i).getStr() + "'");
        }
        System.out.println("Delimiter: " + standaloneSplitter.getDelimiter());
    }
}