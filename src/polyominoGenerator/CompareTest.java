package polyominoGenerator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

import BinaryList.BinaryList;

import static polyominoGenerator.PolyominoEnumeration.*;

public class CompareTest {
	// No of cells per block
	int N = 11;
	// Iterations
	int C = 1;
	
	// Test function of CustomPolyomino
	@Ignore
	@Test
	public void CustomPolyominoTest() {
		List<Polyomino> APblocks = PolyominoGenerator.generateSet(N, new ArrayPolyomino());
		
		//for (Polyomino p : APblocks) printBlock((ArrayPolyomino) p);
		System.out.println("Final count for n="+N+": "+APblocks.size());
	}

	@Test
	public void comparisonTest() {
		
		long start2 = System.currentTimeMillis();
		List<Polyomino> blocks = PolyominoGenerator.generateSet(N);
		double dur2 = System.currentTimeMillis() - start2;
		
		long start1 = System.currentTimeMillis();
		List<Polyomino> APblocks = PolyominoGenerator.generateSet(N, new ArrayPolyomino());
		double dur1 = System.currentTimeMillis() - start1;
		
		Assert.assertEquals(APblocks.size(), blocks.size());
		//Assert.assertEquals(new HashSet<>(APblocks), new HashSet<>(blocks));
		
		System.out.println("Final count for n="+N+": "+blocks.size());
		System.out.println("Total for custom ("+C+" trials): " + dur1/1000 + " secs");
		System.out.println("Total for standard ("+C+" trials): " + dur2/1000 + " secs");
		if (C > 1) {
			System.out.println("Avg. "+dur1/1000/C+" secs/trial (custom)");
			System.out.println("Avg. "+dur2/1000/C+" secs/trial (standard)");
		}
	}
	

	static String[] cellString = PolyominoGenerator.cellString;
	public static void printBlock(ArrayPolyomino p) {
		for (int i = 0; i < p.column(0).size(); i++) {
			String s = p.row(i).toString();
			s = s.replaceAll("[\\[\\], ]", "")
				 .replace("false", cellString[0])
				 .replace("true", cellString[1]);
			System.out.println(s);
		}
		//System.out.print(p.hashCode());
		System.out.println();
	}
}


// Polyomino implementation using ArrayList instead of BinaryList
class ArrayPolyomino extends Polyomino {
	
	ArrayPolyomino() {
		this.size = 1;
		this.array = new ArrayList<>();
		this.array.add(new ArrayList<>());
		this.array.get(0).add(true);
	}
	
	ArrayPolyomino(int size, List<List<Boolean>> array) {
		this.size = size;
		this.array = array;
	}
	
	@Override
	public void add(int r, int c) {
		if (r < -1 || r > array.size() || c < -1 || c > array.get(0).size())
			throw new IndexOutOfBoundsException();
		
		if (r == -1) {
			// Top expansion
			array.add(0, new ArrayList<Boolean>(Collections.nCopies(array.get(0).size(), false)));
			r = 0;
		} else if (r == array.size()) {
			// Bottom expansion
			array.add(new ArrayList<Boolean>(Collections.nCopies(array.get(0).size(), false)));
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
	
	@Override
	public List<Boolean> column(int n) {
		List<Boolean> c = new ArrayList<>();
		
		for (int i = 0; i < array.size(); i++)
			c.add(row(i).get(n));
		
		return c;
	}
	
	@Override
	public List<Boolean> revColumn(int n) {
		List<Boolean> c = new ArrayList<>();
		
		for (int i = array.size()-1; i >= 0; i--)
			c.add(row(i).get(n));
		
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<List<Boolean>> cloneArray() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		for (List<Boolean> b : array)
			newArray.add( (ArrayList<Boolean>) ( ((ArrayList<Boolean>) b).clone() ) );
		
		return newArray;
	}
	
	@Override
	public Polyomino clone() {
		return new ArrayPolyomino(size, cloneArray());
	}
	
	@Override
	public Polyomino rotate180() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		for (List<Boolean> b : array)
			newArray.add( ((ArrayList<Boolean>) b) .reversed() );
		
		return new ArrayPolyomino(size, newArray.reversed());
	}

	@Override
	public Polyomino rotateCW() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		int R = array.size(), C = array.get(0).size();

		for (int j = 0; j < C; j++) {
			newArray.add(new ArrayList<>());
			for (int i = 0; i < R; i++)
				newArray.getLast().add(array.get(i).get(C-j-1));
		}
		
		return new ArrayPolyomino(size, newArray);
	}
	
	@Override
	public Polyomino rotateCCW() {
		List<List<Boolean>> newArray = new ArrayList<>();
		
		int R = array.size(), C = array.get(0).size();

		for (int j = 0; j < C; j++) {
			newArray.add(new ArrayList<>());
			for (int i = 0; i < R; i++)
				newArray.getLast().add(array.get(R-i-1).get(j));
		}
		
		return new ArrayPolyomino(size, newArray);
	}
}