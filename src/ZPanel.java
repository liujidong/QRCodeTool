import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
  
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;  
  
public class ZPanel extends javax.swing.JPanel {  
  
    private static final long serialVersionUID = 1L;  
    private Image image;  
    private int imgWidth;  
    private int imgHeight;  
  
    public int getImgWidth() {  
        return imgWidth;  
    }  
  
    public void setImgWidth(int imgWidth) {  
        this.imgWidth = imgWidth;  
    }  
  
    public int getImgHeight() {  
        return imgHeight;  
    }  
  
    public void setImgHeight(int imgHeight) {  
        this.imgHeight = imgHeight;  
    }  
  
    public ZPanel() {  
    }  
  
    public void setImagePath(String imgPath) {  
        // 该方法不推荐使用，该方法是懒加载，图像并不加载到内存，当拿图像的宽和高时会返回-1；  
        // image = Toolkit.getDefaultToolkit().getImage(imgPath);  
        try {  
            // 该方法会将图像加载到内存，从而拿到图像的详细信息。  
            image = ImageIO.read(new FileInputStream(imgPath));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        setImgWidth(image.getWidth(this));  
        setImgHeight(image.getHeight(this));  
    }  
    public void setImage(Image imageNew) {
        this.image = imageNew;   
    	if(imageNew==null) {return;}
        setImgWidth(image.getWidth(this));  
        setImgHeight(image.getHeight(this));  
    }  
    @Override  
    public void paintComponent(Graphics g1) {  
        int x = 0;  
        int y = 0;  
        Graphics g = (Graphics) g1;  
        if (null == image) {  
            return;  
        }  
  
        g.drawImage(image, x, y, this.getWidth(), this.getHeight(),  this);  
        g = null;  
    }

	public Image getImage() {
		return image;
	}  
    protected void saveToFile() {
        try {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("png");
			// 设置文件类型
			fileChooser.setFileFilter(filter);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置保存路径
			int retval = fileChooser.showSaveDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				System.out.println("你打开的文件夹是: " + fileChooser.getSelectedFile().getPath());
			  File file = fileChooser.getSelectedFile();
			  if (file == null) {
			    return;
			  }
			  if (!file.getName().toLowerCase().endsWith(".png")) {
			    file = new File(file.getParentFile(), file.getName() + ".png");
			  }
			  System.out.println(file.getAbsolutePath());
			  ImageIO.write((BufferedImage)image,"png",file);
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }    
} 