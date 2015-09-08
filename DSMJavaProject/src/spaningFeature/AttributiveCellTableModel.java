package spaningFeature;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.la4j.matrix.Matrix;

import pseudomatrix.PseudoMatrix;
import tableModels.HorizontalMatrix;
import tableModels.MainMatrix;
import tableModels.VerticalMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;

public class AttributiveCellTableModel extends DefaultTableModel {

	protected CellAttribute cellAtt;
	private String verticalMatrix[][];
	

	
	public AttributiveCellTableModel(Matrix mainMatrix) {

		int size  = MainMatrix.getSize();
		Vector names = new Vector(size);
		names.setSize(size);
		setColumnIdentifiers(names);
		dataVector = new Vector();
		setNumRows(size);
		cellAtt = new DefaultCellAttribute(size, size);

		for (int i = 0; i < size; i++) 
			for(int j=0;j<size;j++){
				setValueAt((int)mainMatrix.get(i, j) , i, j);
		}
	}
	
	
	//flag is just to differentiate from the constructor used for the vertical matrxi which is very different since it has the spanning feature
	public AttributiveCellTableModel(ArrayList<String> horizontalMatrix, String flag) {

		int ColCounter = HorizontalMatrix.getColumnNo();
		int RowCounter =1;
		
		Vector names = new Vector(ColCounter);
		names.setSize(ColCounter);
		setColumnIdentifiers(names);
		dataVector = new Vector();
		setNumRows(RowCounter);
		cellAtt = new DefaultCellAttribute(RowCounter, ColCounter);

		for (int i = 0; i < RowCounter; i++) 
			for(int j=0;j<ColCounter;j++){
				setValueAt(horizontalMatrix.get(j), i, j);
		}
	}
	
	
	
	
	
	
	public AttributiveCellTableModel(PseudoMatrix verticalMatrix) {

		int ColCounter = VerticalMatrix.getColCounter();
		int RowCounter = VerticalMatrix.getRowCounter();
		
		Vector names = new Vector(ColCounter);
		names.setSize(ColCounter);
		setColumnIdentifiers(names);
		dataVector = new Vector();
		setNumRows(RowCounter);
		cellAtt = new DefaultCellAttribute(RowCounter, ColCounter);

		for (int i = 0; i < RowCounter; i++) 
			for(int j=0;j<ColCounter;j++){
				
				setValueAt(verticalMatrix.get(i, j) , i, j);
		}
		
		
		
		int i,j;
		
		CellSpan cellAtt = (CellSpan) getCellAttribute();
		int[][] spanMap;
		int[] columns ;
		int[] rows;
		int lengthRows;
		int rowInSpanMap=0;
		 
		
		
		
		//see span sets for each col
		
		for(j=0;j<ColCounter;j++){ 
			
			spanMap = EntityVerticalMatrix.getColSpanSets(j, RowCounter, ColCounter);
			columns = new int[1];
			columns[0] = j;
			for(rowInSpanMap=0;rowInSpanMap<=RowCounter;rowInSpanMap++){
				if(spanMap[rowInSpanMap][0]>1 ){
						lengthRows = spanMap[rowInSpanMap][0];
						rows = new int[lengthRows];  //lengthRows
						//System.out.println("Rezultat COLS pt: "+j+"     lengthRows  "+lengthRows);
						for(i=0;i<lengthRows;i++){
							rows[i] = spanMap[rowInSpanMap][i+1];
							//System.out.print(rows[j]+"   ");
						}
						cellAtt.combine(rows,columns);
					}
			}
				
		}
		
		rowInSpanMap=0;
		//see span sets for each row
		//System.out.println("AICI rowCounter:   "+RowCounter);
		for(i=0;i<RowCounter;i++){
			spanMap = EntityVerticalMatrix.getRowSpanSets(i, RowCounter, ColCounter);
			rows = new int[1];
			rows[0] = i;
			for(rowInSpanMap=0;rowInSpanMap<=ColCounter;rowInSpanMap++){
				if(spanMap[rowInSpanMap][0]>1 ){
						lengthRows = spanMap[rowInSpanMap][0];
						columns = new int[lengthRows];  //lengthRows
						//System.out.println("Rezultat rows pt: "+i+"  set de span: "+rowInSpanMap);
						for(j=0;j<lengthRows;j++){
							columns[j] = spanMap[rowInSpanMap][j+1];
							//System.out.print("  "+columns[j]);
						}
						
						cellAtt.combine(rows,columns);
					}
			}//System.out.println();
				
			}
		//System.out.println();System.out.println();System.out.println();
		
		
		
		
	}

