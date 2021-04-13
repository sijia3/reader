package com.ridko.module.reader.service;

import com.ridko.common.domain.BaseDevMsg;

import java.util.List;

/**
 * 设备持久化服务接口
 * 让用户实现真正的对数据库接口
 * @author sijia3
 * @date 2021/1/11 13:52
 */
public interface BaseDevMsgService {

    /**
     * 获取设备列表
     * @return
     */
    List<BaseDevMsg> getDevMsgList();

    /**
     * 通过mac获取设备
     * @param mac
     * @return
     */
    BaseDevMsg getDevMsgByMac(String mac);

    /**
     * 通过mac更新设备在线状态
     * @param mac
     * @param onlineType
     */
    void updateOnlineStatusByMac(String mac, Integer onlineType);

    /**
     * 保存接口
     * @param devMsg
     */
    void save(BaseDevMsg devMsg);

}
