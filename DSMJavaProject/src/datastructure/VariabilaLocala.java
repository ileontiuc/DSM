package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariabilaLocala extends Entitate implements Using {

	
	private Map using = new HashMap<Entitate,Integer>();
	private int totalDependencies;
	private String declaredType;
	
	public VariabilaLocala(String ID, String name, Parent parent, String declaredType) {
		super(ID, name, parent);
		this.declaredType=declaredType;
		totalDependencies = 0;
	}

	
	
	public int getTotalDependencies() {
		return totalDependencies;
	} 
	
	public int usesStreght(Entitate used){
		
		if( using.containsKey(used) ){
			return (Integer) using.get(used);   
		}
		
	   return 0;
		
		
		
	}
	
	public void addDependency(Entitate expectingInputFrom){
		int x,i;
		ArrayList<Entitate> ancestors = expectingInputFrom.getAncestors();
		Entitate affected;
		
		//add direct dependency
		if( using.containsKey(expectingInputFrom) ){
			x = (Integer) using.get(expectingInputFrom);
			x++;
			using.put(expectingInputFrom,  x );
		}else{
			using.put(expectingInputFrom, 1);
			if(expectingInputFrom instanceof Clasa ){
				//TODO are cum var loc sa depinda direct de o clasa??????? analizat situatiile
				Pachet parentPackage=(Pachet) this.getParent();
				parentPackage.addFanOUTPossibleElement(expectingInputFrom);
			}
		}
		
		//there is now a depency related to all ancestors of expectingInputFrom
		for(i=0;i<ancestors.size();i++){
			affected = ancestors.get(i);
			if( using.containsKey(affected) ){
				x = (Integer) using.get(affected);
				x++;
				using.put(affected,  x );
			}else{
				using.put(affected, 1);
				if(affected instanceof Pachet){
					((Pachet) affected).addFanINPossibleElement(this);
					 //adaug doar o singura data dependenta fata de acel pachet; 
					//selectia se face in metoda din pachet dat aici se "scurteaza" sigur doar aici o sa apara ceva nou
				}
			}
		}
		totalDependencies++;
	}
	
	
}
