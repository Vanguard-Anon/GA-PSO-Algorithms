package com.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.pricing.PricingProblem;

public class Manager {

	private double[] optimal_prices;
	private double best_revenue;
	private ArrayList<double[]> population;

	private final int tournamentSize;
	private final double mutation_rate;
	private final double crossover_rate;

	private PricingProblem pricing_problem;

	private ArrayList<Double> fittest_arr;


	public Manager(int pop_size, int n_items, double mutation_rate, double crossover_rate) {

		//		this.pricing_problem = PricingProblem.randomInstance(n_items);
		this.pricing_problem = PricingProblem.courseworkInstance();

		//init variables
		this.best_revenue = Double.MAX_VALUE;
		this.population = new ArrayList<double[]>();
		this.mutation_rate = mutation_rate;
		this.crossover_rate = crossover_rate;
		this.tournamentSize = pop_size/2;

		// create individual price sets
		for (int i = 0; i < pop_size; i++) {
			double[] prices = this.initialiseValidPrices(n_items);
			this.population.add(prices);
		}

		this.fittest_arr = new ArrayList<Double>();


		initFittest();
	}

	/**
	 * This function creates an array of random price values constrained
	 * between £0.01 and £10.00
	 * 
	 * @param n_items
	 * 		The number of price values
	 * @return
	 */
	public double[] initialiseValidPrices(int n_items) {

		Random rand = new Random();

		//init design array
		double[] design = new double[n_items];

		//look for valid design
		while(!this.pricing_problem.is_valid(design)) {

			//if array is not valid reset array
			design = new double[n_items];

			//add rand values to design between 0 and the apeture size
			for(int i=0; i<design.length; i++) {
				//				min + Math.random() * (max - min);
				double randomValue = 0.01 + rand.nextDouble() * 10;
				design[i] = randomValue;
			}
		}

		return design;
	}


	/**
	 * From the starting population, the price range with the
	 * best revenue is set to the initial fittest
	 */
	public void initFittest() {

		//set first price set in population as the best
		this.optimal_prices = this.getPopulation().get(0);
		this.best_revenue = this.pricing_problem.evaluate(this.getPopulation().get(0));

		//for each price set in population check if it is better than current optimal tour
		for(double[] prices : this.getPopulation()) {

			//get revenue for current prices
			double revenue = this.pricing_problem.evaluate(prices);

			if(revenue > this.best_revenue) {
				this.optimal_prices = prices; 
				this.best_revenue = revenue;
			}
		}

		//add global best to fittest_arr for later analysis
		this.fittest_arr.add(new Double(this.pricing_problem.evaluate(this.optimal_prices)));
	}



	/**
	 * returns an arraylist of prices
	 * @return
	 */
	public ArrayList<double[]> getPopulation(){
		return this.population;
	}


	public double[] getOptimalPrices(){
		return this.optimal_prices;
	}

	public double getBestRevenue(){
		return this.best_revenue;
	}

	public void setBestCost(double new_best_revenue) {
		this.best_revenue = new_best_revenue;
	}

	public void setOptimalRoute(double[] newOptimalPrices) {
		this.optimal_prices = newOptimalPrices;
	}

	public ArrayList getFittestArray() {
		return this.fittest_arr;
	}


	/**
	 * 2-opt swap function.
	 * Given a price range is swaps all elements and evaluates them,
	 * if the swapped array is fitter than the current fittest, update. 
	 * 
	 * @param prices
	 * @return fittest
	 * 			The fittest price range in the neighbourhood
	 */
	public double[] twoOpt(double[] prices) {

		//create clone of prices array
		double[] new_prices = prices.clone();

		//create 2-opt neighbourhood and add initial range
		ArrayList<double[]> neighbourhood = new ArrayList<double[]>();
		neighbourhood.add(new_prices);

		//create 2-opt neighbourhood, no duplicates
		for ( int i = 0; i < new_prices.length-1; i++ ){
			for ( int j = i+1; j < new_prices.length; j++){

				//reverses array
				double[] reverse_prices = new_prices.clone();
				for(int a = 0; a < reverse_prices.length / 2; a++)
				{
					double temp = reverse_prices[a];
					reverse_prices[i] = reverse_prices[reverse_prices.length - a - 1];
					reverse_prices[reverse_prices.length - a - 1] = temp;
				}

				if(!neighbourhood.contains(new_prices) && !neighbourhood.contains(reverse_prices)) {
					neighbourhood.add(new_prices);
				}
				new_prices = this.twoOptSwap(i, j, prices);
			}
		}

		//set the copy of prices as the initial fittest
		double[] fittest = prices.clone();

		//for each prices array in the neighbourhood
		//initially there is only 1 prices array, the original prices array
		for(int i = 0 ; i<neighbourhood.size() ; i++) {

			//calc revenue of current fittest and current evaluated prices
			double fittest_revenue = this.pricing_problem.evaluate(fittest);
			double current_prices_revenue = this.pricing_problem.evaluate(neighbourhood.get(i));

			//if current evaluated prices is better than fittest update
			if(current_prices_revenue>fittest_revenue) {
				fittest = neighbourhood.get(i);
			}
		}
		return fittest;
	}

