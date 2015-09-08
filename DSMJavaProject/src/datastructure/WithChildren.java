package datastructure;

import java.util.ArrayList;

public class WithChildren extends Entitate implements Parent{

	
	private ArrayList<Entitate> children = new ArrayList<Entitate>();
	protected int totalDependencies;
	
	
	public WithChildren(String ID, String name, Parent parent) {
		super(ID, name, parent);
		totalDependencies = 0;
	}
	
	
	public void addChild(Entitate kid){
		children.add(kid);
		totalDependencies += kid.getTotalDependencies();
	}
	
	
	public ArrayList<Entitate> getChildren(){
		return children;
	}
	
	public Entitate getChildrenAt(int index){
		return children.get(index);
	}
	
	public int noChildren(){
		return children.size();
	}
	
	public void printChildren(){
		int size,i;
		size = children.size();
		System.out.println("THIS IS:  "+ this.getName());
		for(i=0;i<size;i++)
			System.out.println(children.get(i));
		
	}
	
	public int getTotalDependencies() {
		return totalDependencies;
	} 
	
	public int usesStreght(Entitate used){
		int noChildren,currentChildIndex;
		int sum =0; 
		Entitate currentChild;
		noChildren = children.size();
		//System.out.println("this is:  "+ this.getName()+"   with  "+ noChildren+"   kids");
		
		
		for(currentChildIndex = 0; currentChildIndex < noChildren; currentChildIndex++){
			
			currentChild =   children.get(currentChildIndex);
			sum += currentChild.usesStreght(used);
			
		}
		//System.out.println("  with sum   "+sum +"    in relations to "+ used.getName());
		
		return sum;
	}
	
	
	public boolean hasClassFirstChild(){
	
		
		for(Entitate current:children){
			if(current instanceof Clasa){
				//System.out.println("the class as first child is:  "+current.getID());
				return true;
			}
		}
		return false;
		
		
		//return true;
	}
	
	
	public boolean duplicateChild(Entitate possibleDuplicate){
		int contor=0;
		for(Entitate kid:children){
			if(possibleDuplicate.getName().equals(kid.getName())){
				contor++;
			}
		}
		return contor>1;
	}
	
	
}
