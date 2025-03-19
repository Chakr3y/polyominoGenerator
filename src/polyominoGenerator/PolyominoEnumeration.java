package polyominoGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

import static polyominoGenerator.Polyomino.Direction;

/**
 * A collection of subclasses for each method of enumerating Polyominoes.
 * Reimplements {@code equals}, {@code hashCode}, and {@code propagate} methods + constructors.
 */
public class PolyominoEnumeration {
	
	/**
	 * Distinct when no pieces can be rigidly transformed into another.
	 * WIP
	 */
	static public class FreePolyomino extends Polyomino {
		@Override
		public FreePolyomino clone() {
			return (FreePolyomino) super.clone();
		}
		
		/*
		 * 
		 */
		@Override
		public int hashCode() { // TODO implement
			if (hc != null) return hc;
			
			final int prime = 31;
			hc = 0;
			
			// Hash rows
			int hrCW = 0;
			int hrCCW = 0;
			int R = array.size();
			for (int i = 0; i < R; i++) {
				hrCW *= prime;
				hrCW += row(i).hashCode() +
						revRow(R-i-1).hashCode();
				hrCCW *= prime;
				hrCCW += row(R-i-1).hashCode() +
						revRow(i).hashCode();
			}
			
			// Hash columns
			int hcCW = 0;
			int hcCCW = 0;
			int C = array.get(0).size();
			for (int i = 0; i < C; i++) {
				hcCW *= prime;
				hcCW += column(i).hashCode() +
						revColumn(C-i-1).hashCode();
				hcCCW *= prime;
				hcCCW += column(C-i-1).hashCode() +
						revColumn(i).hashCode();
			}
			
			hc = hrCW + hrCCW + hcCW + hcCCW + size;
			return hc;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof FreePolyomino))
				return false;
			
			// TODO
			Polyomino other = (Polyomino) obj;
			
			// Check rotations
			if (Objects.equals(array, other.array.reversed()))
				return true;
			
//			if (Objects.equals(array, other.array.stream()
//					.map(b -> b.reversed()).collect(Collectors.toList())))
			List<List<Boolean>> revArray = new ArrayList<>();
			for (List<Boolean> b : other.array)
				revArray.add(b.reversed());
			if (Objects.equals(array, revArray))
				return true;
			
			return true;
		}
		
		@Override
		public Set<Polyomino> propagate() { // TODO
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
	}
	
	
	/**
	 * (AKA One-sided)
	 * Distinct when no pieces can be translated or rotated into another (but can flip).
	 * 
	 * Used by Tetris.
	 */
	static public class ChiralPolyomino extends Polyomino {
		
		public ChiralPolyomino() {
			super();
		}
		
		ChiralPolyomino(int size, List<List<Boolean>> array) {
			this.size = size;
			this.array = array;
		}
		
		@Override
		public ChiralPolyomino clone() {
			return new ChiralPolyomino(size, cloneArray());
		}
		
		/*
		 * Generates hash code by scanning in a windmill pattern both CW and CCW.
		 */
		@Override
		public int hashCode() {
			if (hc != null) return hc;
			
			final int prime = 31;
			hc = 0;
			
			// Hash rows
			int hrCW = 0;
			int hrCCW = 0;
			int R = array.size();
			for (int i = 0; i < R; i++) {
				hrCW *= prime;
				hrCW += row(i).hashCode() +
						revRow(R-i-1).hashCode();
				hrCCW *= prime;
				hrCCW += row(R-i-1).hashCode() +
						revRow(i).hashCode();
			}
			
			// Hash columns
			int hcCW = 0;
			int hcCCW = 0;
			int C = array.get(0).size();
			for (int i = 0; i < C; i++) {
				hcCW *= prime;
				hcCW += column(i).hashCode() +
						revColumn(C-i-1).hashCode();
				hcCCW *= prime;
				hcCCW += column(C-i-1).hashCode() +
						revColumn(i).hashCode();
			}
			
			hc = hrCW + hrCCW + hcCW + hcCCW + size;
			return hc;
		}
		
		@Override
		public boolean equals(Object obj) {
			// Checks base case
			if (super.equals(obj))
				return true;
			if (!(obj instanceof ChiralPolyomino))
				return false;
			
			Polyomino other = (Polyomino) obj;
			// Check rotations
			if (Objects.equals(array, other.rotate180().array))
				return true;
			if (Objects.equals(array, other.rotateCW().array))
				return true;
			if (Objects.equals(array, other.rotateCCW().array))
				return true;
			
			
			return false;
		}
		
		@Override
		public Set<Polyomino> propagate() {
			// BinaryList implements hashCode accounting for symmetry, so Set will automatically prune
			Set<Polyomino> set = new HashSet<>(4 * size);
			//List<Polyomino> set = new ArrayList<>(4*size);
			
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
					
					// Limit bounds to [ n x n/2 ]
					// would need to know final n ??
//					if (x != i && array.size()+1 > size/2+2)
//						continue;
					
					// Deep copy
					Polyomino clone = clone();
					clone.add(x, y);
					
					set.add(clone);
				}
			}
			
			return set;
		}
	}
	
	
	/**
	 * Distinct when no pieces are translation of another.
	 * The default enumeration.
	 */
	static public class FixedPolyomino extends Polyomino {

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof FixedPolyomino))
				return false;
			return true;
		}
	}
}
