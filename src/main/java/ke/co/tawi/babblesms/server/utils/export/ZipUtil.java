package ke.co.tawi.babblesms.server.utils.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class that allows for working with Zip archives.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 9, 2012
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * @version %I%, %G%
 */

public class ZipUtil {
	
	private static Logger logger = LogManager.getLogger(ZipUtil.class);
	
	
	 /**
     * Compresses a collection of files to a destination zip file.
     * 
     * @param listFiles A collection of files and directories
     * @param zipFile This should include the full path of the file e.g. /tmp/myArchive.zip
	 * @return whether the action was successful or not
     */
   public static boolean compressFiles(List<File> listFiles, String zipFile) {
	   boolean success = true;
	   
	   ZipOutputStream zos = null;
       try {
    	    FileUtils.touch(new File(zipFile));
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			
			for (File file : listFiles) {
				addToZipFile(file.toString(), zos, true);				
		    }
			
			zos.flush();
			zos.close();			
		   
		} catch (FileNotFoundException e) {
			logger.error("IOException when trying to when trying to zip file.");
			logger.error(ExceptionUtils.getStackTrace(e));
			success = false;
			
		} catch (IOException e) {
			logger.error("IOException when trying to when trying to zip file.");
			logger.error(ExceptionUtils.getStackTrace(e));
			success = false;
		}
		
		return success;       
   }
   
   
   /**
    * 
    * @param fileName
    * @param zos
    * @param flatten whether or not to flatten the files, that is, whether or 
    * 		 not to add them with their relative original paths
    * @throws FileNotFoundException
    * @throws IOException
    */
   public static void addToZipFile(String fileName, ZipOutputStream zos, boolean flatten) 
		   throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
				
		ZipEntry zipEntry = (flatten) ? 
				new ZipEntry(FilenameUtils.getName(fileName)) : new ZipEntry(fileName);
				
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
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
