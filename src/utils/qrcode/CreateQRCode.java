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
		byte[] d =qrData.getBytes("gb2312");
		if (d.length>0 && d.length <120){
		    boolean[][] s = x.calQrcode(d);
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
			ImageIO.write(bufferedImage, "png", new File("D:/baidu2_QRCode.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
