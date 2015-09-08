package datastructure;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import algorithms.InstabilityFactor;

public class Pachet extends WithChildren {

	private String path, fullName;
	private ArrayList<Entitate> twins = new ArrayList<Entitate>();
	private BigDecimal InstabilityValue;
	
	private ArrayList<Entitate> fanOut = new ArrayList<Entitate>();
	private ArrayList<Entitate> fanIn = new ArrayList<Entitate>();
	
	public Pachet(String ID, String name, Parent parent, String fullName, String path) {
		super(ID, name, parent);
		this.path = path;
		this.fullName = fullName;
		InstabilityValue = new BigDecimal(0);
		
	}
	  //copii  clase sau subpachete
	
	
	public void addFanOUTPossibleElement(Entitate e){
		
		if(!fanOut.contains(e))
			fanOut.add(e);
	}
	
	public void addFanINPossibleElement(Entitate e){
		if(!fanIn.contains(e))
			fanIn.add(e);
	}
	
	public void calcInstability(){
		InstabilityValue = InstabilityFactor.calcInstability(fanOut,fanIn);
	}
	
	public BigDecimal getInstabilityFactor(){
		return InstabilityValue;
	}
	
	public String getFullName(){
		return fullName;
	}
	
	
	public String getName(){
		return fullName;
	}
	
	public String getPath(){
		return path;
	}
	

	
	public boolean hasTwins(){
		return twins.isEmpty();
	}
	
	public void connectTwin(Entitate twin){
		twins.add(twin);
	}
	
	public void printTwins(){
		int size,i;
		size = twins.size();
		
		if(size == 0){
			System.out.println("This package: "+ this.getName()+"  has no twins (aka package with different path and ID but same combination of subpackets)");
		}else{
			System.out.println("THIS IS:  "+ this.getName());
			for(i=0;i<size;i++)
				System.out.println(twins.get(i));
		}
	}
	
	
	public void addChild(Entitate kid, PachetTwin twin){ //TODO kid poate sa fie doar metoda?? asa cred
		//adding a method from a twin. if it is different i add it normaly. if it`s a duplicate then i set the flag for the entity
		addChild(kid);
		kid.setTwinFlagOrigin();
	
	}
	
	
}
