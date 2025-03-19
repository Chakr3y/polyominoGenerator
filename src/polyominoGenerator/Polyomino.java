package polyominoGenerator;

import BinaryList.BinaryList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Polyomino object represented by a boolean matrix.
 * <p>
 * (x,y)<br>
 * 	[0,0] [0,1] [0,2]<br>
 * 	[1,0] [1,1] [1,2]<br>
 * 	[2,0] [2,1] [2,2]
 * <p>
 * Propagation should limit size to (n, n/2), vice versa
 */
public class Polyomino implements Cloneable {
	// Number of blocks
	int size;
	
	// 'array' should maintain rectangular size
	// array[row][column]
	//public boolean[][] array;
	List<List<Boolean>> array;

	/**
	 * Construct a Monomino by default.
	 */
	Polyomino() {
		size = 1;
		array = new ArrayList<>();
		array.add(new BinaryList());
		//array.add(new ArrayList<Boolean>());
		array.get(0).add(true);
	}
	
	/*Polyomino(int s) {
		size = s;
		//array = new boolean[s][(int) Math.ceil(s/2)];
		array = 
	}*/
	
//	/**
//	 * Clone by deep-copying fields.
//	 * @param p Polyomino to be copied
//	 */
//	Polyomino(Polyomino p) {
//		size = p.size();
//		array = p.
//	}
	
	/**
	 * Constructor for cloning (arguments passed should be clones)
	 * @param size
	 * @param array
	 */
	Polyomino(int size, List<List<Boolean>> array) {
		this.size = size;
		this.array = array;
	}
	
	/**
	 * Return number of blocks, NOT ROWS of array.
	 * @return
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Return List that stores the block data.
	 * @return
	 */
	public List<List<Boolean>> getArray() {
		return array;
	}
	
	/**
	 * Adds a block at coord, throw if invalid or already filled in
	 * Coords can overstep bounds by 1 to expand
	 */
	public void add(int r, int c) {
		if (r < -1 || r > array.size() || c < -1 || c > array.get(0).size())
			throw new IndexOutOfBoundsException();
		
		if (r == -1) {
			// Top expansion
			array.add(0, new BinaryList(0, array.get(0).size()));
			//array.add(0, new ArrayList<Boolean>(Collections.nCopies(array.get(0).size(), false)));
			r = 0;
		} else if (r == array.size()) {
			// Bottom expansion
			array.add(new BinaryList(0, array.get(0).size()));
			//array.add(new ArrayList<Boolean>(Collections.nCopies(array.get(0).size(), false)));
		}
		
		if (c == -1) {
			// Left expansion
			for (List<Boolean> b : array)
				b.add(0, false);
			
			c = 0;
		} else if (c == array.get(0).size()) {
			// Right expansion
			for (List<Boolean> b : array)
				b.add(false);
		}
		
		array.get(r).set(c, true);
		size++;
		hc = null;
	}
	
	/**
	 * Get cell status at position (r,c).
	 * If r and c exceed range by 1, returns false
	 * @param r
	 * @param c
	 * @return whether (r,c) is filled in
	 */
	public boolean get(int r, int c) {
		if (r < -1 || r > array.size() || c < -1 || c > array.get(0).size())
			throw new IndexOutOfBoundsException();
		
		// Bound cases
		if (r == -1 || r == array.size() || c == -1 || c == array.get(0).size())
			return false;
		
		return array.get(r).get(c);
	}
	
	// Set to null after modifying object
	protected transient Integer hc;
	/*
	 * Generates hash code by scanning through horizontal/vertical.
	 */
	@Override
	public int hashCode() {
		if (hc != null) return hc;
		
		final int prime = 31;
		hc = 0;
		
		// Hash rows
		int hrow = 0;
		int R = array.size();
		for (int i = 0; i < R; i++) {
			hrow *= prime;
			hrow += row(i).hashCode();
		}
		
		// Hash columns
		int hcol = 0;
		int C = array.get(0).size();
		for (int i = 0; i < C; i++) {
			hcol *= prime;
			hcol += column(i).hashCode();
		}
		
		hc = hrow + hcol + size;
		return hc;
	}
	
	/**
	 * By default, compares fixed polyominoes.
	 * Directly compares array and size.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Polyomino))
			return false;
		
		Polyomino other = (Polyomino) obj;
		if (size != other.size)
			return false;
		return Objects.equals(array, other.array);
	}
	
	/*public boolean[] row(int n) {
		return array[boundR.x + n];
	}
	
	public boolean[] column(int n) {
		boolean[] col = new boolean[array.length];
		for (int i = 0; i < col.length; i++)
			col[i] = array[i][n];
		
		return col;
	}*/

