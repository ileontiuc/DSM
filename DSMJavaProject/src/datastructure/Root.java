package datastructure;

import java.util.ArrayList;

public class Root implements Parent, Ancestor{

	private static Root theRoot;
	private static ArrayList<Entitate> children = new ArrayList<Entitate>();
	
	private static int IDpackageNoClassChild =0, IDpackageWithClassChild=0,grandchildrenAddedToTheRoot=0 ;
	
	
	private Root(){}
	
	public int getTreeDepth(){
		return 0;
	}
	
	public static Root getRoot(){
		if(theRoot == null)
			theRoot = new Root();
		return theRoot;
	}
	
	public void addChild(Entitate kid){
		children.add(kid);
	}
	
	public void printChildren(){
		int size,i;
		size = children.size();
		System.out.println("THIS IS:  "+ this.getName());
		for(i=0;i<size;i++)
			System.out.println(children.get(i).getName());
		
	}
	
	public int getNoChildren(){
		return children.size();
	}
	
	
	public String getName(){
		return "The Singleton ROOT";
	}

	public Entitate getChildAtIndex(int i) {
		return children.get(i);
	}
	
	
	public static void rearangePackages(){
		//root children are only packages that also have at least a class as first children
		
		for(Entitate current: children){
			if(current instanceof Pachet && !((WithChildren) current).hasClassFirstChild()){
				//current has only pachet children
				IDpackageNoClassChild++;
				current.setParent(null);
				rearangeToRoot(current);
			}else{
				IDpackageWithClassChild++;
			}
		}
		
		System.out.println("Pachete cu ID fara copil clasa, eliminate de la root "+IDpackageNoClassChild);
		System.out.println("Pachete initial in root care au ramas in root pentru ca au copil clasa "+IDpackageWithClassChild);
		System.out.println("Numar de subpachete adaugate direct la root "+grandchildrenAddedToTheRoot);
		
	}
	
	public static void rearangeToRoot(Entitate current){
		ArrayList<Entitate> children = ((WithChildren) current).getChildren();
		for(Entitate currentChild:children){
			if( ((WithChildren) current).hasClassFirstChild() ){
				//put this one direct to the root
				grandchildrenAddedToTheRoot++;
				currentChild.setParent(theRoot);
			}else{
				//this also has only subpackages
				rearangeToRoot(currentChild);
			}
		}
	}

}
