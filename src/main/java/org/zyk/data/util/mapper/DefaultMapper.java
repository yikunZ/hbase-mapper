package org.zyk.data.util.mapper;

import org.zyk.data.util.accessor.HbaseClient;
import org.zyk.data.util.converter.DataWrapper;
import org.zyk.data.util.converter.BaseDataWrapper;
import org.zyk.data.util.converter.DefaultDataConverter;

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
