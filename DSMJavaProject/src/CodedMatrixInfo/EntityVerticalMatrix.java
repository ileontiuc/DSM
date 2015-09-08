package CodedMatrixInfo;

import pseudomatrix.PseudoMatrix;
import tableModels.VerticalMatrix;
import datastructure.Entitate;
import datastructure.WithChildren;

public class EntityVerticalMatrix {
	
	private static EntityVerticalMatrix singleObj = null;
	private static PseudoMatrix entityVMatrix = null;
	
	
	
	
	
	private EntityVerticalMatrix(  ){
		entityVMatrix = new PseudoMatrix(); 
	}

	
	public static  EntityVerticalMatrix getEntityVMatrix(){
		if(singleObj == null){
			singleObj = new EntityVerticalMatrix();
		}
		return singleObj;
	}
	
	
	
	
	public PseudoMatrix getMatrix(){
		return entityVMatrix;
	}
	
	public void setValueOn(Entitate ent,int row, int col){
		entityVMatrix.set(ent, row, col);
	}
	
	public static Entitate getValueAt(int row, int col){
		
		if(col>entityVMatrix.columns() || row>entityVMatrix.rows())
			return null;
		
		
		return (Entitate) entityVMatrix.get(row, col);
	}
	
	public static void printMatrixWithNames(){
		int i,j;
		
		for(i=0;i<VerticalMatrix.getRowCounter();i++){
			for(j=0;j<VerticalMatrix.getColCounter();j++){
				if(entityVMatrix.get(i, j) !=null){
					System.out.print(((Entitate) entityVMatrix.get(i, j)).getName()+"    ");
				}else{
					System.out.print(" @ ");
				}
			}
			System.out.println();
		}
	}
	
	
	public void deleteExpiredInfo(int rowC, int colC){
		//System.out.println("inainte de delete");
		//printMatrixWithNames();
		//System.out.println("sterg "+rowC+"   "+colC);
		entityVMatrix.deleteOutsideOfBounds(rowC, colC);
		
	}

	
	
	
	public static String ActionToBeTaken(int rowClicked, int colClicked){
		if(rowClicked == -1 || colClicked==-1)
			return "Empty";
		
		//emtpy cell 
				if(entityVMatrix.get(rowClicked, colClicked)==null)
					return "Empty";
				
		//check to see if entity can or has children, otherwise it cannot be expanded nor collapsed
				if(!   (entityVMatrix.get(rowClicked, colClicked) instanceof WithChildren)     )   {
					return "No Children";
				}else{
					if(((WithChildren) entityVMatrix.get(rowClicked, colClicked)).noChildren()<1){
						// ((WithChildren) entityVMatrix.get(rowClicked, colClicked)).printChildren();
						//System.out.println("ref:  "+entityVMatrix.get(rowClicked, colClicked));
						 return "No Children";
					}
				}
				
				int ColCounter = VerticalMatrix.getColCounter();
				//if clicked pe ultima coloana , se poate face expand
				if(colClicked == ColCounter-1){
					//has children
					return "Expand";
					/*
					if(entityVMatrix.get(rowClicked, colClicked) instanceof WithChildren){
						
						return "Expand";
					}
						
					else{
						return "No Children";
					}*/
						
				}
				//am expand si daca e aceasi valoare pana la final;
				int start = colClicked;
				while(start<ColCounter && (entityVMatrix.get(rowClicked, start) == entityVMatrix.get(rowClicked, colClicked))){
					start++;
				}
				if(start-1==ColCounter-1){
					return "Expand";
				}
				
				return "Collapse";
				
				/*
				//mai am doar un rand pana la final inseamna ca aceia sunt copii lui => collapse
				if(start-1 == ColCounter-2){
					return "Collapse";
				}
				
				start = colClicked+1;
				while(start<ColCounter && (entityVMatrix.get(rowClicked, start) == entityVMatrix.get(rowClicked, colClicked+1))){
					start++;
				}
				if(start-1 == ColCounter-1){
					return "Collapse";
				}
				
				
					return "No Children";
					*/
	}

	public static  int[][] getRowSpanSets(int rowToCheck, int rowC, int colC) {
		
		int i,j;
		Entitate currentEnt;
		int counter = 0;
		int[][] spanMap = new int[colC+1][colC+2];
		int currentPosition;
		int spanSets;
		
		spanSets=0;
		currentPosition =0;
		while(currentPosition<colC ){
			counter=0;
			currentEnt = getValueAt(rowToCheck,currentPosition);
			
			counter++;
			spanMap[spanSets][counter] = currentPosition;
			currentPosition++;
			while(getValueAt(rowToCheck,currentPosition) == currentEnt && currentPosition<colC ){
				counter++;
				spanMap[spanSets][counter] = currentPosition;
				currentPosition++;
			}
			spanMap[spanSets][0]=counter;
			spanSets++;
		}
		
		/*
		System.out.println("matricea pentru spanul pe randuri la  "+rowToCheck);
		for(i=0;i<colC+1;i++){
			for(j=0;j<colC+2;j++){
				System.out.print(spanMap[i][j]+"  ");
			}
			
			System.out.println();
		}*/
		
		return spanMap;
	}
	
	
public static  int[][] getColSpanSets(int colToCheck, int rowC, int colC) {
	
		int i,j;
		Entitate currentEnt;
		int counter;
		
		//System.out.println("dimensiune r "+(colC+1)+"   "+(rowC+1));
		int[][] spanMap = new int[rowC+1][rowC+2];
		int currentPosition;
		int spanSets;
		
		spanSets=0;
		currentPosition =0;
		while(currentPosition<rowC ){  //&& spanSets<colC
			counter=0;
			currentEnt = getValueAt(currentPosition,colToCheck);
			
			counter++;
			spanMap[spanSets][counter] = currentPosition;
			currentPosition++;
			while(getValueAt(currentPosition,colToCheck) == currentEnt && currentPosition<rowC ){
				counter++;
				spanMap[spanSets][counter] = currentPosition;
				currentPosition++;
			}
			spanMap[spanSets][0]=counter;
			spanSets++;
		}
		
		
		return spanMap;
	}



	
	
}
