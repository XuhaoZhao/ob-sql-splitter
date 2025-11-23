# OB SQL Splitter

一个基于ob-sql-parse的SQL分割库，专门用于解析和分割Oracle PL/SQL语句。

## 功能特性

- 支持Oracle PL/SQL语句的智能分割
- 基于ob-sql-parse核心解析器
- 支持多种分隔符（`;`, `/`, `$$`等）
- 处理复杂的PL/SQL块结构（存储过程、函数、触发器等）
- 提供注释处理功能
- 支持流式处理大量SQL语句

## 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>com.oceanbase</groupId>
    <artifactId>ob-sql-splitter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 基本用法

```java


// 创建SQL分割器
SqlSplitter splitter=new SqlSplitter(PlSqlLexer.class);

// 分割SQL语句
        String sql="CREATE PROCEDURE proc1 AS BEGIN NULL; END; / CREATE PROCEDURE proc2 AS BEGIN NULL; END; /";
        List<OffsetString> statements=splitter.split(sql);

// 输出分割结果
        for(OffsetString stmt:statements){
        System.out.println("SQL: "+stmt.getStr());
        System.out.println("Offset: "+stmt.getOffset());
        }
```

### 使用自定义分隔符

```java
// 使用自定义分隔符
SqlSplitter splitter = new SqlSplitter(PlSqlLexer.class, "$$");
```

### 流式处理

```java
import com.oceanbase.odc.core.sql.split.SqlStatementIterator;

// 创建迭代器
SqlStatementIterator iterator = SqlSplitter.iterator(inputStream, StandardCharsets.UTF_8, ";");

// 逐条处理SQL语句
while (iterator.hasNext()) {
    OffsetString statement = iterator.next();
    // 处理单个SQL语句
    System.out.println(statement.getStr());
}
```

## 项目结构

```
ob-sql-splitter/
├── src/main/java/com/oceanbase/odc/core/sql/split/
│   ├── SqlSplitter.java              # 主要的SQL分割器
│   ├── SqlStatementIterator.java     # 流式处理迭代器
│   ├── SqlCommentProcessor.java      # 注释处理器
│   ├── LexerFactory.java             # 词法分析器工厂接口
│   ├── LexerFactories.java           # 词法分析器工厂实现
│   ├── LexerTokenDefinition.java     # 词法Token定义接口
│   ├── LexerTokenDefinitions.java    # 词法Token定义实现
│   ├── OffsetString.java             # 带偏移量的字符串
│   ├── OracleLexerFactory.java       # Oracle词法分析器工厂
│   ├── OracleLexerDefinition.java    # Oracle词法定义
│   ├── OBOraclePLLexerFactory.java   # OB Oracle PL词法分析器工厂
│   └── OBOraclePLLexerDefinition.java # OB Oracle PL词法定义
├── src/test/java/...                 # 测试代码
├── src/test/resources/sql/split/     # 测试数据
└── pom.xml                          # Maven配置
```

## 核心依赖

- **ob-sql-parser**: 核心SQL解析器（版本1.4.2）
- **ANTLR4**: 语法分析器运行时
- **Lombok**: 代码生成工具
- **SLF4J**: 日志接口

## 构建要求

- JDK 17+
- Maven 3.6+

## 构建项目

```bash
mvn clean compile    # 编译项目
mvn test             # 运行测试
mvn package          # 打包项目
```

## 支持的SQL结构

- 匿名PL/SQL块
- 存储过程（PROCEDURE）
- 函数（FUNCTION）
- 触发器（TRIGGER）
- 包（PACKAGE）
- 包体（PACKAGE BODY）
- 类型（TYPE）
- 类型体（TYPE BODY）
- 各种控制结构（IF-ELSE, CASE, LOOP, WHILE, FOR等）

## 许可证

Apache License 2.0