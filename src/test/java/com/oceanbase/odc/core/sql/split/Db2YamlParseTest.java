package com.oceanbase.odc.core.sql.split;

import com.oceanbase.odc.test.dataloader.DataLoaders;
import org.junit.Test;

public class Db2YamlParseTest {
    @Test
    public void parse_alterIndex_yml() {
        DataLoaders.yaml().fromFile("sql/split/sql-splitter-33-db2-alterIndex.yml", Object.class);
    }
}

