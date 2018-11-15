package antenna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Swarm {

	
	
	private double global_best_pos;
	private ArrayList<Particle> particles;
	
	
	public Swarm(double global_best_pos, double[] antenna_instance) {
		
		
		particles = new ArrayList<Particle>();
		
		for(double pos : antenna_instance) {
			Particle particle = new Particle(pos, global_best_pos);
			particles.add(particle);	
		}
		
		
	}
	
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}
	
	public double getGlobalBest(){
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
	
	
	
	
	

