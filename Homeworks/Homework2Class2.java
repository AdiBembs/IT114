package Homeworks;

import java.util.*;

public class Homework2Class2 {
	
	public static void main(String [] args)
	{
		Queue<Homework2> queue = new LinkedList<Homework2>();
		for(int i = 0; i < 10; i++) {
			queue.add(new Homework2(i, "A Value"));
		}
		
		System.out.println("Show queue: " + queue);
		
		Homework2 first = queue.remove();
		System.out.println("Pulled first: " + first);
		System.out.println("Show altered queue: " + queue);
		
		Homework2 peek = queue.peek();
		System.out.println("Just viewing: " + peek);
		System.out.println("Show unaltered queue: " + queue);
		
		Homework2 poll = queue.poll();
		System.out.println("Head of the queue(if empty, returns null): " + poll);
		System.out.println("Show altered queue: " + queue);
		
		int size = queue.size(); 
		System.out.println("Size of the queue: " + size);
		System.out.println("Show unaltered queue: " + queue);
	}

}
