package jwatermark;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * The tool to adding watermark to images, <br>
 * recommended markImage() methods to create watermark image.
 */
public class ImageWatermark {
	
	private static final byte 	OFFSET_X = 10, 
								OFFSET_Y = 10;

	private static final float	DEFAULT_TRANS		= 0.5f;
	private static final String	OUTPUT_FORMAT		= "jpg";
	
	public static final byte 	MARK_LEFT_TOP 		= 1,
								MARK_RIGHT_TOP 		= 2,
								MARK_CENTER 		= 3,
								MARK_LEFT_BOTTOM 	= 4,
								MARK_RIGHT_BOTTOM 	= 5;
	
	/**
	 * Add a text on an image
	 * @param srcImg
	 * @param text
	 * @param font
	 * @param color
	 * @param offsetX
	 * @param offsetY
	 */
	public static void markText(File srcImg, String text, Font font, Color color, int offsetX, int offsetY) {
		if (!isReadableFile(srcImg))
			return;
		
		BufferedImage image = null;
		BufferedOutputStream out = null;
		
		try {
			Image src = ImageIO.read(srcImg);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			// g.setBackground(Color.white);
			g.setColor(color);
			g.setFont(font);
			g.drawString(text, offsetX, height - font.getSize() / 2 - offsetY);
			g.dispose();
			g = null;
			
			out = new BufferedOutputStream(new FileOutputStream(srcImg));
			encodeJPEG(image, out);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(out);
			image = null;
		}
	}

	/**
	 * Mark the watermark on an image
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 * @param mark_position -- Watermark position, the four corners and central respectively, <br>
	 * and so constant that ImageWatermark.MARK_LEFT_TOP
	 */
	public final static void markImage(File srcImg, File markImg, File outputImg, float alpha, int mark_position) {
		if (!isReadableFile(srcImg) || !isReadableFile(markImg))
			return;
		
		BufferedImage image = null;
		BufferedOutputStream out = null;
		
		try {
			Image 	src = ImageIO.read(srcImg),
					watermark = ImageIO.read(markImg);
			
			int srcWidth = src.getWidth(null),
				srcHeight = src.getHeight(null),
				wmWidth = watermark.getWidth(null),
				wmHeight = watermark.getHeight(null);
			
			image = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, srcWidth, srcHeight, null);			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			
			switch (mark_position) {
			case ImageWatermark.MARK_LEFT_TOP:
				g.drawImage(watermark, OFFSET_X, OFFSET_Y, wmWidth, wmHeight, null);
				break;
				
			case ImageWatermark.MARK_LEFT_BOTTOM:
				g.drawImage(watermark, OFFSET_X, (srcHeight - wmHeight - OFFSET_Y), wmWidth, wmHeight, null);
				break;
				
			case ImageWatermark.MARK_CENTER:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X) / 2,
						(srcHeight - wmHeight - OFFSET_Y) / 2, wmWidth, wmHeight, null);
				break;
				
			case ImageWatermark.MARK_RIGHT_TOP:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X), OFFSET_Y, wmWidth, wmHeight, null);
				break;
				
			case ImageWatermark.MARK_RIGHT_BOTTOM:
			default:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X), 
						(srcHeight - wmHeight - OFFSET_Y), wmWidth, wmHeight, null);
			}

			g.dispose();
			g = null;
			
			out = new BufferedOutputStream(new FileOutputStream(outputImg));
			encodeJPEG(image, out);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(out);
			image = null;
		}
	}

	/**
	 * Mark watermark image, default place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 */
	public static void markImage(File srcImg, File markImg, File outputImg, float alpha) {
		markImage(srcImg, markImg, outputImg, alpha, ImageWatermark.MARK_RIGHT_BOTTOM);
	}

	/**
	 * Mark watermark image, default transparency and place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 */
	public static void markImage(File srcImg, File markImg, File outputImg) {
		markImage(srcImg, markImg, outputImg, DEFAULT_TRANS, ImageWatermark.MARK_RIGHT_BOTTOM);
	}

	/**
	 * Mark watermark image, place watermark at random position
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 */
	public static void markImageRandomPos(File srcImg, File markImg, File outputImg, float alpha) {
		int[] a = { ImageWatermark.MARK_LEFT_TOP,
					ImageWatermark.MARK_RIGHT_TOP, 
					ImageWatermark.MARK_LEFT_TOP,
					ImageWatermark.MARK_LEFT_BOTTOM,
					ImageWatermark.MARK_RIGHT_BOTTOM,
					ImageWatermark.MARK_RIGHT_BOTTOM, 
					ImageWatermark.MARK_CENTER };

		int i = new Random().nextInt(a.length);
		markImage(srcImg, markImg, outputImg, alpha, a[i]);
	}

	/**
	 * Semi-transparent, random location imprint. With a slightly higher risk of
	 * lower right corner of the upper left corner, the central minimum risk.<br>
	 * default transparency
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 */
	public static void markImageRandomPos(File srcImg, File markImg, File outputImg) {
		markImageRandomPos(srcImg, markImg, outputImg, DEFAULT_TRANS);
	}
	
	/**
	 * @param file
	 * @return true if the file is readable
	 */
	public static boolean isReadableFile(File file){
		return file != null && file.exists() && file.isFile();
	}
	
	/**
	 * Encode the image and output to output stream
	 * @param image
	 * @param out
	 * @throws Exception
	 */
	private static void encodeJPEG(BufferedImage image, OutputStream out) throws Exception {
//		com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
//		com.sun.image.codec.jpeg.JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
//		param.setQuality(0.9f, false);
//		encoder.encode(image, param);

		ImageIO.write(image, OUTPUT_FORMAT, out);
	}

	/**
	 * Close the outputstream
	 * @param out
	 */
	private static void closeOutputStream(BufferedOutputStream out){
		if (out == null)
			return;
		
		try {
			out.close();
		} catch (IOException e) {
			
		}
		out = null;
	}
}
