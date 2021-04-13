package com.ridko.module.reader.service;

import com.ridko.common.domain.BaseDevMsg;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sijia3
 * @date 2021/1/11 14:15
 */
@Service
public class DevMsgServiceImpl implements BaseDevMsgService{
    @Override
    public List<BaseDevMsg> getDevMsgList() {
        return null;
    }

    @Override
    public BaseDevMsg getDevMsgByMac(String mac) {
        return null;
    }

    @Override
    public void updateOnlineStatusByMac(String mac, Integer onlineType) {

    }

    @Override
    public void save(BaseDevMsg devMsg) {

    }
}
