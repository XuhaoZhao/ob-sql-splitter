import com.oceanbase.odc.core.sql.split.*;
import java.util.List;

public class DebugTest2 {
    public static void main(String[] args) {
        String sql = "--#SET TERMINATOR !\n\nCREATE PROCEDURE TACHI.ROCINANTE.HOLDEN\n  DYNAMIC RESULT SETS 15\n  RETURN 7\n;\n!\nSELECT C1\nFROM T1\nWHERE C2 != 'X'\n;";

        System.out.println("=== Testing Db2StandaloneSqlSplitter ===");
        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();
        List<OffsetString> stmts = splitter.split(sql);

        System.out.println("Total statements: " + stmts.size());
        for (int i = 0; i < stmts.size(); i++) {
            System.out.println("--- Statement " + i + " ---");
            System.out.println("'" + stmts.get(i).getStr() + "'");
            System.out.println("Length: " + stmts.get(i).getStr().length());
        }
    }
}