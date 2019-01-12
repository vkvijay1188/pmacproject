package com.pmac.asses.main.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.pmac.asses.pojo.DailyTemperature;

public class WeatherFileProcessor {

	private SortedSetMultimap<Integer, Integer> tspreadMap;

	//method to process the file and return the result
	public Optional<Entry<Integer, Integer>> processFile(File inputFile) throws IOException {

		tspreadMap = TreeMultimap.create(Ordering.natural(), Ordering.natural());
		FileInputStream inputStream = null;
		Scanner fileScan = null;
		boolean dataLine = false;

		try {

			inputStream = new FileInputStream(inputFile);
			fileScan = new Scanner(inputStream, "UTF-8");

			while (fileScan.hasNextLine()) {

				String readLine = fileScan.nextLine();

				//Position the cursor to column header 
				if (!dataLine && readLine.trim().startsWith("Dy")) {
					dataLine = true;
					continue;
				}
				
				//EOF check
				if (dataLine && readLine.trim().startsWith("mo")) {
					dataLine = false;
				}

				if (dataLine && !readLine.isEmpty()) {

					String[] tokens = readLine.replace("*", "").split("\\s+");
					
					if (tokens.length > 3) {
						DailyTemperature dailyTemp = new DailyTemperature();
						dailyTemp.setDayNumber(Integer.parseInt(tokens[1]));
						dailyTemp.setMaxTemp(Integer.parseInt(tokens[2]));
						dailyTemp.setMinTemp(Integer.parseInt(tokens[3]));
						storeTemperatureSpread(dailyTemp);
					}

				}

			}

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (fileScan != null) {
				fileScan.close();
			}
		}

		return tspreadMap.entries().stream().findFirst();
	}

	private void storeTemperatureSpread(DailyTemperature dailyTemp) {
		Integer difference = dailyTemp.getMaxTemp() - dailyTemp.getMinTemp();

		try {
			tspreadMap.put(difference, dailyTemp.getDayNumber());
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
		}
	}
		 

}