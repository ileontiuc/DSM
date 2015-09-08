package src;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import CodedMatrixInfo.EntityVerticalMatrix;
import spaningFeature.AttributiveCellTableModel;
import tableModels.MainMatrix;
import tableModels.VerticalMatrix;
import compiledinfo.InputData;


public class HorizontalMouseAdapter implements MouseListener {
    	
		
	
 	   public void mouseClicked(MouseEvent e) {
 		   
 		      if (e.getClickCount() == 2) {
 		         JTable target = (JTable)e.getSource();
 		         int rowCliked = target.getSelectedRow();
 		         int columnCliked = target.getSelectedColumn();
 		        // System.out.println("cliked on cell: "+ rowCliked+ "  "+columnCliked+"    with total columns of: "+target.getColumnCount());
 		         
 		         int ColCounter = VerticalMatrix.getColCounter(); 
 		         ClickAction.actionOnClick(columnCliked, ColCounter-1);// echivalent cu click pe tabelul vertical 
 		      }
 		   }
 		 

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {	}
		
		public void mouseOver(MouseEvent e){}

		
}
