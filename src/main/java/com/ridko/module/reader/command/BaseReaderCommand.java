package com.ridko.module.reader.command;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.AbstractReader;

/**
 * 读写器命令抽象类
 *
 * @author sijia3
 * @date 2021/1/6 12:03
 * @param <T> 命令执行器(读写器连接对象)
 * @param <E> 命令参数类型
 * @param <K> 读写器连接对象具体的执行器(由特定sdk决定)
 */
public abstract class BaseReaderCommand<T extends AbstractReader<K, ?>, E, K> implements ReaderCommand {

    protected T reader;

    protected E param;

    protected BaseDevMsg devMsg;        // 附带信息

    public abstract K getExector();

    public BaseReaderCommand(T reader, E param) {
        this.reader = reader;
        this.param = param;
        this.devMsg = reader.getDevMsg();
    }

    public void setReaderHelper(T readerHelper) {
        this.reader = readerHelper;
    }

    public void setParam(E param) {
        this.param = param;
    }

    public T getReaderHelper() {
        return reader;
    }

    public E getParam() {
        return param;
    }

}
