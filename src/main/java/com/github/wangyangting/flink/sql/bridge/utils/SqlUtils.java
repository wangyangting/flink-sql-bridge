package com.github.wangyangting.flink.sql.bridge.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyangting
 * @date 2024-07-26
 */
public class SqlUtils {

    public static List<String> split(String sql) {
        List<String> sqlList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        String[] lines = sql.split("\\r*\\n");
        for (String line : lines) {
            sb.append("\n");

            String[] split = line.split("--");
            String frag = split[0];
            String comment = split.length > 1 ? split[1] : "";

            if (frag.contains(";")) {
                String[] st = frag.split(";+");
                if (st.length > 0) {
                    if (st[0].contains("'") || st[0].contains("\"")) {
                        sb.append(line);
                        continue;
                    } else {
                        sb.append(st[0]);
                    }
                }

                String f = sb.toString().trim();
                if (!f.isEmpty()) {
                    sqlList.add(f);
                }

                sb.setLength(0);

                if (st.length > 1) {
                    sb.append(st[1]);

                    if (!comment.isEmpty()) {
                        sb.append("--").append(comment);
                    }
                }
            } else {
                sb.append(line);
            }
        }

        if (sb.length() > 0) {
            String f = sb.toString().trim();
            if (!f.isEmpty()) {
                sqlList.add(f);
            }
        }

        return sqlList;
    }

    public static void main(String[] args) {
        split("select * ';' from t1 ;").forEach(System.out::println);
        split("select * from t1 ; select a\n , b -- bbb this is normal comment\n , c -- ; ccc ; ... this is sb comment\n from t2\n;;; insert into t1 \n\n\nselect * from t2;").forEach(System.out::println);
    }

}
