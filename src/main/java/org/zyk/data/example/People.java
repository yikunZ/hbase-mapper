package org.zyk.data.example;

import org.zyk.data.annotation.Column;
import org.zyk.data.annotation.Family;
import org.zyk.data.annotation.Rowkey;
import org.zyk.data.annotation.Table;
import org.zyk.data.util.annotation.*;

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