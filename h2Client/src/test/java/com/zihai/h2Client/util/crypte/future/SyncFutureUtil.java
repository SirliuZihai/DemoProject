package com.zihai.h2Client.util.crypte.future;

import com.huasheng.quant.open.sdk.protobuf.common.msg.RequestProto.PBRequest;
import com.huasheng.quant.open.sdk.protobuf.common.msg.ResponseProto.PBResponse;
import com.huasheng.quant.open.sdk.protocol.ProtoBufRequestMessage;
import com.huasheng.quant.open.sdk.protocol.ProtoBufResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SyncFutureUtil {

	private final static Logger logger = LoggerFactory.getLogger(SyncFutureUtil.class);

	public static final Map<String, SyncFuture<ProtoBufResponseMessage>> INVOKE_MAP = new ConcurrentHashMap<>();
	
	public static SyncFuture<ProtoBufResponseMessage> addSendMsg(ProtoBufRequestMessage sendMsg) {
		PBRequest req = sendMsg.getMsgBody();
		String requestId = req.getRequestId();

		SyncFuture<ProtoBufResponseMessage> syncFuture = new SyncFuture<>();
		INVOKE_MAP.put(requestId, syncFuture);
		
		return syncFuture;
	}

	public static ProtoBufResponseMessage getResult(ProtoBufRequestMessage sendMsg) {
		return getResult(sendMsg, 15);
	}

	public static ProtoBufResponseMessage getResult(ProtoBufRequestMessage sendMsg, long timeout) {

		PBRequest req = sendMsg.getMsgBody();
		String requestId = req.getRequestId();

		SyncFuture<ProtoBufResponseMessage> syncFuture = INVOKE_MAP.get(requestId);

		if (syncFuture == null) {
			return null;
		}
		
		try {
			return syncFuture.get(timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("等待响应结果异常，requestId:{}", requestId);
		} finally {
			INVOKE_MAP.remove(requestId);
		}

		return null;
	}

	public static void receivedResult(ProtoBufResponseMessage respMsg) {
		PBResponse resp = respMsg.getMsgBody();
		String requestId = resp.getRequestId();

		SyncFuture<ProtoBufResponseMessage> syncFuture = INVOKE_MAP.get(requestId);

		if (syncFuture == null) {
			return;
		}

		syncFuture.setResponse(respMsg, true);
	}

	public static void clearExpire() {
		long nowTime = System.currentTimeMillis();
		Iterator<Map.Entry<String, SyncFuture<ProtoBufResponseMessage>>> iter = INVOKE_MAP.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry<String, SyncFuture<ProtoBufResponseMessage>> entry = iter.next();
			if (nowTime - entry.getValue().getBeginTime() >= 3 * 60 * 1000) {
				logger.info("清除过期等待，requestId:{}", entry.getKey());
				iter.remove();
			}
		}
	}

}
