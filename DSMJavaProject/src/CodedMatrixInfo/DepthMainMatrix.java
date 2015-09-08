
package CodedMatrixInfo;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

import datastructure.Entitate;
import datastructure.Root;

public class DepthMainMatrix {
	
	private static Matrix depthColorMatrix = null;
	private static DepthMainMatrix singleObj = null;
	
	
	
	public void setMatrix(int[][] a, int size){
		int i,j;
		for(i=0;i<size;i++){
			for(j=0;j<size;j++){
				depthColorMatrix.set(i, j, a[i][j]);
			}
		}
	}
	
	private DepthMainMatrix(  ){
		int startValue = Root.getRoot().getNoChildren()+1 ;
		depthColorMatrix = new CCSMatrix(new double[startValue][startValue]); 
		//TODO bug described in other TODO`s in this class
		depthColorMatrix.set(0, startValue-1, -2);
		
	}

	
	public static  DepthMainMatrix getDepthMainMatrix(){
		if(singleObj == null){
			singleObj = new DepthMainMatrix();
		}
		return singleObj;
	}
	
	public static int getDepthFor(int row, int col){
		return (int) depthColorMatrix.get(row, col);
	}

	
	public static void expand(int posClicked, int noLinesAdded, int newLargerSize){
		int newDepth; //se poate deduce
		int i,j,rowWhereDuplicate,ColToCopyFrom,colWhereDuplicate,rowToCopyFrom,initialValueonClickedCell;
		int initialSize = newLargerSize-noLinesAdded;
		
		//resize to larger matrix
		//TODO due to bug first delelete flag (colt dreapta sus)
		depthColorMatrix.set(0, initialSize+1, 0);
		newLargerSize++;
		depthColorMatrix = depthColorMatrix.resize(newLargerSize,newLargerSize); 
		
		
		
		newDepth = (int) (depthColorMatrix.get(posClicked, posClicked) + 1);
		initialValueonClickedCell = (int) depthColorMatrix.get(posClicked, posClicked);
		
		//mut coloanele si randurile (3 parti)
		
		//patrat VERDE mutat fiecare celula cu n in jos si la dreapta (parcurg matricea de jos in sus de la dreapta la stanga)
		for(i=initialSize; i>posClicked; i--){
			for(j=initialSize; j>posClicked; j--){
				depthColorMatrix.set(i+noLinesAdded-1, j+noLinesAdded-1, depthColorMatrix.get(i, j));
			
			}
		}
		
		
		
		//patrat sheftez mai jos partea MOV HASURATA  (patrat ce ramane identic doar ca trebuie mutat mai jos)
				for(i=initialSize; i>posClicked; i--){
					for(j=posClicked-1; j>=0; j--){
						depthColorMatrix.set(i+noLinesAdded-1, j, depthColorMatrix.get(i, j));
					}
				}
				
			
		//patrat shiftez la dreapta zona NEGRU HASURAT
				for(i=posClicked-1; i>=0; i--){
					for(j=initialSize; j>posClicked; j--){
							depthColorMatrix.set(i, j+noLinesAdded-1, depthColorMatrix.get(i, j));
					}
				}
				
				
		//linie ALBASTRA
		
		//shiftez linia spre dreapta (de la dreapta spre stanga)
		for(i=initialSize+noLinesAdded; i>posClicked; i--){
			//System.out.println("values: "+posClicked+"   "+(i-noLinesAdded));
			if(i-noLinesAdded+1>=0){
				depthColorMatrix.set(posClicked, i, depthColorMatrix.get(posClicked, i-noLinesAdded+1));
			}
		}
		
		
		for(rowWhereDuplicate=posClicked; rowWhereDuplicate<posClicked+noLinesAdded;rowWhereDuplicate++ ){
			for(ColToCopyFrom=posClicked+1;ColToCopyFrom<initialSize+noLinesAdded;ColToCopyFrom++){
				depthColorMatrix.set(rowWhereDuplicate, ColToCopyFrom, depthColorMatrix.get(posClicked, ColToCopyFrom));
			}
		}
		
		
		//coloana ROSIE
		for(i=initialSize+noLinesAdded; i>posClicked; i--){
			if(i-noLinesAdded+1>=0){
				depthColorMatrix.set(i, posClicked, depthColorMatrix.get(i-noLinesAdded+1, posClicked));
				//depthColorMatrix[i-noLinesAdded][posClicked] = -1;
			}
		}
		
		for(colWhereDuplicate=posClicked; colWhereDuplicate<posClicked+noLinesAdded;colWhereDuplicate++ ){
			for(rowToCopyFrom=posClicked+1;rowToCopyFrom<initialSize+noLinesAdded;rowToCopyFrom++){
				depthColorMatrix.set(rowToCopyFrom, colWhereDuplicate, depthColorMatrix.get(rowToCopyFrom, posClicked));
			}
		}
		
		//randul MOV (stanga la celula clickuita)
		for(rowWhereDuplicate= posClicked ; rowWhereDuplicate<posClicked+noLinesAdded;rowWhereDuplicate++ ){
			for(ColToCopyFrom=0;ColToCopyFrom<posClicked;ColToCopyFrom++){
				depthColorMatrix.set(rowWhereDuplicate, ColToCopyFrom, depthColorMatrix.get(posClicked, ColToCopyFrom));
				
			}
		}
		
		//coloana MARO
		for(colWhereDuplicate=posClicked; colWhereDuplicate<posClicked+noLinesAdded;colWhereDuplicate++ ){
			for(rowToCopyFrom=0;rowToCopyFrom<posClicked;rowToCopyFrom++){
				depthColorMatrix.set(rowToCopyFrom, colWhereDuplicate, depthColorMatrix.get(rowToCopyFrom, posClicked));
				}
		}
		
		
		//umplu patratul ramas cu newDepth
		
		for(i=posClicked;i<posClicked+noLinesAdded;i++){
			for(j=posClicked;j<posClicked+noLinesAdded;j++){
				depthColorMatrix.set(i, j, newDepth);
			}
		}
		
		//TODO due to unknown bug colt sus dreapta ii dau valoarea unu pe coloana care este totdeauna in plus ;)
		depthColorMatrix.set(0, newLargerSize-1, 1);
		
	}
	
	
	public static void collapse(int posClicked, int noLinesSubstracted, int smallerSize){   //posClicked care va fii cel curent
		
		int i,j;
		smallerSize++;
		int largerSize = smallerSize + noLinesSubstracted-1;
		
		int coltValue = (int) depthColorMatrix.get(posClicked+1, posClicked+1);
		
		//aduc inapoi patratul MOV
		for(i=0; i<posClicked; i++){
			for(j=posClicked+noLinesSubstracted; j<largerSize; j++){//+1
				if(!  ( j-noLinesSubstracted+1==posClicked) ){
					depthColorMatrix.set(i, j-noLinesSubstracted+1, depthColorMatrix.get(i, j));
					depthColorMatrix.set(i, j, 0);
				}else{
					//System.out.println("MOV am sarit la: r:"+i+"   c: "+(j-noLinesSubstracted+1)+"   ramane la valoarea initiala: "+depthColorMatrix[i][j-noLinesSubstracted+1]);
					//nu fac nimic 
				}
			}
		}
		
		
		//aduc inapoi patratul VERDE
		for(i=posClicked+noLinesSubstracted; i<largerSize; i++){
			for(j=posClicked+noLinesSubstracted; j<largerSize; j++){
				//4 pozitii unde raman valorile vechi; pt verde sunt doar 2
				if(!  ((i-noLinesSubstracted+1==posClicked && j-noLinesSubstracted+1==posClicked+1)||(i-noLinesSubstracted+1==posClicked+1 && j-noLinesSubstracted+1==posClicked))){
					depthColorMatrix.set(i-noLinesSubstracted+1, j-noLinesSubstracted+1, depthColorMatrix.get(i, j));
					depthColorMatrix.set(i, j, 0);
				}else{
					//System.out.println("VERDE am sarit la: r:"+(i-noLinesSubstracted+1)+"   c: "+(j-noLinesSubstracted+1)+"   scad din valoarea initiala: "+depthColorMatrix[i-noLinesSubstracted+1][j-noLinesSubstracted+1]);
					//nu fac nimic pt ca e linie shiftata
				}
			}
		}
		
		
		//shiftez randul clicked doar de la cdiagonala principala inspre dreapta; shiftez cu atatia copii cati am sters
		for(i=posClicked+noLinesSubstracted;i<largerSize;i++){
			depthColorMatrix.set(posClicked, i-noLinesSubstracted+1, depthColorMatrix.get(posClicked, i));
		}
		
		
		//shiftez coloana clicked doar de la cdiagonala principala in jos; shiftez cu atatia copii cati am sters
		for(j=posClicked+noLinesSubstracted;j<largerSize;j++){
			depthColorMatrix.set(j-noLinesSubstracted+1, posClicked, depthColorMatrix.get(j, posClicked));
		}
		
		
		//aduc inapoi patratul ALBASTRU
		
		for(i=posClicked+noLinesSubstracted; i<largerSize; i++){  //+1
			for(j=0; j<posClicked; j++){
				if(! (i-noLinesSubstracted+1==posClicked )){
					 depthColorMatrix.set(i-noLinesSubstracted+1, j, depthColorMatrix.get(i, j));
						depthColorMatrix.set(i, j, 0);
				}else{
					//System.out.println("ALBASTRU am sarit la: r:"+(i-noLinesSubstracted+1)+"   c: "+j+"   ramane la valoarea initiala: "+depthColorMatrix[i-noLinesSubstracted+1][j]);
					// nu fac nimic pt ca sunt pe lini ce trebuie shiftata
				}
				
			}
		}
		
		
		//pun zero in rest dar in colt las valoarea initiala -1 (ca sa se expand corect pe viitor)
		depthColorMatrix.set(posClicked, posClicked, coltValue-1);
		
		depthColorMatrix = depthColorMatrix.resize(smallerSize,smallerSize); 
		//TODO due to unknown bug colt sus dreapta ii dau valoarea unu pe coloana care este totdeauna in plus ;)
		depthColorMatrix.set(0, smallerSize-1, -2);
		
	}
	
	
	
	
	public Matrix getMatrix(){
		return depthColorMatrix;
	}
	
	public static void printMatrix(){
		System.out.println();
		System.out.println();
		System.out.println("Depth matrix, to color:");
		
		int a = depthColorMatrix.rows();
		
		int i,j;
		for(i=0;i<a;i++){
			for(j=0;j<a;j++){
				System.out.print((int)depthColorMatrix.get(i, j)+" ");
			}
		System.out.println();
		}
		
		System.out.println();
		System.out.println();
	}
	
	
	
}




