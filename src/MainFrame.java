import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class MainFrame extends JFrame {
	private JTextArea text;
	private ZPanel zPanel; 
	private JTabbedPane tabs;
	private JCheckBox isLogo;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MainFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 500, 450, 500);
        //setLocationRelativeTo(null);  
        setTitle("二维码工具");
        
        //-------------tab页------------------
        tabs = new JTabbedPane();
        tabs.addTab("文本", initPane1());
        tabs.addTab("二维码", initPane2());
        setContentPane(tabs);
	}
	protected JPanel initPane1() {
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBounds(0, 0, this.getWidth()-10, this.getHeight()-20);
        panel.setLayout(new BorderLayout(10,5));
        JCheckBox isTrim = new JCheckBox("是否去掉空白");
        JButton genBtn = new JButton("生成二维码");
        JPanel panelButs = new JPanel();
        panelButs.add(isTrim);panelButs.add(genBtn);
        panel.add(panelButs,BorderLayout.NORTH);
        text = new JTextArea();
        //panel.add(new JScrollPane(text));
        text.setBounds(0, 0, this.getWidth(), this.getHeight()-30);
        //text.setLineWrap(false);
        JScrollPane scrollText = new JScrollPane(text); 
        scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);         
        panel.add(scrollText,BorderLayout.CENTER);
        //text.addTextListener
        genBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(text.getText().length()>0) {
					try {
						String imgPath = CreateQRCode.toQRCode(text.getText(), null);
						zPanel.setImagePath(imgPath); 
						tabs.setSelectedIndex(1);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "二维码生成失败！", "标题",JOptionPane.ERROR_MESSAGE);  
						e1.printStackTrace();
					}
				}
			}
		});
        isTrim.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				 JCheckBox checkBox = (JCheckBox) e.getSource();
				 if(checkBox.isSelected()) {
					 text.setText(trimByLine(text.getText())); 
				 }
			}
		});
        return panel;
	}
	private String trimByLine(String value) {
		if(value != null && value.length()>0) {
			String[] lines = value.split("\n");
			for (int i = 0; i < lines.length; i++) {
				lines[i]=lines[i].trim();
			}
			return String.join("\n", lines);
		}
		return "";
	}
	protected JPanel initPane2() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBounds(0, 0, this.getWidth()-10, this.getHeight()-20);
        panel.setLayout(new BorderLayout(10,5)); //默认为0，0；水平间距10，垂直间距5
        isLogo = new JCheckBox("中间是否有LOGO");
        JButton browseButton = new JButton("浏览");
        JButton clipboardButton = new JButton("来自剪切板");
        JButton saveBtn = new JButton("保存二维码");
        JPanel panelButs = new JPanel();
        panelButs.add(isLogo);
        panelButs.add(browseButton);
        panelButs.add(clipboardButton);
        panelButs.add(saveBtn);
        panel.add(panelButs, BorderLayout.NORTH);
        zPanel = new ZPanel(); 
        panel.add(zPanel,BorderLayout.CENTER);
        
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                do_browseButton_actionPerformed(arg0);
            }
        });
        clipboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	try {
					zPanel.setImage(getImageFromClipboard());
					//zPanel.repaint();
					decodeShow(zPanel.getImage());
				} catch (Exception e) {
					System.out.println("加载剪切板图像出错：");
					e.printStackTrace();
				}
            }
        });  
        saveBtn.addActionListener(e -> zPanel.saveToFile());
        return panel;
	}
    // 浏览按钮的单击处理事件
    protected void do_browseButton_actionPerformed(ActionEvent arg0) {
        java.awt.FileDialog fd = new FileDialog(this);
        fd.setVisible(true);
        //fileTextField2.setText(fd.getDirectory() + fd.getFile());   
        zPanel.setImagePath(fd.getDirectory() + fd.getFile());  
        //zPanel.setPreferredSize(new Dimension(zPanel.getImgWidth(), zPanel.getImgHeight()));  
  
//        JScrollPane imgSp = new JScrollPane();  
//        imgSp.setViewportView(zPanel);  
//        imgSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
//        imgSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //zPanel.repaint();
        decodeShow(zPanel.getImage());
    } 
    public static Image getImageFromClipboard() throws Exception { 
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard(); 
        Transferable cc = sysc.getContents(null); 
        if (cc == null) 
            return null; 
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) 
            return (Image) cc.getTransferData(DataFlavor.imageFlavor); 
        return null; 
    }
    public void decodeShow(Image image){
    	String resultTxt = "";
		try {
			LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage)image);  
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  

			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
			//优化扫描精度 （增加解析成功率）
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			if(isLogo.isSelected()){
			//复杂模式，开启PURE_BARCODE模式,带图片LOGO的解码方案,否则会出现NotFoundException
			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			}

	    	Result result = new MultiFormatReader().decode(bitmap, hints);
			resultTxt = result.getText();
		} catch (NotFoundException e) {
			resultTxt = "解析二维码图片出错！";
			e.printStackTrace();
		}  
        //return result.getText();  
        text.setText(resultTxt);
        //text.getParent().getParent().setsel
        tabs.setSelectedIndex(0);
    }
    
}
