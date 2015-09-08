package tableModels;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import compiledinfo.ShowList;
import datastructure.Entitate;
import datastructure.Root;


public class HorizontalMatrix  extends AbstractTableModel {

	private static ArrayList<String> horizontalMatrix;
	private static int  ColCounter, RowCounter;
	private ArrayList<Entitate> printedEntities;
	
	
	public static ArrayList<String> getHorizontalMatrix(){
		return horizontalMatrix;
	}
	
	
	public HorizontalMatrix( ){
		
		horizontalMatrix = new ArrayList<String>();
		printedEntities = ShowList.getPrintedEntities();
		
		ColCounter = printedEntities.size();
		RowCounter = 1;
		int i;
		
		for(i=0;i<ColCounter;i++)
			horizontalMatrix.add(i, "  "+printedEntities.get(i).getName());
		
	}

	
	
	@Override
	public int getRowCount() {
		return RowCounter;
	}

	public static int getColumnNo() {
		return ColCounter;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex==0)
			return horizontalMatrix.get(columnIndex);
		return null;
	}


	@Override
	public int getColumnCount() {
		return 0;
	}
	
	
}
