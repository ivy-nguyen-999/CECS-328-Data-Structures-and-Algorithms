import java.util.Scanner;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TheClaim {

	public static void main(String[] args) throws IOException {
		
		File inputFile = new File ("input.txt"); // creates File instance to reference text file in Java		
		Scanner reader = new Scanner(inputFile); // creates Scanner instance to read File in java
		
		char[][] theGameInput = null; // creates a 2D array to keep all the values
		int[] numOfAs = null; // keeps track of number of As in each row
		
		int row = 0; // keeps track of number of row
		int num; // keeps track of number of As in a row
		while (reader.hasNextLine()) { 
			num = 0; // reset
			String line = reader.nextLine();
			
			if (row == 0) { 
				// reads the first line and initializes the size of the arrays
				theGameInput = new char[line.length()][line.length()];
				numOfAs = new int[line.length()];
			}
			
			// loops through each char of the line and add them to array
			for (int column = 0; column < line.length(); column++) {
				if (line.charAt(column) == 'A') {
					num += 1; // increments num by one if that char is A
				}
				theGameInput[row][column] = line.charAt(column);
			}
			numOfAs[row++] = num; // before executing other rows, adds the result to the array
		}
		
		// creates an array that keeps the result
		int[] finalResult = largestSquare(theGameInput, numOfAs);
		
		// create a text file
		File file = new File("output.txt");
		FileWriter fileWriter = new FileWriter(file);
		// wrap FileWriter in BufferedWriter
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		
		// write the result into a text file
		for (int index = 0; index < 7; index++) {
			bufferedWriter.write("" + (finalResult[index] + 1));
			bufferedWriter.newLine();
		}
		bufferedWriter.write("" + (finalResult[7] + 1));
		
		// close:
		bufferedWriter.close();
		reader.close();
	}
	
	// finds the locations of the first two potential As
	public static int[] findAsLocation(char[] row, int numRow, int start, int end) {
		int[] result = new int[8]; // creates an array to holds the results
		for (int first = start; first <= end; first++) {
			if (row[first] == 'A') {
				// adds the row and column of the first A to the array
				result[0] = numRow;
				result[1] = first;
				for (int second = end; second > first; second--) {
					if (row[second] == 'A') {
						// adds the row and column of the second A to the array
						result[2] = numRow;
						result[3] = second;
						return result;
					}
				}
			}
		}
		return result;
	}
	
	// after finding the first two As, makes a square if can.
	public static int[] square(char[][] input, int[] result) {
		// finds the distance between the first two A's
		int distance = Math.abs(result[1] - result[3]);
		
		// there are two options:
		int rowOne = result[0] + distance; // last two A's are below the first two As
		int rowTwo = result[2] - distance; // last two A's are above the first two As
		
		// if the first option:
		if ((rowOne < input.length) &&
				((input[rowOne][result[1]] == '*' && input[rowOne][result[3]] == 'A') ||
				(input[rowOne][result[1]] == 'A' && input[rowOne][result[3]] == '*'))) {
			result[4] = rowOne;
			result[5] = result[1];
			result[6] = rowOne;
			result[7] = result[3];
			return result;
		}
		// if the second option:
		else if ((rowTwo >= 0) &&
				((input[rowTwo][result[1]] == '*' && input[rowTwo][result[3]] == 'A') ||
				(input[rowTwo][result[1]] == 'A' && input[rowTwo][result[3]] == '*'))) {
			result[4] = rowTwo;
			result[5] = result[1];
			result[6] = rowTwo;
			result[7] = result[3];
			return result;
		}
		// return null if we can't make a square
		return null;
	}
	
	//finds the largestSquare without using recursive
	public static int[] largestSquare(char[][] input, int[] numOfAs) {
		// initializes values to use for the loop
		int[] result = new int[8];
		int[] temp = new int[8];
		int greatest = input.length;
		int nextGreatest = 0;
		
		for (int rowNum = 0; rowNum < input.length; rowNum++) {
			// if number of A's in that row is greater than or equal to two
			if (numOfAs[rowNum] > 1) {
				// find the distance of first and last A's in the row
				temp = findAsLocation(input[rowNum], rowNum, 0, input.length - 1);
				int start = temp[1];
				int end = temp[3];
				int distance = Math.abs(start - end);
				
				// continue to next loop if the distance is less than the nextGreatest distance
				if (nextGreatest != 0 && distance < nextGreatest) {
					continue;
				}
				 // if the distance is greater than the nextGreatest distance
				if (distance < greatest && distance > nextGreatest) {
					// tries to make a square
					temp = square(input, temp);
					if (temp != null) {
						nextGreatest = distance;
						result = temp;
					}
					// if we couldn't make a square
					else if (numOfAs[rowNum] > 2) {
						int tempStart = start;
						int tempEnd = end;
						int tempDistance = distance;
						int num = numOfAs[rowNum];
						for (int orderStart = 1; orderStart < numOfAs[rowNum] && tempDistance > nextGreatest; orderStart++) {
							for (int orderEnd = 1; orderEnd < num-- && tempDistance > nextGreatest; orderEnd++) {
								temp = findAsLocation(input[rowNum], rowNum, tempStart, tempEnd);
								tempEnd = temp[3] - 1;
								tempDistance = Math.abs(temp[1] - temp[3]);
								if (tempDistance < greatest && tempDistance > nextGreatest) {
									// tries to make a square
									temp = square(input, temp);
									if (temp != null) {
										nextGreatest = distance;
										result = temp;
									}
								}
							}
							tempStart = tempEnd + 1;
							tempEnd = end;
							tempDistance = distance - tempDistance;
						}
					}
				}		
			}
		}
		return result;
	}	
}
