package antenna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Swarm {
	
	private double[] global_best_pos;
	private double[] particles;
	private AntennaArray antenna_array;
	
	public Swarm(AntennaArray antenna_array, int n_antenna) {
		
		this.antenna_array = antenna_array;
		
		
		particles = new double[n_antenna];
		
		for(int i = 0; i<n_antenna; i++) {
			double[] design = initialiseValidDesign(n_antenna);
			Particle p = new Particle(design);
						
		}
		
		
//		for(double pos : antenna_instance) {
//			Particle particle = new Particle(pos, global_best_pos);
//			particles.add(particle);	
//		}
		
		
	}
	
public double[] initialiseValidDesign(int n_antenna) {
		
		this.position = new double[n_antenna];
    	
    			
    	double apeture_size = n_antenna/2;
		
		while(!antenna.is_valid(antennae_array)) {
			
			for(int i=0; i<antennae_array.length-1; i++) {
				
				double randomValue = 0 + (apeture_size - 0) * rand.nextDouble();

				antennae_array[i] = randomValue;
				
			}
			
			antennae_array[antennae_array.length-1] = apeture_size;
	        Arrays.sort(antennae_array); 

			if(!antenna.is_valid(antennae_array)) {

				antennae_array = new double[(int) array_size];

			}
		}
	
		return antennae_array;
	}
	
	
	
	
	
	
	
	
	public double[] getParticles(){
		return particles;
	}
	
	public double[] getGlobalBest(){
		return this.global_best_pos;
	}
	
	public double[] getPositions() {
		double[] positions = new double[this.getParticles().size()];
		int i = 0;
		for(Particle p: this.getParticles()) {
			positions[i] = p.getBestPosition();
			i++;
		}
		return positions;
	}
	
	
	
	
	
		
		
		
//			REPEAT UNTIL ( termination condition IS satisfied ) DO
//				UPDATE global best;
//				FOR EACH ( particle in population ) DO
//					1. UPDATE velocity and position;
//					2. EVALUATE new position;
//					3. UPDATE personal best;
//				OD
//			OD
		
		
		
	}
	
	
	
	
	

