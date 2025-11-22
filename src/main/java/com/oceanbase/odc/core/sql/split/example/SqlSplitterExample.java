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
package com.oceanbase.odc.core.sql.split.example;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.core.sql.split.SqlSplitter;
import com.oceanbase.odc.core.sql.split.SqlStatementIterator;
import com.oceanbase.tools.sqlparser.oracle.PlSqlLexer;

/**
 * SqlSplitter使用示例
 */
public class SqlSplitterExample {

    public static void main(String[] args) {
        // 示例1: 基本分割
        basicSplittingExample();

        // 示例2: 自定义分隔符
        customDelimiterExample();

        // 示例3: 流式处理
        streamingExample();
    }

    /**
     * 基本分割示例
     */
    public static void basicSplittingExample() {
        System.out.println("=== 基本分割示例 ===");

        String sql = "CREATE PROCEDURE proc1 AS BEGIN NULL; END; /\n" +
                    "CREATE FUNCTION func1 RETURN NUMBER IS BEGIN RETURN 1; END; /\n" +
                    "SELECT * FROM table1;";

        SqlSplitter splitter = new SqlSplitter(PlSqlLexer.class);
        List<OffsetString> statements = splitter.split(sql);

        System.out.println("原始SQL:");
        System.out.println(sql);
        System.out.println("\n分割结果 (" + statements.size() + " 条语句):");

        for (int i = 0; i < statements.size(); i++) {
            OffsetString stmt = statements.get(i);
            System.out.println("语句 " + (i + 1) + ": " + stmt.getStr());
            System.out.println("偏移量: " + stmt.getOffset());
            System.out.println("---");
        }
    }

    /**
     * 自定义分隔符示例
     */
    public static void customDelimiterExample() {
        System.out.println("=== 自定义分隔符示例 ===");

        String sql = "CREATE PROCEDURE proc1 AS BEGIN NULL; END; $$\n" +
                    "CREATE FUNCTION func1 RETURN NUMBER IS BEGIN RETURN 1; END; $$";

        SqlSplitter splitter = new SqlSplitter(PlSqlLexer.class, "$$");
        List<OffsetString> statements = splitter.split(sql);

        System.out.println("原始SQL:");
        System.out.println(sql);
        System.out.println("\n使用$$分隔符分割结果 (" + statements.size() + " 条语句):");

        for (int i = 0; i < statements.size(); i++) {
            OffsetString stmt = statements.get(i);
            System.out.println("语句 " + (i + 1) + ": " + stmt.getStr());
        }
    }

    /**
     * 流式处理示例
     */
    public static void streamingExample() {
        System.out.println("=== 流式处理示例 ===");

        String sql = "CREATE PROCEDURE proc1 AS BEGIN NULL; END; /\n" +
                    "CREATE FUNCTION func1 RETURN NUMBER IS BEGIN RETURN 1; END; /\n" +
                    "SELECT * FROM table1; SELECT * FROM table2;";

        SqlStatementIterator iterator = SqlSplitter.iterator(
            new java.io.ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8, ";"
        );

        System.out.println("使用迭代器逐条处理SQL语句:");
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            OffsetString stmt = iterator.next();
            System.out.println("语句 " + count + ": " + stmt.getStr());
        }
    }
}