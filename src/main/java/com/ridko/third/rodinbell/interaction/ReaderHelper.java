package com.ridko.third.rodinbell.interaction;

import com.jeesite.common.lang.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observer;

/**
 * Base class to operate all the modules.This class include the read and write functions to module.
 * You can receive the data via implementation {@link #DataPackageParser}.Via method
 * {@link #sendCommand(byte[])} send data to module.Reference {@link #setReader(InputStream, OutputStream, DataPackageParser, DataPackageProcess)}
 */

public abstract class ReaderHelper {
    private final static Logger logger = LoggerFactory.getLogger(ReaderHelper.class);

    protected ReaderBase mReader;
    protected RXTXListener mListener;

    /**
     * Constructor
     */
    public ReaderHelper() {
        super();
    }

    /**
     * Set the InputStream(read module data) , OutputStream(write data to module),DataPackageParser(
     * Parser raw data returned by module to data package according to the module protocol.
     * Then transmit the data package to DataPackageProcess) and DataPackageProcess(Process the
     * data package to get different information).
     * @param in  Input stream of module.
     * @param out Output stream of module.
     * @param parser parse the data returned by module.
     * @param process process the data package parsed by parser
     * @throws Exception Throw an error when in or out is empty
     */
    public void setReader(String ip, InputStream in, OutputStream out, DataPackageParser parser, DataPackageProcess process) throws Exception {

        if (in == null || out == null) throw new NullPointerException("in Or out is NULL!");

        if (mReader == null) {

            mReader = new ReaderBase(ip, in, out, parser, process) {

                @Override
                public void onLostConnect() {
                    logger.error("ip:[{}]失去连接", ip);
                    ReaderHelper.this.onLostConnect();
                }

                @Override
                public void reciveData(byte[] btAryReceiveData) {
                    ReaderHelper.this.reciveData(btAryReceiveData);
                }

                @Override
                public void sendData(byte[] btArySendData) {
                    ReaderHelper.this.sendData(btArySendData);
                }
            };
        }
    }

    /**
     * Update the data when have new data return.You can listener the notification
     * if new data return by reader via register Observer.
     * @param observer Register it to observable to listener the return data.
     */
    public void registerObserver(Observer observer) {
        if (mReader != null) {
            mReader.registerObserver(observer);
        } else {
            throw new NullPointerException("The observable is null!");
        }
    }

    /**
     * UnRegister the Observer.
     * @param observer Unregister it to observable to stop listener the return data.
     */
    public void unRegisterObserver(Observer observer) {
        if (mReader != null) {
            mReader.unRegisterObserver(observer);
        } else {
            throw new NullPointerException("The observable is null!");
        }
    }

    /**
     * UnRegister all the Observers;
     */
    public void unRegisterObservers() {
        if (mReader != null) {
            mReader.unRegisterObservers();
        } else {
            throw new NullPointerException("The observable is null!");
        }
    }

    /**
     * Judge the listener Thread is alive or not.
     * @return if true the thread is run,or the thread exit.
     */
    public boolean isAlive() {
        if (mReader == null) {
            return false;
        } else {
            return mReader.IsAlive();
        }
    }

    /**
     * You can invoke this method restart the connection if the connection missing.
     */
    public void startWith() {
        if (mReader == null) {
            throw new NullPointerException("Reader is null!");
        } else {
        	if (!mReader.IsAlive()) {
        		mReader.StartWait();
        	}
        }
    }
    /**
     * Exit the listener thread ,release the resource.
     */
    public void signOut() {
    	 if (mReader != null) {
             mReader.signOut();
             mReader = null;
         }
    	 mListener = null;
    }

    /**
     * Set RXTX listener to listen sending data to reader,receiving data from reader and the connection missing.
     * @param listener the interface {@link RXTXListener}} implementation class will invoke according event method.
     */
    public void setRXTXListener(RXTXListener listener) {
    	this.mListener = listener;
    }

    /**
     * This method will callback when the reader receive the data returned by reader.
     * You can monitor the raw data return from reader via override it.
     * And this method running in child thread.
     *
     * @param btAryReceiveData return data.
     */
    private void reciveData(byte[] btAryReceiveData) {
    	if (mListener != null) {
			mListener.reciveData(btAryReceiveData);
		}
    }

