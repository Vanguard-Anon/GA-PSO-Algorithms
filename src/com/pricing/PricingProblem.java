package com.pricing;

import java.util.Random;

public class PricingProblem {

  /**
   * Creates the instance of pricing problem to be solved in the CS3910 coursework.
   * @return The problem instance.
   */
  public static PricingProblem courseworkInstance(){
	  Random rng = new Random(0);
	  int numberOfGoods = 20;
	  return new PricingProblem(numberOfGoods,rng);
  }

  /**
   * Creates a random instance of pricing problem, drawn from the same distribution as the CS3910 coursework.
   * @return The problem instance.
   */
  public static PricingProblem randomInstance(int numberOfGoods){
	  Random rng = new Random();
	  return new PricingProblem(numberOfGoods,rng);
  }

  // Could add in more, e.g. logit, but probably overkill
  private int[] priceResponseType; // 0: Linear, 1: Constant elasticity, 2: Fixed demand.
  private double[][] priceResponse;
  private double[][] impact;
  private double [][] bnds;

  /**
   * Creates an evaluation model.
   * @param n The number of goods to model
   */
  PricingProblem(int n, Random rng) {

    // Set up random price response curves and impacts
    priceResponse = new double[n][2];
    priceResponseType = new int[n];
    impact = new double[n][n];
    
    for (int i = 0; i < n; i++) {
      System.out.print("Setting up good "+i+" with type: ");
      double type = rng.nextDouble();
      if (type <= 0.4) {
        // Linear
        priceResponseType[i] = 0;
        priceResponse[i][0] = getRandomTotalDemand(rng);
        priceResponse[i][1] = getRandomSatiatingPrice(rng);
        System.out.println(" L ("+priceResponse[i][0]+"/"+priceResponse[i][1]+")");

      } else if (type > 0.4 && type < 0.9) {
        // Constant elasticity
        priceResponseType[i] = 1;

        priceResponse[i][0] = getRandomTotalDemand(rng);
        priceResponse[i][1] = getRandomElasticity(rng);
        System.out.println(" CE ("+priceResponse[i][0]+"/"+priceResponse[i][1]+")");

      } else {
        // Fixed demand
        priceResponseType[i] = 2;

        priceResponse[i][0] = getRandomTotalDemand(rng);
        System.out.println(" FD ("+priceResponse[i][0]+"/"+priceResponse[i][1]+")");

      }
    
      for (int j = 0; j < n; j++)
          impact[i][j] = rng.nextDouble()*0.1;
      impact[i][i] = 0.0;
    }
    
    bnds = new double[priceResponse.length][2];
    double[] dim_bnd = {0.01,10.0};
    for(int i = 0;i<priceResponse.length;++i)
        bnds[i] = dim_bnd;
  }

  /**
   * Rectangular bounds on the search space.
   * @return Vector b such that b[i][0] is the minimum permissible value of the
   * ith solution component and b[i][1] is the maximum.
   */
  public double[][] bounds(){
      return bnds;
  }
  
  /**
   * Check whether a vector of prices is valid.
   * A valid price vector is one in which all prices are at least 1p and at most �10.00
   */
  public boolean is_valid(double[] prices){
    if(prices.length != bounds().length) return false;
    //All antennae lie within the problem bounds
    for(int i = 0;i<prices.length;++i)
        if(prices[i] < bounds()[i][0] || prices[i] > bounds()[i][1] )
            return false;
    return true;
  }
  
  /**
   * Gets the total revenue from pricing the goods as given in the parameter.
   * @param prices An array of prices, of length n, where n is the number of goods in the model.
   * @return The total revenue.
   */
  public double evaluate(double[] prices) {
    if(prices.length != bounds().length)
	  throw new RuntimeException(
	            "PricingProblem::evaluate called on price array of the wrong size. Expected: " + bounds().length +
	            ". Actual: " +
	            prices.length
	  );
	if(!is_valid(prices)) return 0;
	
    //System.out.println("Calculating revenue...");
    double revenue = 0.0;
    for (int i = 0; i < prices.length; i++) {
        //System.out.println("Sold " + getDemand(i, prices) + " units of good " + i + " at " + prices[i]);
        revenue += getDemand(i, prices) * prices[i];
    }
    
    return Math.round(revenue*100.0)/100.0;
  }

  // Get the demand for good i at price p
  private int getDemand(int i, double[] prices) {
    int demand = getGoodDemand(i, prices[i]) + getResidualDemand(i, prices);
    
    // Second sanity check - still cannot have more demand than the market holds
    if (demand > priceResponse[i][0])
      demand = (int)Math.round(priceResponse[i][0]);
    
    return demand;
  }
  
  
  private int getGoodDemand(int i, double p) {
    double demand = 0.0;
    switch (priceResponseType[i]) {
      case(0): // Linear
            demand = priceResponse[i][0] - ((priceResponse[i][0] / priceResponse[i][1]) * p);
            break;
      case(1): // Constant elasticity
            demand = priceResponse[i][0] / (Math.pow(p, priceResponse[i][1]));
            break;
      case(2): // Fixed demand
            demand = priceResponse[i][0];
            break;
      default:
            System.out.println("Error! Incorrect price response curve specified!");
    }
    
    // Sanity check - cannot have more demand than the market holds
    if (demand > priceResponse[i][0])
      demand = (int)Math.round(priceResponse[i][0]);
    
    // Or less than 0 demand
    if (demand < 0)
      demand = 0;
    

    return (int)Math.round(demand);
  }

  
  private int getResidualDemand(int i, double[] p) {
    double demand = 0;
    for (int j = 0; j < priceResponse.length; j++)
      if (i != j) 
        demand = demand +  getGoodDemand(j, p[j]) * impact[j][i];
    return (int)Math.round(demand);
  }


  private double getRandomTotalDemand(Random rng) {
    return rng.nextDouble() * 100;
  }

  private double getRandomSatiatingPrice(Random rng) {
    return rng.nextDouble() * 10;
  }

  private double getRandomElasticity(Random rng) {
    return rng.nextDouble();
  }
}
