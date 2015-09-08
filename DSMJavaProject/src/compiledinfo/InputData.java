package compiledinfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.SplitPaneDSM;
import datastructure.*;

public class InputData {
	
	private static Parent treeRoot;
	private static Parent twinParent;
	private static Map<String, Entitate> entities ;
	private static Map<String,String> entitiesInFile;  //ID-tip 
	private static int contorSubPachetMatches=0, contorRootPachetMatches=0, innerClass=0;
	private static int attributesWithHasClassScopeTrueAndPArentInSystem=0;
	private static int variabilaLocalaAdaugataCuParinteMetodaDinSistem=0;
	private static int parametruAdaugataCuParinteMetodaDinSistem=0;
	private static PrintWriter outClass;
	
	
	private static String caleProblemFiles ;
    private static String caleSkipFiles ;
    
	
	public InputData(String fileName,String problemFilesPath, String skipFilesPath) throws IOException{
		this.caleProblemFiles =problemFilesPath;
	    this.caleSkipFiles = skipFilesPath ;
		treeRoot = Root.getRoot();
		twinParent = TwinParent.getTwinParent();
		entities = EntityCatalog.getEntitiesCatalog();
		processInputFile(fileName);
	}
	
	 public static String read(String fileName) throws IOException {
		    StringBuffer sb = new StringBuffer();
		    BufferedReader in = new BufferedReader(new FileReader(fileName));
		    String s;
		    
		    while((s = in.readLine()) != null) {
		      sb.append(s);
		      sb.append("\n");
		    }
		    
		    in.close();
		    return sb.toString();
		  }
		  
		  
		 
	
	 public static void processEntities(String fileName) throws IOException{
	 
		 	//lista de fisiere cu diverse probleme/situatii exceptie?
		 
		 	String output4 = caleSkipFiles+"/SkipPachet.txt";
		    PrintWriter outPachet = new PrintWriter(new BufferedWriter(new FileWriter(output4)));
		    outPachet.println("");

		 
		 	String output1 = caleSkipFiles+"/SkipClase.txt";
		    outClass = new PrintWriter(new BufferedWriter(new FileWriter(output1)));
		    outClass.println("clase care nu sunt din sistem si le-am sarit(isStub: true)");

		    String output2 = caleSkipFiles+"/SkipMetode.txt";
		    PrintWriter outMethod = new PrintWriter(new BufferedWriter(new FileWriter(output2)));
		    outMethod.println("metode care nu sunt din sistem si le-am sarit (nu au metrici)");

		    String output3 = caleProblemFiles + "/NullMetode.txt";
		    PrintWriter nullMethod = new PrintWriter(new BufferedWriter(new FileWriter(output3)));
		    nullMethod.println("metode la care am avut problema sa le introduc in arbore; metoda fara parinte(am sarit-o )");

		    String output5 = caleSkipFiles+"/SkipAttribute.txt";
		    PrintWriter skipAttribute = new PrintWriter(new BufferedWriter(new FileWriter(output5)));
		    skipAttribute.println("atribute la care parintele nu este in sistem");
		    
		   
		    String output6 = caleProblemFiles +"/hasClassScopeAttribute.txt";
		    PrintWriter  hasClassScopeAttribute = new PrintWriter(new BufferedWriter(new FileWriter(output6)));
		    hasClassScopeAttribute.println("are hasClassScope != true si parintele este in sistem");
		    
		    
		    String output7 = caleSkipFiles+"/SkipLocalVariable.txt";
		    PrintWriter skipLocalVariable = new PrintWriter(new BufferedWriter(new FileWriter(output7)));
		    skipLocalVariable.println("localvariable la care parintele nu este in sistem");
		    
		    String output8 = caleSkipFiles+"/SkipParameter.txt";
		    PrintWriter skipParameter = new PrintWriter(new BufferedWriter(new FileWriter(output8)));
		    skipParameter.println("parameter la care parintele nu este in sistem");
		    
		    
		  
			String input = read(fileName); // argouml-0.34.mse
			String[] splitArray = input.split("FAMIX");

			int i=1;
			
			
			
		    while(splitArray.length > i){  //!splitArray[i].equals(null)
		    	
		    	 String[] item= splitArray[i].split("\\s+");
		 	     String name = "FAMIX"+item[0];
		 	     
		 	     switch(name){
		 	    
		 	    case "FAMIX.Package": 
		 	    	compilePackageInfo(splitArray[i],outPachet); break;
		 	     
		 	    case "FAMIX.Class": 
		 		     compileClassInfo(splitArray[i],outClass); break;     
		     	     
		 	    case "FAMIX.ParameterizableClass": 
		 		     compileClassInfo(splitArray[i],outClass); break;  
		     	     
		 	    case "FAMIX.Method":
		 	    	 compileMethodInfo(splitArray[i],outMethod,nullMethod); break;  
		 	    	 
		 	    case "FAMIX.Attribute": 
		 		   	 compileAttributeInfo(splitArray[i],skipAttribute, hasClassScopeAttribute);break;  
		 		   	 
		 	    case "FAMIX.LocalVariable": 
		 	    	 compileLocalVariableInfo(splitArray[i],skipLocalVariable); break;	
		 	    	 
		 	    case "FAMIX.Parameter": 
		 	    	compileParameterInfo(splitArray[i],skipParameter); break;	
		 		     
		 	     default: break;
		 	     }
		 	     
		 	     i++;
		    } 

		    System.out.println("Numar de pachete care s-au sublcasat:  "+contorSubPachetMatches);
		    System.out.println("Numar de pachete care s-au creat direct din root:  "+contorRootPachetMatches);
		   
		   // System.out.println("attributesWithHasClassScopeTrueAndPArentInSystem   "+attributesWithHasClassScopeTrueAndPArentInSystem);
		    //System.out.println("variabilaLocalaAdaugataCuParinteMetodaDinSistem  "+variabilaLocalaAdaugataCuParinteMetodaDinSistem);
		   // System.out.println("parametruAdaugataCuParinteMetodaDinSistem  "+parametruAdaugataCuParinteMetodaDinSistem);
		    
		    outMethod.close();
		    nullMethod.close();
		    outPachet.close();
		    skipAttribute.close();
		    hasClassScopeAttribute.close();
		    skipLocalVariable.close();
		    skipParameter.close();
	 }
	 
	 
	
	

