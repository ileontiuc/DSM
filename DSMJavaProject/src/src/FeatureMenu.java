package src;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FeatureMenu extends JPanel implements ActionListener {
	
	private JRadioButton instabilityFactorButton;
	private JRadioButton cycleDependenciesButton;
	private static boolean instabilityFactorFeatureFlag = false;
	private static boolean dependencyCycleFeatureFlag = false;
	

	public FeatureMenu() {
		super(new BorderLayout());
		
		instabilityFactorButton = new JRadioButton("Instability Factor Feature");
		instabilityFactorButton.addActionListener(this);
		
		cycleDependenciesButton = new JRadioButton("Dependency Cycles Feature");
		cycleDependenciesButton.addActionListener(this);
		
		
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.add(instabilityFactorButton);
		buttonPanel.add(cycleDependenciesButton);
		
		
		add(buttonPanel, BorderLayout.CENTER  );
		//add(buttonPanel, BorderLayout.PAGE_START);
	}

	public void actionPerformed(ActionEvent e) {
		 if (e.getSource() == instabilityFactorButton) {
	 			//radio button
	 			instabilityFactorFeatureFlag = instabilityFactorButton.isSelected();
	 			SplitPaneDSM.refreshFrame();
	 		}else  if (e.getSource() == cycleDependenciesButton) {
	 			//radio button
	 			dependencyCycleFeatureFlag = cycleDependenciesButton.isSelected();
	 			SplitPaneDSM.refreshFrame();
	 		}
	}

	
	public static boolean getInstabilityFeatureFlag(){
		return instabilityFactorFeatureFlag;
	}
	
	public static boolean getDependencyCycleFeatureFlag(){
		return dependencyCycleFeatureFlag;
	}
	
}
