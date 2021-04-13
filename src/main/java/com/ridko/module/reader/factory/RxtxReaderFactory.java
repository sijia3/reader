package com.ridko.module.reader.factory;

import com.ridko.common.enums.ReaderFunctionEnum;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.support.RxtxReader;
import com.ridko.module.reader.support.RxtxWrapperReader;
import org.springframework.stereotype.Component;

/**
 * 罗丹贝尔读写器对象创建工厂
 * @author sijia3
 * @date 2021/1/13 15:27
 */
@Component
@Deprecated
public class RxtxReaderFactory implements ReaderFactory<RxtxReader>{
    @Override
    public RxtxReader getReader(BaseDevMsg devMsg) {
        String readerType = devMsg.getReaderType();
        RxtxReader rxtxReader = new RxtxReader(devMsg);
        if (ReaderFunctionEnum.UNKNOWN.getValue().equals(readerType)) {
            return rxtxReader;
        }else if (ReaderFunctionEnum.ANO_UNKNOWN.getValue().equals(readerType)) {
            // 返回包装类
            return new RxtxWrapperReader(rxtxReader);
        }
        return null;
    }
}
