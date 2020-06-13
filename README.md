# hbase-mapper
基于HBase JAVA API的封装，提供一个能从JAVA Bean到HBase数据访问客户端。

## 表定义
```
@Table(name = "people")
@Family("cf")
public class People {

    @Rowkey
    private String rowkey;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private double height;

    public People() {
    }

    public People(String rowkey, String name, Integer age, double height) {
        this.rowkey = rowkey;
        this.name = name;
        this.age = age;
        this.height = height;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
```
## 创建Mapper

```
// 构建hbase连接配置创建连接hbase的客户端
Configuration conf = HBaseConfiguration.create();
conf.set("hbase.zookeeper.quorum", "hb-proxy-pub-bp1h8u56427op7107-001.hbase.rds.aliyuncs.com:2181");

// 不带参数的方式可以读取本地hbase-site.xml文件作为配置：
// HbaseClient hbaseClient = HbaseClient.create();
HbaseClient hbaseClient = HbaseClient.create(conf);

// 创建mapper
Mapper mapper = DefaultMapper.create(hbaseClient);
```

## CRUD
```
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
```

### 支持存储的数据类型
- java8大基础数据类型以及对应的Class：int(Integer)、short(Short)、long(Long)、byte(Byte)、double(Double)、float(Float)、boolean(Boolean)、char(Character)
- 常用时间类型：Date、Calendar、LocalDate、LocalDateTime
- 其他常用类型：String、BigDecimal

# hbase-mapper
