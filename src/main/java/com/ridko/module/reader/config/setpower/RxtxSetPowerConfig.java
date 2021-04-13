package com.ridko.module.reader.config.setpower;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.command.BaseReaderCommand;
import com.ridko.module.reader.command.support.RxtxSetPowerCommand;
import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 罗丹贝尔功率配置类
 * @author sijia3
 * @date 2021/1/7 10:07
 */
@Slf4j
public class RxtxSetPowerConfig implements SetPowerConfig<RxtxReader> {
    @Override
    public void setPower(RxtxReader reader, String powerStr) {
        // 将指令添加到queue中
        BaseDevMsg devMsg = reader.getDevMsg();
        log.info("devMsg:[{}]设置功率:[{}]", devMsg.getMac(), powerStr);
        ReaderHelper rfidHelper = reader.getRfidHelper();
        RxtxSetPowerCommand setPowerCommand = new RxtxSetPowerCommand(reader, powerStr);
        LinkedList<BaseReaderCommand> orderQueueMap = reader.getOrderQueueMap();
        orderQueueMap.offer(setPowerCommand);
//        setPowerCommand.execute();
    }
}