	public static void ProcessRelationships(String fileName) throws IOException{
		 
		 
		 	String output4 = caleProblemFiles+"/NullInheritance.txt";
		    PrintWriter nullInheritance = new PrintWriter(new BufferedWriter(new FileWriter(output4)));
		    nullInheritance.println("Am elemente null si am sarit sa introduc relatia de inheritance: ");
		    
		    String output5 = caleProblemFiles+"/NullAccess.txt";
		    PrintWriter nullAccess = new PrintWriter(new BufferedWriter(new FileWriter(output5)));
		    nullAccess.println("Am elemente null si am sarit sa introduc relatia de access: ");

		    
		    String output6 = caleProblemFiles+"/NullInvocation.txt";
		    PrintWriter nullInvocation = new PrintWriter(new BufferedWriter(new FileWriter(output6)));
		    nullInvocation.println("Am elemente null si am sarit sa introduc relatia de invocation: ");



			String input = read(fileName); // argouml-0.34.mse
			String[] splitArray = input.split("FAMIX");
			int i=1;
		    while(splitArray.length > i){  //!splitArray[i].equals(null)
		    	
		    	 String[] item= splitArray[i].split("\\s+");
		 	     String name = "FAMIX"+item[0];
		 	     
		 	     switch(name){
		 	    
		 	     case "FAMIX.Inheritance": 
		 	    	compileInheritanceInfo(splitArray[i],nullInheritance); break; 	
			   		 
		 	     case "FAMIX.Access": 
		 	    	compileAccessInfo(splitArray[i],nullAccess); break;	
			   		 
			     case "FAMIX.Invocation": 
			    	compileInvocationInfo(splitArray[i],nullInvocation);  break;		
		 	    	
		 	     default: break;
		 	     }
		 	     i++;
		    } 
		    
		    nullInheritance.close();
		    nullAccess.close();
		    nullInvocation.close();
	 }
	 
	
	public static void processInputFile(String fileName) throws IOException{
		
		createGeneralEntitiesMap(fileName);
		
		processEntities( fileName);
		EntityCatalog.calculateMaxTreeDepth();
		Root.rearangePackages();
		
		EntityCatalog.eliminateInnerClasses(outClass);
		 outClass.close();
		 
		ProcessRelationships( fileName);
		
		//calculate instabilityfactor since all the required data has been gathered
		int noChildrenInRoot = Root.getRoot().getNoChildren();
		for(int i=0;i<noChildrenInRoot;i++){
			((Pachet) Root.getRoot().getChildAtIndex(i)).calcInstability();
		}
		
		
		/*
		 
		 	    case "FAMIX.JavaSourceLanguage": 
		 	     	 break;
		 	    	 	     
		 	    case "FAMIX.Namespace": 
				   		 break;
		 	     
		 	     	 
		 	     case "FAMIX.Module": 
				   		 break;	 
			 	     	 
			 	 case "FAMIX.FileAnchor": 
					   	 break;	
		 	     
				   		 
		 	     case "FAMIX.PrimitiveType": 
				   		 break;	
		 	    	
				   		 
		 	     case "FAMIX.ParameterType": 
				   		 break;	
				   		 
		 	     case "FAMIX.ParameterizedType": 
				   		 break;	
			 	     	 
				   		 
				case "FAMIX.AnnotationType": 
				   		 break;		    	
							   		 
				case "FAMIX.Enum": 
				   		 break;	
				   		 
				   		 
				case "FAMIX.EnumValue": 
				   		 break;	
				   		 
				case "FAMIX.CaughtException": 
				   		 break;	
				   		 
				case "FAMIX.DeclaredException": 
				   		 break;	
				   		 
				case "FAMIX.ThrownException": 
				   		 break;	
				   		 
				   		 
				case "FAMIX.AnnotationInstance": 
				   		 break;	
				   		 
				case "FAMIX.AnnotationInstanceAttribute": 
				   		 break;		
		*/
	}
	
