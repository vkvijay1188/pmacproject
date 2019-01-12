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
import com.pmac.asses.pojo.SoccerData;

public class SoccerFileProcessor {

	private SortedSetMultimap<Integer, String> soccerDataMap;

	//method to process the file and return the result
	public Optional<Entry<Integer, String>> processFile(File inputFile) throws IOException {

		soccerDataMap = TreeMultimap.create(Ordering.natural(), Ordering.natural());
		FileInputStream inputStream = null;
		Scanner fileScan = null;
		boolean dataLine = false;

		try {

			inputStream = new FileInputStream(inputFile);
			fileScan = new Scanner(inputStream, "UTF-8");

			while (fileScan.hasNextLine()) {

				String readLine = fileScan.nextLine();

				//Position the cursor to column header 
				if ( !dataLine && readLine.trim().startsWith("Team")) {
					dataLine = true;
					continue;
				}
				
				
				//EOF check
				if (dataLine && readLine.trim().startsWith("/pre")) {
					dataLine = false;
				}

				//Datarow
				if (dataLine && !readLine.isEmpty() && ! readLine.trim().startsWith("-")) {

					String[] tokens = readLine.split("\\s+");
					
					if (tokens.length > 3) {
						SoccerData soccerDataObj = new SoccerData();
						soccerDataObj.setTeam(tokens[2]);
						soccerDataObj.setForGoal(Integer.parseInt(tokens[7]));
						soccerDataObj.setAgainstGoal(Integer.parseInt(tokens[9]));
						storeTemperatureSpread(soccerDataObj);
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

		return soccerDataMap.entries().stream().findFirst();
	}

	//methods to calculate the difference and store them as multimap
	private void storeTemperatureSpread(SoccerData soccerDataObj) {
		Integer difference = Math.abs(soccerDataObj.getForGoal() - soccerDataObj.getAgainstGoal());

		try {
			
			soccerDataMap.put(difference, soccerDataObj.getTeam());
			
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
		}
	}
		 

}