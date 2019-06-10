package com.genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;

public class Genetic_Algorithm {

	private static double[] antennae_array;
	private static Random rand;
	private Manager tm;



	public Genetic_Algorithm(int pop_size, int n_prices, double mutation_rate, double crossover_rate) {
		this.tm = new Manager(pop_size, n_prices, mutation_rate, crossover_rate);
	}	


	public Manager getManager() {
		return this.tm;
	}





	public static void main(String[] args) {

		//data for analysis
		ArrayList<Double> max = new ArrayList<Double>();
		ArrayList<Double> initial = new ArrayList<Double>();
		ArrayList<Double> increases = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> iteration_revenues = new ArrayList<ArrayList<Double>>();

		
		
		

		//iterations for each population in GA
		int iterations = 100;
		//number of times algorithm ran for analysis purposes
		int n_epochs = 1;
		//parameters
		int population = 50;
		int n_items = 20;
		double mutation_rate = 0.8;
		double crossover_rate = 0.2;

		for(int iter=1; iter<=n_epochs ; iter++) {

			System.out.println("============================================================");
			System.out.println("epoch " + iter);
			System.out.println("============================================================\n");

			//set start time
			long startTime = System.nanoTime();

			//setup
			Genetic_Algorithm ga = new Genetic_Algorithm(population, n_items, mutation_rate, crossover_rate);
			Manager m = ga.getManager();

			//calc setup time of program
			long setupTime = System.nanoTime() - startTime;

			//get initial revenue before population evolution
			double intital_best_revenue = m.getBestRevenue();
			System.out.println("Start fitness: " + intital_best_revenue);

			//for x amount of iterations evolve the population
			for (int i = 0; i < iterations; i++) {
				m.evolve(m.getPopulation());
			}

			//calc total runtime
			long algTime = System.nanoTime() - startTime - setupTime;


			System.out.println("\n\n============================================================");
			System.out.println("RESULTS FOR PREVIOUS ITERATION");
			System.out.println("============================================================");
			//print runtime
			System.out.println("Setup time = " + setupTime);
			System.out.println("Algorithm time =" + algTime);
			//print revenues
			System.out.println("Initial best price range with a total revenue of " + intital_best_revenue);
			System.out.println("Final best price range with a total revenue of " +  m.getBestRevenue());
			System.out.println("You could increase revenue by " + (m.getBestRevenue() - intital_best_revenue));

			//add maximum revenue found for iteration for later analysis
			max.add((Double) Collections.max(m.getFittestArray()));
			iteration_revenues.add(m.getFittestArray());


			//data for later analysis
			//add best revenue found in iteration
			max.add((Double) Collections.max(m.getFittestArray()));
			//add initial revenue found in iteration
			initial.add((Double) Collections.min(m.getFittestArray()));
			//add the increase in revenue found in iteration
			increases.add(m.getBestRevenue() - intital_best_revenue);
			//add the revenue increments in iteration
			iteration_revenues.add(m.getFittestArray());

		}

		//output results over iterations
		System.out.println("\n\n============================================================");
		System.out.println("RESULTS");
		System.out.println("============================================================");
		System.out.println("best revenue over " + n_epochs + " iteration is: " + Collections.max(max));
		System.out.println("worst revenue over " + n_epochs + " iteration is: " + Collections.min(max));
		double sum = 0;
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



		double[] values = new double[iterations];
		for(int j = 0; j<n_epochs; j++) {
			for(int i = 0 ; i<iterations ; i++) {
				values[i] += iteration_revenues.get(j).get(i);
			}
		}

		// specify file
		//		String file = "ga_results_random.csv";
		String file = "ga_results.csv";
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
				//				System.out.println("average of the best revenue found at iteration " + (i+1) + " of " + n_epochs + " iterations is: " + avg);

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