    /**
     * This method will callback when the reader send the data returned by reader.
     * You can monitor the raw data send to reader via override it.
     *
     * @param btArySendData the send data to reader.
     */
    private void sendData(byte[] btArySendData) {
    	if (mListener != null) {
			mListener.sendData(btArySendData);
		}
    }

    /**
     * This method will callback when the monitor thread of data returned by reader is quit.
     * You can monitor it to do something when the monitor thread of data returned by reader quit.
     * And this method running in child thread.
     */
    private void onLostConnect() {
    	if (mListener != null) {
			mListener.onLostConnect();
		}
    }

    /**
     * Send data to the reader.
     *
     * @param btCMDPackage the data package send to reader.
     * @return Succeeded :0, Failed:-1
     */
    public int sendCommand(byte[] btCMDPackage) {
        return mReader.sendBuffer(btCMDPackage);
    }


    abstract class ReaderBase {
        private String ip = null;
        private WaitThread mWaitThread = null;
        private InputStream mInStream = null;
        private OutputStream mOutStream = null;

        private DataPackageParser mPackageParser;
        private DataPackageProcess mPackageProcess;

        /**
         * Connection Lost. This function is invoked when the listen thread exist;
         */
        public abstract void onLostConnect();

        public void onLostConnect(String ip) {

        }

        /**
         * With reference constructor. Construct a Reader with input and output streams.
         *
         * @param in  Input Stream
         * @param out Output Stream
         */
        public ReaderBase(String ip, InputStream in, OutputStream out, DataPackageParser parser, DataPackageProcess process) throws Exception {
            this.ip = ip;
            this.mInStream = in;
            this.mOutStream = out;
            if (parser == null || process == null)
                throw new Exception("DataPackageParser && DataPackageProcess are null exception!");
            mPackageParser = parser;
            mPackageProcess = process;
            StartWait();
        }

        public boolean IsAlive() {
            return mWaitThread != null && mWaitThread.isAlive();
        }

        public void StartWait() {
            mWaitThread = new WaitThread(ip);
            mWaitThread.start();
        }

        /**
         * Loop receiving data thread.
         *
         * @author Jie
         */
        private class WaitThread extends Thread {
            private boolean mShouldRunning = true;

            private String flag = "";

            private String ip = "";

            public WaitThread() {
                mShouldRunning = true;
            }

            public WaitThread(String ip) {
                mShouldRunning = true;
                this.ip = ip;
            }

            @Override
            public void run() {
                byte[] btAryBuffer = new byte[4096];
                while (mShouldRunning) {
                    try {
                        int nLenRead = mInStream.read(btAryBuffer);
                        if (nLenRead > 0) {
                            byte[] btAryReceiveData = new byte[nLenRead];
                            System.arraycopy(btAryBuffer, 0, btAryReceiveData, 0,
                                    nLenRead);
                            reciveData(btAryReceiveData);
                            mPackageParser.runReceiveDataCallback(btAryReceiveData, mPackageProcess);
                        }
                    } catch (IOException e) {
                        logger.error("ip:[{}] 线程发生io异常 ioexception-1", this.ip);
                        logger.error("ip:[{}] ioexception-1异常日志: [{}]", this.ip, ExceptionUtils.getStackTraceAsString(e));
                        onLostConnect();
                        return;
                    } catch (Exception e) {
                        logger.error("ip:[{}] 线程发送异常 exception-1", this.ip);
                        logger.error("ip:[{}] exception-1异常日志: [{}]", this.ip, ExceptionUtils.getStackTraceAsString(e));
                        onLostConnect();
                        return;
                    }
                }
            }

            public void signOut() {
                mShouldRunning = false;
                interrupt();
            }
        }

        /**
         * Exit receive thread
         */
        public final void signOut() {
        	if (mWaitThread != null) {
        		mWaitThread.signOut();
        		mWaitThread = null;
        	}
            try {
            	if (mInStream != null) {
            		 mInStream.close();
            		 mInStream = null;
            	}

            	if (mOutStream != null) {
            		mOutStream.close();
            		mOutStream = null;
            	}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mInStream = null;
                mOutStream = null;
            }
            unRegisterObservers();
            mPackageParser = null;
            mPackageProcess = null;
        }

        /**
         * Register Observer in order to get update.
         *
         * @param observer
         */
        public void registerObserver(Observer observer) {
            mPackageProcess.addObserver(observer);
        }