	/**
	 * Returns actual List of array
	 * @param n which row
	 * @return
	 */
	public List<Boolean> row(int n) {
		return array.get(n);
	}
	
	/**
	 * Returns deep(?) copy of column
	 * @param n which column
	 * @return
	 */
	public List<Boolean> column(int n) {
		List<Boolean> c = new BinaryList();
		//List<Boolean> c = new ArrayList<>();
		
		for (int i = 0; i < array.size(); i++)
			c.add(row(i).get(n));
		
		return c;
	}
	
	// Reverse row (right-left)
	/*public boolean[] rev_row(int n) {
		boolean[] row = new boolean[array[0].length];
		for (int i = 0; i < row.length; i++)
			row[i] = array[n][row.length-i-1];
		
		return row;
	}*/
	public List<Boolean> revRow(int n) {
		return array.get(n).reversed();
	}
	
	// Reverse column (bottom-up)
	/*public boolean[] rev_column(int n) {
		boolean[] col = new boolean[array.length];
		for (int i = 0; i < col.length; i++)
			col[i] = array[col.length-i-1][n];
		
		return col;
	}*/
	public List<Boolean> revColumn(int n) {
		List<Boolean> c = new BinaryList();
		//List<Boolean> c = new ArrayList<>();
		
		for (int i = array.size()-1; i >= 0; i--)
			c.add(row(i).get(n));
		
		return c;
	}
	
	/**
	 * Returns a deep copy of {@code array}
	 * @return
	 */
	public List<List<Boolean>> cloneArray() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		for (List<Boolean> b : array)
			newArray.add( ((BinaryList) b) .clone() );
			//newArray.add( (ArrayList<Boolean>) ( ((ArrayList<Boolean>) b).clone() ) );
		
		return newArray;
	}
	
	/**
	 * Deep copy clone.
	 */
	@Override
	public Polyomino clone() {
		return new Polyomino(size, cloneArray());
	}
	
	/**
	 * Generates a list of polyomino from adding to each solid cell on all sides. 
	 * @return {@code List<Polyomino>}
	 */
	public Set<Polyomino> propagate() {
		// BinaryList implements hashCode accounting for symmetry, so Set will automatically prune
		Set<Polyomino> set = new HashSet<>(4 * size);
		
		
		// Check every cell of block
		for (int i = 0; i < array.size(); i++)
		for (int j = 0; j < row(i).size(); j++) {
			// Skip if not filled
			if (!get(i,j))
				continue;
			
			for (Direction d : Direction.values()) {
				int x = i, y = j;
				switch (d) {
					case UP:
						x -= 1;
						break;
					case DOWN:
						x += 1;
						break;
					case LEFT:
						y -= 1;
						break;
					case RIGHT:
						y += 1;
						break;
				}
				// check conditions
				if (get(x,y))
					continue;
				
				// Deep copy
				Polyomino clone = clone();
				clone.add(x, y);
				
				set.add(clone);
			}
		}
		
		return set;
	}
	
	enum Direction {
		LEFT,
		RIGHT,
		UP,
		DOWN,
	}
	
	/**
	 * Returns copy with array rotated 180 deg.
	 */
	public Polyomino rotate180() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		for (List<Boolean> b : array)
			newArray.add( ((BinaryList) b) .reversed() );
		
		return new Polyomino(size, newArray.reversed());
	}
	
	/**
	 * Returns copy with array rotated clockwise.
	 */
	public Polyomino rotateCW() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		int R = array.size(), C = array.get(0).size();

		for (int j = 0; j < C; j++) {
			newArray.add(new BinaryList());
			for (int i = 0; i < R; i++)
				newArray.getLast().add(array.get(i).get(C-j-1));
		}
		
		return new Polyomino(size, newArray);
	}
	
	
	/**
	 * Returns copy with array rotated counter-clockwise.
	 */
	public Polyomino rotateCCW() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		int R = array.size(), C = array.get(0).size();

		for (int j = 0; j < C; j++) {
			newArray.add(new BinaryList());
			for (int i = 0; i < R; i++)
				newArray.getLast().add(array.get(R-i-1).get(j));
		}
		
		return new Polyomino(size, newArray);
	}
	
//	/**
//	 * Prunes duplicates of Polyominoes in the list, determined by rotational symmetry
//	 * DEPRECATED?
//	 * @param list List with possible clones
//	 */
//	public static void prune(List<Polyomino> list) {
//		// TODO
//	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < column(0).size(); i++) {
			s += row(i).toString() + "\n";
		}
		
		return s.replace("0", "  ").replace("1", "██");
	}
}
