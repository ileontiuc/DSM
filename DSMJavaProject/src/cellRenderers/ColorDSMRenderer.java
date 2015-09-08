package cellRenderers;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import algorithms.DependencyCycle;
import algorithms.InstabilityFactor;
import src.FeatureMenu;
import tableModels.HorizontalMatrix;
import tableModels.VerticalMatrix;
import CodedMatrixInfo.Constants;
import CodedMatrixInfo.DepthMainMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;
import datastructure.Entitate;
import datastructure.Pachet;




public class ColorDSMRenderer extends DefaultTableCellRenderer {
	
	
		private volatile static int selectedCellRow=-1;
		private volatile static int selectedCellCol=-1;
	
		public static void resetSelectedCell(){
			 selectedCellRow=-1;
			 selectedCellCol=-1;
			 VerticalTextForHorizontalMatrixCellRenderer.resetSelectedCellCol();
			 VerticalColorCellRenderer.resetSelectedCellCol();
		}
		
	
		public ColorDSMRenderer(){}
		
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			Component c = super.getTableCellRendererComponent(
	                table, value, isSelected, hasFocus, row, column);
			
			((JComponent) c).setOpaque(true);
			((JComponent) c).setForeground(Color.BLACK); 
			
			
			int cellW = table.getColumnModel().getColumn(0).getWidth();
			int rowH = table.getRowHeight();
			if(cellW<10 || rowH<10){ 
				((JLabel) c).setText("");  //hide cell value/ suprascrie cu ""
			}
			
			int cellValue = (int) table.getValueAt(row, column);
			if(cellValue<1){ 
				((JLabel) c).setText("");  //hide cell value/ suprascrie cu ""
			}
			
			
			if(hasFocus){
				
				selectedCellRow = row;
				selectedCellCol = column;
				VerticalTextForHorizontalMatrixCellRenderer.setSelectedCellCol(column);
				VerticalColorCellRenderer.setSelectedCellCol(row);
				
				if(row==column){
					((JLabel) c).setText("");  //hide cell value/ suprascrie cu ""
				}
				
				if(colorFromInstabilityFactor(cellValue, row, column)){
					c.setBackground( Colors.instabilityFactorColor);  // liniar addLinearAlgoOn(  Colors.getClickedSelectedColor() , cellValue  )  
					//TODO liniar si la strike?
					return c;
				}
				
				//Dependency Cycle
				if(colorFromDependencyCycle(cellValue, row, column)){
					c.setBackground( Colors.cycleDependencyBackgroundColor); 
					c.getFont().deriveFont(Font.BOLD);
					//c.setForeground(Colors.cycleDependencyTextColor);
					return c;
				}
				
				
				c.setBackground(  addLinearAlgoOn(  Colors.clickedSelectedColor , cellValue  ));
				return c;
				
			}
			
			
			//no focus
			if(colorFromInstabilityFactor(cellValue, row, column)){
				c.setBackground( Colors.instabilityFactorColor);  // liniar addLinearAlgoOn(  Colors.getClickedSelectedColor() , cellValue  )  
				//TODO liniar si la strike?
				return c;
			}
			
			//Dependency Cycle
			if(colorFromDependencyCycle(cellValue, row, column)){
				c.setBackground( Colors.cycleDependencyBackgroundColor); 
				c.getFont().deriveFont(Font.BOLD);
				//c.setForeground(Colors.cycleDependencyTextColor);
				return c;
			}
			
			if(row==selectedCellRow || column==selectedCellCol){
				//sunt pe linie sau pe coloana cu celula clickuita
				if(row==column){
					((JLabel) c).setText("");  //hide cell value/ suprascrie cu ""
				}
				c.setBackground(addLinearAlgoOn(Colors.clickedColor,cellValue));
				return c;
			}
			
			
			//sunt pe diagonala principala sinu este clickuit
			if(row==column){
				c.setBackground(Color.darkGray.brighter());
				((JLabel) c).setText("");  //hide cell value/ suprascrie cu ""
				 return c;
			}
			
			// nu trebuie highlighted (nici nu e clickuit) colorez normal
			int cellDepthValue = DepthMainMatrix.getDepthFor(row, column);
			c.setBackground(addLinearAlgoOn(Colors.getColor4Depth(cellDepthValue),cellValue));
	        return c;
		
		
		}
		
		
		private boolean colorFromInstabilityFactor(int cellValue,int  row,int column) {
			//problema de la instability factor are prioritate fata de orice alta colorare
			if(cellValue >0 ){
				Entitate linie, coloana;
				linie = EntityVerticalMatrix.getValueAt(row, VerticalMatrix.getColCounter()-1);
				coloana = EntityVerticalMatrix.getValueAt(column, VerticalMatrix.getColCounter()-1);  //de pe horizontal am doar string cu numele
				return (FeatureMenu.getInstabilityFeatureFlag() && InstabilityFactor.badInstabilityFactorDependency(linie,coloana));
			}
			return false;
		}
		
		
		private boolean colorFromDependencyCycle(int cellValue, int  row,int column) {
			//problema de la instability factor are prioritate fata de orice alta colorare
			if(cellValue >0 ){
				//Entitate linie, coloana;
				//linie = EntityVerticalMatrix.getValueAt(row, VerticalMatrix.getColCounter()-1);
				//coloana = EntityVerticalMatrix.getValueAt(column, VerticalMatrix.getColCounter()-1);  //de pe horizontal am doar string cu numele
				return (FeatureMenu.getDependencyCycleFeatureFlag() && DependencyCycle.badDirectCycleDependency(row,column));
			}
			return false;
		}


		private Color addLinearAlgoOn(Color minColor, int cellValue){
			
			Color maxColor = new Color(0x000000);
			
			float p;
			if(cellValue<10){
				p=0;
			}else if(cellValue<100){
				p=(float) 0.3;
			}else{
				p=(float) 0.6;
			}
			
			float r,g,b;
			
			r = maxColor.getRed()*p+minColor.getRed()*(1-p);
			g = maxColor.getGreen()*p+minColor.getGreen()*(1-p);
			b = maxColor.getBlue()*p+minColor.getBlue()*(1-p); 
			
			Color finalColour = new Color(Math.round(r),Math.round(g),Math.round(b)); 
			
			return finalColour;
		}
		
	public static void simulateClickOn(int row, int column){
		selectedCellRow = row;
		selectedCellCol = column;
	}

}