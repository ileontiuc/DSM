package spaningFeature;




import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import algorithms.InstabilityFactor;
import CodedMatrixInfo.Constants;
import CodedMatrixInfo.EntityVerticalMatrix;
import cellRenderers.Colors;
import datastructure.Entitate;
import datastructure.Pachet;
import src.FeatureMenu;
import tableModels.HorizontalMatrix;
import tableModels.VerticalMatrix;

public class MultiSpanCellTable extends JTable {

	  public MultiSpanCellTable(TableModel model) {
	    super(model);
	    setUI(new MultiSpanCellTableUI());
	    getTableHeader().setReorderingAllowed(false);
	    setCellSelectionEnabled(true);
	    setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	  }
	  
	  
	  
	  //Implement table cell tool tips.           
      public String getToolTipText(MouseEvent e) {
    	  
          String tip = null;
          int mainCellValue = 0;
          java.awt.Point p = e.getPoint();
          int rowIndex = rowAtPoint(p);
          int colIndex = columnAtPoint(p);
          String rowEntityName = null;
          String colEntityName = null;
          
          Entitate linie, coloana;
		  linie = EntityVerticalMatrix.getValueAt(rowIndex, VerticalMatrix.getColCounter()-1);
		  coloana = EntityVerticalMatrix.getValueAt(colIndex, VerticalMatrix.getColCounter()-1);  //de pe horizontal am doar string cu numele
		  

          try {
        	 mainCellValue = (int) getValueAt(rowIndex, colIndex);
        	 if(mainCellValue>0){
              	rowEntityName = (String) VerticalMatrix.getMatrix().get(rowIndex, VerticalMatrix.getColCounter()-1);
              	colEntityName = HorizontalMatrix.getHorizontalMatrix().get(colIndex);
              	tip = colEntityName+" uses "+rowEntityName+" "+mainCellValue+" times";
              	
              	if(FeatureMenu.getInstabilityFeatureFlag() &&   InstabilityFactor.badInstabilityFactorDependency(linie, coloana)){
              		
              		tip= "<html>"+ tip+"<br><br>"+"Components Instability: "+"<br>";
              		
              		if(!(linie instanceof Pachet) || !(coloana instanceof Pachet)){
              			tip=tip+InstabilityFactor.getParentFor(coloana).getName()+" - "+
              					InstabilityFactor.getParentFor(linie).getName()+"<br>";
              		}
              		
              		tip = tip+ 
              			InstabilityFactor.getParentFor(coloana).getInstabilityFactor()+
              			" ~ "+InstabilityFactor.getParentFor(linie).getInstabilityFactor()
              			+"</html>";
           							//+"Components Instability: " + "<br>"+((Pachet) coloana).getInstabilityFactor().setScale(2,BigDecimal.ROUND_UP)+" ~ "+((Pachet) linie).getInstabilityFactor().setScale(2,BigDecimal.ROUND_UP) + "</html>";
           					//+=System.getProperty("line.separator")+"Instability Factor: ";
              	}
              	
              }
   			}
          catch (RuntimeException e1) {
              //catch null pointer exception if mouse is over an empty line
          }

          return tip;
      }
	  
	  
	  
	  
	 
	  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
	    Rectangle sRect = super.getCellRect(row,column,includeSpacing);
	    if ((row <0) || (column<0) ||
	        (getRowCount() <= row) || (getColumnCount() <= column)) {
	        return sRect;
	    }
	    CellSpan cellAtt = (CellSpan)((AttributiveCellTableModel)getModel()).getCellAttribute();
	    if (! cellAtt.isVisible(row,column)) {
	      int temp_row    = row;
	      int temp_column = column;
	      row    += cellAtt.getSpan(temp_row,temp_column)[CellSpan.ROW];
	      column += cellAtt.getSpan(temp_row,temp_column)[CellSpan.COLUMN];      
	    }
	    int[] n = cellAtt.getSpan(row,column);

	    int index = 0;
	    int columnMargin = getColumnModel().getColumnMargin();
	    Rectangle cellFrame = new Rectangle();
	    int aCellHeight = rowHeight + rowMargin;
	    cellFrame.y = row * aCellHeight;
	    cellFrame.height = n[CellSpan.ROW] * aCellHeight;
	    
	    Enumeration eeration = getColumnModel().getColumns();
	    while (eeration.hasMoreElements()) {
	      TableColumn aColumn = (TableColumn)eeration.nextElement();
	      cellFrame.width = aColumn.getWidth() + columnMargin;
	      if (index == column) break;
	      cellFrame.x += cellFrame.width;
	      index++;
	    }
	    for (int i=0;i< n[CellSpan.COLUMN]-1;i++) {
	      TableColumn aColumn = (TableColumn)eeration.nextElement();
	      cellFrame.width += aColumn.getWidth() + columnMargin;
	    }
	    
	    

	    if (!includeSpacing) {
	      Dimension spacing = getIntercellSpacing();
	      cellFrame.setBounds(cellFrame.x +      spacing.width/2,
	        cellFrame.y +      spacing.height/2,
	        cellFrame.width -  spacing.width,
	        cellFrame.height - spacing.height);
	    }
	    return cellFrame;
	  }
	  
	  
	  private int[] rowColumnAtPoint(Point point) {
	    int[] retValue = {-1,-1};
	    int row = point.y / (rowHeight + rowMargin);
	    if ((row <0)||(getRowCount() <= row)) return retValue;
	    int column = getColumnModel().getColumnIndexAtX(point.x);

	    CellSpan cellAtt = (CellSpan)((AttributiveCellTableModel)getModel()).getCellAttribute();

	    if (cellAtt.isVisible(row,column)) {
	      retValue[CellSpan.COLUMN] = column;
	      retValue[CellSpan.ROW   ] = row;
	      return retValue;
	    }
	    retValue[CellSpan.COLUMN] = column + cellAtt.getSpan(row,column)[CellSpan.COLUMN];
	    retValue[CellSpan.ROW   ] = row + cellAtt.getSpan(row,column)[CellSpan.ROW];
	    return retValue;
	  }

	  
	  public int rowAtPoint(Point point) {
	    return rowColumnAtPoint(point)[CellSpan.ROW];
	  }
	  public int columnAtPoint(Point point) {
	    return rowColumnAtPoint(point)[CellSpan.COLUMN];
	  }
	 

	  
	  public void columnSelectionChanged(ListSelectionEvent e) {
	    repaint();
	  }

	  public void valueChanged(ListSelectionEvent e) {
	    int firstIndex = e.getFirstIndex();
	    int  lastIndex = e.getLastIndex();
	    if (firstIndex == -1 && lastIndex == -1) { // Selection cleared.
	      repaint();
	    }
	    Rectangle dirtyRegion = getCellRect(firstIndex, 0, false);
	    int numCoumns = getColumnCount();
	    int index = firstIndex;
	    for (int i=0;i<numCoumns;i++) {
	      dirtyRegion.add(getCellRect(index, i, false));
	    }
	    index = lastIndex;
	    for (int i=0;i<numCoumns;i++) {
	      dirtyRegion.add(getCellRect(index, i, false));
	    }
	    repaint(dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
	  }
	 
	}