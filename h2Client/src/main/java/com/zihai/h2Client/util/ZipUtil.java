package com.zihai.h2Client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

	/** 缓冲器大小 */
	private static final int BUFFER = 512;
	private static ZipInputStream zipInputStream;

	/**
	 * 取的给定源目录下的所有文件及空的子目录 递归实现
	 *
	 * @param srcFile
	 *
	 * @return
	 */
	private static List<File> getAllFiles(File srcFile) {
		List<File> fileList = new ArrayList<File>();
		File[] tmp = srcFile.listFiles();

		for (int i = 0; i < tmp.length; i++) {

			if (tmp[i].isFile()) {
				fileList.add(tmp[i]);
			}

			if (tmp[i].isDirectory()) {
				if (tmp[i].listFiles().length != 0) {// 若不是空目录，则递归添加其下的目录和文件
					fileList.addAll(getAllFiles(tmp[i]));
				} else {// 若是空目录，则添加这个目录到fileList
					fileList.add(tmp[i]);
				}
			}
		} // end for
		return fileList;
	}

	/**
	 * 取相对路径 依据文件名和压缩源路径得到文件在压缩源路径下的相对路径
	 *
	 * @param dirPath
	 *            压缩源路径
	 * @param file
	 *
	 * @return 相对路径
	 */
	private static String getRelativePath(String dirPath, File file) {
		File dir = new File(dirPath);
		String relativePath = file.getName();

		while (true) {
			file = file.getParentFile();

			if (file == null) {
				break;
			}

			if (file.equals(dir)) {
				break;
			} else {
				relativePath = file.getName() + "/" + relativePath;
			}
		} // end while

		return relativePath;
	}

	/**
	 * 创建文件 根据压缩包内文件名和解压缩目的路径，创建解压缩目标文件， 生成中间目录
	 * 
	 * @param dstPath
	 *            解压缩目的路径
	 * @param fileName
	 *            压缩包内文件名
	 *
	 * @return 解压缩目标文件
	 *
	 * @throws IOException
	 */
	private static File createFile(String dstPath, String fileName) throws IOException {
		String[] dirs = fileName.split("/");// 将文件名的各级目录分解
		File file = new File(dstPath);

		if (dirs.length > 1) {// 文件有上级目录
			for (int i = 0; i < dirs.length - 1; i++) {
				file = new File(file, dirs[i]);// 依次创建文件对象知道文件的上一级目录
			}

			if (!file.exists()) {
				file.mkdirs();// 文件对应目录若不存在，则创建
			}

			file = new File(file, dirs[dirs.length - 1]);// 创建文件

			return file;
		} else {
			if (!file.exists()) {
				file.mkdirs();// 若目标路径的目录不存在，则创建
			}

			file = new File(file, dirs[0]);// 创建文件

			return file;
		}
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param unZipPath
	 *            解压路径，比如C:\\home\\myblog\\project\\
	 * @param fileName
	 *            解压后的文件名，一般命名为项目名，强制要求用户输入，并且保证不为空，
	 *            fileName的上层目录为一个随机生成的32位UUID，以保证项目名重复的依然可以保存到服务器
	 * @param multipartFile
	 *            上传压缩文件
	 *
	 * @return FileHandleResponse 表示上传结果实体对象
	 */
	// @SuppressWarnings("rawtypes")
	public static void unZipFiles(String unZipPath, String fileName, MultipartFile multipartFile,
			String charset) throws IOException {
		String unZipRealPath = unZipPath + UUID.randomUUID().toString().replaceAll("-", "") + "/" + fileName + "/";

		// 如果保存解压缩文件的目录不存在，则进行创建，并且解压缩后的文件总是放在以fileName命名的文件夹下
		File unZipFile = new File(unZipRealPath);
		if (!unZipFile.exists()) {
			unZipFile.mkdirs();
		}
		// String charset = FileUtil.getCharset(unZipRealPath);
		// ZipInputStream用来读取压缩文件的输入流
		String mulFileName = multipartFile.getOriginalFilename().toString();
		switch (charset) {
		case "GBK":
			zipInputStream = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("GBK"));
			break;
		case "UTF-8":
			zipInputStream = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("UTF-8"));
			break;
		}
		// 压缩文档中每一个项为一个zipEntry对象，可以通过getNextEntry方法获得，zipEntry可以是文件，也可以是路径，比如abc/test/路径下
		ZipEntry zipEntry;
		try {
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				String zipEntryName = zipEntry.getName();
				// 将目录中的1个或者多个\置换为/，因为在windows目录下，以\或者\\为文件目录分隔符，linux却是/
				String outPath = (unZipRealPath + zipEntryName).replaceAll("\\+", "/");
				// 判断所要添加的文件所在路径或者
				// 所要添加的路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是,在上面三行已经创建,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				OutputStream outputStream = new FileOutputStream(outPath);
				byte[] bytes = new byte[4096];
				// byte[] bytes = new byte[file.];

				int len;
				// 当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
				while ((len = zipInputStream.read(bytes)) > 0) {
					outputStream.write(bytes, 0, len);
				}
				outputStream.close();
				// 必须调用closeEntry()方法来读入下一项
				zipInputStream.closeEntry();
			}
			zipInputStream.close();
			logger.info("******************解压完毕********************");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static String GBKToUTF8(String str) {
		String res = null;
		try {
			res = new String(str.getBytes("GBK"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public static void main(String[] args) {
		String s = "鎵归噺娉ㄥ唽妯℃澘 -鍛樺伐-1.xls";
		// String s = "批量注册模板 -员工-1.xls";
		String l2 = null;
		try {
			l2 = new String(s.getBytes("GBK"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String l = null;
		// try {
		// l = new String( s.getBytes("UTF-8") , "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(l);
		//
		// String b = "鍛樺伐";
		// String d = "员工-1-公司";
		// String l1 = null;
		// try {
		// l1 = new String( d.getBytes("UTF-8") , "GBK");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(l1);
		//
		// String l2 = null;
		// try {
		// l2 = new String( l1.getBytes("GBK") , "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(l2);

		// boolean flag = StringUtils.isMessyCode(String.valueOf(s));
		// System.out.println(flag);
		// String s1 = "好好的啊-1.xls";
		//
		//
		//
		// boolean sl= Charset.forName("GBK").newEncoder().canEncode(s);
		// System.out.println(sl);
		// boolean sl1= Charset.forName("GBK").newEncoder().canEncode(s1);
		// System.out.println(sl1);
	}

	// public static void main(String[] args) {
	// 员工批量注册模版.zip
	// }
	/**
	 * 解压缩方法
	 *
	 *
	 * @param zipFileName
	 *            压缩文件名
	 * @param dstPath
	 *            解压目标路径
	 *
	 * @return
	 */
	public static boolean unzip(String zipFileName, String dstPath) {
		logger.info("zip uncompressing...");

		try {
			zipInputStream = new ZipInputStream(new FileInputStream(zipFileName));
			ZipEntry zipEntry = null;
			byte[] buffer = new byte[BUFFER];// 缓冲器
			int readLength = 0;// 每次读出来的长度

			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {// 若是zip条目目录，则需创建这个目录
					File dir = new File(dstPath + "/" + zipEntry.getName());
					if (!dir.exists()) {
						dir.mkdirs();
						continue;// 跳出
					}
				}

				File file = createFile(dstPath, zipEntry.getName());// 若是文件，则需创建该文件

				OutputStream outputStream = new FileOutputStream(file);

				while ((readLength = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
					outputStream.write(buffer, 0, readLength);
				}

				outputStream.close();
				logger.error("file uncompressed: " + file.getCanonicalPath());
			} // end while
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			logger.error("unzip fail!");

			return false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			logger.error("unzip fail!");

			return false;
		}

		logger.info("unzip success!");

		return true;
	}

	/**
	 * 压缩方法 （可以压缩空的子目录）
	 * 
	 * @param srcPath
	 *            压缩源路径
	 * @param zipFileName
	 *            目标压缩文件
	 *
	 * @return
	 */
	public static boolean zip(String srcPath, String zipFileName) {
		logger.info("zip compressing...");

		File srcFile = new File(srcPath);
		List<File> fileList = getAllFiles(srcFile);// 所有要压缩的文件
		byte[] buffer = new byte[BUFFER];// 缓冲器
		ZipEntry zipEntry = null;
		int readLength = 0;// 每次读出来的长度

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));

			for (File file : fileList) {
				if (file.isFile()) {// 若是文件，则压缩这个文件
					zipEntry = new ZipEntry(getRelativePath(srcPath, file));
					zipEntry.setSize(file.length());
					zipEntry.setTime(file.lastModified());
					zipOutputStream.putNextEntry(zipEntry);

					InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

					while ((readLength = inputStream.read(buffer, 0, BUFFER)) != -1) {
						zipOutputStream.write(buffer, 0, readLength);
					}

					inputStream.close();
				} else {// 若是目录（即空目录）则将这个目录写入zip条目
					zipEntry = new ZipEntry(getRelativePath(srcPath, file) + "/");
					zipOutputStream.putNextEntry(zipEntry);
				}

			} // end for

			zipOutputStream.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			logger.error("zip fail!");

			return false;
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			logger.error("zip fail!");

			return false;
		}

		logger.info("zip success!");

		return true;
	}

}
