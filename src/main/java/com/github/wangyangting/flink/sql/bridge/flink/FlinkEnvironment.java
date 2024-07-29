package com.github.wangyangting.flink.sql.bridge.flink;

import com.github.wangyangting.flink.sql.bridge.utils.DateUtils;
import java.util.Date;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author wangyangting
 * @date 2024-07-25
 */
public class FlinkEnvironment {

    public static StreamExecutionEnvironment createStreamExecutionEnvironment(CommandLine commandLine) throws ParseException {
        // parameters
        int parallelism = Integer.parseInt(commandLine.getOptionValue("p", "1"));
        boolean checkpointEnable = Boolean.parseBoolean(commandLine.getOptionValue("cp-n", "false"));  // default 10min
        long checkpointInterval = Long.parseLong(commandLine.getOptionValue("cp-i", 1000 * 60 * 10 + ""));  // default 10min
        long checkpointTimeout = Long.parseLong(commandLine.getParsedOptionValue("cp-t", 1000 * 60 * 10 + ""));  // default 10min
        int maxConcurrentCheckpoints = Integer.parseInt(commandLine.getParsedOptionValue("cp-mcc", 1 + ""));

        CheckpointingMode checkpointingMode = null;
        String cpm = commandLine.getParsedOptionValue("cp-m", "AT_LEAST_ONCE");
        if ("AT_LEAST_ONCE".equalsIgnoreCase(cpm)) {
            checkpointingMode = CheckpointingMode.AT_LEAST_ONCE;
        } else if ("EXACTLY_ONCE".equalsIgnoreCase(cpm)) {
            checkpointingMode = CheckpointingMode.EXACTLY_ONCE;
        } else {
            System.out.println("CheckpointingMode["+cpm+"] not found, exit");
            System.exit(0);
        }

        ExternalizedCheckpointCleanup externalizedCheckpointCleanup = null;
        String ecc = commandLine.getParsedOptionValue("cp-ecc", "RETAIN_ON_CANCELLATION");
        if ("RETAIN_ON_CANCELLATION".equalsIgnoreCase(ecc)) {
            externalizedCheckpointCleanup = ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION;
        } else if ("DELETE_ON_CANCELLATION".equalsIgnoreCase(ecc)) {
            externalizedCheckpointCleanup = ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION;
        } else if ("NO_EXTERNALIZED_CHECKPOINTS".equalsIgnoreCase(ecc)) {
            externalizedCheckpointCleanup = ExternalizedCheckpointCleanup.NO_EXTERNALIZED_CHECKPOINTS;
        } else {
            System.out.println("ExternalizedCheckpointCleanup["+ecc+"] not found, exit");
            System.exit(0);
        }

        // verbose
        if (commandLine.hasOption("v")) {
            System.out.println("--------- parameters ---------");
            System.out.println("parallelism: " + parallelism);
            System.out.println("checkpointEnable: " + checkpointEnable);
            System.out.println("checkpointInterval: " + checkpointInterval);
            System.out.println("checkpointingMode: " + checkpointingMode.name());
            System.out.println("checkpointTimeout: " + checkpointTimeout);
            System.out.println("checkpointMaxConcurrentCheckpoints: " + maxConcurrentCheckpoints);
            System.out.println("checkpointExternalizedCheckpointCleanup: " + externalizedCheckpointCleanup.name());
            System.out.println("file: " + commandLine.getOptionValue("f"));
        }

        // env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);

        if (checkpointEnable) {
            env.enableCheckpointing(checkpointInterval);
            env.getCheckpointConfig().setCheckpointTimeout(checkpointTimeout);
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(maxConcurrentCheckpoints);
            env.getCheckpointConfig().setCheckpointingMode(checkpointingMode);
            env.getCheckpointConfig().setExternalizedCheckpointCleanup(externalizedCheckpointCleanup);
        }

        return env;
    }

    public static StreamTableEnvironment createStreamTableEnvironment(StreamExecutionEnvironment env, CommandLine commandLine) {
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String name = commandLine.getOptionValue("n", "flink-sql-job-bridge_" + DateUtils.formatString(new Date(), "yyyyMMddHHmmssSSS"));
        tableEnv.getConfig().getConfiguration().setString("pipeline.name", name);

        if (commandLine.hasOption("v")) {
            System.out.println("pipeline.name: " + name);
        }

        return tableEnv;
    }

}
