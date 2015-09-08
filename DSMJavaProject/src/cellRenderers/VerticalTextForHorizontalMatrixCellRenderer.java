package cellRenderers;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import spaningFeature.VerticalLabelUI;
import src.SplitPaneDSM;
import tableModels.VerticalMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;
import compiledinfo.EntityCatalog;


public class VerticalTextForHorizontalMatrixCellRenderer extends DefaultTableCellRenderer {

	private volatile static int selectedCellCol=-1;
	
	
	
    public VerticalTextForHorizontalMatrixCellRenderer() {
         setUI(new VerticalLabelUI(false));  //false de jos in sus; true invers :P
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         Component def = table.getDefaultRenderer(String.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         c.setBackground(def.getBackground());
         
        
       
         	int ColCounter = VerticalMatrix.getColCounter();
			int cellDepth = EntityVerticalMatrix.getValueAt(column, ColCounter-1).getTreeDepth();
			
			
			if(selectedCellCol == column){
				//this entity was somehow cliked and needs to be highlighted
				c.setBackground(Colors.clickedColor);
				return c;
			}else{
				c.setBackground(Colors.getColor4Depth(cellDepth));
				return c;
			}
			
         
    }

	public static void setSelectedCellCol(int value) {
		 selectedCellCol = value;
		 SplitPaneDSM.refreshFrame();
		 
	}

	public static void resetSelectedCellCol() {
		selectedCellCol = -1;
	}

}