package com.sur.serviceimpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sur.constants.Constants;
import com.sur.service.LogProcess;
import com.sur.service.Search;

@Service("logProcessService")
public class LogProcessImpl implements LogProcess {
	@Resource(name = "searchService")
	private Search searchService;

	public HashSet<String> logErrors;

	@Override
	public HashSet<String> preprocess(CommonsMultipartFile file) {
		logErrors = new HashSet<String>();
		// 获得原始文件名
		String fileName = file.getOriginalFilename();
		System.out.println("原始文件名:" + fileName);

		// 新文件名
		// String newFileName = UUID.randomUUID() + fileName;
		String newFileName = fileName;

		// 上传位置
		File fp = new File(Constants.UPLOAD_DIRECTORY);
		if (!fp.exists()) {
			fp.mkdirs();
		}
		if (!file.isEmpty()) {
			try {
				String filePath = Constants.UPLOAD_DIRECTORY + newFileName;
				System.out.println("filepath = " + filePath);
				InputStreamReader in = new InputStreamReader(file.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(in);
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
				String line = bufferedReader.readLine();
				Pattern pattern = Pattern.compile("\\d+[a-z]ERROR ");
				Pattern splitPattern = Pattern.compile("\\e");
				Boolean isExistErrors = false;
				while (line != null) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						isExistErrors = true;
						// System.out.println("~~~~~~~~~~~~~ " + line);
						String[] log = splitPattern.split(line);
						String[] errorInfo = log[log.length - 2].split(" ");
						String error = String.join(" ", Arrays.copyOfRange(errorInfo, 1, errorInfo.length));
						// System.out.println("error: " + error);
						logErrors.add(error);
					}
					bufferedWriter.write(line);
					line = bufferedReader.readLine();
				}
				if (!isExistErrors) {
					System.out.println("Not find errors!");
				}
				bufferedWriter.flush();
				bufferedReader.close();
				bufferedWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return logErrors;

	}

}
