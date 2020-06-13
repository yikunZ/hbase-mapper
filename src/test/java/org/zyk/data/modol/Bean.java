package org.zyk.data.modol;

import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "bean")
@Family
public class Bean {
    @Rowkey
    private String rowkey;

    @Column
    private Short value;

    @Column
    private Long value2;

    @Column
    private LocalDateTime now;

    @Column
    private Integer age;

    @Column
    private int intValue;

    @Column
    private BigDecimal price;

    @Column
    private Date date;

    @Column(name = "column1")
    private String withColumnName;

    @Column(defaultValue = "defaultValue")
    private String defaultValueColumn;

    @Column
    private char charColumn;

    private String noAnnotation;

    public Bean() {
    }

    public Bean(String rowkey, Short value, long value2,
                int age, int intValue, BigDecimal price, Date date,
                String noAnnotation, String withColumnName, char charColumn) {
        this.rowkey = rowkey;
        this.value = value;
        this.value2 = value2;
        this.now = LocalDateTime.now();
        this.age = age;
        this.price = price;
        this.intValue = intValue;
        this.date = date;
        this.noAnnotation = noAnnotation;
        this.withColumnName = withColumnName;
        this.charColumn = charColumn;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public char getCharColumn() {
        return charColumn;
    }

    public void setCharColumn(char charColumn) {
        this.charColumn = charColumn;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Short getValue() {
        return this.value;
    }

    public void setValue(Short value) {
        this.value = value;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getValue2() {
        return value2;
    }

    public void setValue2(Long value2) {
        this.value2 = value2;
    }

    public String getNoAnnotation() {
        return noAnnotation;
    }

    public void setNoAnnotation(String noAnnotation) {
        this.noAnnotation = noAnnotation;
    }

    public String getWithColumnName() {
        return withColumnName;
    }

    public void setWithColumnName(String withColumnName) {
        this.withColumnName = withColumnName;
    }

    public String getDefaultValueColumn() {
        return defaultValueColumn;
    }

    public void setDefaultValueColumn(String defaultValueColumn) {
        this.defaultValueColumn = defaultValueColumn;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "rowkey='" + rowkey + '\'' +
                ", name=" + value +
                ", value2=" + value2 +
                ", now=" + now +
                ", age=" + age +
                ", price=" + price +
                ", date=" + date +
                ", withColumnName='" + withColumnName + '\'' +
                ", defaultValueColumn='" + defaultValueColumn + '\'' +
                ", charColumn=" + charColumn +
                ", noAnnotation='" + noAnnotation + '\'' +
                '}';
    }
}