	/**
	 * Swaps array elements i and j in array prices and 
	 * returns that swapped array
	 * 
	 * @param i
	 * @param j
	 * @param prices
	 * @return
	 */
	public double[] twoOptSwap(int i, int j, double[] prices) {

		double[] prices_copy = prices.clone();

		double temp = prices_copy[i];
		prices_copy[i] = prices_copy[j];
		prices_copy[j] = temp;

		return prices_copy;
	}

	/** 
	 * Main function for evolving the population.
	 * 
	 * Adds the best solution to the new population then repeatedly 
	 * gets parents and combines/mutates until the new population
	 * is filled. If an new price range is better than the fittest, update.
	 * 
	 * @param population
	 */
	public void evolve(ArrayList<double[]> population) {

		//init the new population after the evolution iteration
		ArrayList<double[]> new_population = new ArrayList<double[]>();
		//clone of the best solution
		double[] best_prices_copy = this.getOptimalPrices().clone();

		//add the best solution of the initial population to the new population - initial survivor
		new_population.add(best_prices_copy);


		int start = new_population.size();
		//begin iterating from the 2nd index in the population, after the initial best survivor
		for(int i = start ; i<population.size() ; i++) {

			// parents
			//			ArrayList<double[]> parents = tournamentSelection(population);
			ArrayList<double[]> parents = roulette_selection(population);
			double[] parent1 = parents.get(0);
			double[] parent2 = parents.get(1);

			// child crossover
			double[] child = new double[parent1.length];
			if (Math.random() <= this.crossover_rate) {
				//				child = crossover_order1(parent1, parent2);
				child = crossover_midpoint(parent1, parent2);
			}

			//mutate the child
			double[] mutated = child;
			if (Math.random() <= this.mutation_rate) {
				mutated = this.mutate(child);
			}

			//calculate the new mutated cost
			double mutated_tour_cost = this.pricing_problem.evaluate(mutated);

			//if the mutated tour cost is better than the current best tour cost, update best
			if(mutated_tour_cost > this.getBestRevenue()) {

				this.setOptimalRoute(mutated); 
				this.setBestCost(mutated_tour_cost);

				System.out.println("New best revenue found: " + this.getBestRevenue());
			}

			//add the mutated child to the new population
			new_population.add(mutated);
		}

		//add global best from evolution iteration to fittest_arr for later analysis
		this.fittest_arr.add(new Double(this.pricing_problem.evaluate(this.optimal_prices)));


		this.population = new_population;
	}


	/**
	 * This function mutates a price range using 2-opt swap with mutation rate
	 * 
	 * @param prices
	 * 			The price range to be mutated
	 * @return The best price range for the 2-opt neighbourhood

	 */
	public double[] mutate(double[] prices) {
		return twoOpt(prices); 
	}


