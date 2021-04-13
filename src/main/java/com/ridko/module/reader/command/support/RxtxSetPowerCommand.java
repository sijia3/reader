package com.ridko.module.reader.command.support;

import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import com.ridko.third.rodinbell.rfid.RFIDReaderHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 设置功率命令
 * @author sijia3
 * @date 2021/1/6 14:07
 */
@Slf4j
public class RxtxSetPowerCommand extends RxtxReaderCommand<String> {


    public RxtxSetPowerCommand(RxtxReader reader, String param) {
        super(reader, param);
    }

    @Override
    public void execute() {
        ReaderHelper readerHelper = getExector();
        String mac = this.reader.getDevMsg().getMac();
        String powersStr = this.param;

        String powerStrTrim = powersStr.replace(" ", "");
        String[] powers = powerStrTrim.split(",");
        System.out.println(powers.length);
        if (powers.length != 4 && powers.length != 8) {
            log.warn("读写器 mac:[{}] remarks 参数长度不等于4或8，未能正常设置功率");
            return;
        }

        if (powers.length == 4) {
            int power0 = Integer.parseInt(powers[0]);
            int power1 = Integer.parseInt(powers[1]);
            int power2 = Integer.parseInt(powers[2]);
            int power3 = Integer.parseInt(powers[3]);
            log.info("mac:[{}]设置功率为：[{}],[{}],[{}],[{}]", readerHelper, power0, power1, power2, power3);
            // This command consumes more than 100mS.
            ((RFIDReaderHelper)readerHelper).setOutputPower((byte) 0xff, (byte)power0, (byte)power1, (byte)power2, (byte)power3);
        }else if (powers.length == 8) {
            int power0 = Integer.parseInt(powers[0]);
            int power1 = Integer.parseInt(powers[1]);
            int power2 = Integer.parseInt(powers[2]);
            int power3 = Integer.parseInt(powers[3]);
            int power4 = Integer.parseInt(powers[4]);
            int power5 = Integer.parseInt(powers[5]);
            int power6 = Integer.parseInt(powers[6]);
            int power7 = Integer.parseInt(powers[7]);
            log.info("mac:[{}]设置功率为：[{}],[{}],[{}],[{}],[{}],[{}],[{}],[{}]", readerHelper, power0, power1, power2, power3
                    ,power4, power5, power6, power7);
            ((RFIDReaderHelper)readerHelper).setOutputPower((byte) 0xff, (byte)power0, (byte)power1, (byte)power2, (byte)power3,
                    (byte)power4, (byte)power5,(byte)power6, (byte)power7);
        }

    }




}
