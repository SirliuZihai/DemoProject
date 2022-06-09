package com.zihai.h2Client.service;

import com.zihai.h2Client.dto.BusinessException;
import com.zihai.h2Client.util.SubTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.locks.ReentrantLock;

/**
 * redis快照机制，将上下文上传至redis，带时间及mac号。同一台机器只会加载自已mac号的策略
 */
public abstract class StrategyService implements Lifecycle {
    static final Logger LOGGER = LoggerFactory.getLogger(StrategyService.class);
    ReentrantLock reentrantLock = new ReentrantLock();
    private volatile StateEnums stateEnums = StateEnums.STOP;
    Thread curThread;


    @Override
    public void start() {
        if (reentrantLock.tryLock()) {
            try {
                setStateEnums(StateEnums.RUNING);
                new Thread(new SubTask() {
                    @Override
                    public void action() {
                        curThread = Thread.currentThread();
                        while (stateEnums != StateEnums.STOP) {
                            if (stateEnums == StateEnums.PAUSE) {
                                strategyWaite();
                            } else {
                                excute();
                            }
                        }
                    }
                }, "strategy").start();
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new BusinessException("启动失败");
        }
    }

    @Override
    public void stop() {
        reentrantLock.lock();
        try {
            setStateEnums(StateEnums.STOP);
            strategyNotify();
            if (curThread != null && curThread != Thread.currentThread())
                curThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void pause() {
        if (reentrantLock.tryLock()) {
            Assert.isTrue(stateEnums == StateEnums.RUNING, "策略非运行中");
            setStateEnums(StateEnums.PAUSE);
            reentrantLock.unlock();
        } else {
            throw new BusinessException("正在操作");
        }

    }

    @Override
    public void resume() {
        if (reentrantLock.tryLock()) {
            try {
                Assert.isTrue(stateEnums != StateEnums.RUNING, "策略已经在运行");
                strategyNotify();
                setStateEnums(StateEnums.RUNING);
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new BusinessException("正在操作");
        }

    }

    protected void strategyWaite() {
        try {
            synchronized (reentrantLock) {
                reentrantLock.wait();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    protected void strategyNotify() {
        synchronized (reentrantLock) {
            reentrantLock.notifyAll();
        }
    }

    /**
     * 主流程
     */
    public abstract void excute();

    @Override
    public StateEnums getStatus() {
        return stateEnums;
    }

    public void setStateEnums(StateEnums stateEnums) {
        this.stateEnums = stateEnums;
    }
}
