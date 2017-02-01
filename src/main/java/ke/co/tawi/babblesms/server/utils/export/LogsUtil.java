package ke.co.tawi.babblesms.server.utils.export;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;

/**
 * A utility class that filters through a file and extracts a specific
 * pattern from the file
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 5, 2012
 * 
 * @author <a href="mailto:maureenc@tawi.mobi">Maureen Jepchumba</a>
 * @version %I%, %G%
 */

public class LogsUtil {
	
	private static final Logger logger = Logger.getLogger(LogsUtil.class);
	
	/**
	 * This method extracts lines in the file containing the specified pattern and return the new
	 * file that only contains those lines. It will return an empty file if the pattern is not 
	 * found
	 * 
	 * @param file The file from which a pattern needs to be extracted
	 * @param newFileName A String prefix of the new file to be created 
	 * @param searchString The string that needs to be extracted from the file
	 * @return newFile The new file containing the pattern
	 */
	public static File getPatternInFile(File file, String newFileName, String searchString) {
		String oldFileName = file.getName();
		File newFile = new File(FileUtils.getTempDirectoryPath() + File.separator + newFileName + "-" + oldFileName);
		
		String matchingString = "";
		String nextLine = "";
		
		Pattern pattern =  Pattern.compile(searchString);
		Matcher matcher = pattern.matcher("");
		
		FileInputStream fstream = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			fstream = new FileInputStream(file);
			bufferedWriter = new BufferedWriter(new FileWriter(newFile));
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//Read File Line By Line
			while ((nextLine = br.readLine()) != null)   {
				matcher.reset(nextLine);
				if (matcher.find()) {
					matchingString = nextLine + "\n";
					bufferedWriter.write(matchingString);
				}
			}
			
			FileUtils.deleteQuietly(file);
			in.close();
			bufferedWriter.flush();
			bufferedWriter.close();
			
		} catch (IOException e) {
			logger.error("IOException when trying to when trying to read file.");
			logger.error(e);
		} 
		return newFile;
     }
	
}

/*
** Local Variables:
**   mode: java
**   c-basic-offset: 2
**   tab-width: 2
**   indent-tabs-mode: nil
** End:
**
** ex: set softtabstop=2 tabstop=2 expandtab:
**
*/