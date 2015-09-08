package compiledinfo;

import java.util.ArrayList;

import datastructure.Entitate;

public class ShowList {

	private static ArrayList<Entitate> printedEntities;
	
	private ShowList(){}
	
	
	public static ArrayList getPrintedEntities(){
		if(printedEntities == null){
			printedEntities = new ArrayList<Entitate>();
		}
		return printedEntities;
	}
	/*
	public static void updatePrintedEntities(Entitate newEntity, int index){
		printedEntities.add(index, newEntity);
	}
	*/
	
	public static void printPrintedEntitiesNames(){
		int size, i;
		size = printedEntities.size();
		if(size>0){
		 for(i=0;i<size;i++)
			System.out.println( printedEntities.get(i).getName() );
		}else
			System.out.println("There are no printed entities at this time");
	}
	
}