	private static void createGeneralEntitiesMap(String fileName) throws IOException {
		
		// all entities from file, fileanchor and others have duplicate id
		String input = read(fileName); // argouml-0.34.mse
		String[] splitArray = input.split("FAMIX");
		entitiesInFile = new HashMap<String, String>();  //ID-tip 
		String type, ID;
		
		int i=1;
	    while(splitArray.length > i){  //!splitArray[i].equals(null)
	    	
	    	 String[] item= splitArray[i].split("\\s+");
	 	     String name = "FAMIX"+item[0];
	 	     String[] entityItem = splitArray[i].split("\\s+");
	 	     
	 	     switch(name){
	 	    
	 	    case "FAMIX.Package": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Package";
	 	    	entitiesInFile.put(ID, type); // key value
	 	    	 break;
	 	     
	 	    case "FAMIX.Class": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Class";
	 	    	entitiesInFile.put(ID, type); // key value
	 		      break;     
	     	     
	 	    case "FAMIX.ParameterizableClass":
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "ParameterizableClass";
	 	    	entitiesInFile.put(ID, type); // key value
	 		      break;  
	     	     
	 	    case "FAMIX.Method":
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Method";
	 	    	entitiesInFile.put(ID, type); // key value
	 	    	  break;  
	 	    	 
	 	    case "FAMIX.Attribute": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Attribute";
	 	    	entitiesInFile.put(ID, type); // key value
	 	    	  break;    
	 		   	 
	 	     case "FAMIX.LocalVariable": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "LocalVariable";
	 	    	entitiesInFile.put(ID, type); // key value
	 	    	  break;   
	 		   
	 	   
	 	    	 	     
	 	    case "FAMIX.Namespace": 
	 	    	  break;   
	 	     	 
	 	     case "FAMIX.Module": 
			   		 break;	 
		 	     	 
		 	 case "FAMIX.FileAnchor": 
				   	 break;	
				   		 
			   		 
	 	     case "FAMIX.PrimitiveType":
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "PrimitiveType";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
		 	     	 
	 	    	
			   		 
	 	     case "FAMIX.ParameterType": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "ParameterType";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
			   		 
			   		 
	 	     case "FAMIX.ParameterizedType": 
	 	    	ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "ParameterizedType";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
		 	     	 
	 	     
			   		 
			case "FAMIX.AnnotationType": 
			   		 break;		    	
						   		 
			case "FAMIX.Enum": 
				ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Enum";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
			   		 
				
			   		 
			case "FAMIX.EnumValue":
				ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "EnumValue";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
			   		 
			
			   		 
			case "FAMIX.CaughtException": 
			   		 break;	
			   		 
			case "FAMIX.DeclaredException": 
			   		 break;	
			   		 
			case "FAMIX.ThrownException":
			   		 break;	
			   		 
			   		 
			   		 
			case "FAMIX.Parameter": 
				ID = entityItem[2].substring(0, entityItem[2].length()-1 );
	 	    	type = "Parameter";
	 	    	entitiesInFile.put(ID, type); // key value
			   		 break;	
			   		 
			
			   		 
			case "FAMIX.AnnotationInstance": 
			   		 break;	
			   		 
			case "FAMIX.AnnotationInstanceAttribute":
			   		 break;			 	
						   		 
			case "FAMIX.Access": 
			   		 break;	
			   		 
			case "FAMIX.Invocation": 
			   		 break;	
			   		 
			 case "FAMIX.Inheritance": 
				 break;	
			   		 
		 	     
	 	     default: break;
	 	     }
	 	     
	 	     i++;
	    } 
		
	}

private static void compileInvocationInfo(String info, PrintWriter nullInvocation){
		
		String senderID, candidatesID;
		 
		 Using sender;
		 Entitate candidates;
		
		String[] item;
		item = info.split("\\s+");
		  
		senderID=item[3].substring(0, item[3].length()-2 );
		candidatesID = item[6].substring(0, item[6].length()-2 );
		
		  
		sender = (Using) entities.get(senderID);  
		candidates = entities.get(candidatesID);
		  
		  if(sender==null || candidates == null){
			  nullInvocation.println();
			  nullInvocation.println("sender: "+sender+"  candidates: "+candidates);
			  nullInvocation.println(info);
		  }else{
			  sender.addDependency(candidates);
			  }
	}
	
