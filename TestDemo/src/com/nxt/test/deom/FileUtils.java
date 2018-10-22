package com.nxt.test.deom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static List<String> getText(){
		List<String> result = new ArrayList<String>();
		try {
			File file = new File("oppositeInnerSet.dat");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		List<String> list = getText();
		System.out.println(list.toString());
	}
}
