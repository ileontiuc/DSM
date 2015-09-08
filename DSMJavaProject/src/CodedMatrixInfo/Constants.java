package CodedMatrixInfo;

import java.math.BigDecimal;

public final class Constants {

	
	private Constants() {
        // restrict instantiation
	}
	
	
	//TODO sa fac singleton si sa ii iau valoarea din clasa de instability factor
	public static final BigDecimal DELTA = new BigDecimal(0.2); //diferenta acceptata la factorul de instabilitate

	public static final double VERTICAL_DIVIDER_POS = 0.3; //in procente cat ocupa partea din stanga
	public static final double HORIZONTAL_DIVIDER_POS = 0.2; //in procente cat ocupa partea de sus

	public static final int SPLITPANE_DIVIDER_SIZE = 3;
	
	public static final int SPLITPANE_INITIAL_WIDTH = 600;
	public static final int SPLITPANE_INITIAL_HEIGHT = 400;
	public static final double SPLITPANE_UP_HEIGHT_PROC = 0.2;  //se face cast pe int la inmultire

	
	public static final int SPLITPANE_MIN_WIDTH = 600;
	public static final int SPLITPANE_MIN_HEIGHT = 400;
}