        /**
         * UnRegister Observer.
         * @param observer
         */
        public void unRegisterObserver(Observer observer) {
            mPackageProcess.deleteObserver(observer);
        }

        /**
         * UnRegister all Observers;
         */
        public void unRegisterObservers() {
            mPackageProcess.deleteObservers();
        }

        /**
         * Read function' This function will be called after read data.
         *
         * @param btAryReceiveData
         */
        public void reciveData(byte[] btAryReceiveData) {
        }

        /**
         * Rewritable function，This function will be called after sending data.
         *
         * @param btArySendData Transmitted Data
         */
        public void sendData(byte[] btArySendData) {
        }

        /**
         * Send data，Use synchronized() to prevent concurrent operation.
         *
         * @param btArySenderData To send data
         * @return Succeeded :0, Failed:-1
         */
        private int sendMessage(byte[] btArySenderData) {

            try {
                synchronized (mOutStream) {        //Prevent Concurrent
                    mOutStream.write(btArySenderData);
                }
            } catch (IOException e) {
                logger.error("ip:[{}] 发送数据异常sendMsg ioexception", this.ip);
                logger.error("ip:[{}] sendMsg ioexception异常日志: [{}]", this.ip, ExceptionUtils.getStackTraceAsString(e));
                onLostConnect();
                return -1;
            } catch (Exception e) {
                logger.error("ip:[{}] 发生381异常,日志: [{}]", this.ip, ExceptionUtils.getStackTraceAsString(e));
                return -1;
            }
            sendData(btArySenderData);
            return 0;
        }

        /**
         * Send the command to module.
         * @param completeCMD the command
         * @return Succeeded :0, Failed:-1
         */
        public final int sendBuffer(byte[] completeCMD) {
            int nResult = sendMessage(completeCMD);
            return nResult;
        }
    }
}


