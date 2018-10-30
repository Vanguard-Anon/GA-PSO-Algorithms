

public class main {

	public static void main(String[] args) {

		AntennaArray antenna = new AntennaArray(3, 90.0);
		
		double[] array = {0.3, 0.9, 1.5};
		
		double peakSSL = antenna.evaluate(array);
		
		System.out.println(peakSSL);

	}

}
