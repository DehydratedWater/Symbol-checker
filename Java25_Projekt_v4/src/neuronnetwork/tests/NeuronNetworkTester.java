package neuronnetwork.tests;

import java.util.Arrays;

import org.junit.Test;

import neuronnetwork.NeuronNetwork;
import neuronnetwork.pattern.SigmoidFunction;

public class NeuronNetworkTester 
{
	@Test
	public void creatingNeuronNetwork()
	{
		System.out.println("Test tworzenia i dzia³ania sieci");
		NeuronNetwork nn = new NeuronNetwork(new int[] {2, 4, 2}, 0.6, new SigmoidFunction());
		double[] input = new double[] {1, 0};
		double[] output = nn.calculateNetwork(input);
		
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(output));
		output = nn.calculateNetwork(input);
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(output));
		output = nn.calculateNetwork(input);
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(output));
	}
	
	@Test
	public void learnNeuronNetworkOrTest()
	{
		NeuronNetwork nn = new NeuronNetwork(new int[] {2, 6, 1}, 0.8, new SigmoidFunction());
		System.out.println("Uczenie sieci neuronowej dzia³ania bramki or");
		System.out.println("Przed uczeniem");
		double[] input = null;
		input = new double[] {0,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {1,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {0,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {1,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		
		input = new double[] {0,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {1,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {0,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {1,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		
		double time = System.nanoTime();
		for(int k = 0; k < 100; k++)
		{
			for(int i = 1; i <= 5; i++)
				orLearn(nn, 1.0/i, 1.0/i);
			
			for(int i = 1; i <= 10; i++)
				orLearn(nn, 1.0/i, 1.0);
			
			for(int i = 5; i <= 10; i++)
				orLearn(nn, 1.0/i, 1.0/i);
			
			for(int i = 1; i <= 10; i++)
				orLearn(nn, 1.0, 1.0/i);
		}
		System.out.println("Uczenie: Time: "+((System.nanoTime()-time)/1000000)+"ms");	
	
	
		
		input = new double[] {0,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {1,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {0,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		input = new double[] {1,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Arrays.toString(nn.calculateNetwork(input)));
		
		input = new double[] {0,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {1,0};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {0,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		input = new double[] {1,1};
		System.out.println("Dla: "+Arrays.toString(input)+" -> "+Math.round(nn.calculateNetwork(input)[0]));
		//System.out.println(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
		System.out.println();
	}

	private void orLearn(NeuronNetwork nn, double in1, double in2) {
		double out = 0;
		if(in1>0.5||in2>0.5)
			out = 1;
		//System.out.println(in1+" "+in2+"->"+out);
		nn.learnNetwork(new double[] {in1,in2}, new double[] {out});
	}
}
