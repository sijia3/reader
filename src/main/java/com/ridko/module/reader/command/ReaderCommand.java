package com.ridko.module.reader.command;

/**
 * 读写器命令接口
 * 将依赖外部api的命令转移到命令类中实现
 * @author sijia3
 * @date 2021/1/6 11:40
 */
public interface ReaderCommand {

    /**
     * 执行命令接口
     */
    void execute();
}
