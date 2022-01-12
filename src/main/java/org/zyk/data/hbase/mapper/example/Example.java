package org.zyk.data.hbase.mapper.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.zyk.data.hbase.mapper.accessor.HbaseClient;
import org.zyk.data.hbase.mapper.mapper.Mapper;
import org.zyk.data.hbase.mapper.mapper.DefaultMapper;

import java.io.IOException;
import java.util.List;

public class Example {
    public static void main(String[] args) throws IOException {

        // 构建hbase连接配置创建连接hbase的客户端
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hb-proxy-pub-bp1h8u56427op7107-001.hbase.rds.aliyuncs.com:2181");

        // 不带参数的方式可以读取本地hbase-site.xml文件作为配置：
        // HbaseClient hbaseClient = HbaseClient.create();
        HbaseClient hbaseClient = HbaseClient.create(conf);

        // 创建mapper
        Mapper mapper = DefaultMapper.create(hbaseClient);

        // 插入数据
        People people1 = new People("010101", "张三", 18, 180.5);
        mapper.put(people1);

        // 判断数据是否存在
        boolean exists = mapper.exists("010101", People.class);

        // 获取数据
        People people2 = mapper.get("010101", People.class);

        // 删除数据
        mapper.delete("010101", People.class);

        // 查询数据
        List<People> peopleList = mapper.scan("010101", "010111", People.class);
    }
}
