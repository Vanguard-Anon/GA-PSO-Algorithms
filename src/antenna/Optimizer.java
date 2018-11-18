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
	
	public Optimizer(int n_particle, int n_antenna, double antenna_angle) {
				
		this.antenna = new AntennaArray(n_antenna, antenna_angle);
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
		
		Optimizer op = new Optimizer(22, 5, 90.0);
		
		Swarm swarm = op.getSwarm();
		
		swarm.search(30);
		
		double[] result = swarm.getGlobalBest();
		
		System.out.print("Final global best = ");
		for(int i = 0; i<result.length ; i++) {
			System.out.print(result[i] + ", ");
		}
		System.out.println(swarm.toString());
		
	}

}
