package tableModels;

import java.util.ArrayList;
import java.util.Map;

import pseudomatrix.PseudoMatrix;
import CodedMatrixInfo.DepthMainMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;
import compiledinfo.EntityCatalog;
import compiledinfo.ShowList;
import datastructure.Entitate;
import datastructure.Root;
import datastructure.WithChildren;

public class VerticalMatrix {

	private static int RowCounter;
	private static int ColCounter;
	private Root treeRoot;
	private static ArrayList<Entitate> printedEntities;
	private Map<String, Entitate> entities;
	private static PseudoMatrix verticalMatrix;
	private static EntityVerticalMatrix entityVMatrix;
	private static DepthMainMatrix depthMainMatrix;
	private static boolean collapseDeleteCol = true;
	
	

	public static PseudoMatrix getMatrix() {
		if (verticalMatrix == null) {
			new VerticalMatrix();
		}
		return verticalMatrix;
	}

	private VerticalMatrix() {
		verticalMatrix = new PseudoMatrix();
		
		depthMainMatrix = DepthMainMatrix.getDepthMainMatrix(); //creez ca sa fie initializata cu zero
		
		treeRoot = Root.getRoot();
		printedEntities = ShowList.getPrintedEntities();
		entityVMatrix = EntityVerticalMatrix.getEntityVMatrix();  
		entities = EntityCatalog.getEntitiesCatalog();
		// treeRoot.printChildren();
		ColCounter = 1;
		RowCounter = treeRoot.getNoChildren();

		int i;
		for (i = 0; i < RowCounter; i++) {
			verticalMatrix.set( treeRoot.getChildAtIndex(i).getName(), i, 0);
			entityVMatrix.setValueOn(treeRoot.getChildAtIndex(i), i, 0);  
			printedEntities.add(i, treeRoot.getChildAtIndex(i));
		}

		
		//ShowList.printPrintedEntitiesNames();
	}

	
	
