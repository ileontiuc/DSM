package pseudomatrix;

import java.util.ArrayList;

public class PseudoMatrix {

	private ArrayList<Element> matrix;
	
	public PseudoMatrix(){
		matrix = new ArrayList<Element>();
	}
	
	
	//adauga element nou sau face update daca deja exista valoare acolo
	public void set(Object objToAdd, int row, int col){
		Element current = getElement(row,col);
		if(current == null){
			Element intermediar = new Element(objToAdd,row,col);
			matrix.add(intermediar);
		}else{
			//change value, elem already exists
			current.changeValue(objToAdd);
		}
		
	}
	
	private Element getElement(int row, int col){
		for(Element current:matrix){
			if(current.getCol()==col && current.getRow()==row){
				return current;
			}
		}
		return null;
	}
	
	//returneaza valoare de acolo; daca nu exista null
	public Object get(int row, int col){
		for(Element current:matrix){
			if(current.getCol()==col && current.getRow()==row){
				return current.getValue();
			}
		}
		return null;
	}

	
	public int rows(){
		int noRows=0;
		
			for(Element current:matrix){
				if(current.getRow()>noRows)
					noRows = current.getRow();
			}
			return noRows;
	}
	
	public int columns(){
		int noCol=0;
		
			for(Element current:matrix){
				if(current.getCol()>noCol)
					noCol = current.getCol();
			}
			return noCol;
	}
	
	
	public void deleteOutsideOfBounds(int rowC, int colC) {
		
		for(Element current:matrix){
			int row = current.getRow(); 
			int col = current.getCol();
			if(row>rowC || col >colC){
				//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@  r "+current.getRow()+"    c "+current.getCol()+"   value: "+current.getValue().getName());
				set(null, row, col);
				//TODO dc nu pot sa remove?????????????
				//matrix.remove(current);
			}
				
		}
		
	}
	
	
}
