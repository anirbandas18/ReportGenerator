package com.sss.report;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Indranil {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Path filePath = Paths.get(args[0]);
		String fullFileName = filePath.getFileName().toString();
		String fileName = fullFileName.split("\\.")[0];
		String fileLocation = filePath.getParent().toString();
		String fullNewFileName = fileName + ".filter";
		Path newFilePath = Paths.get(fileLocation,fullNewFileName);
		BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
		String line = "";
		List<String> values = new ArrayList<>();
		String key = "";
		int count = 0;
		while((line = br.readLine()) != null) {
			String cells[] = line.split("\",\"");
			String firstCell = cells[0].substring(1);
			if(count == 0) {
				key = String.format("%s in ", firstCell);
			} else {
				if(!firstCell.contains(",")) {
					values.add("'" + firstCell + "'");
				}
			}
			count++;
		}
		br.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter(newFilePath.toString()));
		String content = values.toString();
		content = content.substring(1, content.length() - 1);
		bw.write(String.format(key + "(%s)", content));
		bw.newLine();
		bw.flush();
		bw.close();
	}

}
