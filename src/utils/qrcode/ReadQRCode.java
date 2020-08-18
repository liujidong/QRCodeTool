package utils.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;

public class ReadQRCode {

	public static void main(String[] args) throws Exception{
		 File file = new File("D:/baidu2_QRCode.png");
		 
		 BufferedImage bufferedImage = ImageIO.read(file);
		 
		 QRCodeDecoder codeDecoder = new QRCodeDecoder();
		 
		 byte[] bytes = codeDecoder.decode(new MYQRCodeImage(bufferedImage));
		 String result = new String(bytes,"gb2312");
		 System.out.println(result);
	}

}
