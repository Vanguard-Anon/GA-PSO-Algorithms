import java.util.Random;
import java.util.Arrays; 
import java.lang.Math.*;


public class main {

	
	private static double[] antennae_array;
	private static Random rand;
	private static AntennaArray antenna;
	
	
	public static double[] initialiseValidDesign(double array_size) {
		

		antennae_array = new double[(int) array_size];
		
		int apeture_size = (int) Math.ceil(array_size/2);
		System.out.println(apeture_size);
		
		while(!antenna.is_valid(antennae_array)) {
			
			
			for(int i=0; i<antennae_array.length-1; i++) {
				
				double randomValue = 0 + (apeture_size - 0) * rand.nextDouble();

				antennae_array[i] = randomValue;
				
//				System.out.println(antennae_array[i]);

			}
			
			antennae_array[antennae_array.length-1] = apeture_size;
			
	        Arrays.sort(antennae_array); 

		
			
			if(!antenna.is_valid(antennae_array)) {
				

				antennae_array = new double[(int) array_size];

			}
			
			
		}
			
		
		
		
		
	for(double value : antennae_array) {
		System.out.println(value);
	}
		
		
		
		
		return antennae_array;
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) {

		rand = new Random();

		
		antenna = new AntennaArray(4, 90.0);
		
		double[] array = initialiseValidDesign(4);
		
		System.out.println("done");
//		double[] array = {0.5, 1.0, 1.5};
//		
//		double peakSSL = antenna.evaluate(antennae_array);
//		
//		System.out.println(peakSSL);

	}

}
