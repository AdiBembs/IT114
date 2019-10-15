package Homeworks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileSystemSample {
	public void createFileAndGetDetails(String fileName) {
		try {
			File fileReference = new File(fileName);
			if(fileReference.createNewFile()) {
				System.out.println("Didn't exist, created new");
			}
			else {
				System.out.println("File already exists");
			}
		}
			catch(IOException ie) {
			ie.printStackTrace();
		
		}
	}
	public void writeToFile(String fileName, String msg) {
		//Hint: use BufferedWriter for less IO operations (better performance)
		try(FileWriter fw = new FileWriter(fileName)){
			fw.write(msg);
			System.out.println("Wrote " + msg + " to " + fileName);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readFromFile(String fileName) {
		File file = new File(fileName);
		try(Scanner reader = new Scanner(file)){
			String fullText = "";
			while (reader.hasNextLine()) {
				String nl = reader.nextLine();
				System.out.println("Next line: " + nl);
		        fullText += nl;
		        //Scanner.nextLine() returns the line but excludes the line separator
		        //so just append it back so it'll show correctly in the console
		        if(reader.hasNextLine()) {//just a check to not append an extra line ending at the end
		        	fullText += System.lineSeparator();
		        }
		    }
			System.out.println("Contents of " + fileName + ": ");
			System.out.println(fullText);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "TestFile.txt";
		FileSystemSample fss = new FileSystemSample();
		fss.createFileAndGetDetails(fileName);
		fss.writeToFile(fileName, "Hello world! We're writing to files");
		fss.readFromFile(fileName);
	
		String convertStrings = fileName.replaceAll("[1-9]", "");
		String removeSpace = convertStrings.trim().replaceAll(" +", ", ");
		
	
		String newText = fileName.replaceAll("[a-zA-Z]", " ");
		String newTextSpace = newText.trim().replaceAll(" +", ",");
		String[] stringArray = newTextSpace.split(",");
        int[] intArray = new int[stringArray.length];
             for (int i = 0; i < stringArray.length; i++) {
                 //String numeratorString = stringArray[i];
            	 String[] newWord = stringArray[i].replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                 intArray[i] = (int) Integer.parseInt(newWord[i]);
          }
          
          System.out.println("Strings in file: " + removeSpace);
          System.out.println("Number of integers: " + intArray.length);

          System.out.print("Integers in file: ");
          for (int number : intArray) {
              System.out.print(number + ", ");
      }

		
	}

}
