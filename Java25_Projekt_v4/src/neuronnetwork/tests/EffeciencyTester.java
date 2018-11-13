package neuronnetwork.tests;

import org.junit.Test;

public class EffeciencyTester 
{
	@Test
	public void testLoop1()
	{
	double time = System.nanoTime();
	
	long sum = 0;
	for(int i = 1; i < 10000; i++)
	{
		long temp = 0; 
		for(int j = 1; j < 1000; j++)
		{
			temp+=j;
		}
		sum+=temp;
	}
	System.out.println(sum);
	System.out.println("W1 "+((System.nanoTime()-time)/1000000)+"ms");
	}
	
	@Test
	public void testLoop2()
	{
	double time = System.nanoTime();
	
	long sum = 0;
	long temp = 0; 
	
	for(int i = 1; i < 10000; i++)
	{
		temp = 0;
		for(int j = 1; j < 1000; j++)
		{
			temp+=j;
		}
		sum+=temp;
	}
	System.out.println(sum);
	System.out.println("W2 "+((System.nanoTime()-time)/1000000)+"ms");
	}
}
