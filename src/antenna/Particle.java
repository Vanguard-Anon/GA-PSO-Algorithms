package antenna;

import java.util.Random;

public class Particle {

	private double position;
	private double velocity;
	private double perosnal_best_pos;


	public Particle(double pos, double global_best_pos) {

		Random rand = new Random();

		this.position = pos;
		this.perosnal_best_pos = this.position;
		this.velocity = rand.nextDouble() * (global_best_pos - 0) + 0;

	}

	public double getPosition() {
		return position;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getBestPosition() {
		return perosnal_best_pos;
	}
	
	public void setBestPosition(double value) {
		this.perosnal_best_pos = value;
	}

	public void updateVelocity(double global_best) {

		Random rand = new Random();

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
		this.velocity = inertia * this.getVelocity() + (phi1 * r1 * this.getBestPosition() - this.getPosition()) + (phi2 * r2 * global_best + this.getPosition());

	}

	public void updatePosition() {

		this.position = this.getPosition() + this.getVelocity();

	}

	@Override
	public String toString() {

		return "Particle [velocity = " + velocity + ", position = " + position + ", personal best position = " + perosnal_best_pos  + "]";
	}


}
