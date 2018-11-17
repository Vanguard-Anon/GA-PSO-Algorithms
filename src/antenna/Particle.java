package antenna;

import java.util.Arrays;
import java.util.Random;

public class Particle {

	private double[] position;
	private double[] velocity;
	private double[] perosnal_best_pos;
	private AntennaArray antenna_array;
	private double aperture_size;
	private final double initial_inertia = 0.9;
	private final double final_inertia = 0.4;


	public Particle(AntennaArray antenna_array, double[] position, double[] velocity) {

		this.antenna_array = antenna_array;
		this.aperture_size = position[position.length-1];
		
		this.position = position;
		this.perosnal_best_pos = this.position;
		this.velocity = velocity;
	}

	public double[] getPosition() {
		return position;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public double[] getPersonalBestPosition() {
		return perosnal_best_pos;
	}
	
	public void setPersonalBestPosition(double[] new_position) {
		this.perosnal_best_pos = new_position;
	}

	public void updateVelocity(double[] global_best, int n_particle, int iteration, int max_iteration) {
				
//		double[] newVelocity = new double[this.velocity.length];
		double[] newVelocity = this.getVelocity().clone();


		//linear decreasing intera
//		float inertia = (float) ((initial_inertia - final_inertia) * (max_iteration - (iteration / max_iteration)) + final_inertia);
		double inertia = 0.5 + (Math.random()/2);
				
//		System.out.println(inertia);
			

			
			//for each element, update
			for(int i = 0; i<global_best.length ; i++) {
	
				//cognative strength coefficient can be negative
				double phi1 = 0.1;
				//random vector, avoids deterministic alg that will get stuck, explore more of the problem space
				double r1 = Math.random();
	
				//higher phi2, low phi1 means particles dedicating searching for solution in gbest region of space, example of exploitation
				//higher phi1, low phi1 means particles attacted to their personal best and not the global best, swarm less quiick to converge on global best and more likely to search their own pbest regions,  good for searching for good solutions over the search space
				double phi2 = 0.1;
				double r2 = Math.random();
				
				//															cognative attraction, attraction to pbest                    social attraction of particles to best pos 
				double velocity = (inertia * this.getVelocity()[i]) + (phi1 * r1 * (this.getPersonalBestPosition()[i] - this.getPosition()[i])) + (phi2 * r2 * (global_best[i] + this.getPosition()[i]));
			
				//add to new velocity array
				newVelocity[i] = velocity;
			}
			
		
			
			
			
			
//			
//			System.out.print("old vel = ");
//			for(int i = 0; i<this.getVelocity().length ; i++) {
//				System.out.print(this.getVelocity()[i] + ", ");
//			}
//			System.out.println();

			
		if(checkIfValidDirection(newVelocity)) {
			this.velocity = newVelocity;
//			System.arraycopy(newVelocity, 0, this.velocity, 0, this.velocity.length); 
		}
		
//		
//		System.out.print("new vel = ");
//		for(int i = 0; i<newVelocity.length ; i++) {
//			System.out.print(newVelocity[i] + ", ");
//		}
//		System.out.println();

			
		//if direction is valid, set velocity as new velocity
	}
	
	
	public boolean checkIfValidDirection(double[] velocity) {
		
		double[] newPosition = this.getPosition().clone();

		// calculate new position 
		for(int i = 0; i<this.getPosition().length-1 ; i++) {
				newPosition[i] = this.getPosition()[i] + this.getVelocity()[i];
			
		}
		newPosition[newPosition.length-1] = this.getPosition()[newPosition.length-1];

		
		if(antenna_array.is_valid(newPosition)) {
			return true;
		}
		
		return false;		
		
	}
	

	public void updatePosition() {
		
		System.out.print("old position = ");
		for(int i = 0; i<this.getPosition().length ; i++) {
			System.out.print(this.getPosition()[i] + ", ");
		}
		System.out.println("old position Minimum SSL = " + this.antenna_array.evaluate(this.getPosition()));
		
//		
//		
		
		
		
		
//		double[] newPosition = new double[this.position.length];
		double[] newPosition = this.getPosition().clone();


//		while(!this.antenna_array.is_valid(newPosition)) {
			
//			newPosition = new double[this.position.length];
			
			// calculate new position 
			for(int i = 0; i<this.getPosition().length-1 ; i++) {
				double newPositionValue = this.getPosition()[i] + this.getVelocity()[i];
				if(newPositionValue <= this.aperture_size && newPositionValue >= 0) {
					newPosition[i] = newPositionValue;
				}
			}
			newPosition[newPosition.length-1] = this.getPosition()[newPosition.length-1];
			
			if(this.antenna_array.is_valid(newPosition)) {
				this.position = newPosition;
				
				
			}
			
			

//		}
		
		
//		System.arraycopy(randomPosition, 0, position, 0, dimension); 

		
		
		
		
		
		
		
		
		System.out.print("new position = ");
		for(int i = 0; i<this.getPosition().length ; i++) {
			System.out.print(newPosition[i] + ", ");
		}
		System.out.println("new position Minimum SSL = " + this.antenna_array.evaluate(newPosition));
//		
	
		
		
	}

	@Override
	public String toString() {
		return "Particle [velocity = " + velocity + ", position = " + position + ", personal best position = " + perosnal_best_pos  + "]";
	}

}
