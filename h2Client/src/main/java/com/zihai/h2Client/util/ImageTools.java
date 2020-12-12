package com.zihai.h2Client.util;


import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTools {

	public static void amendRotatePhoto(String originpath, String savePath) throws IOException {
		// 取得图片旋转角度
		int angle = getRotateAngleForPhoto(originpath);
		BufferedImage src = ImageIO.read(new File(originpath));
		BufferedImage des = rotateImage(src, angle);
		ImageIO.write(des, "jpg", new File(savePath));
	}

	/**
	 * 读取照片旋转角度
	 * @return 角度
	 */
	public static int getRotateAngleForPhoto(String fileName) {
		File file = new File(fileName);

		int angel = 0;
		Metadata metadata;
		try {
			metadata = JpegMetadataReader.readMetadata(file);
			Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
			if (directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {
				// Exif信息中方向　　
				int orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
				// 原图片的方向信息
				if (6 == orientation) {
					// 6旋转90
					angel = 90;
				} else if (3 == orientation) {
					// 3旋转180
					angel = 180;
				} else if (8 == orientation) {
					// 8旋转90
					angel = 270;
				}
			}
		} catch (JpegProcessingException e) {
			e.printStackTrace();
		} catch (MetadataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return angel;
	}

	/**
	 * 旋转图片
	 */
	public static BufferedImage rotateImage(final BufferedImage bufferedimage,final int degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
				.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
		graphics2d.drawImage(bufferedimage, 0, 0, null);
		graphics2d.dispose();
		return img;
	}

}
