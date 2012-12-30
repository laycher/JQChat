
package com.jq.client.protocol.tcp;

/**
 * 	服务接口,提供各种服务器服务.
 * @param msg 向服务器发送的信息.
 * */
public interface Service<T,U> {
	public T service(U msg);
}