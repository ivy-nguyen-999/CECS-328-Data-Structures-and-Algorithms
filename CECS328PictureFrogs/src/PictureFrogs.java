import java.io.*;
import java.util.*;
import java.lang.Math;

public class PictureFrogs {
	public static void main(String[] args) throws IOException {
		
		File inputFile = new File ("input.txt"); // creates File instance to reference text file in Java		
		Scanner reader = new Scanner(inputFile); // creates Scanner instance to read File in java
		
		int numOfFrogs = Integer.parseInt(reader.nextLine()); // first line: number of frogs
		int numOfPics = Integer.parseInt(reader.nextLine()); // second line: number of step increment
		int numOfSteps = numOfPics * numOfFrogs;
		int[] steps = new int[numOfSteps];
		char[][] pictures = new char[numOfPics][numOfFrogs]; // +: 43, -: 45
		
		// add all the steps into an array
		for (int step = 0; step < numOfSteps; step++) {steps[step] = Integer.parseInt(reader.nextLine());}
		// sort the array: from smallest to largest
		Arrays.sort(steps);
		
		// add all instructions into a 2D char array
		for (int pic = 0; pic < numOfPics; pic++) {
			String instruction = reader.nextLine();
			for (int frog = 0; frog < numOfFrogs; frog++) {pictures[pic][frog] = instruction.charAt(frog);}
		}
		reader.close(); // close reader
		
		// finding when the frog change the sides
		int[] changingTime = new int[numOfFrogs];
		for (int frog = 0; frog < numOfFrogs; frog++) {
			for (int pic = 1; pic < numOfPics; pic++) {
				if ((pictures[pic][frog] == '-' && pictures[pic - 1][frog] == '+')||
					(pictures[pic][frog] == '+' && pictures[pic - 1][frog] == '-')) {
					changingTime[frog] += 1;
				}
			}
		}
		
		// output
		int[] output = new int[numOfSteps];
		for (int frog = 0; frog < numOfFrogs; frog++) {
			int position = 0;
			int index = frog;
			int stay = 0;
			int change = 0;
			for (int pic = 0; pic < numOfPics; pic++) {
				// starting position
				if (pic == 0) {
					int stepsIndex = (frog * numOfPics) + (numOfPics - changingTime[frog] - 1);
					output[index] = startingPosition(steps, pictures[pic][frog], stepsIndex);
				}
				// if the frog stays in one side and doesn't change
				else if ((position < 0 && pictures[pic][frog] == '-') || (position > 0 && pictures[pic][frog] == '+')) {
					stay++;
					int stepsIndex = (frog * numOfPics) + (numOfPics - changingTime[frog] - 1 - stay);
					output[index] = stayTheSame(steps, position, stepsIndex, output[frog]);
				}
				// if the frog switches the side
				else {
					change++;
					int stepsIndex = (frog * numOfPics) + (numOfPics - changingTime[frog] - 1 + change);
					output[index] = switchSides(steps, position, stepsIndex);
				}
				position += output[index]; // update the position of the frog
				index += numOfFrogs; // increment the index of the output array
			}
		}
		
		FileWriter fileWriter = new FileWriter(new File("output.txt")); // create a text file
		BufferedWriter writer = new BufferedWriter(fileWriter); // wrap FileWriter in BufferedWriter
		
		// write out the output
		for (int pic = 0; pic < numOfPics; pic++) {
			writer.write("" + output[pic * numOfFrogs]);
			for (int frog = 1; frog < numOfFrogs; frog++) {
				int index = frog + (pic * numOfFrogs);
				writer.write("," + output[index]);
			}
			writer.newLine();
		}
		writer.close();
	}
	
	public static int startingPosition(int[] steps, char place, int index) {
		if (place == '+') {return steps[index];}
		else {return (-1) * steps[index];}
	}
	
	public static int stayTheSame(int[] steps, int position, int index, int origin) {
		if (Math.abs(position) < steps[index]) {
			if (position > 0) {return steps[index];}
			else {return (-1) * steps[index];}
		}
		else {
			if (position > 0) {return (-1) * steps[index];}
			else {return steps[index];}
		}
	}
	
	public static int switchSides(int[] steps, int position, int index) {
		if (position > 0) {return (-1) * steps[index];}
		else {return steps[index];}
	}
	
}
