package com.ridko.module.reader.support;

import com.ridko.common.anno.Reader;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.common.enums.ReaderBrandEnum;
import com.ridko.common.enums.ReaderFunctionEnum;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.command.support.RxtxGetVersionCommand;
import com.ridko.module.reader.config.setpower.RxtxSetPowerConfig;
import com.ridko.module.reader.observer.rodinbell.RxtxBaseObserver;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import com.ridko.third.rodinbell.rfid.RFIDReaderHelper;
import com.ridko.third.rodinbell.rfid.ReaderConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 罗丹贝尔读写器对象
 * @author sijia3
 * @date 2021/1/6 22:54
 */
@Slf4j
@Component
@Scope(value = "prototype")
@Reader(brand = ReaderBrandEnum.RODIN_BELL, functionType = ReaderFunctionEnum.UNKNOWN)
public class RxtxReader extends AbstractReader<ReaderHelper, RxtxBaseObserver> {

    protected ReaderConnector readerConnector = new ReaderConnector();      // 罗丹贝尔辅助连接器

    private RxtxSetPowerConfig rxtxSetPowerConfig = new RxtxSetPowerConfig();       // 读写器功率配置类

    public RxtxReader(BaseDevMsg devMsg) {
        super(devMsg);
    }

    protected RxtxReader() {
    }


    @Override
    public void setObserver2Reader(ReaderHelper rfidHelper, RxtxBaseObserver readerObserver) {
        readerObserver.setRxtxReader(this);
        rfidHelper.registerObserver(readerObserver);
    }

    /**
     * 罗丹贝尔连接。根据devMsg创建连接对象，成功将对象添加到container维护
     * @param devMsg
     * @return
     */
    @Override
    public ReaderHelper doConnect(BaseDevMsg devMsg) {
        ReaderHelper mReaderHelper = readerConnector.connectNet(devMsg.getIp(), devMsg.getPort());
        if (mReaderHelper != null) {
            // 将该对象添加到map维护
            this.rfidHelper = mReaderHelper;
        }else {
            readerConnector.disConnect();
            return null;
        }
        return this.rfidHelper;
    }

    /**
     * 断开连接
     * 1. 将container容器维护的对象清除
     * 2. 调用connector来维护关闭
     */
    @Override
    public void doDisconnect() {
        readerConnector.disConnect();
    }

    /**
     * 罗丹贝尔读写器启动流程
     * 1. 创建连接
     * 2. 添加观察者
     * 3. 主动发送第一条指令
     * 4. 设置功率，将设置功率命令加进队列中
     * @param producer
     * @return
     */
    @Override
    public boolean start(ReaderProducer producer) {
        // step1: 创建读写器连接
        ReaderHelper readerHelper = this.connect(devMsg);
        if (Objects.nonNull(readerHelper)) {
            // step2: 设置readerHepler和注册observer
            this.rfidHelper = readerHelper;
            RxtxBaseObserver rxtxBaseObserver = new RxtxBaseObserver(this, producer);   // reader对象不持有该Observer
            this.setObserver2Reader(rfidHelper, rxtxBaseObserver);

            // step3：罗丹贝尔读写器需要主动发送第一条指令
            this.sendFirstCommand();

            // step4: 设置功率, 加入队列
            this.setPowerConfig();
            return true;
        }
        return false;
    }

    @Override
    public void setPowerConfig() {
        rxtxSetPowerConfig.setPower(this, getDevMsg().getPowers());
    }

    @Override
    public boolean isAlive() {
        RFIDReaderHelper rfidReaderHelper = (RFIDReaderHelper) rfidHelper;
        return rfidReaderHelper.isAlive();
    }

    private void sendFirstCommand() {
        log.info("devMsg: [{}]向读写器发送第一条指令", devMsg.getMac());
        RxtxGetVersionCommand rxtxGetVersionCommand = new RxtxGetVersionCommand(this, null);
        rxtxGetVersionCommand.execute();
    }
}
