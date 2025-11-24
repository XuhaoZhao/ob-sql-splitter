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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.oceanbase.tools.sqlparser.oracle.PlSqlLexer;
import org.junit.Assert;
import org.junit.Test;

import com.oceanbase.odc.test.dataloader.DataLoaders;
import com.oceanbase.odc.test.dataloader.DataLoaders.TestData;
import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;

/**
 * DB2 SQL splitter tests. We execute all DB2-specific YAML cases under
 * src/test/resources/sql/split whose file name contains "db2".
 */
public class Db2SqlSplitterTest {

    @Test
    public void split_Blank_Empty() {
        String sql = " ";
        Db2StandaloneSqlSplitter sqlSplitter = new Db2StandaloneSqlSplitter();
        Assert.assertTrue(sqlSplitter.split(sql).isEmpty());
    }

    @Test
    public void split_AllDb2YamlCases() throws IOException {
        Path dir = Paths.get("src/test/resources/sql/split");
        List<Path> cases = Files.list(dir)
                .filter(p -> p.getFileName().toString().toLowerCase().contains("db2")
                        && p.getFileName().toString().endsWith(".yml"))
                .sorted()
                .collect(Collectors.toList());
        for (Path p : cases) {
            try {

                 verifyByFileName(p.toString());
            } catch (AssertionError ae) {
                System.err.println("DB2 case failed: " + p);
                throw ae;
            } catch (Exception e) {
                System.err.println("DB2 case parse error: " + p + ", reason=" + e.getMessage());
                throw e;
            }
        }
    }

    @Test
    public void split_DebugSingleCase() throws IOException {
        // Debug helper: run single DB2 YAML to inspect actual outputs
        String fileName = "src/test/resources/sql/split/sql-splitter-29-db2-alterFunctionCompiledSqlScalar.yml";
        TestData testData = DataLoaders.yaml().fromFile(toResourcePath(fileName), TestData.class);
        SqlSplitter sqlSplitter = new SqlSplitter(DB2zSQLLexer.class);
        List<String> stmts = sqlSplitter.split(testData.getOrigin()).stream()
                .map(OffsetString::getStr)
                .collect(Collectors.toList());
        System.out.println("DB2 debug statements count=" + stmts.size());
        for (int i = 0; i < stmts.size(); i++) {
            System.out.println("---- stmt " + i + " ----");
            System.out.println(stmts.get(i));
        }
    }
    private String toResourcePath(String fileName) {
        return fileName.replace("src/test/resources/", "")
                      .replace("src\\test\\resources\\", "")
                      .replace('\\', '/');
    }

    private void verifyByFileName(String fileName) throws IOException {
        String resourcePath = toResourcePath(fileName);
        TestData testData = DataLoaders.yaml().fromFile(resourcePath, TestData.class);
        Db2StandaloneSqlSplitterbbb sqlSplitter = new Db2StandaloneSqlSplitterbbb(DB2zSQLLexer.class);
        List<String> stmts = sqlSplitter.split(testData.getOrigin()).stream()
                .map(OffsetString::getStr)
                .map(Db2SqlSplitterTest::normalize)
                .collect(Collectors.toList());
        List<String> expected = testData.getExpected().stream()
                .map(Db2SqlSplitterTest::normalize)
                .collect(Collectors.toList());
        Assert.assertArrayEquals(expected.toArray(new String[0]), stmts.toArray(new String[0]));
        Assert.assertEquals(testData.getExpected_end_delimiter(), sqlSplitter.getDelimiter());
    }

    @Test
    public void split_DebugInlineSqlScalar() throws IOException {
        String fileName = "src/test/resources/sql/split/sql-splitter-31-db2-alterFunctionInlineSqlScalar.yml";
        TestData testData = DataLoaders.yaml().fromFile(toResourcePath(fileName), TestData.class);
        System.out.println("Expected count=" + testData.getExpected().size());
        for (int i = 0; i < testData.getExpected().size(); i++) {
            System.out.println("-- expected " + i + " --\n" + testData.getExpected().get(i));
        }
        SqlSplitter sqlSplitter = new SqlSplitter(DB2zSQLLexer.class);
        List<String> stmts = sqlSplitter.split(testData.getOrigin()).stream()
                .map(OffsetString::getStr)
                .collect(Collectors.toList());
        System.out.println("Actual count=" + stmts.size());
        for (int i = 0; i < stmts.size(); i++) {
            System.out.println("-- actual " + i + " --\n" + stmts.get(i));
        }
        // compare third element endings
        String exp2 = normalize(testData.getExpected().get(2));
        String act2 = normalize(stmts.get(2));
        System.out.println("exp2 tail codes: " + tailCodes(exp2));
        System.out.println("act2 tail codes: " + tailCodes(act2));
    }

    @Test
    public void split_DebugAlternateOperators() throws IOException {
        String fileName = "src/test/resources/sql/split/sql-splitter-35-db2-alternateOperators.yml";
        TestData testData = DataLoaders.yaml().fromFile(toResourcePath(fileName), TestData.class);
        SqlSplitter sqlSplitter = new SqlSplitter(DB2zSQLLexer.class);
        List<String> stmts = sqlSplitter.split(testData.getOrigin()).stream()
                .map(OffsetString::getStr)
                .collect(Collectors.toList());
        System.out.println("Actual count=" + stmts.size());
        for (int i = 0; i < stmts.size(); i++) {
            System.out.println("---- actual " + i + " ----\n" + stmts.get(i));
        }
    }

    private String tailCodes(String s) {
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        for (int i = Math.max(0, n - 8); i < n; i++) {
            char c = s.charAt(i);
            sb.append((int) c).append(' ');
        }
        return sb.toString();
    }

    private static String normalize(String s) {
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
        }).collect(Collectors.joining("\n"));
        return r;
    }
}
