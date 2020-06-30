
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class CreateQRCode {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			System.out.println("Use:CreateQRCode filePath");
			System.exit(0);
		}
		File f = new File(args[0]);
		long size = f.length() / 8;
		if (size > 2356 * 10) {// >23.56KB
			System.out.println("the file size is " + size / 1024 + "KB which biger than 23.56KB");
			System.exit(0);
		}
		int n = (int) Math.ceil(size / 2356.0);
		System.out.println("pages:" + n);

		String content = "";
		if (f.getName().endsWith(".java") || f.getName().endsWith(".txt") || f.getName().endsWith(".properties")) {
			//content = FileUtils.readFileToString(f);
			content = readTxtFile(f);
			System.out.println(content);
		}
		toQRCode(content, args[0]);

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
    public static String readTxtFile(File file){
        try {
                String encoding="UTF-8";
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    StringBuffer buffer = new StringBuffer();
                    while((lineTxt = bufferedReader.readLine()) != null){
                        System.out.println(lineTxt);
                        buffer.append(lineTxt); // 将读到的内容添加到 buffer 中
                        buffer.append("\n"); // 添加换行符
                    }
                    read.close();
                    return buffer.toString();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }
}