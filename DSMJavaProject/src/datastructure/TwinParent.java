package datastructure;

import java.util.ArrayList;

public class TwinParent implements Parent,Ancestor{

	private static TwinParent twinParent;
	private static ArrayList<Entitate> children = new ArrayList<Entitate>();
	
	private static int IDpackageNoClassChild =0, IDpackageWithClassChild=0,grandchildrenAddedToTheRoot=0 ;
	
	
	private TwinParent(){}
	
	
	public static TwinParent getTwinParent(){
		if(twinParent == null)
			twinParent = new TwinParent();
		return twinParent;
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
		return "The Singleton twin Parent";
	}

	public Entitate getChildAtIndex(int i) {
		return children.get(i);
	}


	@Override
	public int getTreeDepth() {
		return 0;
	}


}
