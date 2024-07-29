# Flink SQL Job Bridge
可提交 Flink SQL 作业到集群中.

## 思路
封装一个Jar，传递参数，解析SQL文件获取多段SQL，然后循环调用 executeSql 方法即可。

## 注意
默认提供的 Jar 没有打进去相关的依赖，需要自行添加相关的依赖到 FLINK_HOME/lib 中。

不过这样很麻烦，可以先打一部分小的依赖到该Jar里面。

## 版本
**flink-1.18.1** 该项目所有依赖基于该版本

## 依赖
默认已经添加以下依赖，按需删除不需要的依赖重新打包即可。
1. org.apache.flink:flink-connector-jdbc
2. mysql:mysql-connector-java
3. com.starrocks:flink-connector-starrocks
4. org.apache.flink:flink-connector-mysql-cdc
5. org.apache.flink:flink-connector-oracle-cdc
6. com.oracle.ojdbc:ojdbc8
7. com.oracle.database.xml:xdb
8. com.oracle.database.jdbc:ojdbc8
9. com.microsoft.sqlserver:mssql-jdbc

## 使用方法
```shell
usage: flink run [-d] flink-sql-job-bridge-1.18.1.jar [args] -f file.sql :
 -cp-ecc,--externalizedCheckpointCleanup <arg>   Checkpoint externalized
                                                 checkpoint cleanup,
                                                 option
                                                 [RETAIN_ON_CANCELLATION,
                                                 DELETE_ON_CANCELLATION,
                                                 NO_EXTERNALIZED_CHECKPOIN
                                                 TS]. default
                                                 RETAIN_ON_CANCELLATION
 -cp-i,--checkpointInterval <arg>                Checkpoint interval ms,
                                                 default 600000
 -cp-m,--checkpointingMode <arg>                 Checkpoint mode, option
                                                 [AT_LEAST_ONCE,
                                                 EXACTLY_ONCE], default
                                                 AT_LEAST_ONCE
 -cp-mcc,--maxConcurrentCheckpoints <arg>        Checkpoint max concurrent
                                                 checkpoints, default 1
 -cp-n,--checkpointEnable                        Checkpoint enable,
                                                 default false
 -cp-t,--checkpointTimeout <arg>                 Checkpoint timeout ms,
                                                 default 60000
 -f,--file <file>                                Flink sql file
 -h,--help                                       Help usage
 -n,--name <arg>                                 Job name, default
                                                 flink-sql-job-bridge_yyyy
                                                 MMddHHmmssSSS
 -p,--parallelism <parallelism>                  Job parallelism, default
                                                 1
 -s,--sql <arg>                                  Flink sql
 -v,--verbose                                    Display more verbose info
```

## 示例
./flink-1.18.1/bin/flink run -d flink-sql-job-bridge-1.18.1.jar -v -f test.sql

## 附 mysql-cdc 到 mysql 的依赖
```shell
./flink-1.18.1/bin/flink run -d \
-C file:////data/opt/flink/_app/ext_lib/flink-connector-mysql-cdc-3.1.1.jar \
-C file:////data/opt/flink/_app/ext_lib/flink-connector-debezium-3.1.1.jar \
-C file:////data/opt/flink/_app/ext_lib/jackson-databind-2.13.2.2.jar \
-C file:////data/opt/flink/_app/ext_lib/jackson-core-2.13.2.jar \
-C file:////data/opt/flink/_app/ext_lib/jackson-annotations-2.13.2.jar \
-C file:////data/opt/flink/_app/ext_lib/flink-connector-jdbc-3.2.0-1.18.jar \
-C file:////data/opt/flink/_app/ext_lib/debezium-core-1.9.8.Final.jar \
-C file:////data/opt/flink/_app/ext_lib/debezium-connector-mysql-1.9.8.Final.jar \
-C file:////data/opt/flink/_app/ext_lib/kafka-clients-3.2.0.jar \
-C file:////data/opt/flink/_app/ext_lib/connect-api-3.2.0.jar \
-C file:////data/opt/flink/_app/ext_lib/debezium-api-1.9.8.Final.jar \
-C file:////data/opt/flink/_app/ext_lib/mysql-connector-java-8.0.27.jar \
-C file:////data/opt/flink/_app/ext_lib/HikariCP-4.0.3.jar \
-C file:////data/opt/flink/_app/ext_lib/debezium-ddl-parser-1.9.8.Final.jar \
-C file:////data/opt/flink/_app/ext_lib/antlr4-runtime-4.8.jar \
-C file:////data/opt/flink/_app/ext_lib/mysql-binlog-connector-java-0.27.2.jar \
-C file:////data/opt/flink/_app/ext_lib/guava-30.1.1-jre.jar \
flink-sql-job-bridge-1.18.1.jar -v -f aaa.sql
```