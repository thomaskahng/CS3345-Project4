package project;

import javax.swing.JOptionPane;

public class Test {
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
				Maze maze = new Maze(rows, cols);
				maze.generateMaze();
				
				//Display the maze
				String mazeResult = maze.printMaze();
				System.out.println(mazeResult);
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
}