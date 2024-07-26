package com.github.wangyangting.flink.sql.bridge.args;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author wangyangting
 * @date 2024-07-25
 */
public class CommandParser {

    public static CommandLine parse(String[] args) {
        // options
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").argName("file").hasArg().required(true).desc("Flink sql file").build());
        options.addOption(Option.builder("p").longOpt("parallelism").argName("parallelism").hasArg().desc("Job parallelism, default 1").build());
        options.addOption(new Option("cp-n", "checkpointEnable", false,"Checkpoint enable, default false"));
        options.addOption(new Option("cp-i", "checkpointInterval", true,"Checkpoint interval ms, default 600000"));
        options.addOption(new Option("cp-m", "checkpointingMode", true,"Checkpoint mode, option [AT_LEAST_ONCE, EXACTLY_ONCE], default AT_LEAST_ONCE"));
        options.addOption(new Option("cp-t", "checkpointTimeout", true,"Checkpoint timeout ms, default 60000"));
        options.addOption(new Option("cp-mcc", "maxConcurrentCheckpoints", true,"Checkpoint max concurrent checkpoints, default 1"));
        options.addOption(new Option("cp-ecc", "externalizedCheckpointCleanup", true,"Checkpoint externalized checkpoint cleanup, option [RETAIN_ON_CANCELLATION, DELETE_ON_CANCELLATION, NO_EXTERNALIZED_CHECKPOINTS]. default RETAIN_ON_CANCELLATION"));
        options.addOption(new Option("n", "name", true,"Job name, default flink-sql-job-bridge_yyyyMMddHHmmssSSS"));
        options.addOption(new Option("s", "sql", true,"Flink sql"));
        options.addOption(new Option("v", "verbose", false,"Display more verbose info"));
        options.addOption(new Option("h", "help", false,"Help usage"));

        // parser
        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                helper.printHelp("Usage:", options);
                System.exit(0);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helper.printHelp("flink run [-d] flink-sql-job-bridge-1.18.1.jar [args] -f file.sql :", options);
            System.exit(0);
        }

        return cmd;
    }

}
