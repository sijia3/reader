package com.ridko.module.reader.base;

import com.ridko.module.client.ReaderBootstrap;
import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.container.ReaderContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 罗丹贝尔检测设备连接线程
 * 定时轮询，遍历readerContainer, 检查最晚的在线时间
 * @author sijia3
 * @date 2021/1/15 16:04
 */
@Slf4j
@Component
public abstract class ReaderConnectCheckThread<T extends AbstractReader> {

    @Autowired
    private ReaderBootstrap readerStarter;

    @Autowired
    private ReaderContainer readerContainer;


    public ReaderConnectCheckThread() {
        new CheckReaderThread().start();
    }

    private class CheckReaderThread extends Thread {
        @Override
        public void run() {

            while (true) {
                try {
                    Map<String, AbstractReader> connMap = readerContainer.getConnMap();
                    Set<Map.Entry<String, AbstractReader>> entries1 = connMap.entrySet();
                    for (Map.Entry<String, AbstractReader> stringTEntry : entries1) {
                        long currentTime = System.currentTimeMillis();
                        String mac = stringTEntry.getKey();
                        AbstractReader reader = stringTEntry.getValue();

                        Long mac2TimeLived = readerContainer.getMac2TimeLived(mac);
                        if (Objects.isNull(mac2TimeLived) || mac2TimeLived == 0L) {
                            continue;
                        }

                        if (currentTime - mac2TimeLived < 10 * 1000 && reader.isAlive()) {
                            continue;
                        }else {
                            log.warn("设备mac:[{}] 连接异常！准备移除connectMap,并尝试重新连接。。", mac);
                            // 做重连处理
                            readerStarter.addReconnectTask(mac);
                        }
                    }
//                    Set<Map.Entry<String, RxtxReader>> entries = rxtxReaderContainer.getConnMap().entrySet();
//                    for (Map.Entry<String, RxtxReader> entry : entries) {
//                        long currentTime = System.currentTimeMillis();
//                        String mac = entry.getKey();
//                        RxtxReader rxtxReader = entry.getValue();
//                        ReaderHelper rfidHelper = rxtxReader.getRfidHelper();
//                        RFIDReaderHelper rfidReaderHelper = (RFIDReaderHelper) rfidHelper;
//                        Long mac2TimeLived = rxtxReaderContainer.getMac2TimeLived(mac);
//                        if(Objects.isNull(mac2TimeLived) || mac2TimeLived == 0L) {
//                            continue;
//                        }
//                        if (currentTime - mac2TimeLived < 10 * 1000 && rfidReaderHelper.isAlive()) {
//                            continue;
//                        }else {
//                            log.warn("设备mac:[{}] 连接异常！准备移除connectMap,并尝试重新连接。。", mac);
//                            //  做重连机制处理
//                            readerStarter.addReconnectTask(mac);
//                        }
//                    }
                    Thread.sleep(6*1000);
                }catch (Exception e) {
                    // todo catch exception
                }
            }
        }
    }

}
