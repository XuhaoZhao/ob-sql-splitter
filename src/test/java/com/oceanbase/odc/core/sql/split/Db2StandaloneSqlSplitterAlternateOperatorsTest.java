/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.core.sql.split;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.oceanbase.odc.test.dataloader.DataLoaders;
import com.oceanbase.odc.test.dataloader.DataLoaders.TestData;

/**
 * Test for Db2StandaloneSqlSplitter with DB2 alternate operators.
 * This test specifically verifies the functionality for the case in
 * sql-splitter-35-db2-alternateOperators.yml
 */
public class Db2StandaloneSqlSplitterAlternateOperatorsTest {

    @Test
    public void split_Db2AlternateOperators() throws Exception {
        // Load the test data from the YAML file
        String fileName = "src/test/resources/sql/split/sql-splitter-35-db2-alternateOperators.yml";
        TestData testData = DataLoaders.yaml().fromFile(fileName.replace("src/test/resources/", ""), TestData.class);

        // Create the standalone DB2 splitter
        Db2StandaloneSqlSplitter sqlSplitter = new Db2StandaloneSqlSplitter();

        // Split the SQL
        List<String> actualStatements = sqlSplitter.split(testData.getOrigin()).stream()
                .map(OffsetString::getStr)
                .map(this::normalize)
                .collect(Collectors.toList());

        // Normalize expected statements
        List<String> expectedStatements = testData.getExpected().stream()
                .map(this::normalize)
                .collect(Collectors.toList());

        // Verify the split results
        Assert.assertEquals("Statement count should match", expectedStatements.size(), actualStatements.size());
        Assert.assertArrayEquals("Statements should match expected results",
                expectedStatements.toArray(new String[0]),
                actualStatements.toArray(new String[0]));

        // Verify the delimiter is correctly set back to default
        Assert.assertEquals("Delimiter should be reset to default",
                testData.getExpected_end_delimiter(),
                sqlSplitter.getDelimiter());
    }

    @Test
    public void split_Db2AlternateOperators_VerifyDelimiterChange() {
        // Test SQL with DB2 alternate operators and terminator change
        String sql = "SELECT C1 FROM T1 WHERE C2 != 'X';\n" +
                     "--#SET TERMINATOR !\n" +
                     "CREATE PROCEDURE TACHI.ROCINANTE.HOLDEN\n" +
                     "  DYNAMIC RESULT SETS 15\n" +
                     "  RETURN 7\n" +
                     ";\n" +
                     "!";

        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();
        List<String> statements = splitter.split(sql).stream()
                .map(OffsetString::getStr)
                .collect(Collectors.toList());

        // Should split into 2 statements
        Assert.assertEquals("Should split into 2 statements", 2, statements.size());

        // First statement should be the SELECT
        Assert.assertTrue("First statement should be SELECT",
                statements.get(0).trim().startsWith("SELECT"));
        Assert.assertTrue("First statement should end with ;",
                statements.get(0).trim().endsWith(";"));

        // Second statement should include the CREATE PROCEDURE with the terminator comment
        Assert.assertTrue("Second statement should include SET TERMINATOR",
                statements.get(1).contains("--#SET TERMINATOR !"));
        Assert.assertTrue("Second statement should include CREATE PROCEDURE",
                statements.get(1).contains("CREATE PROCEDURE"));

        // Delimiter should be reset to default after processing
        Assert.assertEquals("Delimiter should be reset to ;", ";", splitter.getDelimiter());
    }

    @Test
    public void split_Db2AlternateOperators_AllOperators() {
        // Test all DB2 alternate operators in separate statements
        String sql = "SELECT C1 FROM T1 WHERE C2 != 'X';\n" +
                     "SELECT C1 FROM T1 WHERE C2 !< 'X';\n" +
                     "SELECT C1 FROM T1 WHERE C2 !> 'X';";

        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();
        List<String> statements = splitter.split(sql).stream()
                .map(OffsetString::getStr)
                .map(this::normalize)
                .collect(Collectors.toList());

        // Should split into 3 statements
        Assert.assertEquals("Should split into 3 statements", 3, statements.size());

        // Verify each statement contains the correct operator
        Assert.assertTrue("First statement should have != operator",
                statements.get(0).contains("!="));
        Assert.assertTrue("Second statement should have !< operator",
                statements.get(1).contains("!<"));
        Assert.assertTrue("Third statement should have !> operator",
                statements.get(2).contains("!>"));
    }

    @Test
    public void split_Db2AlternateOperators_WithEmptyLines() {
        // Test with empty lines between statements
        String sql = "SELECT C1 FROM T1 WHERE C2 != 'X';\n\n" +
                     "SELECT C1 FROM T1 WHERE C2 !< 'X';\n\n\n" +
                     "SELECT C1 FROM T1 WHERE C2 !> 'X';";

        Db2StandaloneSqlSplitter splitter = new Db2StandaloneSqlSplitter();
        List<String> statements = splitter.split(sql).stream()
                .map(OffsetString::getStr)
                .collect(Collectors.toList());

        // Should still split into 3 statements despite empty lines
        Assert.assertEquals("Should split into 3 statements despite empty lines", 3, statements.size());
    }

    /**
     * Normalize string by replacing Windows newlines with Unix newlines
     * and trimming trailing spaces on each line.
     */
    private String normalize(String s) {
        if (s == null) {
            return null;
        }
        // Unify newlines
        String r = s.replace("\r\n", "\n");
        // Trim trailing spaces on each line
        r = r.lines().map(line -> {
            String t = line.replaceAll("\\s+$", "");
            // If line is only semicolon with leading spaces, drop the spaces
            if (t.matches("\\s*;")) {
                return ";";
            }
            return t;
        }).collect(java.util.stream.Collectors.joining("\n"));
        return r;
    }
}