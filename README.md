# CS3910-Computational-Intelligence Labs


Implementation of a [Genetic Algorithm](https://github.com/Vanguard-Anon/GA-PSO-Algorithms/tree/master/src/tsp) to solve the Travelling Salesman Problem and a [Particle Swarm Algorithm](https://github.com/Vanguard-Anon/GA-PSO-Algorithms/tree/master/src/antenna) to solve the Antenna Array Design Problem.


The Travelling Salesman Problem: Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city?


The Antenna Array Design Problem is to find a solution to a fixed number of antennae on an array such that the peak Side Lobe Level (SSL) is minimised. Constraints satisfied that 
1. the aperture size is exactly equal to half the number of antennae
2. no antenna is within a distance of 0.25 of another



# CS3910-Computational-Intelligence Coursework

Solution for [Market Pricing Problem](https://github.com/Vanguard-Anon/GA-PSO-Algorithms/tree/master/src/com/pricing) by a supermarket using GA and PSO and comparing results.

The PSO algorithm is far superior in finding the optimal solution in terms of effectiveness of the optimal solution and the efficiency in the time it is achieved.


## How to run

### GA:
[GA](https://github.com/Vanguard-Anon/GA-PSO-Algorithms/tree/master/src/com/genetic) use Manager.java constructor and uncomment/comment which
problem instance you would like to run and change parameters in Genetic_Algorithm.java main.



### PSO:
[PSO](https://github.com/Vanguard-Anon/GA-PSO-Algorithms/tree/master/src/com/pso) use Optimizer.java constructor and uncomment/comment which
problem instance you would like to run change parameters in Optimizer.java main.


### Default parameters

Iterations over swarm/population = 100
swarm/population size = 50
items = 20 (if running random instance otherwise coursework instance default is 20)

#### GA:
- mutation rate = 0.2
- crossover rate = 0.8
- tournement selection: routletter wheel (can change to tournement)

#### PSO:
- phi1 = phi2 = 0.5 + ln2
- inertia = chaotic:
  - z = Math.random();
  - z = (4.0 * z) * (1-z);
  - inertia = (initial_inertia - final_inertia) * (max_iteration - iteration) / max_iteration + (final_inertia * z);

initial inertia = 0.9
final inertia = 0.4