	/**
	 * This function takes a population and evaluates each
	 * price range to determines the sum of all the revenues
	 * and then the percentages of each revenue compared to 
	 * the sum of the revenues.
	 * 
	 * At random, a number between the max and min of the percentages
	 * is generated and the closest percentage to that random value
	 * will the selected as a parent. Do this twice to generate a
	 * pair.
	 * 
	 * @param population The population of elements
	 * @return List containing 2 parents
	 */
	private ArrayList<double[]> roulette_selection(ArrayList<double[]> population) {

		ArrayList<double[]> population_copy = (ArrayList<double[]>) population.clone();

		//init parents and add 1st two in population
		ArrayList<double[]> parents = new ArrayList<double[]>(2);

		//init array to hold the percentages of each revenue compared to the sum of the revenues
		double[] percentages = new double[population.size()];

		//determine the sum of revenues
		double sum = 0;
		for(double[] prices : population_copy) {
			sum += this.pricing_problem.evaluate(prices);
		}

		//calc percentages
		for(double[] prices : population_copy) {
			double percentage = (this.pricing_problem.evaluate(prices) / sum) * 100;
			percentages[population.indexOf(prices)] = percentage;	
		}

		//determine min and max
		Arrays.sort(percentages);
		double min = percentages[0];
		double max = percentages[percentages.length-1];

		//generate 2 parents
		for(int j = 0 ; j<2 ; j++) {

			//min + Math.random() * (max - min);
			double rand = min + Math.random() * (max - min);

			//calc diff between first elem and random number
			double delta = Math.abs(percentages[0] - rand);
			int index = 0;
			for(int i = 1; i < percentages.length; i++){
				//calc diff between element i and random number
				double i_delta = Math.abs(percentages[i] - rand);

				//if element i delta is closest than the current closest element, update
				if(i_delta < delta){
					index = i;
					delta = i_delta;
				}
			}
			//add closest element as parent
			parents.add(population.get(index));
		}
		return parents;
	}


	/**
	 * This function takes a subset of the population determined by the 
	 * tournament size and evaluates each element in that subset. The top
	 * 2 elements in the subset are selected as parents and returned for
	 * combining.
	 * 
	 * @param population The population of elements
	 * @return List containing 2 parents
	 */
	private ArrayList<double[]> tournamentSelection(ArrayList<double[]> population) {

		ArrayList<double[]> population_copy = (ArrayList<double[]>) population.clone();

		Collections.shuffle(population_copy);

		//init parents and add 1st two in population
		ArrayList<double[]> parents = new ArrayList<double[]>(2);
		parents.add(population_copy.get(0));
		parents.add(population_copy.get(1));


		//for subset of population determined by the tournament size
		//evaluate elements and top 2 are parents
		for (int i = 2; i < tournamentSize; i++) {

			double[] current_prices = population_copy.get(i);

			for(int j = 0; j <parents.size() ; j++) {


				double optimal_prices_revenue = this.pricing_problem.evaluate(parents.get(j));
				double current_prices_cost = this.pricing_problem.evaluate(current_prices);

				if(current_prices_cost > optimal_prices_revenue) {

					parents.set(j, current_prices);
					break;
				}
			}
		}

		return parents;
	}


	/**
	 * Order 1 crossover function 
	 * 
	 * @param parent1
	 * @param parent2
	 * @return child
	 */
	public double[] crossover_order1(double[] parent1, double[] parent2) {

		Random random = new Random();

		int size = parent1.length;

		// choose two random numbers for the start and end of segment
		int number1 = (int) (Math.random() * size);
		int number2 = (int) (Math.random() * size);

		// make the smaller the start and the larger the end
		int start = Math.min(number1, number2);
		int end = Math.max(number1, number2);


		double[] child = new double[size];

		//copy section of values from parent1
		for(int i = start; i<=end; i++) {
			child[i] = parent1[i];
		}

		//copy remaining values from parent 2 
		for(int i = 0; i<start ; i++) {
			if(child[i] == 0.0) {
				child[i] = parent2[i];
			}
		}
		for(int i = end; i<size ; i++) {
			if(child[i] == 0.0) {
				child[i] = parent2[i];
			}
		}

		return child;
	}

	/**
	 * 
	 * This crossover function takes the midpoint of each
	 * element in 2 parent arrays and adds it to a child array 
	 * 
	 * @param parent1
	 * @param parent2
	 * @return child
	 */
	public double[] crossover_midpoint(double[] parent1, double[] parent2) {

		Random random = new Random();

		int size = parent1.length;
		double[] child = new double[size];

		//calc midpoints
		for(int i = 0 ; i<size; i++) {
			double midpoint = (parent1[i] + parent2[i]) / 2;
			child[i] = midpoint;
		}

		return child;
	}

}



