package antenna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Swarm {
	
	private double[] global_best_pos;
	private ArrayList<Particle> particles;
	private AntennaArray antenna_array;
	
	public Swarm(AntennaArray antenna_array, int n_particle, int n_antenna) {
		
		this.antenna_array = antenna_array;
//		particles = new double[n_particle];
		particles = new ArrayList<Particle>(n_particle);
		
		for(int i = 0; i<n_particle; i++) {
			double[] design = initialiseValidDesign(n_antenna);
			double[] velocity = initialiseVelocity();
			Particle p = new Particle(design, velocity);
			particles.add(p);			
		}
		
		this.global_best_pos = this.initialiseGlobalBestPosition();
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
	
	
	public double[] initialiseVelocity() {
		double[] velocity = new double[]{0.0,0.0,0.0};
		return velocity;		
	}
	
	public double[] initialiseGlobalBestPosition() {
		double[] velocity = new double[]{0.0,0.0,0.0};
		return velocity;		
	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public double[] getGlobalBest(){
		return this.global_best_pos;
	}
	
	public AntennaArray getArray() {
    	return this.antenna_array;
    }
	
	
	public void search(int duration) {
		
		long start= System.currentTimeMillis();
    	long end = start+(duration*1000);
    	
    	AntennaArray antenna_arr = this.getArray();
    	
    	while(System.currentTimeMillis() < end) {
    		   		
    		for(Particle p : this.getParticles()) {
    			
    			p.updateVelocity(this.getGlobalBest());
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
    			//if new position is better than the global best, set new position as the global best
    			if(result < gbest_result) {
    				p.setPersonalBestPosition(p.getPosition());
    			}
    		}	
    	}
	}
	
//	public double[] getPositions() {
//		double[] positions = new double[this.getParticles().size()];
//		int i = 0;
//		for(Particle p: this.getParticles()) {
//			positions[i] = p.getBestPosition();
//			i++;
//		}
//		return positions;
//	}
	
	
	
	
	
		
		
		
//			REPEAT UNTIL ( termination condition IS satisfied ) DO
//				UPDATE global best;
//				FOR EACH ( particle in population ) DO
//					1. UPDATE velocity and position;
//					2. EVALUATE new position;
//					3. UPDATE personal best;
//				OD
//			OD
		
	
	@Override
	public String toString() {
		return "Global Best = " + this.global_best_pos + "\n" + "Minimum SSL = " + this.getArray().evaluate(this.global_best_pos);
	}
		
		
}
	
	
	
	
	

