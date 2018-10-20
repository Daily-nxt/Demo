package com.nxt.test.deom;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		InetAddress addr = InetAddress.getLocalHost();  
        String ip=addr.getHostAddress().toString(); //获取本机ip 
        System.out.println(ip);
	}

}
