package com.pso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import com.pricing.PricingProblem;


public class Swarm {

	private double[] global_best_pos;
	private ArrayList<Particle> particles;
	private PricingProblem pricing_problem;
	private int n_items;
	private int n_particle;
	
	private ArrayList<Double> fittest_arr;

	/**
	 * Swarm will generate Particles each having a set of prices set
	 * in a range of £0.01 and £10.00 and velocity.
	 * 
	 * @param pricing_problem
	 * 			The evaluator that will validate and evaluate if price values
	 * 			are correct
	 * @param n_particle
	 * 			The number of particles to create
	 * @param n_items
	 * 			The number of price values each Particle will have
	 */
	public Swarm(PricingProblem pricing_problem, int n_particle, int n_items) {

		//set parameters
		this.pricing_problem = pricing_problem;
		this.n_items = n_items;
		this.n_particle = n_particle;

		//init the arraylist of particles, this is the swam
		particles = new ArrayList<Particle>(n_particle);

		//add n number of particles to the swarm
		for(int i = 0; i<n_particle; i++) {
			addParticle();		
		}

		this.fittest_arr = new ArrayList<Double>();

		
		//set initial global best
		this.initialiseGlobalBestPosition(this.getParticles());
		
		
	}


	/**
	 * This function creates an array of random price values constrained
	 * between £0.01 and £10.00
	 * 
	 * @param n_items
	 * 		The number of price values
	 * @return
	 */
	public double[] initialiseValidDesign(int n_items) {
		Random rand = new Random();

		//init design array
		double[] design = new double[n_items];

		//look for valid design
		while(!pricing_problem.is_valid(design)) {
			//if array is not valid reset array
			design = new double[n_items];

			//add rand values to design between 0 and the apeture size
			for(int i=0; i<design.length; i++) {
				//min + Math.random() * (max - min);
				design[i] = 0.01 + rand.nextDouble() * 10.00;
			}
		}
		return design;
	}


	/**
	 * This function initialises the velocity values for each price position.
	 * Each position is half the difference between 2 indexes in the prices arrays
	 * 
	 * @param n_items
	 * 		The number of price values
	 * @return
	 */
	public double[] initialiseVelocity(int n_items) {
		Random rand = new Random();

		//create 2 random prices
		double[] design_1 = initialiseValidDesign(n_items);
		double[] design_2 = initialiseValidDesign(n_items);

		//init velocity array
		double[] velocity = new double[n_items];

		//create velocity values for each position
		for(int i = 0; i<velocity.length; i++) {
			double delta = design_1[i] - design_2[i];
			velocity[i] = delta/2;	
		}
		return velocity;		
	}

	/**
	 * This function uses the initially created swarm of particles
	 * and find the particle with the prices position that generates
	 * the best revenue and sets this position as the global best.	 * 
	 * 
	 * @param particles
	 * 		The initial swarm of particles
	 */
	public void initialiseGlobalBestPosition(ArrayList<Particle> particles) {
		//iterate through each particle in the swarm
		for(Particle p : particles) {
			//if gbest is not set, set particle position as gbest
			if(this.global_best_pos == null) {
				this.setGlobalBest(p.getPosition());
			}
			//calc fitness of particle position
			double result = this.pricing_problem.evaluate(p.getPosition());

			//if particle position fitness is better than the gbest position fitness, set particle position as new gbest
			if(result > this.pricing_problem.evaluate(this.global_best_pos)) {
				this.setGlobalBest(p.getPosition());
			}
		}
		
		//add global best to fittest_arr for later analysis
		this.fittest_arr.add(new Double(this.getEvaluator().evaluate(this.getGlobalBest())));

	}

	/**
	 * Returns Particles in Swarm
	 * 
	 * @return
	 */
	public ArrayList<Particle> getParticles(){
		return particles;
	}

	/**
	 * @return The current global best price range
	 */
	public double[] getGlobalBest(){
		return this.global_best_pos.clone();
	}

	/**
	 * @param design
	 * 			The prices range that will be set to the new global best
	 */
	public void setGlobalBest(double[] design){
		this.global_best_pos = design;
	}

	
	public PricingProblem getEvaluator() {
		return this.pricing_problem;
	}
	
	
	public ArrayList getFittestArray() {
		return this.fittest_arr;
	}

	/**
	 * Adds a new particle to the swarm with a new valid design and velocity
	 */
	public void addParticle() {
		double[] design = initialiseValidDesign(this.n_items);
		double[] velocity = initialiseVelocity(this.n_items);
		Particle p = new Particle(this.pricing_problem, design, velocity);
		particles.add(p);	
	}

	/**
	 * 
	 * @param iterations
	 */
	public void search(int iterations) {

		PricingProblem pricing_problem = this.getEvaluator();

		for(int iter = 1; iter<=iterations ; iter++) {

			System.out.println("epoch #"+iter);
			for(Particle p : this.getParticles()) {

				p.updateVelocity(this.getGlobalBest(), this.n_particle, iter, iterations);
				this.updatePosition(p);

				//evaluate particle p position using antenna array
				double result = pricing_problem.evaluate(p.getPosition());
				//evaluate personal best position of particle
				double pbest_result = pricing_problem.evaluate(p.getPersonalBestPosition());
				//if new position is better than its personal best, set new position as personal best
				if(result > pbest_result) {
					p.setPersonalBestPosition(p.getPosition());
				}

				//evaluate global best position of all particles
				double gbest_result = pricing_problem.evaluate(this.getGlobalBest());
				//if pbest position is better than the global best, set pbest position as the global best of the swarm
				if(pbest_result > gbest_result) {
					this.setGlobalBest(p.getPersonalBestPosition());
					
					

					System.out.println("New best price range with a total revenue of " + this.pricing_problem.evaluate(this.getGlobalBest()));
				}

			}	
			
			
			//add global best to fittest_arr for later analysis
			this.fittest_arr.add(new Double(this.getEvaluator().evaluate(this.getGlobalBest())));
		}
	}

	/**
	 * This function takes a individual Particle in the Swarm and
	 * updates its array of positions. It updates each position one
	 * by one by adding the current position and its corresponding 
	 * velocity.
	 * 
	 * When each index is updated, the new positions array is validated to
	 * check if it conforms to the PricingProblems constraints (the prices
	 * must be between £0.01 and £10.00).
	 * 
	 * If a position index is updated and produced an invalid particle position,
	 * the position is not updated and instead it keeps its current position.
	 * 
	 * @param particle
	 * 			The particles that is going to have its position updated.
	 */
	public void updatePosition(Particle particle) {

		//take the current particle  and make a copy of it for its new position
		double[] newPosition = particle.getPosition();


		// calculate new price position using its velocity for each position index 
		for(int i = 0; i<particle.getPosition().length ; i++) {
			//update index
			newPosition[i] = particle.getPosition()[i] + particle.getVelocity()[i];

			//if updating an index does not return a valid range, reset index to original position
			if(!this.pricing_problem.is_valid(newPosition)) {
				newPosition[i] = particle.getPosition()[i];
			}
		}

		//after updating the position, set new position as current position
		particle.setPosition(newPosition);	
	}
}






