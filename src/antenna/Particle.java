package antenna;

import java.util.Arrays;
import java.util.Random;

public class Particle {

	private double[] position;
	private double[] velocity;
	private double[] perosnal_best_pos;

	public Particle(double[] position, double[] velocity) {

		this.position = position;
		this.perosnal_best_pos = this.position;
		this.velocity = velocity;
	}

	public double[] getPosition() {
		return position;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public double[] getPersonalBestPosition() {
		return perosnal_best_pos;
	}
	
	public void setPersonalBestPosition(double[] new_position) {
		this.perosnal_best_pos = new_position;
	}

	public void updateVelocity(double[] global_best) {
		
		Random rand = new Random();
	
		for(int i = 0; i<global_best.length ; i++) {
			double inertia = 0.6;
			//cognative strength coefficient can be negative
			double phi1 = 0.2;
			//random vector, avoids deterministic alg that will get stuck, explore more of the problem space
			double r1 = Math.random();

			//higher phi2, low phi1 means particles dedicating searching for solution in gbest region of space, example of exploitation
			//higher phi1, low phi1 means particles attacted to their personal best and not the global best, swarm less quiick to converge on global best and more likely to search their own pbest regions,  good for searching for good solutions over the search space
			double phi2 = 0.6;
			double r2 = Math.random();
			
			//															cognative attraction, attraction to pbest                    social attraction of particles to best pos 
			double velocity = inertia * this.getVelocity()[i] + (phi1 * r1 * this.getPersonalBestPosition()[i] - this.getPosition()[i]) + (phi2 * r2 * global_best[i] + this.getPosition()[i]);
		
			this.velocity[i] = velocity;
		}
	}

	public void updatePosition() {
		
		for(int i = 0; i<this.getPosition().length ; i++) {
			double position = this.getPosition()[i] + this.getVelocity()[i];
			this.position[i] = position;
		}
	}

	@Override
	public String toString() {
		return "Particle [velocity = " + velocity + ", position = " + position + ", personal best position = " + perosnal_best_pos  + "]";
	}

}
