package com.ysma.ppt.intf;

public interface ILogService {

    /**
     * log日志
     * @param args 入参
     * @param annLog 注解[渠道、onOff]
     * @param costTime 耗时
     * @param objs 返参/异常
     */
    void log(Object[] args, AnnLog annLog, long costTime, Object... objs);
}
