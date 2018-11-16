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
	private double[] best_result;

	
	public Optimizer(int n_particle, int n_antenna) {
				
		this.antenna = new AntennaArray(n_antenna, 90.0);
		this.swarm = new Swarm(this.antenna, n_particle, n_antenna);
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
	
	public static void main(String[] args) {
		
		Optimizer op = new Optimizer(15, 3);
		
		Swarm swarm = op.getSwarm();
		
		swarm.search(10);

				
		System.out.println(swarm.toString());
		
	}

}
