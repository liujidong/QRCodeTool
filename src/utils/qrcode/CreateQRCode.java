package utils.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class CreateQRCode {

	public static void main(String[] args) throws Exception{
		String qrData = "www.baidu.com";
		byte[] d =qrData.getBytes("gb2312");
		String fileName = "baidu2_QRCode.png";
		toQRCode(d, fileName);

	}
	public static String toQRCode(byte[] bytes,String fileName) throws Exception{
		if(bytes==null) {return null;}
		if(fileName == null || fileName.length()==0) {
			fileName = "tmp";
		}			
		String format = "png";
		String filePath = fileName;
		if(!fileName.endsWith(format)) {
			filePath += "." + format;
		}
		Qrcode x = new Qrcode();
		x.setQrcodeErrorCorrect('M');//纠错等级
		x.setQrcodeEncodeMode('B');//N 数字   A a-z  B代表其他内容
		/*
		* 版本   1-40
		* 从21x21（版本1），到177x177（版本40），每一版本符号比前一版本每边增加4个模块。
		* */
		int version = 7;
		x.setQrcodeVersion(version);
		//画的长度根据版本的不同，大小不同， 下面的长度计算公式固定
		int width = 67+12*(version-1);
		int height = 67+12*(version-1);
		//BufferedImage.TYPE_INT_RGB    指定图片的RGB 值为int型 的 8位
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		/*
		* Graphics	绘图
		* 
		* */
		Graphics2D gs = bufferedImage.createGraphics();
		gs.setBackground(Color.WHITE);
		gs.setColor(Color.BLACK);
		gs.clearRect(0, 0, width, height);
		int pixff = 2;//偏移量

		if (bytes.length>0 && bytes.length <120){
		    boolean[][] s = x.calQrcode(bytes);
		    for (int i=0;i<s.length;i++){
				for (int j=0;j<s.length;j++){
				    if (s[j][i]) {
				    	gs.fillRect(j*3+pixff,i*3+pixff,3,3);
				    }
				}
		    }
		}
		gs.dispose();
		bufferedImage.flush();
		try {
			ImageIO.write(bufferedImage, format, new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
}
