package cellRenderers;

import java.awt.Color;

public class Colors {

	private static Color[] colors = {new Color(0xFFFFFF), new Color(0xCFD0EC), new Color(0xD6C3B2),new Color(0xB5B7CC),new Color(0xCAB2C0),new Color(0xB1CBBE)};  
	
	
	public static Color clickedColor = new Color(0xFFFFD0);
	public static Color clickedSelectedColor = new Color(0xDCDCB6);
	public static Color instabilityFactorColor = new Color(0xF25383);
	public static Color cycleDependencyTextColor = new Color(0x188464);
	public static Color cycleDependencyBackgroundColor = new Color(0x7BD5BA);
	
	public static Color getColor4Depth(int depth){
		return colors[depth%6]; 
	}
	
	
	
	
	
	public static Color getMainGridColor(){
		return new Color(185,185,185); //gri deschis
	}
	
}




/*******************************************************************************
interpolare liniara pe culori

private Color maxColor = new Color(0xE5DF3B);   //yellow   pink=0x872657  
private Color minColor = new Color(0xFFFFFF);

int maxTreeDepth = EntityCatalog.getMaxTreeDepth();
			int ColCounter = VerticalMatrix.getColCounter();
			int RowCounter = VerticalMatrix.getRowCounter();
			int cellValue = DepthMainMatrix.getDepthFor(row, column);
			
			//System.out.println("##################maxTreeDepth:   "+ maxTreeDepth+"   cellDepth  "+parentOfClickedCellDepth+"   at row: "+row+"   and col "+column);
			
			float p = cellValue/(float)maxTreeDepth;
			
			float r,g,b;
			
			r = maxColor.getRed()*p+minColor.getRed()*(1-p);
			g = maxColor.getGreen()*p+minColor.getGreen()*(1-p);
			b = maxColor.getBlue()*p+minColor.getBlue()*(1-p); 
			
			//System.out.println("valori float:  "+r+"   "+g+"    "+b);
			//System.out.println("valori int:  "+Math.round(r)+"   "+Math.round(g)+"    "+Math.round(b));
			
			Color currentColor = new Color(Math.round(r),Math.round(g),Math.round(b)); 
			



******************************************************************************/