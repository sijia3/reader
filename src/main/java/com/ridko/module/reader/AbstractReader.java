package com.ridko.module.reader;

import com.jeesite.common.utils.SpringUtils;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.command.BaseReaderCommand;
import com.ridko.module.reader.container.ReaderContainer;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;

/**
 * 抽象读写器模块
 * 默认连接读写器对象主要维护属性： 1. 连接对象 2. 读写设备记录 3. 连接次数 4. 心跳时间 5. 执行命令队列
 * 并提供三个基础功能(交由子类实现)
 * 1. 设置观察者到读写器对象
 * 2. 设置功率
 * 3. 判断读写器对象是否存货方法
 * 4. 提供一套读写器的启动方法(自定义时序)
 * 其它功能，如设置读tid功能等，可通过自定义插件形式拓展包装进行加入
 * 实现该类的Reader对象，需要通过注解{@link com.ridko.common.anno.Reader} 来标识
 * 实现可参考 {@link com.ridko.module.reader.support.RxtxReader}
 * @author sijia3
 * @date 2021/1/5 17:23
 * @param <T> 读写器的连接对象 readerConnector or readerHelper
 * @param <E> 读写器的观察者 readerObserver
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractReader<T, E> implements Device<T> {

    protected T rfidHelper;             // 读写器连接对象
    protected BaseDevMsg devMsg;        // 读写器设备记录
    protected int connectCount = 0;     // 连接次数
    protected long heartBeatTime;       // 心跳时间
    protected LinkedList<BaseReaderCommand> orderQueueMap = new LinkedList<>();     // 命令队列
    protected ReaderContainer readerContainer = SpringUtils.getBean(ReaderContainer.class);        // 全局维护的读写器map对象

    public AbstractReader(BaseDevMsg devMsg) {
        this.devMsg = devMsg;
    }

    protected AbstractReader() {
    }

    /**
     * 抽象方法，让子类实现连接
     * @param devMsg
     * @return
     */
    protected abstract T doConnect(BaseDevMsg devMsg);

    /**
     * 抽象方法，让子类实现断开连接
     */
    protected abstract void doDisconnect();

    /**
     * 抽象函数，将观察者设置到读写器连接对象
     * @param rfidHelper
     * @param readerObserver
     */
    public abstract void setObserver2Reader(T rfidHelper, E readerObserver);


    /**
     * 抽象函数， 让不同类型的读写器自定义启动流程
     * 基本步骤为：将传入的producer放到特定观察者，并将观察者注册到reader对象
     * @param producer
     * @return
     */
    public abstract boolean start(ReaderProducer producer);

    /**
     * 抽象函数， 让不同的读写器自定义功率配置(将指令存进队列)
     */
    public abstract void setPowerConfig();

    /**
     * 抽象函数，让子类实现不同读写器的判断存活(给维护心跳的线程用)
     * @return
     */
    public abstract boolean isAlive();


    @Override
    public T connect(BaseDevMsg devMsg) {
        T connect = doConnect(devMsg);
        if (Objects.nonNull(connect)) {
            readerContainer.addReader(devMsg.getMac(), this);
        }
        return this.rfidHelper;
    }


    @Override
    public void disconnect() {
        readerContainer.removeReader(devMsg.getMac());
        doDisconnect();
    }

    public BaseDevMsg getDevMsg() {
        return devMsg;
    }
}
