package com.github.wangyangting.flink.sql.bridge;

import com.github.wangyangting.flink.sql.bridge.args.CommandParser;
import com.github.wangyangting.flink.sql.bridge.flink.FlinkEnvironment;
import com.github.wangyangting.flink.sql.bridge.utils.FileUtils;
import com.github.wangyangting.flink.sql.bridge.utils.SqlUtils;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author wangyangting
 * @date 2024-07-25
 */
public class SqlBridgeJob {

    public static void main(String[] args) throws Exception {
        // args
        CommandLine cmd = CommandParser.parse(args);
        String sql = null;
        if (cmd.hasOption("f")) {
            sql = FileUtils.readFileToString(cmd.getOptionValue("f"));
            if (sql.trim().length() == 0) {
                System.out.println("file is empty, exit 0");
                System.exit(0);
            } else {
                System.out.println("--- file content:\n" + sql);
            }
        }

        // Flink Env
        StreamExecutionEnvironment env = FlinkEnvironment.createStreamExecutionEnvironment(cmd);
        StreamTableEnvironment tableEnv = FlinkEnvironment.createStreamTableEnvironment(env, cmd);

        System.out.println("\n\n--------- sql fragments ---------");

        // sql split
        List<String> sqlList = SqlUtils.split(sql);
        for (int i=0; i<sqlList.size(); i++) {
            String s = sqlList.get(i);
            int serial = i + 1;
            if (s !=null && !s.trim().isEmpty()) {
                System.out.println("--- sql fragment ["+serial+"] execute :\n" + s);
                System.out.println();
                tableEnv.executeSql(s);
            } /*else {
                System.out.println("--- sql fragment ["+serial+"] is empty, skip it " + s);
            }*/
        }

        System.out.println("--------- submitted ---------");
    }

}
