
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import utils.FileUtils;

public class CreateQRCode {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			System.out.println("Use:CreateQRCode filePath/content");
			System.exit(0);
		}
		String content = getContent(args[0]);
		System.out.println(content);
		toQRCode(content, args[0]);

	}
	private static String getContent(String arg0) {
		String content = "";
		File f = new File(arg0);
		if(f.exists()) {
			long size = f.length() / 8;
			if (size > 2356 * 10) {// >23.56KB
				System.out.println("the file size is " + size / 1024 + "KB which biger than 23.56KB");
				System.exit(0);
			}
			int n = (int) Math.ceil(size / 2356.0);
			System.out.println("pages:" + n);
	
			if (f.getName().endsWith(".java") || f.getName().endsWith(".txt") || f.getName().endsWith(".properties")) {
				//content = FileUtils.readFileToString(f);
				content = FileUtils.readTxtFile(f);
			}
		}else {
			content = arg0;
		}	
		return content;
	}
	public static String toQRCode(String content,String fileName) throws Exception{
		if(fileName == null || fileName.length()==0) {
			fileName = "tmp";
		}
		if (null != content && content.length() > 0) {
			int wh = 800;
			String format = "png";
			HashMap hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			hints.put(EncodeHintType.MARGIN, 2);

			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, wh, wh, hints);
			String filePath = fileName + "." + format;
			Path path = new File(filePath).toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);

			System.out.println("OK");
			return filePath;
		}
		return null;
	}

}