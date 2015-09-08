package datastructure;

import java.util.ArrayList;

public class PachetTwin extends Pachet {

	
	private Pachet originalTwin;
	
	
	public PachetTwin(String ID, String name, Parent parent, String fullName, String path, Pachet originalTwin) {
		super(ID, name, parent,fullName,path);
		this.originalTwin = originalTwin;
		
	}
	  
	
	public Pachet getOriginalTwin(){
		return originalTwin;
	}
	
	
	public void addChild(Entitate kid){
		//adding the entity as child for pachet
		super.addChild(kid);
		//adding same method to originalTwin
		originalTwin.addChild(kid, this);
	}
	
	
	public Parent getParent(){
		return originalTwin;
	}
	
	
}
