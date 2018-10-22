package com.nxt.test.deom;

public class TestFinally {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i = 0;
		while(i < 10) {
			i++;
			try {
				if(i == 1) {
					continue;
				}
				if(i == 3) {
					break;
				}
				System.out.println("while:" + i);
			}catch(Exception e) {
				
			}finally {
				System.out.println("finally:" + i);
			}
		}
		System.out.println("end");
	}

}
