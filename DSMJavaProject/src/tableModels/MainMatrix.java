package tableModels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

import CodedMatrixInfo.DepthMainMatrix;
import compiledinfo.ShowList;
import datastructure.Entitate;
import datastructure.Root;

//import dataStructure.Entitate;


public class MainMatrix extends AbstractTableModel{

	private static MainMatrix instance;
	private static Matrix DSMmatrix = null;
	private static int Counter;
	private Root treeRoot;
	private static ArrayList<Entitate> printedEntities;
	private static MainMatrix singleObj = null;
	
	
	
	public Matrix getMainMAtrix(){
		return DSMmatrix;
	}
	
	
	public int getMaxValue(){
		return (int) DSMmatrix.max();
	}
	
	
	private MainMatrix(){
		treeRoot = Root.getRoot(); 
		printedEntities = ShowList.getPrintedEntities();
		Counter = treeRoot.getNoChildren();
		int startValue = printedEntities.size() +1;
		
		DSMmatrix = new CCSMatrix(new double[startValue][startValue]);
		DSMmatrix.set(0, startValue-1, -2);
		//in matrice o sa am doar valori pozitiive pentru dependente si -1 pe diagonala principala, -2 ca si flag
		
		int i,j;
		int x=0;
		
		Entitate user; //merg pe coloana si trec in jos cat de mult user foloseste entitatea de pe rand
		Entitate used;
		
		for(i=0;i<Counter;i++){
			user = printedEntities.get(i);
			for(j=0;j<Counter;j++){
				used = printedEntities.get(j);
				if(i == j)//sunt pe diagonala principala
					DSMmatrix.set(i, j, -1);
				else{
					DSMmatrix.set(j, i, user.usesStreght(used));	
				}
			}
		}
		
	}
	
	public static MainMatrix getMainDataTable(){
		if(singleObj == null){
			singleObj = new MainMatrix();
		}else{
			/*
			try{
			throw new Exception("nu am facut apel corect; matricea trebuia creata la inceput initial si cand fac update sa trimit numarul de linii cu care am modificat");
			}catch(Exception e){
				e.printStackTrace();
			}*/
			
		}
		return singleObj;
	}
	
	
	public static MainMatrix getMainDataTable(int noLinesChanged, String s){
		singleObj.update(noLinesChanged,s);
		return singleObj;
	}
	
	
	
	@Override
	public int getRowCount() {
		return Counter;  
	}

	@Override
	public int getColumnCount() {
		return Counter; 
	}
	
	public static int getSize(){
		return Counter;  
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return (int) DSMmatrix.get(rowIndex, columnIndex);
	}
	
	public static void printMainMatrix(int size){
		int i,j;
		for(i=0;i<size;i++){
			for(j=0;j<size;j++){
				System.out.print((int) DSMmatrix.get(i, j)+" ");
			}
			System.out.println();
		}
	}
	
	public void update(int noLinesChanged, String flag){
		Counter = printedEntities.size();
		
		//resize the matrix
		int initialSize;
		int largerSize,smallerSize;
		switch(flag){
		case "expand":
			//trebuie sa maresc matricea cu noLinesChanged
			initialSize = Counter - noLinesChanged+2;
			
			//delete flag colt dreapta sus
			
			DSMmatrix.set(0, initialSize-1, 0);
			
			//expand matrix
			largerSize = Counter+1;
			DSMmatrix = DSMmatrix.resize(largerSize, largerSize);
			//add new flag
			DSMmatrix.set(0, Counter, -2);
			break;
			
		case "collapse":
			//trebuie sa micsorez matricea cu noLinesChanged
			initialSize = Counter + noLinesChanged;
			
			//delete flag colt dreapta sus
			DSMmatrix.set(0, initialSize-1, 0);
			
			//collapse matrix
			smallerSize = Counter+1;
			DSMmatrix = DSMmatrix.resize(smallerSize, smallerSize);
			//add new flag
			DSMmatrix.set(0, Counter, -2);
			break;
			
		default: try{
			throw new Exception("am trimis un caz gresit. se poate doar expand sau collapse pe matrice nu altceva");
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		
		
		int i,j;
		
		Entitate user; //merg pe coloana si trec in jos cat de mult user foloseste entitatea de pe rand
		Entitate used;
	
		for(i=0;i<Counter;i++){
			user = printedEntities.get(i);
			for(j=0;j<Counter;j++){
				used = printedEntities.get(j);
				if(i == j)//sunt pe diagonala principala
					DSMmatrix.set(i, j, -1);
				else
					DSMmatrix.set(j, i, user.usesStreght(used));
			}
			
		}
	}

}

