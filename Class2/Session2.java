package Class2;

public class Session2 {
	
	public static void main(String [] args)
	{
		String name = "Bob";
		if("Bob".contentEquals(name))
		{
			System.out.println("Bob equals name");
		}
		if(name.equals("Bob"))
		{
			System.out.println("name equals Bob");
			//it will throw a null pointer exception
		}
		if("Bob" == name)
		{
			System.out.println("Bob is name");
		}
		int count = 0;
		float floatCount = 0.0f;
		
		float total = 0f;
		
		for(int i = 0; i < 10; i++)
		{
			count++;
			floatCount += 0.1f;
		}
		
		System.out.println("Count: " + count);
		System.out.println("Float Count: " + floatCount);
		if(floatCount == 1)
		{
			System.out.println("It is actually 10");
		}
	}

}
