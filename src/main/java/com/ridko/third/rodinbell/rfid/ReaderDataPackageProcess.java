package com.ridko.third.rodinbell.rfid;


import com.ridko.third.rodinbell.interaction.DataPackageProcess;
import com.ridko.third.rodinbell.rfid.bean.MessageTran;

/**
 * The implementation class of DataPackageProcess.
 */

public class ReaderDataPackageProcess extends DataPackageProcess {
    @Override
    public void analyData(byte[] btPackage) {
        MessageTran msgTran = new MessageTran(btPackage);
        if (msgTran != null) {
            setChanged();
            notifyObservers(msgTran);
        }
    }
}
