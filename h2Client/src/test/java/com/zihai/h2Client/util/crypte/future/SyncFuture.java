package com.zihai.h2Client.util.crypte.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SyncFuture<T> implements Future<T> {

	private CountDownLatch latch = new CountDownLatch(1);

	/** 响应结果 */
	private T response;

	/** 完成标记 */
	private volatile boolean doneFlag = false;

	/** Futrue的请求时间，用于计算Future是否超时 */
	private long beginTime = System.currentTimeMillis();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	/**
	 * 是否已完成
	 */
	@Override
	public boolean isDone() {
		return doneFlag;
	}

	/** 获取响应结果，直到有结果才返回 */
	@Override
	public T get() throws InterruptedException {
		latch.await();
		return this.response;
	}

	/** 获取响应结果，直到有结果或者超过指定时间就返回 */
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException {
		if (latch.await(timeout, unit)) {
			return this.response;
		}
		return null;
	}

	/** 用于设置响应结果，并且做countDown操作，通知请求线程 */
	public void setResponse(T response) {
		this.setResponse(response, true);
	}

	/**
	 * 设置响应结果
	 * 
	 * @param response 响应结果
	 * @param done     是否完成 true:完成并释放锁
	 */
	public void setResponse(T response, boolean done) {
		this.response = response;
		this.doneFlag = done;

		if (done) {
			latch.countDown();
		}
	}

	/**
	 * 已完成
	 */
	public void done() {
		doneFlag = true;
		latch.countDown();
	}

	public T getResponse() {
		return response;
	}

	public long getBeginTime() {
		return beginTime;
	}
}