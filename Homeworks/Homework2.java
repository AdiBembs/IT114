package Homeworks;

public class Homework2 {
	
	public int key;
	public String value;
	
	public Homework2(int k, String v)
	{
		this.key = k;
		this.value = v;
	}

	@Override
	public String toString()
	{
		return "{'key':'" + this.key + "', 'value':'" + this.value + "'}";
	}

}
