package antenna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Swarm {
	
	private double[] global_best_pos;
	private ArrayList<Particle> particles;
	private AntennaArray antenna_array;
	private int n_antenna;
	private int n_particle;

	
	public Swarm(AntennaArray antenna_array, int n_particle, int n_antenna) {
		
		this.antenna_array = antenna_array;
		this.n_antenna = n_antenna;
		this.n_particle = n_particle;

		particles = new ArrayList<Particle>(n_particle);
		
		for(int i = 0; i<n_particle; i++) {
			double[] design = initialiseValidDesign(n_antenna);
			double[] velocity = initialiseVelocity(n_antenna);
			Particle p = new Particle(antenna_array, design, velocity);
			particles.add(p);			
		}
		
		this.global_best_pos = this.initialiseGlobalBestPosition(this.getParticles());
		
		System.out.print("Initial global best position = ");
		for(int i = 0; i<this.global_best_pos.length ; i++) {
			System.out.print(this.global_best_pos[i] + ", ");
		}
		System.out.println("Initial gbest Minimum SSL = " + this.getArray().evaluate(this.global_best_pos));
	}
	
	public double[] initialiseValidDesign(int n_antenna) {
		
		Random rand = new Random();

		//init design array
		double[] design = new double[n_antenna];
		//calc apeture_size
    	double apeture_size = (double)n_antenna/2;
		
    	//look for valid design
		while(!antenna_array.is_valid(design)) {
			
	        //if array is not valid reset array
			design = new double[n_antenna];

			//add rand values to design between 0 and the apeture size
			for(int i=0; i<design.length; i++) {
				double randomValue = 0 + (apeture_size - 0) * rand.nextDouble();
				design[i] = randomValue;
			}
			
			//make last element the apeture size and then sort array
			design[design.length-1] = apeture_size;
	        Arrays.sort(design); 
		}
	
		return design;
	}
	
	
	public double[] initialiseVelocity(int n_antenna) {
		
		Random rand = new Random();
		
		double[] velocity = new double[n_antenna];
		
		for(int i = 0; i<velocity.length; i++) {
			double randomVelocity = 0 + (n_antenna/2 - 0) * rand.nextDouble();
			velocity[i] = randomVelocity;	
		}
		return velocity;		
	}
	
	public double[] initialiseGlobalBestPosition(ArrayList<Particle> particles) {
		
		double[] gbest = new double[this.getNAntenna()];
		AntennaArray antenna_array = this.getArray();
		
		for(Particle p : particles) {
			
			//if gbest is not set, set particle position as gbest
			if(gbest == null) {
				gbest = p.getPosition();
			}
			
			//calc fitness of particle position
			double result = antenna_array.evaluate(p.getPosition());
			
			//if particle position fitness is better than the gbest position fitness, set particle position as new gbest
			if(result < antenna_array.evaluate(gbest)) {
				gbest = p.getPosition();
			}
		}
		
		return gbest;		
	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public double[] getGlobalBest(){
		return this.global_best_pos;
	}
	
	public void setGlobalBest(double[] design){
		this.global_best_pos = design;
	}
	
	public AntennaArray getArray() {
    	return this.antenna_array;
    }
	
	public int getNAntenna() {
		return this.n_antenna;
	}
	
	
	public void search(int duration) {
		
    	AntennaArray antenna_arr = this.getArray();
    	
    	for(int iter = 1; iter<=duration ; iter++) {

    		for(Particle p : this.getParticles()) {
    			
    			p.updateVelocity(this.getGlobalBest(), this.n_particle, iter, duration);
    			p.updatePosition();
    			
    			//evaluate particle p position using antenna array
    			double result = antenna_arr.evaluate(p.getPosition());
    			//evaluate personal best position of particle
    			double pbest_result = antenna_arr.evaluate(p.getPersonalBestPosition());
    			//if new position is better than its personal best, set new position as personal best
    			if(result < pbest_result) {
    				p.setPersonalBestPosition(p.getPosition());
    			}
    			
    			//evaluate global best position of all particles
    			double gbest_result = antenna_arr.evaluate(this.getGlobalBest());
    			//if pbest position is better than the global best, set pbest position as the global best of the swarm
    			if(pbest_result < gbest_result) {
    				this.setGlobalBest(p.getPersonalBestPosition());
    				
    				System.out.print("has new position updated = ");
        			for(int i = 0; i<p.getPosition().length ; i++) {
        				System.out.print(p.getPersonalBestPosition()[i] + ", ");
        			}
        			System.out.println("updated? position Minimum SSL = " + this.antenna_array.evaluate(p.getPersonalBestPosition()));
    			}
    			
    		
    			
    			
    		}	
    	}
	}
	
	@Override
	public String toString() {
		return "Minimum SSL = " + this.getArray().evaluate(this.global_best_pos);
	}
		
		
}
	
	
	
	
	

