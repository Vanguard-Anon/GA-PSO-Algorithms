package antenna;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays; 
import java.lang.Math.*;


public class Optimizer {

	
	private static double[] antennae_array;
	private static Random rand;
	private AntennaArray antenna;
	private Swarm swarm;
	private double best_result;

	
	public Optimizer() {
		
		int n_antenna = 3;
		this.antenna = new AntennaArray(n_antenna, 90.0);
		
		double[] antenna_array = this.initialiseValidDesign(n_antenna);
		
		this.swarm = new Swarm(0.5, antenna_array);
		
		this.best_result = this.antenna.evaluate(swarm.getPositions());
	}
	
	
	public double[] initialiseValidDesign(double array_size) {
		
		antennae_array = new double[(int) array_size];
    	
    			
    	double apeture_size = array_size/2;
		
		while(!antenna.is_valid(antennae_array)) {
			
			for(int i=0; i<antennae_array.length-1; i++) {
				
				double randomValue = 0 + (apeture_size - 0) * rand.nextDouble();

				antennae_array[i] = randomValue;
				
			}
			
			antennae_array[antennae_array.length-1] = apeture_size;
	        Arrays.sort(antennae_array); 

			if(!antenna.is_valid(antennae_array)) {

				antennae_array = new double[(int) array_size];

			}
		}
	
		return antennae_array;
	}
	
	
	public void search(int duration) {
				
		long start= System.currentTimeMillis();
    	long end = start+(duration*1000);
    	
    	AntennaArray antenna_arr = this.getArray();
    	Swarm swarm = this.getSwarm();
    	
    	while(System.currentTimeMillis() < end) {
    		   		
    		
    		for(Particle p : swarm.getParticles()) {
    			
    			p.updateVelocity(swarm.getGlobalBest());
    			p.updatePosition();
    			
    			double result = antenna_arr.evaluate(swarm.getPositions());
    			
    			if(result < this.best_result) {
    				p.setBestPosition(p.getPosition());
    			}

    			
    		}
    		
    		
    		
    	}
	}
	
    	
    public void addSwarm(Swarm swarm) {
    	this.swarm = swarm;
    }
    
    public Swarm getSwarm() {
    	return this.swarm;
	}
    
    public AntennaArray getArray() {
    	
    	return this.antenna;
    	
    }
    
    public String toString() {
    	return "minimum SSL is: " + this.best_result;
    }
	
	
	public static void main(String[] args) {

		rand = new Random();

		
		
		Optimizer op = new Optimizer();
	
		
		
		op.search(10);
		
		ArrayList<Particle> particles = op.getSwarm().getParticles();
		
		for(Particle p : particles) {
			System.out.println(p.toString());
		}
		
		System.out.println(op.toString());
		
		
		
		

	}

}
