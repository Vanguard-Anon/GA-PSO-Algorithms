# CS3910-Computational-Intelligence Labs


Implementation of a Genetic Algorithm to solve the Travelling Salesman Problem and a Particle Swarm Algorithm to solve the Antenna Array Design Problem.


The Travelling Salesman Problem: Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city?


The Antenna Array Design Problem is to find a solution to a fixed number of antennae on an array such that the peak Side Lobe Level (SSL) is minimised. Constraints satisfied that 
    1. the aperture size is exactly equal to half the number of antennae
    2. no antenna is within a distance of 0.25 of another






# CS3910-Computational-Intelligence Coursework

Solution for Market Pricing by a supermarket


############################################
############### How to run #################
############################################


GA: Go to Manager.java constructor and uncomment/comment which
problem instance you would like to run

PSO: Go to Optimizer.java constructor and uncomment/comment which
problem instance you would like to run


Go to Genetic_Algorithm.java or Optimizer.java main method and change
parameters and run class for GA and PSO respectively. 



############################################
###########  Default parameters   ##########
############################################

Iterations over swarm/population = 100
swarm/population size = 50
items = 20 (if running random instance otherwise coursework instance default is 20)

GA:
mutation rate = 0.2
crossover rate = 0.8
tournement selection: routletter wheel (can change to tournement)


PSO:
phi1 = phi2 = 0.5 + ln2
inertia = chaotic:

z = Math.random();
z = (4.0 * z) * (1-z);
inertia = (initial_inertia - final_inertia) * (max_iteration - iteration) / max_iteration + (final_inertia * z);

initial inertia = 0.9
final inertia = 0.4



