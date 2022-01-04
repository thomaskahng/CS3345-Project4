package project4;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

public class Maze {
	public static void main(String[] args) {	
		//User input in window
		String askRows = JOptionPane.showInputDialog("Number of rows?");
		String askCols = JOptionPane.showInputDialog("Number of cols?");
		
		//Try catch to check validity
		try {
			//Parse input of number of rows and cols
			int rows = Integer.parseInt(askRows);
			int cols = Integer.parseInt(askCols);
			
			//Continue if positive values entered
			if (rows >= 2 && cols >= 2) {
				int numElems = rows * cols;
				DisjSets ds = new DisjSets(numElems);
				
				/** In matrix
				 * row of array = row
				 * column of array = col - 1
				 * 0 index row is top edge
				 * last index column is right edge
				 */
				String mazeArr[][] = new String[rows + 1][cols + 1];
				int numsInMat[][] = new int[rows + 1][cols + 1];
				
				generateMazeMatrix(mazeArr, numsInMat, rows, cols);
				generateMaze(mazeArr, numsInMat, ds);
				displayMaze(mazeArr);
			}
			//Throw alert if number invalid
			else 
				alert();
		}
		//Throw number format exception if format is bad
		catch (NumberFormatException n) {
			alert();
		}
	}
	
	private static void alert() {
		JOptionPane.showInputDialog("Invalid number!");
	}
	
	private static void generateMazeMatrix(String[][] mazeArr, int[][] numsInMat, int rows, int cols) {
		int num = 0;
		
		//Loop through rows and columns to create maze
		for (int i=0; i<=rows; i++) {
			for (int j=0; j<=cols; j++) {
				//Make top edge
				if (i == 0) {
					numsInMat[i][j] = -1;
					
					if (j == 0 || j == cols)
						mazeArr[i][j] = "  ";
					else
						mazeArr[i][j] = " _";
				}
				else {
					//Make right edge
					if (j == cols) {
						numsInMat[i][j] = -1;
						
						if (i == rows)
							mazeArr[i][j] = " ";
						else
							mazeArr[i][j] = "|";
					}
					//Make normal walls
					else {
						if (i == 1 && j == 0)
							mazeArr[i][j] = " _";
						else
							mazeArr[i][j] = "|_";
						
						numsInMat[i][j] = num;
						++num;
					}
				}
			}
		}
		//Make entry and exit of maze
		mazeArr[1][0] = " _";
		mazeArr[rows][cols - 1] = "| ";
	}
	
