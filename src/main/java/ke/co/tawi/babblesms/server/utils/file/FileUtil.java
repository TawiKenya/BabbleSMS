/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Various file utilities.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class FileUtil {

	private static Logger logger = LogManager.getLogger(FileUtil.class);
	
	/**
	* Identify file type of file with provided path and name
	* using JDK 7's Files.probeContentType(Path).
	*
	* @param fileName Name of file whose type is desired.
	* @return String representing identified type of file with provided name.
	*/
	public static String identifyFileType(final String fileName) {
	
		String fileType = "Undetermined";
		final File file = new File(fileName);
		try	{
			fileType = Files.probeContentType(file.toPath());
			
		} catch (IOException e) {
			logger.error("ERROR: Unable to determine file type for " + fileName);
			logger.error(e);
		}
		
		return fileType;
	}
}

