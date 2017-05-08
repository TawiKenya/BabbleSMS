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
package ke.co.tawi.babblesms.server.servlet.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jasypt.util.text.BasicTextEncryptor;


/**
 * Generates images from fonts.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class FontImageGenerator extends HttpServlet {
     		
	
	// The following are settings for the Captcha mechanism
	final int CAPTCHA_IMAGE_FONT_SIZE = 25;
	final int CAPTCHA_IMAGE_FONT_STYLE = 0;
	final String CAPTCHA_IMAGE_FONT_TYPE = "Arial";
	final String CAPTCHA_IMAGE_FOREGROUND_RGB = "140-140-140";
	final String CAPTCHA_IMAGE_BACKGROUND_RGB = "233-233-233";

	// This variable is to be used especially with the Jasypt library.
	public final static String SECRET_KEY = "Vuwachip2";
	
	private BasicTextEncryptor textEncryptor;
	
	private Logger logger;
	
	/**
	 * 
	 * @param config
	 * @throws ServletException
	 */
	@Override
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SECRET_KEY);
		
		logger = LogManager.getLogger(this.getClass());
    }

    
    /**
	 * 
	 * @param request
      * @param response
      * @throws ServletException
      * @throws IOException  
	 */	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("image/png");
		
		String text = request.getParameter("text");		
		
		if(text == null || text.length() == 0) {
			text = "Error";
		}				
		
		int[] fgArray = {Integer.parseInt(CAPTCHA_IMAGE_FOREGROUND_RGB.split("-")[0]), 
				Integer.parseInt(CAPTCHA_IMAGE_FOREGROUND_RGB.split("-")[1]), 
				Integer.parseInt(CAPTCHA_IMAGE_FOREGROUND_RGB.split("-")[2])};
		
		int[] bgArray = {Integer.parseInt(CAPTCHA_IMAGE_BACKGROUND_RGB.split("-")[0]), 
				Integer.parseInt(CAPTCHA_IMAGE_BACKGROUND_RGB.split("-")[1]), 
				Integer.parseInt(CAPTCHA_IMAGE_BACKGROUND_RGB.split("-")[2])};
				
		BufferedImage buffImage = getBufferedImage(URLDecoder.decode(textEncryptor.decrypt(text), "UTF-8"), 
				CAPTCHA_IMAGE_FONT_TYPE, CAPTCHA_IMAGE_FONT_STYLE, CAPTCHA_IMAGE_FONT_SIZE,
				new Color(fgArray[0], fgArray[1], fgArray[2]), 
				new Color(bgArray[0], bgArray[1], bgArray[2]));
		
        OutputStream os = response.getOutputStream();
        ImageIO.write(buffImage, "png", os);
        os.close();        
	}
	
    
	/**
	 * 
	 * @param request
	 * @param response
      * @throws ServletException 
      * @throws IOException  
	 */	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doPost(request, response);
	}
	
	
    /**
	 * 
	 * @param text
	 * @param fontType
	 * @param foreground
	 * @param background
	 * @return BufferedImage
	 */
	private BufferedImage getBufferedImage(String text, String fontType, int fontStyle, int fontSize, 
			Color foreground, Color background) {
		Font font = null;
		BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffer.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		FontRenderContext frc = g.getFontRenderContext();
		
		try {			
			font = new Font(fontType, fontStyle, fontSize);
			Rectangle2D bounds = font.getStringBounds(text, frc);
			
			// calculate the size of the text
			int width = (int) bounds.getWidth();
			int height = (int) bounds.getHeight();
			
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			g = buffer.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);				
			g.setFont(font);

			// draw the text						
			g.setColor(background);
			g.fillRect(0, 0, width, height);
			g.setColor(foreground);
			g.drawString(text, 0, (int)-bounds.getY());			
			
		} catch(IndexOutOfBoundsException e) { 
			logger.error("IndexOutOfBoundsException e");
			logger.error(e);
			
		} catch(IllegalStateException e) { 
			logger.error("IllegalStateException e");
			logger.error(e);
			
		} catch(IllegalArgumentException e) { 			
			logger.error("IllegalArgumentException e");
			logger.error(e);
		}	
		
		return buffer;
	}

}
