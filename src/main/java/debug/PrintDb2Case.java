package debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.oceanbase.odc.core.sql.split.Db2StandaloneSqlSplitter;
import com.oceanbase.odc.core.sql.split.OffsetString;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PrintDb2Case {
    public static class TestData {
        public String origin;
        public List<String> expected;
        public String expected_end_delimiter;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: PrintDb2Case <yaml-file>");
            System.exit(2);
        }
        String path = args[0];
        String yaml = Files.readString(Paths.get(path));
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestData data = mapper.readValue(yaml, TestData.class);
        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();
        List<OffsetString> out = splitter.split(data.origin);
        System.out.println("Statements: " + out.size());
        for (int i = 0; i < out.size(); i++) {
            System.out.println("--- stmt " + i + " ---\n" + out.get(i).getStr());
        }
        System.out.println("Delimiter now: " + splitter.getDelimiter());
    }
}

