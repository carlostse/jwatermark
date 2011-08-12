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
import java.util.Random;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * The tool to adding watermark to images,recommended markImage () methods to
 * create watermark image.
 */
public class ImageWatermark {
	
	private static final byte 	OFFSET_X = 10, 
								OFFSET_Y = 10;

	public static final byte 	MARK_LEFT_TOP 		= 1,
								MARK_RIGHT_TOP 		= 2,
								MARK_CENTER 		= 3,
								MARK_LEFT_BOTTOM 	= 4,
								MARK_RIGHT_BOTTOM 	= 5;

	/**
	 * add a text to an image. as a single color, the effect is rather poor.
	 * @param srcImg
	 * @param text
	 * @param font
	 * @param color
	 * @param offset_x
	 * @param offset_y
	 */
	public static void markText(File srcImg, String text, Font font, Color color, int offset_x, int offset_y) {
		if (!isReadableFile(srcImg))
			return;
		
		BufferedOutputStream out = null;
		try {
			Image src = ImageIO.read(srcImg);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			// g.setBackground(Color.white);
			g.setColor(color);
			g.setFont(font);
			g.drawString(text, offset_x, height - font.getSize() / 2 - offset_y);
			g.dispose();

			out = new BufferedOutputStream(new FileOutputStream(srcImg));
			
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(out);
		}
	}

	/**
	 * A logo picture to picture and watermark, effective this way one instance: <br>
	 * http://www.mvgod.com/images/poster/NaNiYaChuanQi2XKaiSiBinWangZi-10285-12811-19978-13383/3.jpg
	 * 
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
		
		BufferedOutputStream out = null;
		
		try {
			Image src = ImageIO.read(srcImg);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);

			Image mark_img = ImageIO.read(markImg);
			int mark_img_width = mark_img.getWidth(null);
			int mark_img_height = mark_img.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			
			switch (mark_position) {
			case ImageWatermark.MARK_LEFT_TOP:
				g.drawImage(mark_img, OFFSET_X, OFFSET_Y, mark_img_width, mark_img_height, null);
				break;
				
			case ImageWatermark.MARK_LEFT_BOTTOM:
				g.drawImage(mark_img, OFFSET_X, (height - mark_img_height - OFFSET_Y), mark_img_width, mark_img_height, null);
				break;
				
			case ImageWatermark.MARK_CENTER:
				g.drawImage(mark_img, (width - mark_img_width - OFFSET_X) / 2,
						(height - mark_img_height - OFFSET_Y) / 2, mark_img_width, mark_img_height, null);
				break;
				
			case ImageWatermark.MARK_RIGHT_TOP:
				g.drawImage(mark_img, (width - mark_img_width - OFFSET_X), OFFSET_Y, mark_img_width, mark_img_height, null);
				break;
				
			case ImageWatermark.MARK_RIGHT_BOTTOM:
			default:
				g.drawImage(mark_img, (width - mark_img_width - OFFSET_X), 
						(height - mark_img_height - OFFSET_Y), mark_img_width, mark_img_height, null);
			}

			g.dispose();
			
			out = new BufferedOutputStream(new FileOutputStream(outputImg));
			
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(out);
		}
	}

	/**
	 * mark watermark image, default place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 */
	public static void markImage(File srcImg, File markImg, File outputImg, float alpha) {
		markImage(srcImg, markImg, outputImg, alpha, ImageWatermark.MARK_RIGHT_BOTTOM);
	}

	/**
	 * mark watermark image, default 0.5 transparency and place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 */
	public static void markImage(File srcImg, File markImg, File outputImg) {
		markImage(srcImg, markImg, outputImg, 0.5f, ImageWatermark.MARK_RIGHT_BOTTOM);
	}

	/**
	 * mark watermark image, place watermark at random position
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 */
	public static void markImageRandomPos(File srcImg, File markImg, File outputImg, float alpha) {
		int[] a = { ImageWatermark.MARK_LEFT_TOP,
				ImageWatermark.MARK_RIGHT_TOP, ImageWatermark.MARK_LEFT_TOP,
				ImageWatermark.MARK_LEFT_BOTTOM,
				ImageWatermark.MARK_RIGHT_BOTTOM,
				ImageWatermark.MARK_RIGHT_BOTTOM, ImageWatermark.MARK_CENTER };

		int i = new Random().nextInt(a.length);
		markImage(srcImg, markImg, outputImg, alpha, a[i]);
	}

	/**
	 * Semi-transparent, random location imprint. With a slightly higher risk of
	 * lower right corner of the upper left corner, the central minimum risk.<br>
	 * default 0.5 transparency
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 */
	public static void markImageRandomPos(File srcImg, File markImg, File outputImg) {
		markImageRandomPos(srcImg, markImg, outputImg, 0.5f);
	}
	
	/**
	 * @param file
	 * @return true if the file is readable
	 */
	public static boolean isReadableFile(File file){
		return file != null && file.exists() && file.isFile();
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
