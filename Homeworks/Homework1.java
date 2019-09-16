package Homeworks;
import java.util.*;

public class Homework1 {
	
	public static void main(String [] args)
	{
		//Question 1 and Question 2
		List<String> animalString = new ArrayList<String>();
		animalString.add("Eel");
		animalString.add("Cat");
		animalString.add("Dog");
		animalString.add("Fish");
		animalString.add("Lizard");
		animalString.add("Ant");
		
		Collections.sort(animalString, Collections.reverseOrder());
		System.out.println("Reverse Order: ");
		for(String z:animalString)
		{
			System.out.print(z + ", ");
		}
		
		//Question 3
		Collections.shuffle(animalString);
		System.out.println(" ");
		System.out.println("Shuffle: ");
		for(String x:animalString)
		{
			System.out.print(x + ", ");
		}
		
		//Question 4
		List<Integer> IntegerList = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
		{
			IntegerList.add(i);
		}
		
		int counter = 0;
		for(int i:IntegerList)
		{
			counter += i;
		}
		System.out.println(" ");
		System.out.println("Integer Sum of the ArrayList = " + counter);
		
		for(int x: IntegerList)
		{
			if(x == 0)
			{
				System.out.println(x + " is zero");
			}
			if(x%2 == 0 && x != 0)
			{
				System.out.println(x + " is an even number");
			}
			else if(x%2 == 1 && x != 0)
			{
				System.out.println(x + " is an odd number");
			}
		}
		
		//Question 5
		for(int i = animalString.size() - 1; i > 0; i--)
		{
			int rand = (int)(Math.random() * (animalString.size() - 1)) + 1; //creates a random integer from 1 - last index in animal string
			String temp = animalString.get(i); //Assigns a temporary string to the current index of animalString
			animalString.set(i, animalString.get(rand));
			animalString.set(rand, temp);
			//The previous two lines (69-70) is assigning the value such that the strings within animalString is shuffled
		}
		
		System.out.print("");
		System.out.println("Shuffled list: " + animalString); //Print the new shuffled string
	}

}