	private static void compileAccessInfo(String info, PrintWriter nullAccess){
		
		String variableID, accessorID, receiverID="n/a";
		 
		 Using accessor;
		 Entitate variable;
		
		String[] item;
		item = info.split("\\s+");
		  
		variableID=item[3].substring(0, item[3].length()-2 );
		accessorID = item[6].substring(0, item[6].length()-2 );
		if(item[7].substring(0, item[7].length()-2 ).equals("receiver")){
			receiverID = item[9].substring(0, item[9].length()-2 );
		}
		  
		   accessor = (Using) entities.get(accessorID);  
		   variable = entities.get(variableID);
		  
		  if(accessor==null || variable == null){
			  nullAccess.println();
			  nullAccess.println("accesor: "+accessor+"  variable: "+variable);
			  nullAccess.println(info);
		  }else{
			// System.out.println("adaug dependenta subclass :  "+subclass.getName() + "   la super: "+ superclass.getName());
			  accessor.addDependency(variable);
			  }
	}
	
	
	
	private static void compileInheritanceInfo(String info, PrintWriter nullInheritance){
		
		String inheritanceID, subclassID, superclassID;
		Using subclass; 
		Entitate superclass;
		String[] item;
		item = info.split("\\s+");
		  
		  inheritanceID=item[2].substring(0, item[2].length()-1 );
		  subclassID = item[5].substring(0, item[5].length()-2 );
		  superclassID = item[8].substring(0, item[8].length()-2 );
		  
		  subclass = (Using) entities.get(subclassID);  
		  superclass = entities.get(superclassID);
		  
		  if(subclass==null || superclass == null){
			  nullInheritance.println();
			  nullInheritance.println("subclass: "+subclass+"  superclass: "+superclass);
			  nullInheritance.println(info);
		  }else{
			  subclass.addDependency(superclass);
			  }
	}
	
	
	
	
	private static void compileParameterInfo(String info,PrintWriter skipParameter) {
		
		String[] entityItem;
	 	String[] entitySubItem;
		String ID,name, declaredType, parentBehaviouralEntity;
		entityItem = info.split("\\s+");
		
		ID = entityItem[2].substring(0, entityItem[2].length()-1 );
		name = entityItem[4].substring(1, entityItem[4].length()-2 );
		parentBehaviouralEntity = entityItem[7].substring(0, entityItem[7].length()-2 );
		declaredType = entityItem[10].substring(0, entityItem[10].length()-2 );
		
		
		//System.out.println("  "+declaredType+"   "+parentBehaviouralEntity);
		//System.out.println(info);
		
		if(entities.containsKey(parentBehaviouralEntity)){
				//am parintele in sistem
				if(entities.get(parentBehaviouralEntity) instanceof Metoda){
						Parametru currentParametru = new Parametru(ID,name, (Parent) entities.get(parentBehaviouralEntity) ,declaredType);
						parametruAdaugataCuParinteMetodaDinSistem++;
					
				}else{
					// am atribute ce au parinte altceva 
					System.out.println("PARINTELE PARAMETRULUI este in sistem DAR NU este metoda");
					System.out.println(info);
				}
			
			
				
		}else{
			//clasa parinte nu exista in sistem (dar e in fisier)
			skipParameter.println("parent is: "+entitiesInFile.get(parentBehaviouralEntity));
			skipParameter.println(info);
			skipParameter.println();
		}
		
	}
	
	
	private static void compileLocalVariableInfo(String info,PrintWriter skipLocalVariable) {
		
		String[] entityItem;
	 	String[] entitySubItem;
		String ID,name, declaredType, parentBehaviouralEntity;
		entityItem = info.split("\\s+");
		
		ID = entityItem[2].substring(0, entityItem[2].length()-1 );
		name = entityItem[4].substring(1, entityItem[4].length()-2 );
		declaredType = entityItem[7].substring(0, entityItem[7].length()-2 );
		parentBehaviouralEntity = entityItem[10].substring(0, entityItem[10].length()-2 );
		
		
		//System.out.println("  "+declaredType+"   "+parentBehaviouralEntity);
		//System.out.println(info);
		
		if(entities.containsKey(parentBehaviouralEntity)){
				//am parintele in sistem
				if(entities.get(parentBehaviouralEntity) instanceof Metoda){
						VariabilaLocala currentLocalVariable = new VariabilaLocala(ID,name, (Parent) entities.get(parentBehaviouralEntity)  ,declaredType);
						variabilaLocalaAdaugataCuParinteMetodaDinSistem++;
					
				}else{
					// am atribute ce au parinte altceva 
					System.out.println("PARINTELE VARIABILEI LOCALE este in sistem DAR NU este metoda");
					System.out.println(info);
				}
			
			
				
		}else{
			//clasa parinte nu exista in sistem (dar e in fisier)
			skipLocalVariable.println("parent is: "+entitiesInFile.get(parentBehaviouralEntity));
			skipLocalVariable.println(info);
			skipLocalVariable.println();
		}
		
		
		
		
		
	}
	
