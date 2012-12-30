package com.jq.util;

/**
 * 	垃圾处理类.
 *  守护线程.
 * 	每30分钟执行一次,建议JVM执行垃圾处理.
 * */
public class GC extends Thread{
	
	public GC(){
		setDaemon(true);	//设置此进程为守护线程
		setPriority(NORM_PRIORITY);	//更改优先级
		start();
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(1800000);	//60*30=1800s=1800000ms
				System.gc();	//调用系统的垃圾处理gc
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
