package com.ridko.module.reader.support;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.observer.rodinbell.RxtxBaseObserver;
import com.ridko.third.rodinbell.interaction.ReaderHelper;

/**
 * 罗丹贝尔读写器装饰器
 * 通过装饰器模式，附加拓展功能(推荐使用)
 * @author sijia3
 * @date 2021/1/14 9:44
 */
public class RxtxWrapperReader extends RxtxReader {

    private RxtxReader rxtxReader;

    public RxtxWrapperReader(RxtxReader rxtxReader) {
        this.rxtxReader = rxtxReader;
    }


    @Override
    public void setObserver2Reader(ReaderHelper rfidHelper, RxtxBaseObserver readerObserver) {
        rxtxReader.setObserver2Reader(rfidHelper, readerObserver);
    }


    @Override
    public boolean start(ReaderProducer producer) {
        rxtxReader.start(producer);
        // 增强, 定制功能

        return false;
    }

    @Override
    public ReaderHelper connect(BaseDevMsg devMsg) {
        return rxtxReader.connect(devMsg);
    }

    @Override
    public void disconnect() {
        rxtxReader.disconnect();
    }

}
