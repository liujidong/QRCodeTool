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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
	private JTextArea text;
	private ZPanel zPanel; 
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
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("文本", initPane1());
        tabs.addTab("二维码", initPane2());
        setContentPane(tabs);
	}
	protected JPanel initPane1() {
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBounds(0, 0, this.getWidth()-10, this.getHeight()-20);
        panel.setLayout(null);
        text = new JTextArea(10,50);
        //panel.add(new JScrollPane(text));
        text.setBounds(0, 0, this.getWidth(), this.getHeight()-30);
        panel.add(text);
        return panel;
	}
	protected JPanel initPane2() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBounds(0, 0, this.getWidth()-10, this.getHeight()-20);
        panel.setLayout(new BorderLayout(10,5)); //默认为0，0；水平间距10，垂直间距5
        JButton browseButton = new JButton("浏览");
        //panel.add(browseButton, BorderLayout.NORTH);
        JButton clipboardButton = new JButton("来自剪切板");
        //panel.add(clipboardButton, BorderLayout.NORTH); 
        JPanel panelButs = new JPanel();
        panelButs.add(browseButton);
        panelButs.add(clipboardButton);
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
					zPanel.setImagePath(getImageFromClipboard());
					zPanel.repaint();
				} catch (Exception e) {
					System.out.println("加载剪切板图像出错：");
					e.printStackTrace();
				}
            }
        });        
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
        zPanel.repaint();
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
}
