package polyominoGenerator;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import static polyominoGenerator.PolyominoEnumeration.*;

public class PolyominoGenerator {
	// number of cells
	static int N;

	public static void main(String[] args) {
		// Change this variable
		N = 11;
		// Iterations
		int C = 10;
		
		long start = System.currentTimeMillis();
	
		List<Polyomino> blocks = new ArrayList<>();
		Polyomino seed = new Polyomino();
		
		for (int t = 0; t < C; t++) blocks = generateSet(N, seed);
		
		double dur = System.currentTimeMillis() - start;
		
		// print final product
		int j = 0;
		for (Polyomino p : blocks) {
			//System.out.println(j++);
			printBlock(p);
		}

		System.out.println("Final count for n="+N+" ("+seed.getClass().getSimpleName()+"): "+blocks.size());
		System.out.println("Total ("+C+" trials): " + dur/1000 + " secs");
		if (C > 1)
			System.out.println("Avg. "+dur/1000/C+" secs/trial");
	}
	
	/**
	 * Overload with Polyomino as default
	 * @see PolyominoGenerator#generateSet(int, T)
	 */
	public static List<Polyomino> generateSet(int n) {
		return generateSet(n, new Polyomino());
	}
	
	/**
	 * Generate the set of polyominoes
	 * @param n number of cells (>0)
	 * @param baseP base instance to propagate from
	 * @return a list, not a set
	 */
	public static <T extends Polyomino> List<Polyomino> generateSet(int n, T baseP) {
		List<Polyomino> blocks = new ArrayList<>();
		Set<Polyomino> blockSet = new HashSet<>();
		blocks.add(baseP);
		
		for (int i = 1; i < n; i++) {
			for (Polyomino p : blocks)
				// propagate
				blockSet.addAll(p.propagate());

			// prune clones
			blocks.clear();
			blocks.addAll(blockSet);
			blockSet.clear();
		}
		
		return blocks;
	}
	
	// 0 for blank, 1 for filled in cell
	static String[] cellString = {"‹›","██"};
	/**
	 * Print a Polyomino with formatting
	 * @param p
	 */
	public static void printBlock(Polyomino p) {
		for (int i = 0; i < p.column(0).size(); i++) {
			String s = p.row(i).toString();
			s = s.replace("0", cellString[0])
				 .replace("1", cellString[1]);
			System.out.println(s);
		}
		//System.out.print(p.hashCode());
		System.out.println();
	}
}
