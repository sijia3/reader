package com.ridko.module.reader.container;

import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.support.RxtxReader;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象读写器管理容器类
 * 维护读写器map<mac, reader>
 * @author sijia3
 * @date 2021/1/6 21:28
 */
@Component
public class ReaderContainer {

    /**
     * key: mac
     * value: AbstractReader
     */
    protected Map<String, AbstractReader> connMap = new ConcurrentHashMap<>();

    /**
     * 根据mac获取心跳时间
     * @param mac
     * @return
     */
    public Long getMac2TimeLived(String mac) {
        AbstractReader reader = connMap.get(mac);
        long heartBeatTime = reader.getHeartBeatTime();
        return heartBeatTime;
    }

    /**
     * 根据mac设置心跳时间
     * @param mac
     * @param updateTime
     */
    public void setMac2TimeLived(String mac, Long updateTime) {
        AbstractReader reader = connMap.get(mac);
        reader.setHeartBeatTime(updateTime);
    }

    /**
     * 把mac对应的reader对象添加进容器中
     * @param mac
     * @param reader
     */
    public void addReader(String mac, AbstractReader reader) {
        if (!connMap.containsKey(mac)) {
            connMap.put(mac, reader);
        }
    }

    /**
     * 把mac对应的reader对象删除
     * @param mac
     */
    public void removeReader(String mac) {
        connMap.remove(mac);
    }

    public AbstractReader getReader(String mac) {
        return connMap.get(mac);
    }

    public Map<String, AbstractReader> getConnMap() {
        return connMap;
    }
}
