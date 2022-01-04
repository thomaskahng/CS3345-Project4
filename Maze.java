package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {
	//Class variables
	private int rows;
	private int cols;
	private int size;
	private List<Cell> walls;
	
	DisjSets ds;
	Random rand = new Random();
	
	public Maze(int r, int c) {
		//Define class variables
		rows = r;
		cols = c;
		size = rows * cols;
		walls = new ArrayList<Cell>();
		
		//Make cells for each maze space
		for (int i=0; i<size; i++)
			walls.add(new Cell());
		ds = new DisjSets(size);
	}
	
	public void generateMaze() {
		int cnt = 0;
		
		while (cnt < size - 1) {	
			//Randomly pick a space and temporary left, right, up, or down direction
			int space = rand.nextInt(size);
			int adjSpace = 0;
			int direction = rand.nextInt(4);
			
			//Bottom wall
			if (direction == 0) {
				//If space is below last row, get the space of next row
				if (space < (size - cols))
					adjSpace = space + cols;
				//Else, continue
				else
					continue;
			}
			//Right wall
			else if (direction == 1){
				int nextSpace = space + 1;
				
				//If the next column element's space is not at first column, get space of next column
				if (nextSpace % cols != 0)
					adjSpace = nextSpace;
				//Else, continue
				else
					continue;
			}
			//Top wall
			else if (direction == 2) {
				//If space is greater than first row, get space of previous row
				if (space >= cols)
					adjSpace = space - cols;
				//Else, continue
				else
					continue;
			}
			//Left Wall
			else if (direction == 3) {
				//If space is not at first column, get space of preious column
				if (space % cols != 0)
					adjSpace = space - 1;
				else
					continue;
			}
			//If space and adjacent space are not in same disjoint set
			if (ds.find(space) != ds.find(adjSpace)) {
				//Link to root and union them
				ds.union(ds.find(space), ds.find(adjSpace));
				
				//Right wall (current space)
				if (adjSpace == space + 1)
					walls.get(space).setRight(false);
				
				//Bottom wall (current space)
				else if (adjSpace == space + cols)
					walls.get(space).setBottom(false);
				
				//Left wall (one space left)
				else if (adjSpace == space - 1)
					walls.get(adjSpace).setRight(false);
				
				//Top wall (one space below)
				else if (adjSpace == space - cols)
					walls.get(adjSpace).setBottom(false);
				++cnt;
			}
		}
	}
	
	public String printMaze() {
		String maze = "";
		
		//Top of maze
		for (int i=0; i<cols; i++) 
			maze += " _";
		maze += "\n";
		
		for (int i=0; i<size-1; i++) {
			//Farthest left except first
			if (i % cols == 0) {
				if (i == 0)
					maze += " ";
				else
					maze += "|";
			}
			//Make the walls
			maze += walls.get(i).makeWalls();
			
			//If reached last space of column
			if (i % cols == cols - 1)
				maze += "\n";
		}
		return maze;
	}
}

class Cell {
	//Class variables
	private boolean bottom;
	private boolean right;
	private String bottomWall;
	private String rightWall;
	
	public Cell() {
		//Define class variables
		bottom = true;
		right = true;
		bottomWall = " ";
		rightWall = " ";
	}
	
	public String makeWalls() {
		//Make the walls for cell
		if (bottom == true) {
			bottomWall = "_";
		}		
		if (right == true) {
			rightWall = "|";
		}
		return bottomWall + rightWall;
	}
	
	//Will there be right wall in cell?
	public void setRight(boolean r) {
		right = r;
	}
	
	//Will there be bottom wall in cell?
	public void setBottom(boolean b) {
		bottom = b;
	}
}

/**
* Disjoint set class, using union by rank and path compression.
* Elements in the set are numbered starting at 0.
* @author Mark Allen Weiss
*/
class DisjSets {
	/**
	 * Construct the disjoint sets object.
	 * @param numElements the initial number of disjoint sets.
	 */
	public DisjSets(int numElements) {
		s = new int [numElements];
		for (int i=0; i<s.length; i++)
			s[i] = -1;
	}
	
	/**
	 * Union two disjoint sets using the height heuristic.
	 * For simplicity, we assume root1 and root2 are distinct
	 * and represent set names.
	 * @param root1 the root of set 1.
	 * @param root2 the root of set 2.
	 */
	public void union(int root1, int root2) {
		//root2 is deeper
		if (s[root2] < s[root1]) {
			// Make root2 new root
			s[root1] = root2;   
		}
		else {
			//Update height if same
			if (s[root1] == s[root2])
				s[root1]--;         
			//Make root1 new root
			s[root2] = root1;     
		}
	}
	
	/**
	 * Perform a find with path compression.
	 * Error checks omitted again for simplicity.
	 * @param x the element being searched for.
	 * @return the set containing x.
	 */
	public int find(int x) {
		if (s[x] < 0)
			return x;
		else
			return s[x] = find(s[x]);
	}
	private int[] s;
}
