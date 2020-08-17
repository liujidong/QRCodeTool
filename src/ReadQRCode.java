import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ReadQRCode {

	public static void main(String[] args) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			
			File file = new File("D:/code/img.png");
			
			BufferedImage image = ImageIO.read(file);
			
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			HashMap hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			Result result = formatReader.decode(binaryBitmap,hints);
			System.out.println("解析结果："+result.toString());
			System.out.println("二维码格式类型："+result.getBarcodeFormat());
			System.out.println("二维码文本内容："+result.getText());
		} catch (NotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	public static String decodeQR(Image image0){
    	String resultTxt = "";
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			
			BufferedImage image = (BufferedImage)image0;
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			HashMap hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			Result result = formatReader.decode(binaryBitmap,hints);
			resultTxt = result.getText();
		} catch (NotFoundException e) {
			resultTxt = "解析二维码图片出错！";
			e.printStackTrace();
		}
		return resultTxt;
	}
}
