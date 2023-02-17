package com.ict.common;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileReName {
	// 파일 확장자가 3자리 일때만 가능한 메소드를 만들었다.
	// ex) jpeg는 따로 만들어줘야해
	public String exec(String path, String file_name) {
		File dir = new File(path);
		String[] arr = dir.list();
		List<String> k = Arrays.asList(arr);
		boolean res = k.contains(file_name);
		if (res) {
			String pre_name = file_name.substring(0, file_name.length() - 4);
			// 확장자구나
			String after_name = file_name.substring(pre_name.length());
			file_name = pre_name + "1" + after_name;

			System.out.println(pre_name);
			System.out.println(after_name);
		}
		return file_name;
	}
}
