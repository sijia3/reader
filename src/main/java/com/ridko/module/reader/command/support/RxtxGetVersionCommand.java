package com.ridko.module.reader.command.support;

import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import com.ridko.third.rodinbell.rfid.RFIDReaderHelper;

/**
 * 罗丹贝尔获取版本指令
 * @author sijia3
 * @date 2021/1/18 16:07
 */
public class RxtxGetVersionCommand extends RxtxReaderCommand<Void>{
    public RxtxGetVersionCommand(RxtxReader reader, Void param) {
        super(reader, param);
    }

    @Override
    public void execute() {
        ((RFIDReaderHelper)reader.getRfidHelper()).getFirmwareVersion((byte)0xff);
    }
}
