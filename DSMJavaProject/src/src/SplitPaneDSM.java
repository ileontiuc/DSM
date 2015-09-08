package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import CodedMatrixInfo.Constants;
import spaningFeature.AttributiveCellTableModel;
import spaningFeature.MultiSpanCellTable;
import tableModels.HorizontalMatrix;
import tableModels.MainMatrix;
import tableModels.VerticalMatrix;
import cellRenderers.ColorDSMRenderer;
import cellRenderers.Colors;
import cellRenderers.VerticalColorCellRenderer;
import cellRenderers.VerticalTextCellRenderer;
import cellRenderers.VerticalTextForHorizontalMatrixCellRenderer;
import compiledinfo.InputData;
import datastructure.Entitate;
 
//SplitPaneDemo itself is not a visible component.
public class SplitPaneDSM extends JPanel {
   
    private static JFrame frame;
    private static JSplitPane downSplitPane, mainSplitPane, upSplitPane;
    
    
	private final static ColorDSMRenderer DSMcell = new ColorDSMRenderer();
	private final static VerticalColorCellRenderer VCell = new VerticalColorCellRenderer();
	private final static VerticalTextForHorizontalMatrixCellRenderer HCell = new VerticalTextForHorizontalMatrixCellRenderer();
	private final static VerticalTextCellRenderer verticalText = new VerticalTextCellRenderer();
	
	 private static MultiSpanCellTable vTable = null, mainTable=null, hTable=null;
	 private JTable emptyTable;
	
	private static int verticalDividerLocation;
	private static int  horizontalDividerPosition;
	
	private JRadioButton expandButton;
	private boolean instabilityFactorFeatureFlag =false;
	 
	
	
