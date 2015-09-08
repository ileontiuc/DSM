package cellRenderers;


import java.awt.Component;

import javax.swing.JTable;
import javax.swing.plaf.metal.MetalLabelUI;
import javax.swing.table.DefaultTableCellRenderer;

import spaningFeature.VerticalLabelUI;
import src.SplitPaneDSM;
import tableModels.VerticalMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;
import compiledinfo.EntityCatalog;


public class VerticalColorCellRenderer extends DefaultTableCellRenderer {

	private volatile static int selectedCellRow=-1;
	
	
		public VerticalColorCellRenderer(){
		}
		
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			 Component def = table.getDefaultRenderer(String.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	         Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	         c.setBackground(def.getBackground());
	         
	         //click pe celula pe vertical highlight  <=> click pe diagonala principala
	         
	         //System.out.println(VerticalMatrix.getColCounter()    + "     "+column+"   4value: "+value);
	         if(hasFocus ){
	        	 VerticalTextForHorizontalMatrixCellRenderer.setSelectedCellCol(row);
				 VerticalColorCellRenderer.setSelectedCellCol(row);
				 ColorDSMRenderer.simulateClickOn(row,row);
	         }
	         
	         boolean alreadySet=false;
	         //daca celula se expand i jos atunci inseamna ca am parinte cu copii expandati si trebuie scris pe verticala
	         if(row+1 < VerticalMatrix.getRowCounter()){
	        	 if(EntityVerticalMatrix.getValueAt(row, column) == EntityVerticalMatrix.getValueAt(row+1, column)){
	        		 setUI(new VerticalLabelUI(false));
	        		 alreadySet=true;
	        	 }
	         }else{
	        	 if(row-1>=0){
	        		 if(EntityVerticalMatrix.getValueAt(row, column) == EntityVerticalMatrix.getValueAt(row-1, column)){
		        		 setUI(new VerticalLabelUI(false));
		        		 alreadySet=true;
		        	 }
	        	 }
	         }
	        	 
	         if(!alreadySet){
	        	 setUI(new MetalLabelUI());	 
	         }
	         
	         
			//get maxDepth
			int maxTreeDepth = EntityCatalog.getMaxTreeDepth();
			int cellDepth = EntityVerticalMatrix.getValueAt(row, column).getTreeDepth();
			
			if(selectedCellRow == row && VerticalMatrix.isAPrintedEntity(row, column)){
				//this entity was somehow cliked and needs to be highlighted
				c.setBackground(Colors.clickedColor);
			}else{
				c.setBackground(Colors.getColor4Depth(cellDepth));
			}
			
	            return c;
		}


		public static void resetSelectedCellCol() {
			selectedCellRow = -1;
			
		}


		public static void setSelectedCellCol(int row) {
			 selectedCellRow = row;
			 SplitPaneDSM.refreshFrame();
			
		}
}