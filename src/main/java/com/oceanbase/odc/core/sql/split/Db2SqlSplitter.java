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

import java.io.InputStream;
import java.nio.charset.Charset;

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;

public class Db2SqlSplitter extends SqlSplitter {

    public Db2SqlSplitter() {
        super(DB2zSQLLexer.class);
    }

    public Db2SqlSplitter(String delimiter) {
        super(DB2zSQLLexer.class, delimiter);
    }

    public Db2SqlSplitter(String delimiter, boolean addDelimiter) {
        super(DB2zSQLLexer.class, delimiter, addDelimiter);
    }

    public static SqlStatementIterator iterator(InputStream input, Charset charset, String delimiter) {
        return iterator(input, charset, delimiter, true);
    }

    public static SqlStatementIterator iterator(InputStream input, Charset charset, String delimiter,
            boolean addDelimiter) {
        return SqlSplitter.iterator(DB2zSQLLexer.class, input, charset, delimiter, addDelimiter);
    }
}
