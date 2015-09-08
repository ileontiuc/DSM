package datastructure;

public interface Parent {
	public void addChild(Entitate kid);
}

/*
 * provide a common reference type for packages who are 
 * type Entitate but their parent is type root (not Entitate)
 * in order that the method getParent() works
 */
