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
package com.oceanbase.odc.core.shared.constant;

/**
 * Database dialect types
 */
public enum DialectType {

    OB_MYSQL,
    OB_ORACLE,
    ORACLE,
    MYSQL,
    ODP_SHARDING_OB_MYSQL,
    DORIS;

    public boolean isMysqlFamily() {
        return this == OB_MYSQL || this == MYSQL || this == ODP_SHARDING_OB_MYSQL;
    }

    public boolean isOracleFamily() {
        return this == OB_ORACLE || this == ORACLE;
    }

    public boolean isMysql() {
        return isMysqlFamily();
    }

    public boolean isOracle() {
        return isOracleFamily();
    }

    public boolean isDoris() {
        return this == DORIS;
    }
}