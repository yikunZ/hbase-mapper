package org.zyk.data.util.model;

import net.sf.cglib.reflect.FastClass;
import org.zyk.data.util.annotation.Column;
import org.zyk.data.util.annotation.Family;
import org.zyk.data.util.annotation.Rowkey;
import org.zyk.data.util.annotation.Table;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 表定义信息
 */
public class TableDefineInfo {
    private Class<?> clazz;

    private FastClass fastClass;

    private Family familyAnnotation;

    private Table tableAnnotation;

    private String familyName;

    private String tableName;

    private boolean nullValueHandle;

    private ColumnDefineInfo rowkeyColumn;

    private HashMap<String, ColumnDefineInfo> columns;

    public TableDefineInfo(Class<?> clazz) {
        this.clazz = clazz;
        this.fastClass = FastClass.create(clazz);

        this.familyAnnotation = clazz.getAnnotation(Family.class);
        if (familyAnnotation != null) {
            this.familyName = this.familyAnnotation.value();
        } else {
            this.familyName = Family.defaultValue;
        }

        this.tableAnnotation = clazz.getAnnotation(Table.class);
        this.nullValueHandle = this.tableAnnotation.nullValueHandle();
        this.familyName = this.familyAnnotation.value();
        this.tableName = this.tableAnnotation.name();

        // 解析列
        this.columns = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                ColumnDefineInfo columnDefineInfo = new ColumnDefineInfo(this, field);
                this.columns.put(columnDefineInfo.getColumnName(), columnDefineInfo);
            } else if (field.isAnnotationPresent(Rowkey.class)) {
                this.rowkeyColumn = new ColumnDefineInfo(this, field);
            }
        }
    }

    public FastClass getFastClass() {
        return fastClass;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Family getFamilyAnnotation() {
        return familyAnnotation;
    }

    public Table getTableAnnotation() {
        return tableAnnotation;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean getNullValueHandle() {
        return nullValueHandle;
    }

    public ColumnDefineInfo getRowkeyColumn() {
        return rowkeyColumn;
    }

    public HashMap<String, ColumnDefineInfo> getColumns() {
        return columns;
    }
}