	 private static void compileAttributeInfo(String info, PrintWriter skipAttribute, PrintWriter hasClassScopeAttribute) {
			
		 	String[] entityItem;
		 	String[] entitySubItem;
			String ID,name, parentType, declaredType, hasClassScope="n/a";
			entityItem = info.split("\\s+");
			
			ID = entityItem[2].substring(0, entityItem[2].length()-1 );
			name = entityItem[4].substring(1, entityItem[4].length()-2 );
			parentType = entityItem[7].substring(0, entityItem[7].length()-2 );
			declaredType = entityItem[10].substring(0, entityItem[10].length()-2 );
			
			if(entityItem[11].substring(1, entityItem[11].length() ).equals("hasClassScope")){
				hasClassScope = entityItem[12].substring(0, entityItem[12].length()-1 );
			}
			
			//System.out.println("  "+entityItem[11].substring(1, entityItem[11].length() )+"   "+hasClassScope);
			
			if(entities.containsKey(parentType)){
					//am parintele in sistem
					if(entities.get(parentType) instanceof Clasa){
							Atribut currentAttribute = new Atribut(ID,name, (Parent) entities.get(parentType)  ,declaredType);
							if(!hasClassScope.equals("true")){
								hasClassScopeAttribute.println(info);
								hasClassScopeAttribute.println();
							}else{
								attributesWithHasClassScopeTrueAndPArentInSystem++;
							}
						
					}else{
						// am atribute ce au parinte altceva 
						System.out.println("PARINTELE ATRIBUTULUI este in sistem DAR NU este clasa");
						System.out.println(info);
					}
				
				
					
			}else{
				//clasa parinte nu exista in sistem (dar e in fisier)
				skipAttribute.println("atributul de mai jos are hasClassScope: "+hasClassScope+"   parintele este: "+entitiesInFile.get(parentType));
				skipAttribute.println(info);
				skipAttribute.println();
			}
		 
		}

	
	private static void compileMethodInfo(String info, PrintWriter outMethod, PrintWriter nullMethod) {
		Metoda currentMethod;
		 
	    String[] entityItem;
	 	String[] entitySubItem;
		String ID, parentClassID , declaredTypeClassID, name, delimitator;
		entityItem = info.split("\\s+");
		
		ID = entityItem[2].substring(0, entityItem[2].length()-1 );
		parentClassID = entityItem[8].substring(0, entityItem[8].length()-2 );
		declaredTypeClassID = entityItem[11].substring(0, entityItem[11].length()-2 );
		name = entityItem[13].substring(1, entityItem[13].length()-2 );
		
		if( hasMethodMetrics(info) ){  
			if(entities.get(parentClassID) == null){
				//no parent class?
				nullMethod.println("nu are parent type class");
				nullMethod.println(info);
			}else{
				currentMethod = new Metoda(ID,name, (Parent) entities.get(parentClassID),
						declaredTypeClassID);
				entities.put(ID, currentMethod);
			}
			
		}else{
			//metode care nu sunt din sistem nu au metrici
			outMethod.println( info);
		}
		
		
	}

