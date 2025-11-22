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

import org.antlr.v4.runtime.Token;

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;

import com.oceanbase.tools.sqlparser.obdb2.DB2zSQLLexer;

class Db2LexerDefinition implements LexerTokenDefinition {

    @Override
    public int DIV() {
        return DB2zSQLLexer.SLASH;
    }

    @Override
    public int SPACES() {
        return DB2zSQLLexer.WS;
    }

    @Override
    public int ANTLR_SKIP() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int SINGLE_LINE_COMMENT() {
        return DB2zSQLLexer.SQLCOMMENT;
    }

    @Override
    public int MULTI_LINE_COMMENT() {
        return DB2zSQLLexer.SQLCOMMENT;
    }

    @Override
    public int DECLARE() {
        return DB2zSQLLexer.DECLARE;
    }

    @Override
    public int BEGIN() {
        return DB2zSQLLexer.BEGIN;
    }

    @Override
    public int END() {
        return DB2zSQLLexer.END;
    }

    @Override
    public int CREATE() {
        return DB2zSQLLexer.CREATE;
    }

    @Override
    public int OR() {
        return DB2zSQLLexer.OR;
    }

    @Override
    public int REPLACE() {
        return DB2zSQLLexer.REPLACE;
    }

    @Override
    public int EDITIONABLE() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int NONEDITIONABLE() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int PROCEDURE() {
        return DB2zSQLLexer.PROCEDURE;
    }

    @Override
    public int FUNCTION() {
        return DB2zSQLLexer.FUNCTION;
    }

    @Override
    public int PACKAGE() {
        return DB2zSQLLexer.PACKAGE;
    }

    @Override
    public int TYPE() {
        return DB2zSQLLexer.TYPE;
    }

    @Override
    public int TRIGGER() {
        return DB2zSQLLexer.TRIGGER;
    }

    @Override
    public int ALTER() {
        return DB2zSQLLexer.ALTER;
    }

    @Override
    public int BODY() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int IDENT() {
        return DB2zSQLLexer.SQLIDENTIFIER;
    }

    @Override
    public int REGULAR_ID() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int DELIMITED_ID() {
        return Token.MIN_USER_TOKEN_TYPE;
    }

    @Override
    public int FOR() {
        return DB2zSQLLexer.FOR;
    }

    @Override
    public int LOOP() {
        return DB2zSQLLexer.LOOP;
    }

    @Override
    public int IF() {
        return DB2zSQLLexer.IF;
    }

    @Override
    public int REPEAT() {
        return DB2zSQLLexer.REPEAT;
    }

    @Override
    public int CASE() {
        return DB2zSQLLexer.CASE;
    }

    @Override
    public int LANGUAGE() {
        return DB2zSQLLexer.LANGUAGE;
    }

    @Override
    public int EXTERNAL() {
        return DB2zSQLLexer.EXTERNAL;
    }

    @Override
    public int IS() {
        return DB2zSQLLexer.IS;
    }

    @Override
    public int AS() {
        return DB2zSQLLexer.AS;
    }

    @Override
    public int MEMBER() {
        return DB2zSQLLexer.MEMBER;
    }

    @Override
    public int STATIC() {
        return DB2zSQLLexer.STATIC;
    }

    @Override
    public int SEMICOLON() {
        return DB2zSQLLexer.SEMICOLON;
    }

    @Override
    public int ELSE() {
        return DB2zSQLLexer.ELSE;
    }

    @Override
    public int THEN() {
        return DB2zSQLLexer.THEN;
    }

    @Override
    public int RIGHTBRACKET() {
        return DB2zSQLLexer.RPAREN;
    }

    @Override
    public int LEFTBRACKET() {
        return DB2zSQLLexer.LPAREN;
    }

    @Override
    public int GREATER_THAN_OP() {
        return DB2zSQLLexer.GT;
    }

    @Override
    public int WHILE() {
        return DB2zSQLLexer.WHILE;
    }
}
