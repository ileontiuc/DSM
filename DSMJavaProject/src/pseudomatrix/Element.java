package pseudomatrix;


public class Element {

	private Object value;
	private int rowKey;
	private int colKey;
	
	public Element(Object value, int r, int c){
		this.value = value;
		rowKey = r;
		colKey = c;
	}
	
	public int getRow(){
		return rowKey;
	}
	
	public int getCol(){
		return colKey;
	}
	
	public Object getValue(){
		return value;
	}
	
	public void changeValue(Object value){
		this.value = value;
	}
	
}
