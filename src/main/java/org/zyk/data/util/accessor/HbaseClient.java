package org.zyk.data.util.accessor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * hbase客户端，封装了Connection和Admin
 */
public class HbaseClient {
    private final Configuration config;

    private Connection connection;

    public static HbaseClient create() {
        return new HbaseClient();
    }

    public static HbaseClient create(Configuration config) {
        return new HbaseClient(config);
    }

    HbaseClient() {
        config = HBaseConfiguration.create();
        config.addResource(new Path("hbase-site.xml"));
    }

    private HbaseClient(Configuration config) {
        this.config = config;
    }

    public Connection getConnection() throws IOException {
        if (connection != null ) {
            if (connection.isClosed()) {
                throw new IOException("hbase connection is closed.");
            }
            return connection;
        }

        synchronized (this) {
            if (connection != null ) {
                if (connection.isClosed()) {
                    throw new IOException("hbase connection is closed.");
                }
                return connection;
            }
            connection = ConnectionFactory.createConnection(config);
            return connection;
        }
    }

    public Admin getAdmin() throws IOException {
        return getConnection().getAdmin();
    }
}
