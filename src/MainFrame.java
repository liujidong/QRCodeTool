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

import utils.zxing.CreateQRCode;
import utils.zxing.ReadQRCode;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private ZPanel zPanel; 
	private JTabbedPane tabs;
	private JCheckBox isLogo;
    private JButton saveBtn;
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
        setTitle("二维码工具2.0");
        
        //-------------tab页------------------
        tabs = new JTabbedPane();
        tabs.addTab("文本转二维码", initPane1());
        tabs.addTab("识别二维码", initPane2());
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
        panel.add(panelButs,BorderLayout.SOUTH);
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
						saveBtn.setEnabled(true);
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
        JButton browseButton = new JButton("浏览二维码图片");
        JButton clipboardButton = new JButton("来自剪切板");
        saveBtn = new JButton("保存二维码");
        saveBtn.setEnabled(false);
        JPanel panelButs = new JPanel();
        panelButs.add(isLogo);
        panelButs.add(browseButton);
        panelButs.add(clipboardButton);
        panel.add(panelButs, BorderLayout.NORTH);
        zPanel = new ZPanel(); 
        panel.add(zPanel,BorderLayout.CENTER);
        JPanel panelButsSouth = new JPanel();
        panelButsSouth.add(saveBtn);
        panel.add(panelButsSouth,BorderLayout.SOUTH);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                do_browseButton_actionPerformed(arg0);
            }
        });
        clipboardButton.addActionListener(new ActionListener() {
            @Override
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
        fd.setFilenameFilter((dir,name)->{
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg");
        });
        fd.setVisible(true);
        //fileTextField2.setText(fd.getDirectory() + fd.getFile());   
        zPanel.setImagePath(fd.getDirectory() + fd.getFile());  
        //zPanel.setPreferredSize(new Dimension(zPanel.getImgWidth(), zPanel.getImgHeight()));  
  
//        JScrollPane imgSp = new JScrollPane();  
//        imgSp.setViewportView(zPanel);  
//        imgSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
//        imgSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //zPanel.repaint();
        if(fd.getFile() != null) {
            decodeShow(zPanel.getImage());
        }
    } 
    public static Image getImageFromClipboard() throws Exception { 
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard(); 
        Transferable cc = sysc.getContents(null); 
        if (cc == null) {
            return null;
        }
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) 
        {return (Image) cc.getTransferData(DataFlavor.imageFlavor); }
        return null; 
    }

    /**
     * 识别二维码图片
     * @param image 二维码图片
     */
    public void decodeShow(Image image){
    	String resultTxt = ReadQRCode.decodeQR(image,isLogo.isSelected()); 
        //return result.getText();  
        text.setText(resultTxt);
        //text.getParent().getParent().setsel
        tabs.setSelectedIndex(0);
        zPanel.setImage(image);
        saveBtn.setEnabled(true);
    }
    
}
