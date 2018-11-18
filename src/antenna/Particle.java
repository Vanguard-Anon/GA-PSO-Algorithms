package antenna;

import java.util.Arrays;
import java.util.Random;

public class Particle {

	private double[] position;
	private double[] velocity;
	private double[] perosnal_best_pos;
	private AntennaArray antenna_array;
	private double aperture_size;
	private final double initial_inertia = 0.9;
	private final double final_inertia = 0.4;


	public Particle(AntennaArray antenna_array, double[] position, double[] velocity) {

		this.antenna_array = antenna_array;
		this.aperture_size = position[position.length-1];
		
		this.position = position;
		this.perosnal_best_pos = this.position;
		this.velocity = velocity;
	}

    /**
     * Get a copy of the position of the particle.
     */
	public double[] getPosition() {
		return position.clone();
	}
	
	 /**
     * Assign new position value to the particle.
     */
	public void setPosition(double[] position) {
		this.position = position;
	}

    /**
     * Get a copy of the velocity of the particle.
     */
	public double[] getVelocity() {
		return velocity.clone();
	}
	
	 /**
     * Assign new velocity value to the particle.
     */
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	 /**
     * Get a copy of the personal best solution.
     */
	public double[] getPersonalBestPosition() {
		return perosnal_best_pos.clone();
	}
	
	public void setPersonalBestPosition(double[] new_position) {
		this.perosnal_best_pos = new_position;
	}

	public void updateVelocity(double[] global_best, int n_particle, int iteration, int max_iteration) {
				
//		double[] newVelocity = new double[this.velocity.length];
		double[] newVelocity = this.getVelocity();

		//linear decreasing intera
//		float inertia = (float) ((initial_inertia - final_inertia) * (max_iteration - (iteration / max_iteration)) + final_inertia);
		double inertia = 0.5 + (Math.random()/2);
							
		//for each element, update
		for(int i = 0; i<global_best.length; i++) {

			//cognative strength coefficient can be negative
			double phi1 = 0.1;
			//random vector, avoids deterministic alg that will get stuck, explore more of the problem space
			double r1 = Math.random();

			//higher phi2, low phi1 means particles dedicating searching for solution in gbest region of space, example of exploitation
			//higher phi1, low phi1 means particles attacted to their personal best and not the global best, swarm less quiick to converge on global best and more likely to search their own pbest regions,  good for searching for good solutions over the search space
			double phi2 = 0.1;
			double r2 = Math.random();
			
			//															cognative attraction, attraction to pbest                    social attraction of particles to best pos 
			double velocity = (inertia * this.getVelocity()[i]) + (phi1 * r1 * (this.getPersonalBestPosition()[i] - this.getPosition()[i])) + (phi2 * r2 * (global_best[i] - this.getPosition()[i]));
		
			//add to new velocity array
			newVelocity[i] = velocity;
		}
		
		this.setVelocity(newVelocity);
		
	}
	
	@Override
	public String toString() {
		return "Particle [velocity = " + velocity + ", position = " + position + ", personal best position = " + perosnal_best_pos  + "]";
	}

}
