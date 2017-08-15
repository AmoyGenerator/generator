package app.other.zip;



/**
 * 类别说明:
 */
import java.io.*;
import org.apache.tools.zip.*;
import java.util.Enumeration;

/**
 * 功能:zip压缩、解压(支持中文文件名) 程序使用Ant 1.7.1 中的ant.jar
 * 
 * 仅供编程学习参考.
 * 
 * @author abc
 * @date 2008-8-3
 * @Usage: 压缩:java AntZip -zip "directoryName" 解压:java AntZip -unzip
 *         "fileName.zip"
 */

public class AntZip {
	private ZipFile zipFile;

	private ZipOutputStream zipOut; // 压缩Zip

	private ZipEntry zipEntry;

	private static int bufSize; // size of bytes

	private byte[] buf;

	private int readedBytes;

	private String root;

	public AntZip() {
		this(512);
	}

	public AntZip(int bufSize) {
		this.bufSize = bufSize;
		this.buf = new byte[this.bufSize];
	}

	// 压缩文件夹内的文件
	public void doZip(String zipDirectory, String dir) {// zipDirectoryPath:需要压缩的文件夹名
		File file;
		File zipDir;

		zipDir = new File(zipDirectory);
		String zipFileName = dir + zipDir.getName() + ".zip";// 压缩后生成的zip文件名
		root = zipDir.getName();
		try {
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFileName)));
			handleDir(zipDir, this.zipOut);

			this.zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 由doZip调用,递归完成目录文件读取
	private void handleDir(File dir, ZipOutputStream zipOut) throws IOException {
		FileInputStream fileIn;
		File[] files;

		files = dir.listFiles();

		if (files.length == 0) {// 如果目录为空,则单独创建之.
			// ZipEntry的isDirectory()方法中,目录以"/"结尾
			System.out.println(dir.toString());
			zipOut.putNextEntry(new ZipEntry(dir.getName() + "/"));
			zipOut.closeEntry();
		} else {// 如果目录不为空,则分别处理目录和文件.
			for (File fileName : files) {
				// System.out.println(fileName);

				if (fileName.isDirectory()) {
					handleDir(fileName, zipOut);
				} else {
					fileIn = new FileInputStream(fileName);
					String realPath = fileName.getAbsolutePath();

					zipOut.putNextEntry(new ZipEntry(realPath
							.substring(realPath.indexOf(root))));
					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
						zipOut.write(this.buf, 0, this.readedBytes);
					}

					this.zipOut.closeEntry();
				}
			}
		}
	}

	// 解压指定zip文件
	public void unZip(String unZipfileName, String dir) {// unZipfileName需要解压的zip文件名
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;

		try {
			this.zipFile = new ZipFile(unZipfileName);

			for (Enumeration entries = this.zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				file = new File(dir + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (parent != null && !parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(file);
					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, this.readedBytes);
					}
					fileOut.close();

					inputStream.close();
				}
			}
			this.zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 设置缓冲区大小
	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}

	// 测试AntZip类
	public static void main(String[] args) throws Exception {

		AntZip zip = new AntZip();
		zip.setBufSize(1024);
//		zip.doZip("E:\\struts2学习资料\\testZIP\\是就",
//				"E:\\struts2学习资料\\testZIP\\");
		 zip.unZip("D:\\eeeeeeee\\apache-tomcat-5.5.26.zip","D:\\eeeeeeee\\");
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

}