package src;

import cellRenderers.ColorDSMRenderer;
import compiledinfo.ShowList;
import spaningFeature.AttributiveCellTableModel;
import tableModels.MainMatrix;
import tableModels.VerticalMatrix;
import CodedMatrixInfo.DepthMainMatrix;
import CodedMatrixInfo.EntityVerticalMatrix;
import datastructure.Entitate;
import datastructure.WithChildren;

public class ClickAction {
	
	
	private static String actionTaken;
	
	public static String actionOnClickWithFeedback(int rowCliked, int columnCliked){
		actionOnClick(rowCliked,columnCliked);
		return actionTaken;
	}
	

public static void actionOnClick(int rowCliked, int columnCliked) {
	//System.out.println("$ rowCliked:  "+rowCliked+"    col: "+columnCliked);
	 AttributiveCellTableModel newVerticalModel = null;
      AttributiveCellTableModel newDataModel;
      
      WithChildren clickedEntity;
	   int noChildrenToShow ;
       //System.out.println("action to be taken: "+EntityVerticalMatrix.ActionToBeTaken(rowCliked, columnCliked));
       //System.out.println("on matrix: ");
       //EntityVerticalMatrix.getEntityVMatrix().printMatrixWithNames();
	   ColorDSMRenderer.resetSelectedCell();
   	  
	   actionTaken = EntityVerticalMatrix.ActionToBeTaken(rowCliked, columnCliked);
	   switch(actionTaken){
   	   		case "Expand":  //cliked on the last column => expand event
   	   						//trebuie sa stiu pe ce entitate am clickuit inainte ca sa o shiftez la stanga ca sa fac loc pentru copii. am nevoie de numarul de copii ca sa stiu cate randuri adaug.
			   	   			
   	   			
   	   						int lastColumnOnVertical = 0;
					        if(VerticalMatrix.getColCounter()>0){
					        	lastColumnOnVertical = VerticalMatrix.getColCounter()-1;
					        }
					        clickedEntity= (WithChildren) EntityVerticalMatrix.getValueAt(rowCliked,lastColumnOnVertical );
					        noChildrenToShow =  clickedEntity.noChildren();
					        
   	   						newVerticalModel = new AttributiveCellTableModel(VerticalMatrix.getMatrix(rowCliked)){
		 	 		     	    	public boolean isCellEditable(int row, int column) {
		 	 		     	           return false;
		 	 		     	       }
		 	 		     	    }; 
		 	 		     	  
		 	 		     	 newDataModel = new AttributiveCellTableModel(MainMatrix.getMainDataTable(noChildrenToShow, "expand").getMainMAtrix()){
		 	 		     	    	public boolean isCellEditable(int row, int column) {
			 	 		     	           return false;
			 	 		     	       }
			 	 		     	    }; 
			 	 		     	    
			 	 		     	    
		 	  		        newVerticalModel.fireTableDataChanged();
		 	  		        newDataModel.fireTableDataChanged();
		 	  		        
		 	  		    
		 	  		      
		 	  		       //new mainTable
		 	  		        	// createdepthMatrix based on new printed entities (to color)
		 					DepthMainMatrix.expand(rowCliked, noChildrenToShow, ShowList.getPrintedEntities().size() );  //VerticalMatrix.getRowCounter()
		 	  		        SplitPaneDSM.updateFrame(newVerticalModel,newDataModel);
		 	 		        	 
		 	 		        	 break;
		 	 		        	 
   	   		case "Collapse" : //collapse event ; cliked on something already expanded 
   	   							int lastVerticalColumn = VerticalMatrix.getColCounter()-1;
   	   							Entitate firstChildOfClikedEntity =  EntityVerticalMatrix.getValueAt(rowCliked,lastVerticalColumn );
   	   							clickedEntity= (WithChildren) firstChildOfClikedEntity.getParent();
						       
   	   							
   	   						EntityVerticalMatrix entityVMatrix = EntityVerticalMatrix.getEntityVMatrix();
   	   						int myRow = rowCliked;
   	   						int contor = 0;
   	   						while(myRow< VerticalMatrix.getRowCounter() &&
   	   							entityVMatrix.getValueAt(rowCliked, columnCliked).getID() == entityVMatrix.getValueAt(myRow, columnCliked).getID()){
   	   							contor++;
	   	   						myRow++;
   	   						}
   	   						noChildrenToShow = contor;
   	   							
		 		    	   		 newVerticalModel = new AttributiveCellTableModel(VerticalMatrix.getMatrix(rowCliked,columnCliked,noChildrenToShow )){
		 		 		 	    	public boolean isCellEditable(int row, int column) {
		 		 		 	           return false;
		 		 		 	       }
		 		 		 	    }; 
		 		 		 	    
		 		 		 	 newDataModel = new AttributiveCellTableModel(MainMatrix.getMainDataTable(noChildrenToShow, "collapse").getMainMAtrix()){
		 	 		     	    	public boolean isCellEditable(int row, int column) {
			 	 		     	           return false;
			 	 		     	       }
			 	 		     	    }; 
			 	 		     	    
			 	 		     	    
		 	  		        newVerticalModel.fireTableDataChanged();
		 		 		        newDataModel.fireTableDataChanged();
		 		 		      
		 		 		       //new mainTable
		 		 		        	// createdepthMatrix based on new printed entities (to color)
			 					DepthMainMatrix.collapse(rowCliked, noChildrenToShow, VerticalMatrix.getRowCounter());
		 		 		        
		 		 		       SplitPaneDSM.updateFrame(newVerticalModel,newDataModel);
		 		 		         
		 		 		         break;
		 		 		         
   	   		case "Empty" : System.out.println("SWITCH: this cell is not clickable ->empty"+"");
   	   		
   	   								break;
   	   								
   	   		case "No Children" : System.out.println("SWITCH: this cell is not clickable -> no children");
   	   								break;
   	   								
   	   							
   	   }
	
}

}