	public static MultiSpanCellTable getHTable(){
		return hTable;
	}
	
	
     public SplitPaneDSM() {
    	 
    	 ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
   	  
    	 
    	 //VERTICAL Table   vTable
 	    AttributiveCellTableModel verticalTableModel = new AttributiveCellTableModel(VerticalMatrix.getMatrix()){
 	    	public boolean isCellEditable(int row, int column) {
 	           return false;
 	       }
 	    };
 	   
 	    vTable = new MultiSpanCellTable( verticalTableModel ){
 	    	public TableCellRenderer getCellRenderer(int row, int column) {
 	    		return VCell;
        }};
 	    vTable.addMouseListener(new VerticalMouseAdapter() );
 	    
 	    
 	    //MainDATA Table
 	    
 	    
 	    AttributiveCellTableModel mainTableModel = new AttributiveCellTableModel( MainMatrix.getMainDataTable().getMainMAtrix()){
	    	public boolean isCellEditable(int row, int column) {
	 	           return false;
	 	       }
	 	    };
	 	  
	 	    //text pe centru la dsm
	 	DSMcell.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);      
	 	DSMcell.setHorizontalAlignment( JLabel.CENTER );
	 	
	 	    
 	   mainTable = new MultiSpanCellTable( mainTableModel ){
 		  public TableCellRenderer getCellRenderer(int row, int column) {
              return DSMcell;
          }
 	   };
 	  mainTable.getColumnModel().getColumn(0).setCellRenderer( DSMcell );
 	  mainTable.setGridColor(Colors.getMainGridColor());
 	  
 	   
 	   //Empty corner -> Settings Corner
 	 	//emptyTable = new JTable();
 	 	FeatureMenu featureMenu = new FeatureMenu();
 	 
 	 	 
 	    //HORIZONTAL Table
 	 	AttributiveCellTableModel hTableModel = new AttributiveCellTableModel( new HorizontalMatrix().getHorizontalMatrix(),"horizontal" ){
	    	public boolean isCellEditable(int row, int column) {
	 	           return false;
	 	       }
	 	    };
 	 	 hTable = new MultiSpanCellTable( hTableModel ){
 	 		  public TableCellRenderer getCellRenderer(int row, int column) {
 	              return HCell;
 	          }
 	 	   };
	    hTable.setRowSelectionAllowed(false);
	    hTable.addMouseListener(new HorizontalMouseAdapter() );
 	    
	    
 	 	upSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,featureMenu,hTable);
 	 	upSplitPane.setDividerSize(Constants.SPLITPANE_DIVIDER_SIZE);
 	 	int up_height = (int) (Constants.SPLITPANE_INITIAL_HEIGHT * Constants.SPLITPANE_UP_HEIGHT_PROC);
 	 	upSplitPane.setMinimumSize(new Dimension(Constants.SPLITPANE_INITIAL_WIDTH,up_height ));
 	 	
 	 	downSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,vTable,mainTable);
 	 	downSplitPane.setDividerSize(Constants.SPLITPANE_DIVIDER_SIZE);
 	 	downSplitPane.setMinimumSize(new Dimension(Constants.SPLITPANE_INITIAL_WIDTH, Constants.SPLITPANE_INITIAL_HEIGHT - up_height));
 	 	 
	 	   
 	 
 	mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,upSplitPane,downSplitPane);
   // mainSplitPane.setOneTouchExpandable(true);
    mainSplitPane.setDividerSize(Constants.SPLITPANE_DIVIDER_SIZE);
 	mainSplitPane.setMinimumSize(new Dimension(Constants.SPLITPANE_INITIAL_WIDTH,Constants.SPLITPANE_INITIAL_HEIGHT));
 	mainSplitPane.setDividerLocation(up_height);  //linia de pe orizontala
 	
 	//nu se poate folosi dividerul de pe orizontala 
 	mainSplitPane.setEnabled( false );
 	
    }
     
     
    
    private static void createAndShowGUIforFile(String fileName,String problemFilesPath, String skipFilesPath ) {
 
        //Create and set up the window.
        frame = new JFrame("DSM");
        
        frame.addComponentListener(new ComponentAdapter() 
        {  
                public void componentResized(ComponentEvent evt) {
                    Component c = (Component)evt.getSource();
                    verticalDividerLocation =  (int) (mainSplitPane.getSize().width* Constants.VERTICAL_DIVIDER_POS);   
         	        horizontalDividerPosition = (int) (mainSplitPane.getSize().height * Constants.HORIZONTAL_DIVIDER_POS);
         		   
         		   
         		   downSplitPane.setDividerLocation(verticalDividerLocation); 
         		   upSplitPane.setDividerLocation(verticalDividerLocation);
         		   mainSplitPane.setDividerLocation(horizontalDividerPosition);
                   resizeTables(0);
                }
                
                
        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //creez structura de entitati din fisier si am root-ul global in root.java
        try {
			new InputData(fileName, problemFilesPath,skipFilesPath);  //keep only sideEffects
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        SplitPaneDSM splitPaneDemo = new SplitPaneDSM();
        
        
        frame.getContentPane().add(mainSplitPane);
        frame.setMinimumSize(new Dimension(Constants.SPLITPANE_MIN_WIDTH,Constants.SPLITPANE_MIN_HEIGHT));
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
    }
 
    
    public static void refreshFrame(){
    	frame.repaint();
    	
    }
    
   private static void generateAllClicks() {
	   
	   //ArrayList<Entitate> printedEntities;
	   int clickOnRow = 0;
	   int lastCurrentRow = VerticalMatrix.getRowCounter();
	   int clickOnCol = VerticalMatrix.getColCounter()-1;
	   boolean expandEventsOnClickedRow = true;
	   
	   //cand ajung pe ultimul rand si nu pot sa fac expand ma opresc
	   while(clickOnRow<=lastCurrentRow){
		   System.out.println("false clicking on "+clickOnRow+"  "+clickOnCol);
		   if( ClickAction.actionOnClickWithFeedback(clickOnRow, clickOnCol) == "Expand"){
			   clickOnCol = VerticalMatrix.getColCounter()-1;
			   lastCurrentRow = VerticalMatrix.getRowCounter();
		   } else{
			   clickOnRow++;
		   }
	   }
	  
	}


private static void resizeTable(JTable table, int rowSize, int colSize, int noCol){
	  if(rowSize>0)
		  table.setRowHeight(rowSize);
	  else
		  table.setRowHeight(1);//default minimum value
       TableColumn column = null;
		    for (int i = 0; i < noCol; i++) {
		        column = table.getColumnModel().getColumn(i);
		        column.setMinWidth(0);
		        column.setPreferredWidth(colSize);
		    } 
   }

    public static void resizeTables(int horizontalMoveDown){
    	
    	
    	
    	int mainSize = MainMatrix.getSize();
    	int totalH = mainSplitPane.getSize().height - (Constants.SPLITPANE_DIVIDER_SIZE );
    	int totalW = mainSplitPane.getSize().width - (Constants.SPLITPANE_DIVIDER_SIZE);
    	
    	
    	//mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	//hTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	
    	
    	//H1 inaltime primul rand
    	// W1 latime prima coloana
    	BigDecimal H1 = new BigDecimal( Constants.HORIZONTAL_DIVIDER_POS * totalH +horizontalMoveDown);
    	BigDecimal H2 = new BigDecimal( (1-Constants.HORIZONTAL_DIVIDER_POS) * totalH -horizontalMoveDown);
    	BigDecimal W1 = new BigDecimal(Constants.VERTICAL_DIVIDER_POS * totalW);  //# 0.4
    	BigDecimal W2 = new BigDecimal((1-Constants.VERTICAL_DIVIDER_POS) * totalW);
    	
    	
    	//main data table has mainRowH & mainColW ; mainRowH is the same for hTable and RowH same for vTable
    	double mainRowH, mainColW, rowH, colW;
    	BigDecimal mainSizeBig = new BigDecimal(MainMatrix.getSize());
    	int mainRowSize, mainColSize;
    	mainRowSize = H2.divide(mainSizeBig,RoundingMode.HALF_DOWN).intValue();
    	mainColSize = W2.divide(mainSizeBig,RoundingMode.HALF_DOWN).intValue();
    	
    	
    	//lines in table
    	if(mainRowSize > 10 && mainColSize >10){
    		
        	H2 = new BigDecimal( (1-Constants.HORIZONTAL_DIVIDER_POS) * totalH - mainSize-horizontalMoveDown);
        	W1 = new BigDecimal(Constants.VERTICAL_DIVIDER_POS * totalW - VerticalMatrix.getColCounter());  //#0.4
        	W2 = new BigDecimal((1-Constants.VERTICAL_DIVIDER_POS) * totalW - mainSize);  //mainSize
        	
        	//System.out.println("H2= "+H2+"  W1= "+W1+"  W2= "+W2);
        	//System.out.println("-  "+VerticalMatrix.getColCounter()+"   -"+mainSize);
        	
    		mainRowSize = H2.divide(mainSizeBig,RoundingMode.HALF_DOWN).intValue();
        	mainColSize = W2.divide(mainSizeBig,RoundingMode.HALF_DOWN).intValue();
        	
        	mainTable.setIntercellSpacing(new Dimension(0,1));
        	vTable.setIntercellSpacing(new Dimension(0,1));
        	hTable.setIntercellSpacing(new Dimension(0,1));
    
    }else{
    		//tabelul e prea inghesuit ca sa mai adaug si liniile
    		mainTable.setIntercellSpacing(new Dimension(0,0));
        	vTable.setIntercellSpacing(new Dimension(0,0));
        	hTable.setIntercellSpacing(new Dimension(0,0));
    	}
    	
    	
    	//System.out.println("mainRowsize  "+mainRowSize+"   maincolsize  "+ mainColSize+"   with a total of  "+mainSize);
    	//System.out.println(); System.out.println();  System.out.println();
    	
    	//resize MAIN
    	resizeTable(mainTable,mainRowSize,mainColSize,mainSize);  //
    	
    	
    	//resize VERTICAL
    	int vColNo = vTable.getModel().getColumnCount();
    	BigDecimal vColNoBig = new BigDecimal(vTable.getModel().getColumnCount());
    	int vColSize;
    	vColSize = W1.divide(vColNoBig,RoundingMode.HALF_DOWN).intValue();
    	resizeTable(vTable,mainRowSize,vColSize,vColNo);
    	
    	//resize HORIZONTAL
    	int hRowNo = hTable.getModel().getRowCount(); //totdeauna 1 :P
    	BigDecimal hRowNoBig = new BigDecimal(hTable.getModel().getRowCount());
    	int hRowSize;
    	hRowSize = H1.divide(hRowNoBig,RoundingMode.HALF_DOWN).intValue();
    	resizeTable(hTable,hRowSize,mainColSize,mainSize);
    	
    	
    	
    	//text size
    	
    	if(mainRowSize>15)
    		mainRowSize = 12;
    	if(mainColSize>15)
    		mainColSize = 12;
    	
    	vTable.setFont(new Font("Serif",Font.BOLD,mainRowSize));
    	hTable.setFont(new Font("Serif",Font.BOLD,mainColSize));
    	
    	int minValue = mainRowSize;
    	if(mainRowSize>mainColSize)
    		minValue = mainColSize;
    	int finalValue = 12;
    	if(mainRowSize != 12)
    		finalValue = minValue*2/3;
    	mainTable.setFont(new Font("Serif",Font.BOLD,finalValue));
    	
    }

	public static void updateFrame(AttributiveCellTableModel newVerticalModel, AttributiveCellTableModel newDataModel) {
		
		/*
		 final CellSpan cellAtt =(CellSpan)newVerticalModel.getCellAttribute();
		    
	     int[] columns =  {0};
		  int[] rows    ={0,1,2};
		  cellAtt.combine(rows,columns);
		 */
		
		verticalDividerLocation =  (int) (mainSplitPane.getSize().width* Constants.VERTICAL_DIVIDER_POS);   
	       horizontalDividerPosition = (int) (mainSplitPane.getSize().height *  Constants.HORIZONTAL_DIVIDER_POS); 
		
		downSplitPane.setDividerLocation(verticalDividerLocation); 
		   upSplitPane.setDividerLocation(verticalDividerLocation);
		   mainSplitPane.setDividerLocation(horizontalDividerPosition);
		
		
		//updated vTable in same reference ,sa pot sa resize
		    vTable = new MultiSpanCellTable(newVerticalModel){
			   public TableCellRenderer getCellRenderer(int row, int column) {
 	               return VCell;
 	           }
		   };
		 
		   vTable.setRowSelectionAllowed(false);
		   vTable.setColumnSelectionAllowed(false);
		   vTable.addMouseListener(new VerticalMouseAdapter() );
	       
	       
	       JScrollPane newVTableScrollPane = new JScrollPane(vTable);
	       
	       
	       
	 	  
	 	//updated MainDATA Table
	       mainTable =new MultiSpanCellTable( newDataModel ){
		 	      public TableCellRenderer getCellRenderer(int row, int column) {
		 	               return DSMcell;
		 	           }
		 	      
		 	    //#
		 	      
		 	    };
	 	   
	       mainTable.setRowSelectionAllowed(false);
	       //updatedDataTable.addMouseListener(new MouseAdapter() );
	       JScrollPane newMainTableScrollPane = new JScrollPane(mainTable);
	    
	       downSplitPane.setRightComponent(mainTable);
	       downSplitPane.setLeftComponent(vTable);
	       
	       
	       
	      
	      //HORIZONTAL TABLE
	       AttributiveCellTableModel hTableModel = new AttributiveCellTableModel( new HorizontalMatrix().getHorizontalMatrix(),"horizontal" ){
		    	public boolean isCellEditable(int row, int column) {
		 	           return false;
		 	       }
		 	    };
	 	 	 hTable = new MultiSpanCellTable( hTableModel ){
	 	 		  public TableCellRenderer getCellRenderer(int row, int column) {
	 	              return HCell;
	 	          }
	 	 	   };
		    hTable.setRowSelectionAllowed(false);
		    hTable.addMouseListener(new HorizontalMouseAdapter() );
		    
	 	   upSplitPane.setRightComponent(hTable);
	 	   upSplitPane.setDividerSize(Constants.SPLITPANE_DIVIDER_SIZE);
	 	  resizeTables(0);
	}
 
	
	
	public static void generateDSM(final String fileName, final String problemFilesPath, final String skipFilesPath, final boolean expandAllFlag){
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	
	            	
	            	System.out.println(fileName);
	            	System.out.println(problemFilesPath);
	            	System.out.println(skipFilesPath);
	            	
	            	 createAndShowGUIforFile(fileName,problemFilesPath,skipFilesPath);
		              if(expandAllFlag){
		        			generateAllClicks();
		        		}
		            }
		        });
		    }
	
	
	
	
	 public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	
	            	String fileName = "C:/Users/ioana/Dropbox/testCaiet.txt";
	            			//"C:/Users/ioana/Dropbox/argouml-0.34.mse/argouml-0.34.mse"; 
	            			//"C:/Users/ioana/Dropbox/testCaietAlltypes.txt"; 
	            			//"C:/Users/ioana/Dropbox/argouml-0.34.mse/argouml-0.34.mse";
	            			//"C:/Users/ioana/Dropbox/testCaiet.txt"; 
	            	
	            	String problemFilesPath = "C:/Users/ioana/Dropbox/LICENTA/problemFiles";
	    			//"C:/Users/ioana/Dropbox/LICENTA/problemFiles";
	            	
	            	String skipFilesPath ="C:/Users/ioana/Dropbox/LICENTA/SkipEntities";  
	        		//"C:/Users/ioana/Dropbox/LICENTA/SkipEntities";
	            	 
	            	boolean expandAllFlag = true;
	            	
	                createAndShowGUIforFile(fileName,problemFilesPath,skipFilesPath);
	                if(expandAllFlag){
	        			generateAllClicks();
	        		}
	            }
	        });
	    }
	    
	    

}
       

