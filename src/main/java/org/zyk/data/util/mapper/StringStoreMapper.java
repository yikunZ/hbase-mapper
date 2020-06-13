package org.zyk.data.util.mapper;

import org.zyk.data.util.accessor.HbaseClient;
import org.zyk.data.util.converter.BaseDataWrapper;
import org.zyk.data.util.converter.DataWrapper;
import org.zyk.data.util.converter.StringStoreDataConverter;

public class StringStoreMapper extends BaseMapper {
    StringStoreMapper() {
        super();
    }

    StringStoreMapper(HbaseClient hbaseClient) {
        super(hbaseClient);
    }

    public static Mapper create() {
        return new StringStoreMapper();
    }

    public static Mapper create(HbaseClient hbaseClient) {
        return new StringStoreMapper(hbaseClient);
    }

    DataWrapper setDataTransform() {
        return BaseDataWrapper.create(StringStoreDataConverter.create());
    }
}
