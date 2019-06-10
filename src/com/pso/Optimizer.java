package com.pso;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.pricing.PricingProblem;

import au.com.bytecode.opencsv.CSVWriter;


public class Optimizer {

	private static double[] antennae_array;
	private static Random rand;
	private PricingProblem pricingProblem;
	private Swarm swarm;

	/**
	 * @param n_particle
	 * 			The number of particles to  create
	 * @param n_items
	 * 			The number of price values each Particle will have
	 */
	public Optimizer(int n_particle, int n_items) {

		//				this.pricingProblem = PricingProblem.randomInstance(n_items);
		this.pricingProblem = PricingProblem.courseworkInstance();

		this.swarm = new Swarm(this.pricingProblem, n_particle, n_items);
	}	

	public Swarm getSwarm() {
		return this.swarm;
	}




	public static void main(String[] args) throws IOException {

		//data for analysis
		ArrayList<Double> max = new ArrayList<Double>();
		ArrayList<Double> initial = new ArrayList<Double>();
		ArrayList<Double> increases = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> iteration_revenues = new ArrayList<ArrayList<Double>>();




		//number of search iteration for swarm
		int iterations = 100;
		//number of times algorithm ran for analysis purposes
		int n_epochs = 1;
		//swarm parameters
		int population = 50;
		int n_items = 20;


		for(int iter=1; iter<=n_epochs ; iter++) {

			System.out.println("============================================================");
			System.out.println("epoch " + iter);
			System.out.println("============================================================\n");

			//set start time
			long startTime = System.nanoTime();

			//initialise
			Optimizer op = new Optimizer(population, n_items);
			Swarm swarm = op.getSwarm();

			//calc setup time of program
			long setupTime = System.nanoTime() - startTime;

			//get the initial revenue before the swarm optimisation
			double initial_revenue = swarm.getEvaluator().evaluate(swarm.getGlobalBest());

			//optimise
			swarm.search(iterations);

			//calc total runtime
			long algTime = System.nanoTime() - startTime - setupTime;


			//get the optimised prices and revenue
			double[] result = swarm.getGlobalBest();
			double final_revenue = swarm.getEvaluator().evaluate(swarm.getGlobalBest());

			System.out.println("\n\n============================================================");
			System.out.println("RESULTS FOR PREVIOUS ITERATION");
			System.out.println("============================================================");
			//print runtime
			System.out.println("Setup time = " + setupTime);
			System.out.println("Algorithm time =" + algTime);
			//print revenues
			System.out.println("Initial best price range with a total revenue of " + initial_revenue);
			System.out.println("Final best price range with a total revenue of " + final_revenue);
			System.out.println("You could increase revenue to " + (final_revenue - initial_revenue));





			//data for later analysis
			//add best revenue found in iteration
			max.add((Double) Collections.max(swarm.getFittestArray()));
			//add initial revenue found in iteration
			initial.add((Double) Collections.min(swarm.getFittestArray()));
			//add the increase in revenue found in iteration
			increases.add(final_revenue - initial_revenue);
			//add the revenue increments in iteration
			iteration_revenues.add(swarm.getFittestArray());


		}

		//output results over iterations
		System.out.println("\n\n============================================================");
		System.out.println("OVERALL RESULTS");
		System.out.println("============================================================");
		double sum = 0;
		for(Object fittest : initial) {
			sum += (double) fittest;			
		}
		System.out.println("average initial revenue over " + n_epochs + " iteration is: " + sum/initial.size());
		sum = 0;
		for(Object fittest : increases) {
			sum += (double) fittest;			
		}
		System.out.println("average increase in revenue over " + n_epochs + " iteration is: " + sum/increases.size());
		System.out.println("best revenue over " + n_epochs + " iteration is: " + Collections.max(max));
		System.out.println("worst revenue over " + n_epochs + " iteration is: " + Collections.min(max));
		sum = 0;
		for(Object fittest : max) {
			sum += (double) fittest;			
		}
		double mean = sum/max.size();
		System.out.println("average best revenue over " + n_epochs + " iteration is: " + mean);

		sum = 0;
		for(Object fittest : max) {
			sum += Math.pow(((double) fittest - mean), 2);			
		}
		double std = Math.sqrt(sum/max.size());
		System.out.println("std of best revenue over " + n_epochs + " iteration is: " + std);



		//////////////////
		//WRITE TO CSV FOR LATER ANALYSIS
		/////////////////
		//sort data for display
		double[] values = new double[iterations];
		for(int j = 0; j<n_epochs; j++) {
			for(int i = 0 ; i<iterations ; i++) {
				values[i] += iteration_revenues.get(j).get(i);
			}
		}
		// specify file
		//		String file = "pso_results.csv";
		String file = "pso_results.csv";
		//before we open the file check to see if it already exists
		boolean alreadyExists = new File(file).exists();
		try {
			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(new FileWriter(file));


			// adding header to csv
			String[] header = { "iteration", "revenue" };
			writer.writeNext(header);

			for(int i = 0; i<values.length ; i++) {
				double avg = (double) values[i]/n_epochs;
				// add data to csv
				String[] data1 = { String.valueOf(i+1), String.valueOf(avg) };
				writer.writeNext(data1);
			}

			// closing writer connection
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
