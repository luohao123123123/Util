package com.Utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HbaseUtil {
    private static final String ips = "master,slave1,slave2";

    public HbaseUtil() {
    }

    public static Configuration getConfig() {
        return getConfig(ips);
    }

    public static Configuration getConfig(String ips) {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", ips);
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.client.keyvalue.maxsize", "524288000");
        config.set("hbase.client.operation.timeout", "300000");
        config.set("hbase.rpc.timeout", "300000");
        config.setLong("hbase.client.scanner.caching", 50L);
        config.setLong("hbase.client.scanner.timeout.period", 240000L);
        return config;
    }

    public static Connection getConnection() {
        try {
            return ConnectionFactory.createConnection(getConfig());
        } catch (IOException var1) {
            var1.printStackTrace();
            return null;
        }
    }
}