	public AttributiveCellTableModel() {
		this((Vector) null, 0);
	}

	public AttributiveCellTableModel(int numRows, int numColumns) {

		Vector names = new Vector(numColumns);
		names.setSize(numColumns);
		setColumnIdentifiers(names);
		dataVector = new Vector();
		setNumRows(numRows);
		cellAtt = new DefaultCellAttribute(numRows, numColumns);

	}

	public AttributiveCellTableModel(Vector columnNames, int numRows) {
		columnIdentifiers = columnNames;
		dataVector = new Vector();
		setNumRows(numRows);
		cellAtt = new DefaultCellAttribute(numRows, columnNames.size());
	}

	public AttributiveCellTableModel(Object[] columnNames, int numRows) {
		this(convertToVector(columnNames), numRows);
	}

	public AttributiveCellTableModel(Vector data, Vector columnNames) {
		setDataVector(data, columnNames);
	}

	public AttributiveCellTableModel(Object[][] data, Object[] columnNames) {
		setDataVector(data, columnNames);
	}

	public void setDataVector(Vector newData, Vector columnNames) {
		if (newData == null)
			throw new IllegalArgumentException(
					"setDataVector() - Null parameter");
		dataVector = new Vector(0);
		columnIdentifiers = columnNames;
		dataVector = newData;

		//
		cellAtt = new DefaultCellAttribute(dataVector.size(),
				columnIdentifiers.size());

		newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	/*
	 * public void addColumn(Object columnName, Vector columnData) { //if
	 * (columnName == null) //throw new
	 * IllegalArgumentException("addColumn() - null parameter");
	 * columnIdentifiers.addElement(columnName); int index = 0; Enumeration
	 * eeration = dataVector.elements(); while (eeration.hasMoreElements()) {
	 * Object value; if ((columnData != null) && (index < columnData.size()))
	 * value = columnData.elementAt(index); else value = null; ((Vector)
	 * eeration.nextElement()).addElement(value); index++; }
	 * 
	 * // cellAtt.addColumn();
	 * 
	 * fireTableStructureChanged(); }
	 */
	
	private void addRow() {
		Vector newData = null;
		newData = new Vector(getColumnCount());
		dataVector.addElement(newData);

		//
		cellAtt.addRow();

		newRowsAdded(new TableModelEvent(this, getRowCount() - 1,
				getRowCount() - 1, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.INSERT));
	}
	
	public void addRow(Vector rowData) {
		Vector newData = null;
		if (rowData == null) {
			newData = new Vector(getColumnCount());
		} else {
			rowData.setSize(getColumnCount());
		}
		dataVector.addElement(newData);

		//
		cellAtt.addRow();

		newRowsAdded(new TableModelEvent(this, getRowCount() - 1,
				getRowCount() - 1, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.INSERT));
	}

	public void insertRow(int row, Vector rowData) {
		if (rowData == null) {
			rowData = new Vector(getColumnCount());
		} else {
			rowData.setSize(getColumnCount());
		}

		dataVector.insertElementAt(rowData, row);

		//
		cellAtt.insertRow(row);

		newRowsAdded(new TableModelEvent(this, row, row,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	public CellAttribute getCellAttribute() {
		return cellAtt;
	}

	public void setCellAttribute(CellAttribute newCellAtt) {
		int numColumns = getColumnCount();
		int numRows = getRowCount();
		if ((newCellAtt.getSize().width != numColumns)
				|| (newCellAtt.getSize().height != numRows)) {
			newCellAtt.setSize(new Dimension(numRows, numColumns));
		}
		cellAtt = newCellAtt;
		fireTableDataChanged();
	}


	

}