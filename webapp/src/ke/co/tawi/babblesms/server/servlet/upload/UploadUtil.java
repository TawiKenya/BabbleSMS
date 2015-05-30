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
package ke.co.tawi.babblesms.server.servlet.upload;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Description of class.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., 29 May 2015  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class UploadUtil {

	private static Logger logger = Logger.getLogger(UploadUtil.class);
	
	/**
	 * @param file
	 * @return the feedback of having inspected the file, whether it was proper
	 */
	protected String inspectContactFile(File file) {
		String feedback = ContactUpload.UPLOAD_SUCCESS;
		int count = 1;
		
		LineIterator lineIterator =  null;
		try {
			lineIterator = FileUtils.lineIterator(file, "UTF-8");
			
			String line;
			String[] tokens;
			while (lineIterator.hasNext()) {
			     line = lineIterator.nextLine();
			     
			     tokens = StringUtils.split(line, ',');
			     
			     if(tokens.length != 3 && line.length() > 0) {// Every row must have 3 columns
			    	 return ("Invalid format on line " + count + ": " + line);
			     }
			     
			     count++;
		    }
			
		} catch (IOException e) {
			logger.error("IOException when inspecting: " + file);
			logger.error(e);
			
		} finally {
			if(lineIterator != null) {
				lineIterator.close();
			}		   
		}
		 
		return feedback;
	}
}

