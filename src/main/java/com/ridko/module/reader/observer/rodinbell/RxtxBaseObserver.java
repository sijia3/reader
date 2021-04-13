package com.ridko.module.reader.observer.rodinbell;

import com.jeesite.common.utils.SpringUtils;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.producer.SqProducer;
import com.ridko.module.reader.command.BaseReaderCommand;
import com.ridko.module.reader.command.support.RxtxFastSwitchCommand;
import com.ridko.module.reader.command.support.RxtxReaderCommand;
import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import com.ridko.third.rodinbell.rfid.rxobserver.RXObserver;
import com.ridko.third.rodinbell.rfid.rxobserver.bean.RXInventoryTag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 罗丹贝尔观察者
 * 继承api的观察者，并附加部分属性，如读写器对象，生产者对象等
 * 其它品牌的读写器接入也类似
 * @author sijia3
 * @date 2021/1/6 11:06
 */
@Slf4j
@Getter
@Setter
public class RxtxBaseObserver extends RXObserver {

    private  RxtxReader rxtxReader;        // 罗丹贝尔连接对象
    private ReaderProducer producer;        // 消息生产者
    protected String inventoryParam;        // 盘存指令发送参数, 根据devMsg.getAntParam配置
    private SqProducer sqProducer = SpringUtils.getBean(SqProducer.class);

    public RxtxBaseObserver(RxtxReader rxtxReader, ReaderProducer producer) {
        this.rxtxReader = rxtxReader;
        if (Objects.nonNull(producer)) {
            this.producer = producer;
        }else {
            this.producer = sqProducer;
        }
        initInventotyParam(rxtxReader);
    }

    private void initInventotyParam(RxtxReader reader) {
        BaseDevMsg devMsg = reader.getDevMsg();
        inventoryParam = devMsg.getOpenAnts();
    }


    /**
     * 标签回调信息
     * @param tag The {@link RXInventoryTag} object contain tag information.
     */
    @Override
    protected void onInventoryTag(RXInventoryTag tag) {
        // 将不同读写器返回的内容按照type分类，归到相应的队列中
        String mac = getDevMsg().getMac();
        String epc = tag.strEPC;
        epc = epc.replace(" ", "");
        byte ant = tag.btAntId;
        int rssi = Integer.parseInt(tag.strRSSI);
        // 向标签添加生产者类型信息
        String processType = getDevMsg().getProcessType();

        BaseTagMsg baseTagMsg = BaseTagMsg.builder()
                .ant(ant)
                .epc(epc)
                .mac(mac)
                .receiveTime(new Date())
                .rssi(rssi)
                .processType(processType)
                .build();
        producer.addTagMsg(baseTagMsg);
    }

    /**
     * 快速轮询盘存指令最终回调接口
     * @param tagEnd {@link RXInventoryTag} object.
     */
    @Override
    protected void onFastSwitchAntInventoryTagEnd(RXInventoryTag.RXFastSwitchAntInventoryTagEnd tagEnd){
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long timeToLive = System.currentTimeMillis();
        rxtxReader.setHeartBeatTime(timeToLive);

        // checkQueue and sendCommand
        checkAndExecuteCmd();
    }

    @Override
    protected void onExeCMDStatus(byte cmd,byte status) {
        // 发送指令后的回调
        checkAndExecuteCmd();
    }

    @Override
    protected void onOperationTagEnd(int operationTagCount) {
        checkAndExecuteCmd();
    }

    /**
     * 检查并执行指令
     * step1. 检查命令队列
     * step2. 如果不为空，取出队列指令，并执行
     * step3. 如果为空，执行盘存指令
     */
    private void checkAndExecuteCmd() {
        boolean commandQueueIsNotEmpty = checkCommandQueueIsNotEmpty();
        BaseReaderCommand command;
        if (commandQueueIsNotEmpty) {
            // do queue command
            log.info("获取队列中的command命令对象！");
            command = getCommandQueue().pollFirst();
        }else {
            command = getInventoryCommand();
        }

        //step2. 执行指令
        command.execute();
    }

    private RxtxReaderCommand getInventoryCommand() {
        RxtxFastSwitchCommand command = new RxtxFastSwitchCommand(rxtxReader, getDevMsg().getOpenAnts());
        return command;
    }

    private boolean checkCommandQueueIsNotEmpty() {
        LinkedList<BaseReaderCommand> commandQueue = getCommandQueue();
        if (Objects.nonNull(commandQueue) && !commandQueue.isEmpty()) {
            return true;
        }
        return false;
    }


    private BaseDevMsg getDevMsg() {
        return rxtxReader.getDevMsg();
    }

    private ReaderHelper getReaderHelper() {
        return rxtxReader.getRfidHelper();
    }

    private LinkedList<BaseReaderCommand> getCommandQueue() {
        LinkedList<BaseReaderCommand> orderQueueMap = rxtxReader.getOrderQueueMap();
        return orderQueueMap;
    }


}
