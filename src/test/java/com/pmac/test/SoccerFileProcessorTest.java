package com.pmac.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.pmac.asses.main.io.SoccerFileProcessor;

public class SoccerFileProcessorTest {

	@Test
	public void test() throws IOException {

		URL url = this.getClass().getResource("/soccer.dat");
		File testFile = new File(url.getFile());

		SoccerFileProcessor fileProcessor = new SoccerFileProcessor();
		Optional<Entry<Integer, String>> tspreadMap = fileProcessor.processFile(testFile);

		Assert.assertNotNull("tspreadMap Map is null;", tspreadMap);
		Assert.assertTrue("Missing keys in tspreadMap;", tspreadMap.isPresent());

		if (tspreadMap.isPresent()) {
			
			Assert.assertTrue("Temperature spread difference mismatch in tspreadMap;", Integer.valueOf(1).equals(tspreadMap.get().getKey()));
			Assert.assertTrue("Temperature spread Day Value  mismatch in tspreadMap;", "Aston_Villa".equals(tspreadMap.get().getValue()));

		}

	}

}
