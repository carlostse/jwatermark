/*
Copyright (c) 2011, Carlos Tse <copperoxide@gmail.com>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package jwatermark;

import static jwatermark.Constant.*;
import static jwatermark.Util.*;
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

	private static final float	DEFAULT_TRANS	= 0.5f;
	private static final String	OUTPUT_FORMAT	= "jpg";

	/**
	 * Add a text on an image
	 * @param srcImg
	 * @param text
	 * @param font
	 * @param color
	 * @param offsetX
	 * @param offsetY
	 * @return true if success or fail if failed
	 */
	public static boolean markText(File srcImg, String text, Font font, Color color, int offsetX, int offsetY) {
		if (!isReadableFile(srcImg))
			return true;

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
			log("markText, Exception: ", e);
			return false;
		} finally {
			closeOutputStream(out);
			image = null;
		}
		return true;
	}

	/**
	 * Mark the watermark on an image
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 * @param position -- Watermark position, the four corners and central respectively, <br>
	 * and so constant that ImageWatermark.MARK_LEFT_TOP
	 * @return true if success or fail if failed
	 */
	public final static boolean markImage(File srcImg, File markImg, File outputImg, float alpha, byte position) {
		if (!isReadableFile(srcImg) || !isReadableFile(markImg))
			return false;

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

			switch (position) {
			case MARK_LEFT_TOP:
				g.drawImage(watermark, OFFSET_X, OFFSET_Y, wmWidth, wmHeight, null);
				break;

			case MARK_LEFT_BOTTOM:
				g.drawImage(watermark, OFFSET_X, (srcHeight - wmHeight - OFFSET_Y), wmWidth, wmHeight, null);
				break;

			case MARK_CENTER:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X) / 2,
						(srcHeight - wmHeight - OFFSET_Y) / 2, wmWidth, wmHeight, null);
				break;

			case MARK_RIGHT_TOP:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X), OFFSET_Y, wmWidth, wmHeight, null);
				break;

			case MARK_RIGHT_BOTTOM:
			default:
				g.drawImage(watermark, (srcWidth - wmWidth - OFFSET_X),
						(srcHeight - wmHeight - OFFSET_Y), wmWidth, wmHeight, null);
			}

			g.dispose();
			g = null;

			out = new BufferedOutputStream(new FileOutputStream(outputImg));
			encodeJPEG(image, out);

		} catch (Exception e) {
			log("markImage, Exception: ", e);
			return false;
		} finally {
			closeOutputStream(out);
			image = null;
		}
		return true;
	}

	/**
	 * Mark watermark in whole image
	 * @param srcImg
	 * @param markImg
	 * @param outputImg
	 * @param alpha
	 * @param mark_position
	 * @return true if success or fail if failed
	 */
	public final static boolean markWholeImage(File srcImg, File markImg, File outputImg, float alpha) {
		if (!isReadableFile(srcImg) || !isReadableFile(markImg))
			return false;

		BufferedImage image = null;
		BufferedOutputStream out = null;
		int x, y = 0;

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

			// round to integer
			int xTimes = srcWidth / wmWidth,
				yTimes = srcHeight / wmHeight;

			// at least 1
			if (xTimes < 1) xTimes = 1;
			if (yTimes < 1) yTimes = 1;
			log("wmW: " + wmWidth + ", srcW: " + srcWidth + ", xTimes: " + xTimes);
			log("wmH: " + wmHeight + ", srcH: " + srcHeight + ", yTimes: " + yTimes);

			// draw the watermark to the whole image
			while (y < yTimes){
				x = 0;
				while (x < xTimes){
//					log("x: " + x + ", y: " + y);
					g.drawImage(watermark, (x++ * wmWidth), (y * wmHeight), wmWidth, wmHeight, null);
				}
				y++;
			}

			g.dispose();
			g = null;

			out = new BufferedOutputStream(new FileOutputStream(outputImg));
			encodeJPEG(image, out);

		} catch (Exception e) {
			log("markWholeImage, Exception: ", e);
			return false;
		} finally {
			closeOutputStream(out);
			image = null;
		}
		return true;
	}

	/**
	 * Mark watermark image, default place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 * @return true if success or fail if failed
	 */
	public static boolean markImage(File srcImg, File markImg, File outputImg, float alpha) {
		return markImage(srcImg, markImg, outputImg, alpha, MARK_RIGHT_BOTTOM);
	}

	/**
	 * Mark watermark image, default transparency
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param position -- Watermark position
	 * @return
	 */
	public static boolean markImage(File srcImg, File markImg, File outputImg, byte position) {
		return markImage(srcImg, markImg, outputImg, DEFAULT_TRANS, position);
	}

	/**
	 * Mark watermark image, default transparency and place watermark at right bottom
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @return true if success or fail if failed
	 */
	public static boolean markImage(File srcImg, File markImg, File outputImg) {
		return markImage(srcImg, markImg, outputImg, DEFAULT_TRANS, MARK_RIGHT_BOTTOM);
	}

	/**
	 * Mark watermark image, place watermark at random position
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @param alpha -- alpha composite 0 - 1,　0 Full transparency, 1 Opaque
	 * @return true if success or fail if failed
	 */
	public static boolean markImageRandomPos(File srcImg, File markImg, File outputImg, float alpha) {
		byte[] a = {MARK_LEFT_TOP,
					MARK_RIGHT_TOP,
					MARK_LEFT_TOP,
					MARK_LEFT_BOTTOM,
					MARK_RIGHT_BOTTOM,
					MARK_RIGHT_BOTTOM,
					MARK_CENTER };

		return markImage(srcImg, markImg, outputImg, alpha, a[new Random().nextInt(a.length)]);
	}

	/**
	 * Semi-transparent, random location imprint. With a slightly higher risk of
	 * lower right corner of the upper left corner, the central minimum risk.<br>
	 * default transparency
	 * @param srcImg -- source image
	 * @param markImg -- watermark logo image
	 * @param outputImg -- output image
	 * @return true if success or fail if failed
	 */
	public static boolean markImageRandomPos(File srcImg, File markImg, File outputImg) {
		return markImageRandomPos(srcImg, markImg, outputImg, DEFAULT_TRANS);
	}

	public final static boolean markWholeImage(File srcImg, File markImg, File outputImg) {
		return markWholeImage(srcImg, markImg, outputImg, DEFAULT_TRANS);
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
			log("closeOutputStream, Exception: ", e);
		}
		out = null;
	}
}