//        import java.io.IOException;
//        import java.io.InputStream;
//        import java.io.OutputStream;
//        import java.util.Observer;
//
///**
// * Base class to operate all the modules.This class include the read and write functions to module.
// * You can receive the data via implementation {@link #DataPackageParser}.Via method
// * {@link #sendCommand(byte[])} send data to module.Reference {@link #setReader(InputStream, OutputStream, DataPackageParser, DataPackageProcess)}
// */
//
//        public abstract class ReaderHelper {
//
//        protected ReaderBase mReader;
//        protected RXTXListener mListener;
//        /**
//         * Constructor
//         */
//        public ReaderHelper() {
//        super();
//        }
//
//        /**
//         * Set the InputStream(read module data) , OutputStream(write data to module),DataPackageParser(
//         * Parser raw data returned by module to data package according to the module protocol.
//         * Then transmit the data package to DataPackageProcess) and DataPackageProcess(Process the
//         * data package to get different information).
//         * @param in  Input stream of module.
//         * @param out Output stream of module.
//         * @param parser parse the data returned by module.
//         * @param process process the data package parsed by parser
//         * @throws Exception Throw an error when in or out is empty
//         */
//        public void setReader(InputStream in, OutputStream out, DataPackageParser parser, DataPackageProcess process) throws Exception {
//
//        if (in == null || out == null) throw new NullPointerException("in Or out is NULL!");
//
//        if (mReader == null) {
//
//        mReader = new ReaderBase(in, out, parser, process) {
//
//        @Override
//        public void onLostConnect() {
//        ReaderHelper.this.onLostConnect();
//        }
//
//        @Override
//        public void reciveData(byte[] btAryReceiveData) {
//        ReaderHelper.this.reciveData(btAryReceiveData);
//        }
//
//        @Override
//        public void sendData(byte[] btArySendData) {
//        ReaderHelper.this.sendData(btArySendData);
//        }
//        };
//        }
//        }
//
//        //获取数据 zhouyujie
//        public void fetchData()
//        {
//        if (mReader != null) {
//        mReader.fetchData();
//        } else {
//        throw new NullPointerException("连接出现错误!");
//        }
//        }
//
//        /**
//         * Update the data when have new data return.You can listener the notification
//         * if new data return by reader via register Observer.
//         * @param observer Register it to observable to listener the return data.
//         */
//        public void registerObserver(Observer observer) {
//        if (mReader != null) {
//        mReader.registerObserver(observer);
//        } else {
//        throw new NullPointerException("The observable is null!");
//        }
//        }
//
//        /**
//         * UnRegister the Observer.
//         * @param observer Unregister it to observable to stop listener the return data.
//         */
//        public void unRegisterObserver(Observer observer) {
//        if (mReader != null) {
//        mReader.unRegisterObserver(observer);
//        } else {
//        throw new NullPointerException("The observable is null!");
//        }
//        }
//
//        /**
//         * UnRegister all the Observers;
//         */
//        public void unRegisterObservers() {
//        if (mReader != null) {
//        mReader.unRegisterObservers();
//        } else {
//        throw new NullPointerException("The observable is null!");
//        }
//        }
//
//        /**
//         * Judge the listener Thread is alive or not.
//         * @return if true the thread is run,or the thread exit.
//         */
//        public boolean isAlive() {
//        if (mReader == null) {
//        return false;
//        } else {
//        return mReader.IsAlive();
//        }
//        }
//
//        /**
//         * You can invoke this method restart the connection if the connection missing.
//         */
//        public void startWith() {
//        if (mReader == null) {
//        throw new NullPointerException("Reader is null!");
//        } else {
//        if (!mReader.IsAlive()) {
//        mReader.StartWait();
//        }
//        }
//        }
//        /**
//         * Exit the listener thread ,release the resource.
//         */
//        public void signOut() {
//        if (mReader != null) {
//        mReader.signOut();
//        mReader = null;
//        }
//        mListener = null;
//        }
//
//        /**
//         * Set RXTX listener to listen sending data to reader,receiving data from reader and the connection missing.
//         * @param listener the interface {@link RXTXListener}} implementation class will invoke according event method.
//         */
//        public void setRXTXListener(RXTXListener listener) {
//        this.mListener = listener;
//        }
//
//        /**
//         * This method will callback when the reader receive the data returned by reader.
//         * You can monitor the raw data return from reader via override it.
//         * And this method running in child thread.
//         *
//         * @param btAryReceiveData return data.
//         */
//        private void reciveData(byte[] btAryReceiveData) {
//        if (mListener != null) {
//        mListener.reciveData(btAryReceiveData);
//        }
//        }
//
//        /**
//         * This method will callback when the reader send the data returned by reader.
//         * You can monitor the raw data send to reader via override it.
//         *
//         * @param btArySendData the send data to reader.
//         */
//        private void sendData(byte[] btArySendData) {
//        if (mListener != null) {
//        mListener.sendData(btArySendData);
//        }
//        }
//
//        /**
//         * This method will callback when the monitor thread of data returned by reader is quit.
//         * You can monitor it to do something when the monitor thread of data returned by reader quit.
//         * And this method running in child thread.
//         */
//        private void onLostConnect() {
//        if (mListener != null) {
//        mListener.onLostConnect();
//        }
//        }
//
//        /**
//         * Send data to the reader.
//         *
//         * @param btCMDPackage the data package send to reader.
//         * @return Succeeded :0, Failed:-1
//         */
//        public int sendCommand(byte[] btCMDPackage) {
//        return mReader.sendBuffer(btCMDPackage);
//        }
//
//
//        abstract class ReaderBase {
//        private WaitThread mWaitThread = null;
//        private InputStream mInStream = null;
//        private OutputStream mOutStream = null;
//
//        private DataPackageParser mPackageParser;
//        private DataPackageProcess mPackageProcess;
//
//        /**
//         * Connection Lost. This function is invoked when the listen thread exist;
//         */
//        public abstract void onLostConnect();
//
//        /**
//         * With reference constructor. Construct a Reader with input and output streams.
//         *
//         * @param in  Input Stream
//         * @param out Output Stream
//         */
//        public ReaderBase(InputStream in, OutputStream out, DataPackageParser parser, DataPackageProcess process) throws Exception {
//        this.mInStream = in;
//        this.mOutStream = out;
//        if (parser == null || process == null)
//        throw new Exception("DataPackageParser && DataPackageProcess are null exception!");
//        mPackageParser = parser;
//        mPackageProcess = process;
//        StartWait(); //zhouyujie
//        }
//
//        //获取数据 zhouyujie
//        public void fetchData()
//        {
//        byte[] btAryBuffer = new byte[4096];
//        try {
//        int nLenRead = mInStream.read(btAryBuffer);
//        if (nLenRead > 0) {
//        byte[] btAryReceiveData = new byte[nLenRead];
//        System.arraycopy(btAryBuffer, 0, btAryReceiveData, 0,
//        nLenRead);
//        reciveData(btAryReceiveData);
//        mPackageParser.runReceiveDataCallback(btAryReceiveData, mPackageProcess);
//        }
//        } catch (IOException e) {
//        onLostConnect();
//        return;
//        } catch (Exception e) {
//        onLostConnect();
//        return;
//        }
//        }
//
//
//
//        public boolean IsAlive() {
//        return mWaitThread != null && mWaitThread.isAlive();
//        }
//
//        public void StartWait() {
//        mWaitThread = new WaitThread();
//        mWaitThread.start();
//        }
//
//        /**
//         * Loop receiving data thread.
//         *
//         * @author Jie
//         */
//        private class WaitThread extends Thread {
//        private boolean mShouldRunning = true;
//
//        public WaitThread() {
//        mShouldRunning = true;
//        }
//
//        @Override
//        public void run() {
//        byte[] btAryBuffer = new byte[4096];
//        while (mShouldRunning) {
//        try {
//        int nLenRead = mInStream.read(btAryBuffer);
//
//        if (nLenRead > 0) {
//        byte[] btAryReceiveData = new byte[nLenRead];
//        System.arraycopy(btAryBuffer, 0, btAryReceiveData, 0,
//        nLenRead);
//        reciveData(btAryReceiveData);
//        mPackageParser.runReceiveDataCallback(btAryReceiveData, mPackageProcess);
//        }
//        } catch (IOException e) {
//        onLostConnect();
//        return;
//        } catch (Exception e) {
//        onLostConnect();
//        return;
//        }
//        }
//        }
//
//        public void signOut() {
//        mShouldRunning = false;
//        interrupt();
//        }
//        }
//
//        /**
//         * Exit receive thread
//         */
//        public final void signOut() {
//        if (mWaitThread != null) {
//        mWaitThread.signOut();
//        mWaitThread = null;
//        }
//        try {
//        if (mInStream != null) {
//        mInStream.close();
//        mInStream = null;
//        }
//
//        if (mOutStream != null) {
//        mOutStream.close();
//        mOutStream = null;
//        }
//        } catch (IOException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//        mInStream = null;
//        mOutStream = null;
//        }
//        unRegisterObservers();
//        mPackageParser = null;
//        mPackageProcess = null;
//        }
//
//        /**
//         * Register Observer in order to get update.
//         *
//         * @param observer
//         */
//        public void registerObserver(Observer observer) {
//        mPackageProcess.addObserver(observer);
//        }
//
//        /**
//         * UnRegister Observer.
//         * @param observer
//         */
//        public void unRegisterObserver(Observer observer) {
//        mPackageProcess.deleteObserver(observer);
//        }
//
//        /**
//         * UnRegister all Observers;
//         */
//        public void unRegisterObservers() {
//        mPackageProcess.deleteObservers();
//        }
//
//        /**
//         * Read function' This function will be called after read data.
//         *
//         * @param btAryReceiveData
//         */
//        public void reciveData(byte[] btAryReceiveData) {
//        }
//
//        /**
//         * Rewritable function锛孴his function will be called after sending data.
//         *
//         * @param btArySendData Transmitted Data
//         */
//        public void sendData(byte[] btArySendData) {
//        }
//
//        /**
//         * Send data锛孶se synchronized() to prevent concurrent operation.
//         *
//         * @param btArySenderData To send data
//         * @return Succeeded :0, Failed:-1
//         */
//        private int sendMessage(byte[] btArySenderData) {
//
//        try {
//        synchronized (mOutStream) {        //Prevent Concurrent
//        mOutStream.write(btArySenderData);
//        }
//        } catch (IOException e) {
//        onLostConnect();
//        return -1;
//        } catch (Exception e) {
//        return -1;
//        }
//        sendData(btArySenderData);
//        return 0;
//        }
//
//        /**
//         * Send the command to module.
//         * @param completeCMD the command
//         * @return Succeeded :0, Failed:-1
//         */
//        public final int sendBuffer(byte[] completeCMD) {
//        int nResult = sendMessage(completeCMD);
//        return nResult;
//        }
//        }
//        }
