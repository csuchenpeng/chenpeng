/**
 *
 */
package weibo4j.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * version:1
 * @author Nalic
 */
//import java.util.Iterator;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import javax.imageio.stream.MemoryCacheImageInputStream;
//
//import com.sun.imageio.plugins.bmp.BMPImageReader;
//import com.sun.imageio.plugins.gif.GIFImageReader;
//import com.sun.imageio.plugins.jpeg.JPEGImageReader;
//import com.sun.imageio.plugins.png.PNGImageReader;

/**
 * 临时存储上传图片的内容，格式，文件信息等
 * 
 * @author zhulei
 * 
 */
public class ImageItem {
	private byte[] content;
	private String name;
	private String contentType;
	
	public ImageItem(String name,byte[] content) throws Exception{
		String imgtype=getContentType(content);
		
	    if(imgtype!=null&&(imgtype.equalsIgnoreCase("image/gif")||imgtype.equalsIgnoreCase("image/png")
	            ||imgtype.equalsIgnoreCase("image/jpeg"))){
	    	this.content=content;
	    	this.name=name;
	    	this.content=content;
	    }else{
	    	throw new IllegalStateException(
            "Unsupported image type, Only Suport JPG ,GIF,PNG!");
	    }
	}
	
	public byte[] getContent() {
		return content;
	}
	public String getName() {
		return name;
	}
	public String getContentType() {
		return contentType;
	}
	/**
	 * version:1
	 * @author Nalic
	 */
	public String getContentType(byte[] mapObj) throws IOException {
		String type = "";
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		try {
			bais = new ByteArrayInputStream(mapObj);
			byte[] data = new byte[128];
			baos = new ByteArrayOutputStream();
			while (bais.read(data) != -1) {
				baos.write(data);
			}
			byte[] buffer = baos.toByteArray();

			StringBuffer sb = new StringBuffer();
			int tmp = 0;
			for (int i = 0; i < 16; i++) {
				tmp = buffer[i];
				if (tmp >= 32 && tmp <= 127) {
					sb.append((char) tmp);
				}
			}
			String head = sb.toString();
			//System.out.println(head);
			if (head.toUpperCase().startsWith("GIF")) {
				type = "image/gif";
			} else if (head.toUpperCase().startsWith("JFIF")) {
				type = "image/jpeg";
			} else if (head.toUpperCase().startsWith("PNG")) {
				type = "image/png";
			} else if (head.toUpperCase().startsWith("BM")) {
				type = "application/x-bmp";
			}
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException ioe) {

				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ioe) {

				}
			}
		}
		return type;
	}
	/**
	 * version:1
	 * @author Nalic
	 */
//	public static String getContentType(byte[] mapObj) throws IOException {
//
//		String type = "";
//		ByteArrayInputStream bais = null;
//		MemoryCacheImageInputStream mcis = null;
//		try {
//			bais = new ByteArrayInputStream(mapObj);
//			mcis = new MemoryCacheImageInputStream(bais);
//			Iterator itr = ImageIO.getImageReaders(mcis);
//			while (itr.hasNext()) {
//				ImageReader reader = (ImageReader) itr.next();
//				if (reader instanceof GIFImageReader) {
//					type = "image/gif";
//				} else if (reader instanceof JPEGImageReader) {
//					type = "image/jpeg";
//				} else if (reader instanceof PNGImageReader) {
//					type = "image/png";
//				} else if (reader instanceof BMPImageReader) {
//					type = "application/x-bmp";
//				}
//			}
//		} finally {
//			if (bais != null) {
//				try {
//					bais.close();
//				} catch (IOException ioe) {
//
//				}
//			}
//			if (mcis != null) {
//				try {
//					mcis.close();
//				} catch (IOException ioe) {
//
//				}
//			}
//		}
//		return type;
//	}
}
