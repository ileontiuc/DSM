package src;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileChooserMenu extends JPanel implements ActionListener {
	static private final String newline = "\n";
	private JButton openButton, OKButton,pathButton;
	private JRadioButton expandButton;
	private JTextArea log;
	private JFileChooser filec, folderc;
	private static JFrame frame;
	
	private static String fileName,path; 
	private static boolean expandAllFlag = false;



	public FileChooserMenu() {
		super(new BorderLayout());
		
		//Create the log first, because the action listeners
		//need to refer to it.
		log = new JTextArea(10,40);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
		
		//Create a file chooser
		filec = new JFileChooser();
		filec.setFileSelectionMode(JFileChooser.FILES_ONLY);
		folderc = new JFileChooser();
		folderc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		openButton = new JButton("Input Data File");
		openButton.addActionListener(this);
		
		pathButton = new JButton("Reports Folder");
		pathButton.addActionListener(this);
		
		expandButton = new JRadioButton("Expand all");
		expandButton.addActionListener(this);
		
		
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.add(openButton);
		buttonPanel.add(pathButton);
		buttonPanel.add(expandButton);
		
		OKButton = new JButton("Generate DSM");
		OKButton.addActionListener(this);
		
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		add(OKButton,BorderLayout.PAGE_END );
	}

	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = filec.showOpenDialog(FileChooserMenu.this);
		
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = filec.getSelectedFile();
				fileName = file.getAbsolutePath();
				fileName = fileName.replace(File.separator,"/" );
				log.append("Selected as input " + file.getAbsolutePath() + newline);
				//file.getName() + "." + newline);
			} else {
				log.append("Select file command cancelled by user." + newline);
			}
				log.setCaretPosition(log.getDocument().getLength());
			
			//Handle path button action.
		} else if (e.getSource() == pathButton) {
			
			int returnVal = folderc.showOpenDialog(FileChooserMenu.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				//File file = folderc.getCurrentDirectory();
				path = folderc.getSelectedFile().getAbsolutePath();
				path = path.replace(File.separator,"/" );
				log.append("Selected as input folder " + path + newline);
				//file.getName() + "." + newline);
			} else {
				log.append("Select path command cancelled by user." + newline);
			}
				log.setCaretPosition(log.getDocument().getLength());
			
			
		}  else if (e.getSource() == expandButton) {
			//radio button
			expandAllFlag = expandButton.isSelected();
			if(expandAllFlag){
				log.append("Selected expand all from beginning option" + newline);
			}else{
				log.append("Deselected expand option" + newline);
			}
			
		} else if (e.getSource() == OKButton) {
			if(fileName==null){
				log.append("No input file selected" + newline);
			}else if(path == null){
				log.append("No folder path selected" + newline);
			}else{
				String problemFilesPath, skipFilesPath;
				problemFilesPath = path+"/problemFiles";
				skipFilesPath = path+"/SkipEntities";
				
				//System.out.println("apelez dsm cu "+fileName);
				//System.out.println(problemFilesPath);
				//System.out.println(skipFilesPath);
				
				new File(problemFilesPath).mkdirs();
				new File(skipFilesPath).mkdirs();
				SplitPaneDSM.generateDSM(fileName, problemFilesPath,skipFilesPath, expandAllFlag);
				frame.dispose();
			}
			
		}
	}

	
	static void createAndShowGUI() {
		//Create and set up the window.
		
		if(frame == null){
			frame = new JFrame("FileChooserDemo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Add content to the window.
			frame.add(new FileChooserMenu());
			
			//Display the window.
			frame.pack();
			frame.setVisible(true);
		}
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			UIManager.put("swing.boldMetal", Boolean.FALSE); 
			createAndShowGUI();
		}
		});
	}
}