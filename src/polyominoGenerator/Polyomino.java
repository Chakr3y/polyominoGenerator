package polyominoGenerator;

import static polyominoGenerator.Direction.*;

public class Polyomino {
	private class Pair {
		public int x, y;
		
		Pair() {
			this(0, 0);
		}
		
		Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int range() {
			return y-x;
		}
	}
	
	public int size;
	
	// array[row][column]
	public boolean[][] array;
	// pair is (before inclusive, after exclusive)
	Pair boundR, boundC;
	
	
	Polyomino(int s) {
		size = s;
		array = new boolean[s][s];
	}
	
	/*
	transient int hC;
	@Override
	public int hashCode() {
		if (hC != null) return hC;
		
		return 1;
	}*/
	
	// Run after array construction to establish bounds
	public void trim() {
		
	}
	
	public boolean[] row(int n) {
		return array[boundR.x + n];
	}
	
	// Reverse row
	public boolean[] rev_row(int n) {
		boolean[] row = new boolean[size];
		
		return row;
	}
	
}