	//method updateExpand (same name because after update the caller method needs the object reference)
	public static PseudoMatrix getMatrix(int rowLastCliked) {
		// update expand
		int i, j;
		int indexToAddFrom;
		int noChildrenToShow;
		int childNo;
		WithChildren clickedEntity;
		Entitate currentChild;

		
		
			clickedEntity = (WithChildren) printedEntities.get(rowLastCliked);
			noChildrenToShow = clickedEntity.noChildren();
				RowCounter += noChildrenToShow - 1;
				
		if( !columnToPlaceChildrenAlreadyExists(rowLastCliked, noChildrenToShow)){
					// am coloana noua
			
				if ((ColCounter > 1 && verticalMatrix.get(rowLastCliked, ColCounter - 2) != null)
						|| ColCounter == 1) {
					// put all entities on new last column (push them to the right)
					for (i = 0; i < RowCounter; i++){
						//for (j = ColCounter - 1; j >= 0; j--) {
						j=ColCounter-1;
							verticalMatrix.set(verticalMatrix.get(i, j), i, j+1);
							entityVMatrix.setValueOn(entityVMatrix.getValueAt(i,j), i, j+1);
						//}
					}
					ColCounter++;
				}

				verticalMatrix.set(verticalMatrix.get(rowLastCliked, ColCounter - 1), rowLastCliked, ColCounter - 2);
				entityVMatrix.setValueOn(entityVMatrix.getValueAt(rowLastCliked,ColCounter - 1), rowLastCliked, ColCounter - 2);  
				Entitate parentWithChildrenExpanded = entityVMatrix.getValueAt(rowLastCliked,ColCounter - 1);

				indexToAddFrom = rowLastCliked; // first child position
				printedEntities.remove(indexToAddFrom);// eliminate the paarent
				childNo = 0;
				forwardFromPrintedLine(rowLastCliked, noChildrenToShow);
				// adding the children  
					int a = 0;
					for (i = rowLastCliked; i < rowLastCliked + noChildrenToShow; i++) {
						currentChild = clickedEntity.getChildrenAt(childNo);
						verticalMatrix.set(currentChild.getName(), i, ColCounter - 1);
						printedEntities.add(indexToAddFrom, currentChild);
						
						entityVMatrix.setValueOn(currentChild, i, ColCounter - 1);
						
						entityVMatrix.setValueOn(parentWithChildrenExpanded, i, ColCounter - 2);
						verticalMatrix.set(parentWithChildrenExpanded.getName(), i, ColCounter - 2);
						
						//adding parent to the left if needed
						a=3;
						while(ColCounter-a>=0 && entityVMatrix.getValueAt(i, ColCounter-a)==null){
							entityVMatrix.setValueOn(entityVMatrix.getValueAt(rowLastCliked, ColCounter-a), i, ColCounter - a);
							
							verticalMatrix.set(verticalMatrix.get(rowLastCliked, ColCounter - a), i, ColCounter - a);
							a++;	
						}
						indexToAddFrom++;
						childNo++;
					}
					
						entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
						deleteDuplicateNeighbourColumn( ColCounter-2);
						entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
						
						
				}else{
					//caz in care nu trebuie sa adaug coloana. exista deja printati fratii vitregi
		
					Entitate parentWithChildrenExpanded = entityVMatrix.getValueAt(rowLastCliked,ColCounter - 1);

					indexToAddFrom = rowLastCliked; // first child position
					printedEntities.remove(indexToAddFrom);// eliminate the paarent
					
					childNo = 0;
					forwardFromPrintedLine(rowLastCliked, noChildrenToShow);
					
						int a = 0;
						//am pus bine parintele si trebuie sa memorez pozitia lui ca sa stiu sa adaug copie dupa copil pana la pozitia lui
						
						currentChild = clickedEntity.getChildrenAt(childNo);
						verticalMatrix.set(currentChild.getName(), rowLastCliked, ColCounter - 1);
						printedEntities.add(indexToAddFrom, currentChild);
						entityVMatrix.setValueOn(currentChild, rowLastCliked, ColCounter - 1);
						
						//adaug copilul spre stanga ca sa fiu aliniat cu ce am in restul matricii
						int distanceFromRightMargin =2;  //2
						while(ColCounter-distanceFromRightMargin>0 && entityVMatrix.getValueAt(rowLastCliked,ColCounter-distanceFromRightMargin )==clickedEntity
								&& entityVMatrix.getValueAt(rowLastCliked,ColCounter-distanceFromRightMargin-1 )==clickedEntity){
							entityVMatrix.setValueOn(currentChild, rowLastCliked, ColCounter - distanceFromRightMargin);
							verticalMatrix.set( currentChild.getName(), rowLastCliked, ColCounter - distanceFromRightMargin);
						
							distanceFromRightMargin++;
						}
						
						//in while se sterge si parintele inclusiv, il pun inapoi pe ultima pozitie unde mai era
						//distanceFromRightMargin--;
						entityVMatrix.setValueOn(clickedEntity, rowLastCliked, ColCounter - distanceFromRightMargin);
						verticalMatrix.set( clickedEntity.getName(), rowLastCliked, ColCounter - distanceFromRightMargin);
						
						int lastPositionOfParent = ColCounter-distanceFromRightMargin;
						indexToAddFrom++;
						childNo++;
						for (i = rowLastCliked+1; i < rowLastCliked + noChildrenToShow; i++) {
							currentChild = clickedEntity.getChildrenAt(childNo);
							printedEntities.add(indexToAddFrom, currentChild);
							for(j=0;j<ColCounter;j++){
								if(j<=lastPositionOfParent){
									//copiez ce am pe randul de mai sus pt ca e vorba de parinti
									entityVMatrix.setValueOn(entityVMatrix.getValueAt(i-1, j), i, j);
									verticalMatrix.set(verticalMatrix.get(i-1, j), i, j);
								}else{
									entityVMatrix.setValueOn(currentChild, i, j);
									verticalMatrix.set(currentChild.getName(), i, j);
									
								}
							}
							indexToAddFrom++;
							childNo++;
							
						}
							entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
							deleteDuplicateNeighbourColumn( ColCounter-2);
							entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
							
				}

		return verticalMatrix;
	}