	private static boolean hasMethodMetrics(String info) {
		
		if(info.contains("(OCIO")  || info.contains("(OCDO") || info.contains("(NOPAR")
				|| info.contains("(NOOC") || info.contains("(NOAV") || info.contains("(MAXNESTING")
				|| info.contains("(LDA") || info.contains("(LOCOMM") || info.contains("(LOC")
				|| info.contains("(ICIO") || info.contains("(ICDO") || info.contains("(DR")
				|| info.contains("(CYCLO") || info.contains("(ALD") || info.contains("(ATFD")){
			return true;
		}
		return false;
	}

	private static void compileClassInfo(String info, PrintWriter outClass) {
		Clasa currentClass;
	 
	    String[] entityItem;
	 	String[] entitySubItem;
		String ID, parentPackageID , name, delimitator, container;
		entityItem = info.split("\\s+");
		
		ID = entityItem[2].substring(0, entityItem[2].length()-1 );
		delimitator =  entityItem[6];
		
		if(delimitator.equals("(parentPackage")){  //adica nu are valoare (isStub 
			parentPackageID = entityItem[8].substring(0, entityItem[8].length()-2);
			name = entityItem[13].substring(1, entityItem[13].length()-2);
			container = entityItem[11].substring(0, entityItem[11].length()-2);
					
			currentClass = new Clasa(ID,name, (Parent) entities.get(parentPackageID), container );
			entities.put(ID, currentClass);
		}else{
			//clase care nu sunt din sistem (isStub: true)
			outClass.println(info );			 
		}
		
		
	}

	public static void compilePackageInfo(String splitArray, PrintWriter outPachet){
		
		Pachet currentPackage;
	    int  noSubPackages;
	    String[] entityItem;
	 	String[] entitySubItem;
		String packageID, packageFullPathName, pathName, fullName, packageName;
		entityItem = splitArray.split("\\s+");
		Pachet originalTwin;
		
		packageID=entityItem[2].substring(0, entityItem[2].length()-1 );
		packageFullPathName = entityItem[4].substring(1, entityItem[4].length()-2 );
		 
		
		
		//elimin pachetele care nu sunt din sistem
		if(!packageFullPathName.equals("_unknown_package_") ){
			entitySubItem =  packageFullPathName.split("::");
			pathName = entitySubItem[0];
			
			fullName = packageFullPathName.substring( pathName.length()+2 , 
													  packageFullPathName.length() ); 
			//fullName dupa primele ::  
			noSubPackages = entitySubItem.length;
			packageName = entitySubItem[noSubPackages-1];
			
			//fullName = fullName.replaceAll("::", ".");// sa fie ca si in Java  //TODO vazut daca influenteaza modificarea in .
			
			if((originalTwin = EntityCatalog.sameNameForPackage(fullName)) != null){
				//create and connect as  twin
				currentPackage = new PachetTwin(packageID,packageName, twinParent, fullName ,pathName, originalTwin);
				entities.put(packageID, currentPackage);  
				originalTwin.connectTwin(currentPackage);
				
				
			}else{
				//pachetele care au parinte deja existent cu id se subclaseaza
				Pachet parentPackage = EntityCatalog.parentOfPackage(entitySubItem);
				if(parentPackage == null){
					//create package object
					contorRootPachetMatches++;
					currentPackage = new Pachet(packageID,packageName, treeRoot, fullName ,pathName);	 
				}else{
					contorSubPachetMatches++;
					currentPackage = new Pachet(packageID,packageName, (Parent) parentPackage, fullName ,pathName);
				}
			}
			
			// add new entity to global map
			entities.put(packageID, currentPackage);  
			
		}else{
			outPachet.println("am eliminat   "+ splitArray);
		}
		
	}

}
