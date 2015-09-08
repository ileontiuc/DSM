package datastructure;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import compiledinfo.EntityCatalog;


public abstract class Entitate implements Ancestor {

	private String ID;
	private String name;
	private Parent parent;
	private int treeDepth;
	private Ancestor parentForDepth;
	
	private boolean IS_FROM_TWIN = false;
	
	public void setTwinFlagOrigin(){
		IS_FROM_TWIN = true;  //if flag true then when i set the name i must also add the path
		Pachet visualParent = ((PachetTwin) parent).getOriginalTwin();
		if(visualParent.duplicateChild(this)){
			name = name +" ("+ getParentPath()+")";
		//"############################                      ########################################                            ###########################################################################################";
		}
	}
	
	
	
	//daca am elemente din twin le afisez calea
	public String getParentPath(){
		if(this instanceof PachetTwin){
			return ((Pachet) this).getPath();
		}
		return ((Entitate) parent).getParentPath();
	}
	
	
	public Entitate(String ID, String name, Parent parent){
		
		this.ID = ID;
		this.name = name;
		this.parent  = parent;
		parent.addChild(this);
		this.parentForDepth = (Ancestor) parent;
		treeDepth = getTreeDepth();
	}
	
	public int getTreeDepth(){
		return 1+ parentForDepth.getTreeDepth();
	}
	
	public void setParent(Parent parent){
		this.parent = parent;
	}
	public Parent getParent(){
		return parent;
	}
	
	public WithChildren getNotRootParent(){
		if(parent instanceof Root)
			return null;
		return (WithChildren) parent;
	}
	
	public boolean isRoot(){
		if(parent == null)
			return true;
		else return false;
	}
	
	public ArrayList<Entitate> getAncestors(){
		ArrayList<Entitate> ancestors;
		
		Ancestor parent;
		Entitate realParent;
		//System.out.println("THE parent OF: "+this.getName() );
		
		parent =  (Ancestor) this.getParent();
		
		if(parent.getName().equals("The Singleton ROOT")){
			return new ArrayList<Entitate>();
		}
		
		realParent = (Entitate) parent;
		//System.out.println("THE PARENT IS!: "+ realParent.getName() );
		
		ancestors = realParent.getAncestors();
		ancestors.add(realParent);
		
		return ancestors;
			
	}
	
	
	public Pachet getParentPackage(){
		ArrayList<Entitate> ancestors = getAncestors();
		int i;
		int size = ancestors.size();
		for(i=size-1;i>=0;i--){
			if(ancestors.get(i) instanceof Pachet){
				return (Pachet) ancestors.get(i);
			}
		}
		
		//TODO make it prettier
		/*
		System.out.println("       ->          "+size);
		for(i=0;i<size;i++)
			System.out.print(ancestors.get(i)+"  ");
		System.out.println("!!!!!!!!!! orice entitate trebuie sa aiba un pachet eventually"); se intampla in 3 cazuri
		*/
		return null;
	}
	
	
	
	//possible change parent method ??? (or does that happen in code first?)
	
	public String getName(){
		return name;
	}
	
	public String getID(){
		return ID;
	}
	
	public abstract int getTotalDependencies();
	
	public abstract int usesStreght(Entitate used);


}
