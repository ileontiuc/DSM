package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clasa extends WithChildren implements Using{
	
	private Map using = new HashMap<Entitate,Integer>();
	
	private String container;
	
	
	public Clasa(String ID, String name, Parent parent, String container ) {
		super(ID, name,parent);
		this.container = container;
	}

	public String getContainer(){
		return container;
	}
	
	
	public int usesStreght(Entitate used){
		int sum=0;
		if( using.containsKey(used) ){
			sum= (Integer) using.get(used);    //suprascris metoda caz special
		}
		sum += super.usesStreght(used);
	   return sum;
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
			//System.out.println("am adaugat dep la: "+ this.getName() + " fata de   "+ expectingInputFrom.getName() + "  cu val x= "+ x);
			
			
			
		}else{
			using.put(expectingInputFrom, 1);
			//System.out.println("am adaugat dep la: "+ this.getName() + " fata de   "+ expectingInputFrom.getName()+"  cu val 1 prima data");
			
			if(expectingInputFrom instanceof Clasa ){
				
				/*
				Entitate rezerva = this;
				//merg in sus pana la primul pachet SAU ma opresc daca am ajung in root si avem o entitate afara
				while(!(rezerva.getParent() instanceof Pachet) || ( rezerva.getParent() instanceof Root)){
					rezerva = (Entitate) rezerva.getParent();
				}*/
				
				//TODO doar pt cazul in care toate clasele se alfa in pachet si in pachet NU exista subpachete si clase pe langa ele. Atunci trebuie memorat de la ce clase imi adaug dependente ca sa nu le adug de 2 ori din subpachete diferite. ar trebui sa fie ok asa pt sisteme...
				//caz particular cand am toate clase intr-un pachet fara subpachete., e xact ce o sa fie la sisteme (unde impart cu loppata :P)
				//ex; cazul de inheritance cand o clasa depinde de alta clasa
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
				//System.out.println("am adaugat dep la: "+ this.getName() + " fata de   "+ affected.getName() + "  cu val x= "+ x);
				
			}else{
				using.put(affected, 1);
				//System.out.println("am adaugat dep la: "+ this.getName() + " fata de   "+ affected.getName()+"  cu val 1 prima data");
				if(affected instanceof Pachet){
					((Pachet) affected).addFanINPossibleElement(this);
					 //adaug doar o singura data dependenta fata de acel pachet,
					//selectia se face in metoda din pachet dat aici se "scurteaza" sigur doar aici o sa apara ceva nou
					
				}
			}
		}
		totalDependencies++;
		//System.out.println(expectingInputFrom.toString());
	}

	
}
