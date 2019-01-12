package com.pmac.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.pmac.asses.main.io.WeatherFileProcessor;

public class WeatherFileProcessorTest {

	@Test
	public void test() throws IOException {

		URL url = this.getClass().getResource("/w_data.dat");
		File testFile = new File(url.getFile());

		WeatherFileProcessor fileProcessor = new WeatherFileProcessor();
		Optional<Entry<Integer, Integer>> tspreadMap = fileProcessor.processFile(testFile);

		Assert.assertNotNull("tspreadMap Map is null;", tspreadMap);
		Assert.assertTrue("Missing keys in tspreadMap;", tspreadMap.isPresent());

		if (tspreadMap.isPresent()) {
			
			Assert.assertTrue("Temperature spread difference mismatch in tspreadMap;", Integer.valueOf(2).equals(tspreadMap.get().getKey()));
			Assert.assertTrue("Temperature spread Day Value  mismatch in tspreadMap;", Integer.valueOf(14).equals(tspreadMap.get().getValue()));

		}

	}

}
