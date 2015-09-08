package toTest;

import org.junit.Test;

import CodedMatrixInfo.DepthMainMatrix;

public class TestDepthMainMatrix {

	@Test
	public void testExpand() {
		
		
		DepthMainMatrix myClass = DepthMainMatrix.getDepthMainMatrix();
		
		
		
		int[][] mydepthColorMatrix = { 
				{1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
				{1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
				{1, 1, 3, 3, 3, 2, 2, 0, 0, 0 },
				{1, 1, 3, 3, 3, 2, 2, 0, 0, 0 },
				{1, 1, 3, 3, 3, 2, 2, 0, 0, 0 },
				{1, 1, 2, 2, 2, 3, 3, 0, 0, 0 },
				{1, 1, 2, 2, 2, 3, 3, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
				};
		
		
		myClass.setMatrix(mydepthColorMatrix,10);
		
		int[][] expectedResultAfterExapand = { 
				{1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
				{1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
				{1, 1, 4, 4, 3, 2, 2, 2, 0, 0 },
				{1, 1, 4, 4, 3, 2, 2, 2, 0, 0 },
				{1, 1, 3, 3, 3, 2, 2, 2, 0, 0 },
				{1, 1, 3, 3, 3, 2, 2, 2, 0, 0 },
				{1, 1, 2, 2, 2, 2, 3, 3, 0, 0, },
				{1, 2, 2, 2, 2, 2, 3, 3, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
				};
		
		
		
		//myClass.printMatrix(10);
		
		myClass.expand(2, 2, 9);
		//myClass.printMatrix(10);
		//assertArrayEquals(expectedResultAfterExapand,myClass.getMatrix() );
		
		
	}

}
