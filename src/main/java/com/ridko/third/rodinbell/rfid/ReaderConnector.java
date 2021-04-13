package com.ridko.third.rodinbell.rfid;


import com.jeesite.common.lang.ExceptionUtils;
import com.ridko.third.rodinbell.interaction.ModuleConnector;
import com.ridko.third.rodinbell.interaction.ReaderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The implementation class of ModuleConnector.
 *
 */
public class ReaderConnector implements ModuleConnector {
	private final static Logger logger = LoggerFactory.getLogger(ReaderConnector.class);

	private  final String HOSTNAME_REGEXP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

	private RFIDReaderHelper mRFIDReaderHelper;
	private Socket mSocket;
	private InetSocketAddress mRemoteAddr;
//	private SerialPort mSeialPort = null;
	private final String USER = "USER";

	@Override
	public ReaderHelper connectCom(final String port, final int baud) {
		return null;
	}

	@Override
	public ReaderHelper connectNet(final String host, final int port) {

		if (host.matches(HOSTNAME_REGEXP))
			;
		else {
			return null;
		}

		try {
			mRemoteAddr = new InetSocketAddress(host, port);
			mSocket = new Socket();
		} catch (Exception e1) {
			return null;
		}

		try {
			mSocket.connect(mRemoteAddr, 5000);
//			mSocket.setSoTimeout(5000);
			logger.info("读写器ip:[{}]设置read超时时间为:[{}]", host, mSocket.getSoTimeout());
			return connect(host, mSocket.getInputStream(),mSocket.getOutputStream());
		} catch (Exception e) {
			logger.warn("[{}]", ExceptionUtils.getStackTraceAsString(e));
			return null;
		}
	}

//	@Override
	public ReaderHelper connect(String ip, InputStream in, OutputStream out) {
		mRFIDReaderHelper = new RFIDReaderHelper();
		try {
			mRFIDReaderHelper.setReader(ip, in,out,new ReaderDataPackageParser(),new ReaderDataPackageProcess());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mRFIDReaderHelper;
	}

	@Override
	public ReaderHelper connect(InputStream in, OutputStream out) {
		return null;
	}

	@Override
	public boolean isConnected() {
		return (mRFIDReaderHelper != null ? mRFIDReaderHelper.isAlive() : false);
	}

	@Override
	public void disConnect() {
		if (mRFIDReaderHelper != null) {
			mRFIDReaderHelper.signOut();
			mRFIDReaderHelper = null;
		}
		try {
			if (mSocket != null) {
				mSocket.close();
				mSocket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mRemoteAddr = null;
	}

}
