package com.sp.framework.registry.zk.plus.bean;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class ZkSerializerImpl implements ZkSerializer {

	public byte[] serialize(Object data) throws ZkMarshallingError {
		return data.toString().getBytes();
	}

	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		return new String(bytes);
	}

}
