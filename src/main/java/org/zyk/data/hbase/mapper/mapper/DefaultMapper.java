package org.zyk.data.hbase.mapper.mapper;

import org.zyk.data.hbase.mapper.accessor.HbaseClient;
import org.zyk.data.hbase.mapper.converter.BaseDataWrapper;
import org.zyk.data.hbase.mapper.converter.DataWrapper;
import org.zyk.data.hbase.mapper.converter.DefaultDataConverter;

public class DefaultMapper extends BaseMapper {
    DefaultMapper() {
        super();
    }

    DefaultMapper(HbaseClient hbaseClient) {
        super(hbaseClient);
    }

    public static Mapper create() {
        return new DefaultMapper();
    }

    public static Mapper create(HbaseClient hbaseClient) {
        return new DefaultMapper(hbaseClient);
    }

    DataWrapper setDataTransform() {
        return BaseDataWrapper.create(DefaultDataConverter.create());
    }
}
