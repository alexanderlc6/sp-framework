package com.sp.framework.utilities.http.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.sp.framework.utilities.http.HttpJsonClient;

/**
 *********************************************** 
 * @Copyright (c) by soap All right reserved.
 * @Create Date: 2015年12月24日 上午9:35:25
 * @Create Author: luchao1@yonghui.cn
 * @File Name: RemoteInvocationSerializing
 * @Function: dubbo序列化，取自 org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor
 * @Last version: 1.0
 * @Last Update Date:
 * @Change Log:
 ************************************************* 
 */
public class DubboRemoteUtil<T> extends AbstractHttpInvokerRequestExecutor {
	
	/**
	 * @Project common-dubbo
	 * @Package com.yh.common.dubbo.remote
	 * @Method invoker方法.<br>
	 * @Description 通过tcp调用dubbo服务，此方法仅调用dubbo协议为http的接口，其他接口未测试
	 * @author Alex Lu
	 * @date 2015年12月24日 下午12:42:27
	 * @param callback	返回的class类型
	 * @param url		需要调用url地址
	 * @param methodName	需要调用的方法名
	 * @param arguments		传入的参数，按顺序传输
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoker(Class<T> callback,String url,String methodName, Object... arguments) throws IOException, ClassNotFoundException {
		RemoteInvocation invocation = new RemoteInvocation();
		Class<?>[] parameterTypes = new  Class<?>[arguments.length];
		for(int i = 0; i < arguments.length; i++) {
			parameterTypes[i] = arguments[i].getClass();
		}
		invocation.setMethodName(methodName);
		invocation.setParameterTypes(parameterTypes);
		invocation.setArguments(arguments);
		DubboRemoteUtil<T> serializing = new DubboRemoteUtil<T>();
		ByteArrayOutputStream outputStream = serializing.getByteArrayOutputStream(invocation);
		InputStream is = HttpJsonClient.postJsonDataByJson(url, outputStream.toByteArray(), 0);
		RemoteInvocationResult result = serializing.doReadRemoteInvocationResult(new ObjectInputStream(is));
		return (T) result.getValue();
	}

	@Override
	protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
			throws Exception {
		return null;
	}
}
