package com.ridko.module.reader.factory;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.support.RxtxReader;
import org.springframework.stereotype.Component;

/**
 * @author sijia3
 * @date 2021/1/15 10:32
 */
@Component
@Deprecated
public class DTReaderFactory implements ReaderFactory<RxtxReader>{
    @Override
    public RxtxReader getReader(BaseDevMsg devMsg) {
        return null;
    }
}