	private static void generateMaze(String[][] mazeArr, int[][] numsInMat, DisjSets ds) {
		int visited = 0;
		int maxVisits = numsInMat[numsInMat.length - 1][numsInMat[0].length - 2];
		int i = 1;
		int j = 0;
		
		Stack<Integer> rowInd = new Stack<Integer>();
		Stack<Integer> colInd = new Stack<Integer>();
		
 		while (visited <= maxVisits) {
			//Left, down, right, and up locations on matrix
			int left = j - 1;
			int down = i + 1;
			int right = j + 1;
			int up = i - 1;
			
			//See if left, right, down, and up exist
			boolean hasLeft = true;
			boolean hasDown = true;
			boolean hasRight = true;
			boolean hasUp = true;
			
			//List of possible wall knocks
			List<String> knocks = new ArrayList<String>();
			
			//Top left corner
			if (i == 1 && j == 0) {
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			//Top right corner
			else if (i == 1 && j == numsInMat[0].length - 2) {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			//Bottom left corner
			else if (i == numsInMat.length - 1 && j == 0) {
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
			}
			//Bottom right corner
			else if (i == numsInMat.length - 1 && j == numsInMat[0].length - 2) {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
			}
			//Top edge
			else if (i == 1 && (j > 0 && j < numsInMat[0].length - 2)) {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			//Bottom edge
			else if (i == numsInMat.length - 1 && (j > 0 && j < numsInMat[0].length - 2)) {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
			}
			//Left edge
			else if ((i > 1 && i < numsInMat.length - 1) && j == 0) {
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			//Right edge
			else if ((i > 1 && i < numsInMat.length - 1) && j == numsInMat[0].length - 2) {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			//In the middle
			else {
				hasLeft = hasPath(ds, numsInMat[i][j], numsInMat[i][left]);
				hasRight = hasPath(ds, numsInMat[i][j], numsInMat[i][right]);
				hasUp = hasPath(ds, numsInMat[i][j], numsInMat[up][j]);
				hasDown = hasPath(ds, numsInMat[i][j], numsInMat[down][j]);
			}
			String toBreak = breakWalls(knocks, hasLeft, hasRight, hasUp, hasDown);
			
			if (knocks.size() ==  0) {
				i = rowInd.pop();
				j = colInd.pop();
			}
			else {
				rowInd.push(i);
				colInd.push(j);
				
				//Break left wall
				if (toBreak == "Left") {
					ds.union(numsInMat[i][j], numsInMat[i][left]);
					mazeArr[i][j] = " _";					
					j = left;
				}
				//Break right wall
				else if (toBreak == "Right") {
					ds.union(numsInMat[i][j], numsInMat[i][right]);
					mazeArr[i][right] = " _";					
					j = right;
				}
				//Break upper wall
				else if (toBreak == "Up") {
					ds.union(numsInMat[i][j], numsInMat[up][j]);
					mazeArr[up][j] = "| ";
					i = up;
				}
				//Break lower wall
				else if (toBreak == "Down") {
					ds.union(numsInMat[i][j], numsInMat[down][j]);
					mazeArr[i][j] = "| ";				
					i = down;
				}
				++visited;
			}
		}
	}
	
	private static boolean hasPath(DisjSets ds, int node1, int node2) {
		//Sets
		int set1 = ds.find(node1);
		int set2 = ds.find(node2);
		
		//If roots equal, has path because they are in same set
		if (set1 == set2)
			return true;
		else 
			return false;
	}
	
	private static String breakWalls(List<String> knocks, boolean hasLeft, boolean hasRight, boolean hasUp, boolean hasDown) {
		//Places to store data
		Random rand = new Random();
		String knock = "";
		
		//If no union exists, add directions to list of knocks
		if (!hasLeft)
			knocks.add("Left");
		if (!hasRight)
			knocks.add("Right");
		if (!hasUp)
			knocks.add("Up");
		if (!hasDown)
			knocks.add("Down");
		
		//Random integer based on size of list of knocks
		int size = knocks.size();
		int randInt = 0;
		
		//Random integer of definite size
		if (size > 0)
			randInt = rand.nextInt(size);
		
		//We can only knock 1 wall
		if (size == 1) 
			knock = knocks.get(0);
		
		//We an knock 2 walls
		else if (size == 2) {
			if (randInt == 0)
				knock = knocks.get(0);
			else if (randInt == 1)
				knock = knocks.get(1);
		}
		//We an knock 3 walls
		else if (size == 3) {
			if (randInt == 0)
				knock = knocks.get(0);
			else if (randInt == 1)
				knock = knocks.get(1);
			else if (randInt == 2)
				knock = knocks.get(2);
		}
		//We an knock 4 walls
		else if (size == 4) {
			if (randInt == 0)
				knock = knocks.get(0);
			else if (randInt == 1)
				knock = knocks.get(1);
			else if (randInt == 2)
				knock = knocks.get(2);
			else if (randInt == 3)
				knock = knocks.get(3);
		}
		return knock;
	}
	
	private static void displayMaze(String[][] mazeArr) {
		for (int i=0; i<mazeArr.length; i++) {
			for (int j=0; j<mazeArr[0].length; j++) 
				System.out.print(mazeArr[i][j]);
			System.out.println();
		}
	}
}