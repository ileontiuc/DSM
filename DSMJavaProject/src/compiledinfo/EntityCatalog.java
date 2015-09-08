package compiledinfo;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import datastructure.Clasa;
import datastructure.Entitate;
import datastructure.Pachet;

public class EntityCatalog {

	private static Map<String, Entitate> entities;
	private static int maxTreeDepth =0;
	private static int innerClassesDeleted=0;
	
	private EntityCatalog(){}
	
	public static Map<String, Entitate> getEntitiesCatalog(){
		if(entities == null){
			entities = new HashMap<String, Entitate>();
		}
		return entities;	
	}

	public static Pachet sameNameForPackage(String fullName) {
		
		//iterate thru map; if package test fullName
		Entitate current;
		for(String key: entities.keySet()){
			current = entities.get(key);
			if(current instanceof Pachet){
				if( ((Pachet) current).getFullName().equals(fullName) ){
					return (Pachet) current;
				}
					
			}
		}
		return null;
	}
	
	
	public static void calculateMaxTreeDepth(){
		
		//System.out.println("FROM entity Catalog: depths");
		
		Entitate current;
		int currentDepth;
		for(String key: entities.keySet()){
			current = entities.get(key);
			currentDepth = current.getTreeDepth();
			//System.out.println(current.getName()+"      depth: "+ current.getTreeDepth());
			if(currentDepth > maxTreeDepth){
				maxTreeDepth = currentDepth;
				
			}
		}
	}
	
	public static int getMaxTreeDepth(){
		return maxTreeDepth;
	}
	
	
public static Pachet parentOfPackage(String[] entitySubItem) {
		String pathToBeFoundAsParent="";
		for(int i=1;i<entitySubItem.length-1;i++){
			pathToBeFoundAsParent +=(entitySubItem[i]+"::");
		}
		if(pathToBeFoundAsParent.length()-2 <1)
			return null;
		
		pathToBeFoundAsParent = pathToBeFoundAsParent.substring(0, pathToBeFoundAsParent.length()-2);
		for(String key: entities.keySet()){
			Entitate current = entities.get(key);
			if(current instanceof Pachet){
				if( ((Pachet) current).getFullName().equals(pathToBeFoundAsParent) ){
					return (Pachet) current;
				}
			}
		}
		return null;
	}
	
	
	public static void eliminateInnerClasses(PrintWriter outClass){
		
		outClass.println("Inner classes pe care le-am sters");
		
		String container;
		Entitate current;
		for(String key: entities.keySet()){
			current = entities.get(key);
			if(current instanceof Clasa){
					container = ((Clasa) current).getContainer();
					if(entities.containsKey(container) && entities.get(container) instanceof Clasa){
						innerClassesDeleted++;
						outClass.println("ID: "+current.getID()+"  nume "+current.getName()+"  container: "+((Clasa) current).getContainer());
						entities.remove(current);
					}
			}
		}
		System.out.println("No of inner classes deleted: "+innerClassesDeleted);
		outClass.println("No of inner classes deleted: "+innerClassesDeleted);
	
	}
}


/*
* to get the entities catalog:
* EntityMap entities = EntityMap.getEntitiesCatalog();
*/