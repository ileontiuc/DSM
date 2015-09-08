package algorithms;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import tableModels.VerticalMatrix;
import CodedMatrixInfo.Constants;
import CodedMatrixInfo.EntityVerticalMatrix;
import datastructure.Entitate;
import datastructure.Pachet;

public class InstabilityFactor {

	// orice logica si decizie ce tine de formule de la factorul de instabilitate
	
	public static BigDecimal calcInstability( ArrayList<Entitate> fanOut,  ArrayList<Entitate> fanIn){
		
		BigDecimal InstabilityValue;
		int Ce = 0; //efferent coupling = no of external classes on which the component depends FANOUT; -1=not available
		int Ca = 0; //afferent coupling = no of external classes that depend on this component FANIN ; -1 = not available
		
		Ce = fanOut.size();
		Ca = fanIn.size();
		
		if((Ca+Ce)>0){
			
			
			BigDecimal CE = new BigDecimal(Ce);
			BigDecimal CA = new BigDecimal(Ca);
			
			//System.out.println(Ce+"    "+Ca);
			InstabilityValue = CE.divide(CA.add(CE),MathContext.DECIMAL64);
		}else{
			InstabilityValue = new BigDecimal(0);
		}
		
		return InstabilityValue;
	}
	
	
	public static Pachet getParentFor(Entitate e){
		Pachet echivalentE;
		if(e instanceof Pachet ){
			echivalentE = (Pachet) e;
		}else{
			echivalentE = e.getParentPackage();
		}
		return echivalentE;
	}
	
	
	public static boolean badInstabilityFactorDependency(Entitate linie, Entitate coloana){
		
			// testez dacapachetele parinte (sau chiar el daca e cazul) au diferenta gresita la factorul de instabilitate; 
			//TODO !!!! consider cazul in care primul pachet parinte e cel ok (adica nu exista subpachete, ceea ce se intampla in argouml) !!!!!!!!!!!!!
			
			Pachet echivalentRow, echivalentCol;
			
			echivalentRow = getParentFor(linie);
			echivalentCol = getParentFor(coloana);
			
			
			if(echivalentRow!=null && echivalentCol!=null){
				BigDecimal linieValue = ((Pachet) echivalentRow).getInstabilityFactor();
				BigDecimal coloanaValue = ((Pachet) echivalentCol).getInstabilityFactor();
				if(!linieValue.equals(0) && !coloanaValue.equals(0)){
					
					BigDecimal diferenta = coloanaValue.subtract(linieValue);
					if(  diferenta.compareTo( new BigDecimal(0)) <= 0  &&  diferenta.abs().compareTo( Constants.DELTA) >=0  ){  
						return true;
					}
				}
			}
		
		return false;
	}
}
