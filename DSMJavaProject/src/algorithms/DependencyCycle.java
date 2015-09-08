package algorithms;

import java.math.BigDecimal;

import tableModels.MainMatrix;
import CodedMatrixInfo.Constants;
import datastructure.Entitate;
import datastructure.Pachet;

public class DependencyCycle {

	public static boolean badDirectCycleDependency(int row, int col){
		
		MainMatrix dsmTable = MainMatrix.getMainDataTable();
		
		if(dsmTable!=null){
			int cellValue = (int) dsmTable.getValueAt(row, col);
			int simetricDP = (int) dsmTable.getValueAt(col, row);
			//simetric fata de diagonala principala
			if( cellValue>0 &&  simetricDP>0 ){
				return true;
			} 
		}
		
		return false;
		
}
	
}
