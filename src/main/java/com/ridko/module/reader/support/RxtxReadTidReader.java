package com.ridko.module.reader.support;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;

/**
 * 继承拓展模式
 * @author sijia3
 * @date 2021/1/8 16:03
 */
public class RxtxReadTidReader extends RxtxReader {

    public RxtxReadTidReader(BaseDevMsg devMsg) {
        super(devMsg);
    }

    // 实现自己的config


    //

    public void openReadTip() {
        // do open tip config
    }

    @Override
    public boolean start(ReaderProducer producer) {
        boolean start = super.start(producer);
        if (start) {
            System.out.println("rxtx reader tid 执行start");
            openReadTip();
            return true;
        }
        return false;
    }
}
