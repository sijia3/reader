package com.ridko.module.reader.command.support;

import com.ridko.module.reader.support.RxtxReader;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import com.ridko.third.rodinbell.rfid.RFIDReaderHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author sijia3
 * @date 2021/1/7 13:45
 */
public class RxtxFastSwitchCommand extends RxtxReaderCommand<String>{

    public RxtxFastSwitchCommand(RxtxReader reader, String param) {
        super(reader, param);
    }

    @Override
    public void execute() {
        List<Byte> byteList = convert2ListParam(param);
        if (byteList.size() == 4) {
            ((RFIDReaderHelper) reader.getRfidHelper()).fastSwitchAntInventory((byte) 0xff,
                    byteList.get(0),(byte)0x01,
                    byteList.get(1),(byte)0x01,
                    byteList.get(2),(byte)0x01,
                    byteList.get(3),(byte)0x01,
                    (byte)0x0A, (byte)0x01);
        }else if (byteList.size() == 8) {
            ((RFIDReaderHelper) reader.getRfidHelper()).fastSwitchAntInventory((byte) 0xff,
                    byteList.get(0),(byte)0x01,
                    byteList.get(1),(byte)0x01,
                    byteList.get(2),(byte)0x01,
                    byteList.get(3),(byte)0x01,
                    byteList.get(4),(byte)0x01,
                    byteList.get(5),(byte)0x01,
                    byteList.get(6),(byte)0x01,
                    byteList.get(7),(byte)0x01,
                    (byte)0x0A, (byte)0x01);
        }
    }

    /**
     * 天线配置paramStr转换成List
     * @param param
     * @return
     */
    private static List<Byte> convert2ListParam(String param) {
        String[] split = param.split(",");
        List<Byte> antBytes = new ArrayList<>();
        Arrays.stream(split).forEach(s -> {
            Byte aByte = Byte.valueOf(s);
            antBytes.add(aByte);
        });
        return antBytes;
    }


    public static void main(String[] args) {
        String param = "18,18,18,18";
        List<Byte> bytes = RxtxFastSwitchCommand.convert2ListParam(param);
        return;
    }
}
