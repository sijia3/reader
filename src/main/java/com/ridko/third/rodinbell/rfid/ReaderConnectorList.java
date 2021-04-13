package com.ridko.third.rodinbell.rfid;//package com.ridko.third.rodinbell.rfid;
//
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import com.ridko.third.rodinbell.interaction.ReaderHelper;
//import com.jeesite.modules.reader.ridko.common.type.QueueType;
//import com.jeesite.modules.reader.ridko.domain.DevMsg;
//import com.jeesite.modules.reader.ridko.process.consumer.AbstractReaderProcess;
//import com.jeesite.modules.reader.ridko.process.factory.BaseFactory;
//import com.jeesite.modules.reader.ridko.process.producer.ProducerService;
//import com.jeesite.modules.reader.ridko.process.observer.BaseRXObserver;
//import com.jeesite.modules.reader.ridko.process.listener.BaseRXTXListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
////import com.module.interaction.ReaderHelper;
//
//
////在一个线程里面接收多个tcp连接的数据
//public class ReaderConnectorList {
//
//	protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	List<ReaderHelper> g_list_ReaderHelper = new ArrayList<ReaderHelper>();
//
//
//	//调用的程序入口
//	public void connectNetList(Map<String,String> _ips) {
//
//		for(Map.Entry<String, String> entry : _ips.entrySet()){
//			String m_ip = entry.getKey();
//			int m_port = Integer.parseInt(entry.getValue());
//			ReaderConnector mConnector = new ReaderConnector();
//			ReaderHelper mReaderHelper = mConnector.connectNet(m_ip, m_port);
//			if (mReaderHelper != null) {
//				System.out.println("连接 "+m_ip+":"+m_port+" 成功!" + mReaderHelper.isAlive());
//				g_list_ReaderHelper.add(mReaderHelper);
//
//				DevMsg devMsg = new DevMsg();
//				devMsg.setType("1");
//				devMsg.setMac(m_ip);
//				devMsg.setIp(m_ip);
//				devMsg.setPort(4001);
//				RFIDReaderHelper rfidReaderHelper = (RFIDReaderHelper) mReaderHelper;
//				BaseFactory baseFactory = BaseFactory.getFactory(devMsg.getType());
//				AbstractReaderProcess abstractReaderProcess = baseFactory.createConsumer(QueueType.MQ);
//				ProducerService producerService = baseFactory.createProducer(QueueType.MQ);
//				BaseRXObserver baseRXObserver = new BaseRXObserver();
//				baseRXObserver.setDevMsg(devMsg);
//				baseRXObserver.setmReaderHelper(mReaderHelper);
//				baseRXObserver.setProducerService(producerService);
//
//				mReaderHelper.registerObserver(baseRXObserver);
//				mReaderHelper.setRXTXListener(new BaseRXTXListener());     // 每一个连接需要一个Listener
//
//				abstractReaderProcess.setting(rfidReaderHelper);
//			} else {
//				System.out.println("连接 "+m_ip+":"+m_port+" 失败!" + mConnector.isConnected());
//
//			}
//		}
//		System.out.println("开启线程轮询");
//		WaitThread mWaitThread = new WaitThread();
//		mWaitThread.start();
//
//
//	}
//
//	private class WaitThread extends Thread {
//
//		@Override
//		public void run() {
//			byte[] btAryBuffer = new byte[4096];
//			int count = 0;
//			//在一个线程里面接收多个tcp连接的数据
//			while (true)
//			{
//				Date startD = new Date();
//				try {
//					for (int i = 0; i < g_list_ReaderHelper.size(); i++) {
//						ReaderHelper m_ReaderHelper = g_list_ReaderHelper.get(i);
//						m_ReaderHelper.fetchData();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				logger.info("轮询次数:{}, 时间:{}", count++, new Date().getTime()-startD.getTime());
//			}
//		}
//	}
//
//`
//}
//