	private static boolean columnToPlaceChildrenAlreadyExists(
			int rowLastCliked, int noChildrenToShow) {
		
		if(ColCounter == 1)
			return false;
		
		
		if( entityVMatrix.getValueAt(rowLastCliked, ColCounter-1) != entityVMatrix.getValueAt(rowLastCliked, ColCounter-2)){
			return false;
		}
		
		//merg in sus si verific daca s-au afisat copii
		int distanceFromClickedRow=1;
		while(rowLastCliked-distanceFromClickedRow>=0){
			if(entityVMatrix.getValueAt(rowLastCliked-distanceFromClickedRow, ColCounter-1) != EntityVerticalMatrix.getValueAt(rowLastCliked-distanceFromClickedRow, ColCounter-2)){
				return true;
			}
			distanceFromClickedRow++;
		}
		//merg in jos
		distanceFromClickedRow=1;
		//lucrez pe dimensiunile vechi
		//rowLastCliked+=(noChildrenToShow-1);
		
		while(rowLastCliked+distanceFromClickedRow<RowCounter){
			if(entityVMatrix.getValueAt(rowLastCliked+distanceFromClickedRow, ColCounter-1) != EntityVerticalMatrix.getValueAt(rowLastCliked+distanceFromClickedRow, ColCounter-2)){
				return true;
			}
			distanceFromClickedRow++;
		}
		
			return false;
			
				
	}

	public static PseudoMatrix getMatrix(int rowLastCliked, int columnLastClicked, int noChildrenToEliminate) {
		// update COLLAPSE

	
		// if it got here it means the cell is clickable and can be collapsed
		Entitate childOfClikedEntity, firstChildOfClickedEntity;
		int  i, j;
		firstChildOfClickedEntity = printedEntities.get(rowLastCliked);
		Entitate clickedEntity =  entityVMatrix.getValueAt(rowLastCliked, columnLastClicked); 
		
			//asigurat prin faptul ca a ajuns aici la click
				
				// eliminate children from getting printed
		
			for (i = rowLastCliked; i < (rowLastCliked + noChildrenToEliminate); i++) {
				printedEntities.remove(rowLastCliked); // eliminate current child from getting printed
			}
		
				

				// aduc in sus liniile
				backwardFromPrintedLine(rowLastCliked, noChildrenToEliminate);
				// +1(adica - -) row for parent that is now to be printed
				RowCounter -= noChildrenToEliminate - 1; 

				// add parent to print 
				printedEntities.add(rowLastCliked,clickedEntity);

				
				for(i=columnLastClicked;i<ColCounter;i++){
					verticalMatrix.set( verticalMatrix.get(rowLastCliked, columnLastClicked)  , rowLastCliked, i);
					entityVMatrix.setValueOn(entityVMatrix.getValueAt(rowLastCliked, columnLastClicked), rowLastCliked, i);
				}
				
				entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
				
				int oldColCounter = ColCounter;
				for(i=oldColCounter-1;i>columnLastClicked;i--){
					deleteDuplicateNeighbourColumn( i); 
				}
				entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
		
				
		return verticalMatrix;

	}

