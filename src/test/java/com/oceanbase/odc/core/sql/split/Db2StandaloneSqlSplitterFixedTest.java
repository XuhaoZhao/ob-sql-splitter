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

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;
import org.junit.Assert;
import org.junit.Test;

import com.oceanbase.odc.test.dataloader.DataLoaders;
import com.oceanbase.odc.test.dataloader.DataLoaders.TestData;

/**
 * Test for the fixed Db2StandaloneSqlSplitter with DB2 EXEC SQL support.
 * This test specifically verifies the functionality for the case in
 * sql-splitter-49-db2-call.yml
 */
public class

Db2StandaloneSqlSplitterFixedTest {

    @Test
    public void split_Db2AlternateOperators() throws Exception {
        // Load the test data from the YAML file
        String fileName = "src/test/resources/sql/split/sql-splitter-49-db2-call.yml";
        TestData testData = DataLoaders.yaml().fromFile(fileName.replace("src/test/resources/", ""), TestData.class);

        // Create the fixed DB2 splitter
        Db2StandaloneSqlSplitterFixed sqlSplitter = new Db2StandaloneSqlSplitterFixed(DB2zSQLLexer.class);

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