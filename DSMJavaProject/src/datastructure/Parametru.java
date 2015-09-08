package datastructure;

import java.util.ArrayList;

public class Parametru extends Entitate{

	
	private int totalDependencies;
	private String declaredType;
	
	public Parametru(String ID, String name, Parent parent, String declaredType) {
		super(ID, name, parent);
		this.declaredType=declaredType;
		totalDependencies = 0;
	}
	
	public int getTotalDependencies() {
		return totalDependencies;
	}

	@Override
	public int usesStreght(Entitate used) {
		return 0;
	} 
	
}
