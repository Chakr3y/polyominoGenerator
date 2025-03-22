# Polyomino Generator

Personal project on generating [polyominoes](https://en.wikipedia.org/wiki/Polyomino) *really* quickly. All 3 enumeration types are implemented. Hashing is perhaps the most significant concept that speeds up the algorithm.
* Run on my machine, fixed undecominoes ($`n=11`$), all **135268** of them, can be generated in under 0.5 seconds.

Originally intended for *Tetris*-like game.

## To do

- implement FreePolyomino enumeration
- refine algorithm (e.g. chiral and free polyomino should be restricted to $`n`$ x $`\frac{n}{2}`$ )

## Dependencies

- [BinaryList](https://github.com/Chakr3y/BinaryList) (idk how to properly package my projects yet, so installation will suck)
- JUnit (for testing)

## Algorithm Details

The general algorithm for generating $`n`$-omino is simple:
1. Start with a set containing 1 monomino ($`n=1`$).
2. For each $`n`$-omino that we have, try creating a new ($`n+1`$)-omino by adding a block to each existing block, in each of the 4 directions.
3. Prune the duplicates. Check for symmetries if you need to.
4. Recurse through 2 and 3 with the new set of ($`n+1`$)-ominoes that we have.

As $`n`$ increases, the number of polyominoes increase exponentially, and so does the time it takes to execute step 2 and 3. Notably during step 3, **if we individually check duplicates against every pair of polyominoes we have, it will compound the time complexity with another factor of** $`O(m^2)`$, with $`m`$ being the exponentially increasing size of the set. So we need to decrease the time complexity of these steps.

An important thing to note is that there are 3 *enumeration* types (i.e. ways to distinguish) polyominoes according to their symmetries: free, one-sided/chiral, and fixed. See more at [Wikipedia: Polyomino#Free, one-sided, and fixed polyominoes](https://en.wikipedia.org/wiki/Polyomino#Free,_one-sided,_and_fixed_polyominoes). These enumeration types dictate the rules that we follow in deciding what to prune (i.e. how we define `equals` in Java). Also, translational symmetry is irrelevant, since we won't expand the dimensions of the array until placing a block in that direction.

So, how do we improve this algorithm? We can minimize the number of checks we have to perform by coming up with a hashing algorithm for polyominoes. The algorithm I came up with essentially scans the polyomino in a windmill pattern, hashes the array view, then applies the [polynomial rolling hash](https://en.wikipedia.org/wiki/Rolling_hash). (Illustration to be added) Implementing the `hashCode` and `equals` function for a class in Java allows `HashSet` to automatically prune duplicates we put inside the set.
