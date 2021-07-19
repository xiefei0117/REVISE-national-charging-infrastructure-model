package application.model.GeneticAlgorithm;

import java.util.Random;

import application.controller.UIParameters;

//Some math functions used in the algorithm
public class CommonMath {
	
	//private Random random;

			
	static Random random = new Random(UIParameters.gettxtRandomSeedValue());
	
	// binary randomly select with equal probability
	public static boolean selectedOrNot () {
		if (random.nextDouble()<0.5)
			return false;
		else
			return true;
	}
	
	// binary randomly select with a threshold, e.g., threshold = 0.7, then true probability = 0.3 
	public static boolean selectedOrNot (double threshold) {
		if (random.nextDouble()>threshold)
			return false;
		else
			return true;
	}
	
	//binary randomly select based on the cost of two components, lower cost has higher probability to be selected
	public static boolean selectedOrNotWeighted(double cost1, double cost2) {
		if(random.nextDouble()<cost1/(cost1+cost2))
			return false;
		else
			return true;
	}
	
	//uniformly pick up a integer number between min and max 
	public static short randomIntRange (int min, int max) {		
		return (short) (min + (int)(random.nextDouble()*((max - min)+1)));		
	}
}