	private static void deleteDuplicateNeighbourColumn(int col) {
		
		int i;
		int identicCells = 0;
		int colToEliminate;
		boolean eliminateLeftNeigbour=false;
		boolean eliminateRightNeighbour=false;
		
		
		//test for left neighbour
		if(col-1>=0){
			identicCells =0;
			for(i=0;i<RowCounter;i++){
				if( entityVMatrix.getValueAt(i, col) ==  entityVMatrix.getValueAt(i, col-1)  ){
					identicCells++;
				}
			}
		}
		
		if(identicCells == RowCounter){ //identic columns
			eliminateLeftNeigbour = true;
		}
		
		//test for right neighbour
				if(col+1<ColCounter){
					identicCells =0;
					for(i=0;i<RowCounter;i++){
						if( entityVMatrix.getValueAt(i, col) ==  entityVMatrix.getValueAt(i, col+1)  ){
							identicCells++;
						}
					}
				}
				
				if(identicCells == RowCounter){ //identic columns
					eliminateRightNeighbour = true;
				}
			
				
		if(eliminateLeftNeigbour || eliminateRightNeighbour){ //identic columns
			eliminateColumn(col);
		}
		
	}

	private static void forwardFromPrintedLine(int start, int jumpSize) {
		int i, j;
		if(jumpSize>1){
			for (i = RowCounter - 1; i > start + jumpSize - 1; i--){
				for (j = 0; j < ColCounter; j++) {
					
					verticalMatrix.set(verticalMatrix.get(i - jumpSize + 1, j), i, j);
					verticalMatrix.set(null, i - jumpSize + 1, j);
					
					
					entityVMatrix.setValueOn(entityVMatrix.getValueAt(i - jumpSize + 1, j), i, j);
					entityVMatrix.setValueOn(null, i - jumpSize + 1, j);
					//System.out.println("adding5 "+entityVMatrix.getValueAt(i - jumpSize + 1, j).getName()+"   at row: "+i+"  at col:  "+j);
					
				}
				
				
			}
		}
		
	}
	
	
	private static void eliminateColumn(int index) {
		int i;
		if (index == (ColCounter - 1)) {
			ColCounter--;
			entityVMatrix.deleteExpiredInfo(RowCounter, ColCounter);
		} else {
			for (i = 0; i < RowCounter; i++) {
				verticalMatrix.set(verticalMatrix.get(i, index + 1), i, index);
				entityVMatrix.setValueOn(entityVMatrix.getValueAt(i, index+1), i, index);
				
			}
			index++;
			eliminateColumn(index);
		}

	}

	private static void backwardFromPrintedLine(int start, int jumpSize) {
		int i, j;
		for (i = start + jumpSize; i < RowCounter; i++)
			// leave start row empty;
			for (j = 0; j < ColCounter; j++) {
				verticalMatrix.set(verticalMatrix.get(i, j), i - jumpSize + 1, j);
				//verticalMatrix[i][j] = null;
				
				entityVMatrix.setValueOn(entityVMatrix.getValueAt(i, j), i - jumpSize + 1, j);
				
			}
	}

	public static  void printMatrix() {
		int i, j;
		

		for (i = 0; i < RowCounter; i++) {
			for (j = 0; j < ColCounter; j++)
				System.out.print(verticalMatrix.get(i, j) + "  ");
			System.out.println();
		}

		System.out.println();
		System.out.println();
		System.out.println();
	}

	private void printVisibleEntitites() {

		int i;

		System.out.println("PrintedEntities: " + printedEntities.size());
		System.out.println();

		for (i = 0; i < printedEntities.size(); i++) {
			System.out.print(printedEntities.get(i).getName());
			System.out.println();
		}

		System.out.println();
		System.out.println();
		System.out.println();

	}
	
	
	public static int getRowCounter(){
		return RowCounter;
	}

	
	public static int getColCounter(){
		return ColCounter;
	}

	public static boolean getColumnDeleteFlag() {
		return false;
	}
	
	public static boolean isAPrintedEntity(int row,int  column){
		String originalValue = (String) verticalMatrix.get(row, column);
		for(int i = column+1;i<getColCounter();i++){
			if(!originalValue.equals(verticalMatrix.get(row, i)))
				return false;
		}
		
		return true;
	}
	
	
}
