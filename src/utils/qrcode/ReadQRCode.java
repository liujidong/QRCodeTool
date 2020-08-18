package utils.qrcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

public class ReadQRCode {

	public static void main(String[] args) throws Exception{
		 File file = new File("baidu2_QRCode.png");
		 
		 BufferedImage bufferedImage = ImageIO.read(file);
		 String charset = "gb2312";
		 System.out.println(decodeQR(bufferedImage, charset));

	}
	public static String decodeQR(Image image0,String charset){
		 String result;
		try {
			QRCodeDecoder codeDecoder = new QRCodeDecoder();
			 BufferedImage bufferedImage = (BufferedImage)image0;
			 byte[] bytes = codeDecoder.decode(new MYQRCodeImage(bufferedImage));
			 result = charset==null?new String(bytes):new String(bytes,charset);
		} catch (DecodingFailedException | UnsupportedEncodingException e) {
			result = "解析二维码图片出错！";
			e.printStackTrace();
		}
		return result;
	}
	public static String decodeQR(Image image0){
		return decodeQR(image0, null);
	}
}
