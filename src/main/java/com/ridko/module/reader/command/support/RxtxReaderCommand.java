package com.ridko.module.reader.command.support;

import com.ridko.module.reader.command.BaseReaderCommand;
import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;

/**
 * 罗丹贝尔读写器命令抽象类
 * @author sijia3
 * @date 2021/1/6 12:22
 * @param <E> 命令参数类型，由不同的命令决定参数类型
 */
public abstract class RxtxReaderCommand<E> extends BaseReaderCommand<RxtxReader, E, ReaderHelper> {

    public RxtxReaderCommand(RxtxReader rxtxReader, E param) {
        super(rxtxReader, param);
    }

    @Override
    public ReaderHelper getExector() {
        return reader.getRfidHelper();
    }
}
