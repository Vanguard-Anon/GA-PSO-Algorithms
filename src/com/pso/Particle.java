package com.pso;

import com.pricing.PricingProblem;

public class Particle {

	private double[] position;
	private double[] velocity;
	private double[] perosnal_best_pos;
	private PricingProblem pricing_problem;
	private final double initial_inertia = 0.9;
	private final double final_inertia = 0.4;
	
	private double z;


	public Particle(PricingProblem pricing_problem, double[] position, double[] velocity) {

		this.pricing_problem = pricing_problem;
		
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

	
	/**
	 * The function updates the velocity of the particle based on its
	 * current position, the global best position of the swarm and
	 * some values.
	 * 
	 * There are a few ways to set the inertia component;
	 * 1) Linear Decreasing
	 * 			When inertia is big, particle swarm trend to global search. 
	 * 			When inertia is small, particle swarm trend to local search. 
	 * 			With each iteration, the magnitude of the inertia decreases. This
	 * 			means as the swarm iteration increase, it will switch from trending
	 * 			away from the global best and toward the local best. In other terms,
	 * 			It will switch from exploration of the global space and instead
	 * 			focus on the exploitation of the local area around its personal best.
	 * 2)  Chaotic
	 * 			Chaotic optimization has better characteristics for mountain-climbing and
	 * 			getting away from the local optimum, therefore chaotic optimization is 
	 * 			better than the stochastic search means
	 * 3) Random
	 * 			Yeet
	 * 
	 * @param global_best
	 * 			The global best price range 
	 * @param n_particle
	 * 			The number of particles in swarm
	 * @param iteration
	 * 			The current iteration of the swarm
	 * @param max_iteration
	 * 			The total number of iterations the swarm have
	 */
	public void updateVelocity(double[] global_best, int n_particle, int iteration, int max_iteration) {
				
		//init new velocity array
		double[] newVelocity = new double[global_best.length];

		//linear decreasing interia
//		double inertia = (initial_inertia - final_inertia) * (max_iteration - iteration) / max_iteration + final_inertia;

		//chaotic inertia
		//set variable for chaotic inertia
		double z = Math.random();
		z = (4.0 * z) * (1-z);
		double inertia = (initial_inertia - final_inertia) * (max_iteration - iteration) / max_iteration + (final_inertia * z);
		
		//random inertia
//		double inertia = 0.5 + (Math.random()/2);
							
		//for each element, update
		for(int i = 0; i<global_best.length; i++) {

			//cognative strength coefficient can be negative
			double phi1 = Math.log(2)+0.5;
			//random vector, avoids deterministic alg that will get stuck, explore more of the problem space
			double r1 = Math.random();

			//higher phi2, low phi1 means particles dedicating searching for solution in gbest region of space, example of exploitation
			//higher phi1, low phi1 means particles attacted to their personal best and not the global best, swarm less quiick to converge on global best and more likely to search their own pbest regions,  good for searching for good solutions over the search space
			double phi2 = Math.log(2)+0.5;
			double r2 = Math.random();
			
			//															cognative attraction, attraction to pbest                    social attraction of particles to best pos 
			double velocity = (inertia * this.getVelocity()[i]) + (phi1 * r1 * (this.getPersonalBestPosition()[i] - this.getPosition()[i])) + (phi2 * r2 * (global_best[i] - this.getPosition()[i]));
		
			//add to new velocity array
			newVelocity[i] = velocity;
		}
		
		//set velocity
		this.setVelocity(newVelocity);			
	}
}
