package org.zyk.data.hbase.mapper.mapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zyk.data.hbase.mapper.accessor.HbaseClient;
import org.zyk.data.hbase.mapper.modol.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseMapperTest {
    private Mapper mapper;

    @Before
    public void createMapper() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hb-proxy-pub-bp1h8u56427op7107-001.hbase.rds.aliyuncs.com:2181");
        HbaseClient hbaseClient = HbaseClient.create(conf);
        mapper = DefaultMapper.create(hbaseClient);
    }

    @Test
    public void shortValueTest() throws IOException {
        ShortValueBean bean = new ShortValueBean("1", (short) 10, (short) 20, (short) 30, (short) 40);
        mapper.put(bean);
        ShortValueBean getBean = mapper.get("1", ShortValueBean.class);
        Assert.assertEquals(bean, getBean);

        bean = new ShortValueBean("2", (short) 11, (short) 20, (short) 30, (short) 40);
        mapper.put(bean);

        List<ShortValueBean> beanList = mapper.scan("1", "3", ShortValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void longValueTest() throws IOException {
        LongValueBean putBean1 = new LongValueBean("1", 10L, 20L, 30L, 40L);
        mapper.put(putBean1);
        LongValueBean getBean = mapper.get("1", LongValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new LongValueBean("2", 10L, 20L, 30L, 40L);
        mapper.put(putBean1);

        List<LongValueBean> beanList = mapper.scan("1", "3", LongValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void integerValueTest() throws IOException {
        IntegerValueBean putBean1 = new IntegerValueBean("1", 10, 20, 30, 40);
        mapper.put(putBean1);
        IntegerValueBean getBean = mapper.get("1", IntegerValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new IntegerValueBean("2", 10, 20, 30, 40);
        mapper.put(putBean1);

        List<IntegerValueBean> beanList = mapper.scan("1", "3", IntegerValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void booleanValueTest() throws IOException {
        BooleanValueBean putBean1 = new BooleanValueBean("1", true, false, true, false);
        mapper.put(putBean1);
        BooleanValueBean getBean = mapper.get("1", BooleanValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new BooleanValueBean("2", true, false, true, false);
        mapper.put(putBean1);

        List<BooleanValueBean> beanList = mapper.scan("1", "3", BooleanValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void floatValueTest() throws IOException {
        FloatValueBean putBean1 = new FloatValueBean("1", 10.0f, 20.123f, 30.45631f, 1234567.45631f);
        mapper.put(putBean1);
        FloatValueBean getBean = mapper.get("1", FloatValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new FloatValueBean("2", 10.0f, 20.123f, 30.45631f, 1234567.45631f);
        mapper.put(putBean1);

        List<FloatValueBean> beanList = mapper.scan("1", "3", FloatValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void doubleValueTest() throws IOException {
        DoubleValueBean putBean1 = new DoubleValueBean("1", 10.0, 20.123, 30.45631, 1234567.45631);
        mapper.put(putBean1);
        DoubleValueBean getBean = mapper.get("1", DoubleValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new DoubleValueBean("2", 10.0, 20.123, 30.45631, 1234567.45631);
        mapper.put(putBean1);

        List<DoubleValueBean> beanList = mapper.scan("1", "3", DoubleValueBean.class);
        assert beanList.size() == 2;
    }

    @Test
    public void stringValueTest() throws IOException {
        StringValueBean putBean1 = new StringValueBean("1", "test", "test1", "test2");
        mapper.put(putBean1);
        StringValueBean getBean = mapper.get("1", StringValueBean.class);
        Assert.assertEquals(putBean1, getBean);

        putBean1 = new StringValueBean("2", "test", "test1", "test2");
        mapper.put(putBean1);

        List<StringValueBean> beanList = mapper.scan("1", "3", StringValueBean.class);
        assert beanList.size() == 2;

        // 清理
        mapper.delete("1", StringValueBean.class);
        mapper.delete("2", StringValueBean.class);
    }

    @Test
    public void deleteTest() throws IOException {
        StringValueBean putBean1 = new StringValueBean("1", "test", "test1", "test2");
        mapper.put(putBean1);

        mapper.delete("1", LongValueBean.class);
        boolean exists = mapper.exists("1", LongValueBean.class);
        assert !exists;
    }

    @Test
    public void deleteBatchTest() throws IOException {
        for (int i = 0; i <= 11; i++) {
            StringValueBean putBean1 = new StringValueBean(String.valueOf(i), "test", "test1", "test2");
            mapper.put(putBean1);

            boolean exists = mapper.exists(String.valueOf(i), StringValueBean.class);
            assert exists;
        }

        for (int i = 0; i <= 10; i++) {
            mapper.delete(String.valueOf(i), StringValueBean.class);
            boolean exists = mapper.exists(String.valueOf(i), StringValueBean.class);
            assert !exists;
        }

        boolean exists = mapper.exists("11", StringValueBean.class);
        assert exists;
    }

    @Test
    public void existsTest() throws IOException {
        DoubleValueBean putBean1 = new DoubleValueBean("1", 10.0, 20.123, 30.45631, 1234567.45631);
        mapper.put(putBean1);

        boolean exists = mapper.exists("1", StringValueBean.class);
        assert exists;

        mapper.delete("1", StringValueBean.class);
    }

    @Test
    public void batchPutTest() throws IOException {
        List<DoubleValueBean> beanList = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            DoubleValueBean putBean1 = new DoubleValueBean(String.valueOf(i), 10.0, 20.123, 30.45631, 1234567.45631);
            beanList.add(putBean1);
        }
        mapper.put(beanList);

        for (int i = 0; i <= 100; i++) {
            boolean exists = mapper.exists(String.valueOf(i), DoubleValueBean.class);
            assert exists;

            mapper.delete(String.valueOf(i), StringValueBean.class);
        }
    }

    @Test
    public void getByColumnsTest() throws IOException {
        DoubleValueBean putBean1 = new DoubleValueBean("1", 10.0, 20.123, 30.45631, 1234567.45631);
        mapper.put(putBean1);

        List list = mapper.get("1", "test_table", Arrays.asList(Double.class, double.class),
                Arrays.asList("double:value", "double:valuePrimitive"));

        Assert.assertEquals(list.get(0), 10.0);
        Assert.assertEquals(list.get(1), 20.123);
    }

    @Test
    public void scanTest() throws IOException {
        DoubleValueBean putBean1 = new DoubleValueBean("1", 10.0, 20.123, 30.45631, 1234567.45631);
        mapper.put(putBean1);

        DoubleValueBean putBean2 = new DoubleValueBean("2", 10.1, 22.123, 32.45631, 12345672.45631);
        mapper.put(putBean2);

        List<List<Object>> list = mapper.scan("1", "3", "test_table",
                Arrays.asList(Double.class, double.class),
                Arrays.asList("double:value", "double:valuePrimitive"));

        Assert.assertEquals(list.get(0).get(0), putBean1.getValue());
        Assert.assertEquals(list.get(0).get(1), putBean1.getValuePrimitive());
        Assert.assertEquals(list.get(1).get(0), putBean2.getValue());
        Assert.assertEquals(list.get(1).get(1), putBean2.getValuePrimitive());
    }
}
