package src;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;


public class VerticalMouseAdapter implements MouseListener {
    	
	
	
 	   public void mouseClicked(MouseEvent e) {
 		   
 		      if (e.getClickCount() == 2) {
 		         JTable target = (JTable)e.getSource();
 		         int rowCliked = target.getSelectedRow();
 		         int columnCliked = target.getSelectedColumn();
 		        
 		         //System.out.println("cliked on cell: "+ rowCliked+ "  "+columnCliked+"    with total columns of: "+target.getColumnCount());
 		         
 		         ClickAction.actionOnClick(rowCliked, columnCliked);
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
