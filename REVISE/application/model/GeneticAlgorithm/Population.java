package application.model.GeneticAlgorithm;

import application.controller.CalculationController;

//population list for the GA algorithm
public class Population {
	OrderedListIndividual individuals;
	private final CalculationController controller1;	//userinterface

	//constructor, initialize with a population size
	public Population(int populationSize,boolean Multithread, CalculationController controller1) {
		this.controller1 = controller1;
		individuals = new OrderedListIndividual();
		//System.out.print("Initialize population: ");          //display
		Individual firstIndividual = new Individual(true, true,true);
		saveIndividual(firstIndividual);
		this.controller1.JFXTextAreaAppendText("*");               //display
		
		for (int i=1; i<populationSize-1; i++) {
			Individual newIndividual = new Individual(true, Multithread);

				while(individuals.isExist(newIndividual)) {
					//System.out.print(i +"-exist ");  //display
					newIndividual = new Individual(false, Multithread);
				}
				newIndividual.calculateObjectiveCost(Multithread);
				saveIndividual(newIndividual);
				this.controller1.JFXTextAreaAppendText("*");      //display
		}
		Individual lastIndividual = new Individual(true, "none", Multithread);
		saveIndividual(lastIndividual);
		this.controller1.JFXTextAreaAppendText("*");  
		this.controller1.JFXTextAreaAppendText("\nA pool of "+populationSize + " genes are initialized\n");
	}
	
	//constructor, initialize with a population size
	public Population(int populationSize, boolean Multithread) {
		controller1 = null;
		individuals = new OrderedListIndividual();
		Individual firstIndividual = new Individual(true, true,true);
		saveIndividual(firstIndividual);
		
		for (int i=1; i<populationSize-1; i++) {
			Individual newIndividual = new Individual(true, Multithread);

				while(individuals.isExist(newIndividual)) {
					//System.out.print(i +"-exist ");  //display
					newIndividual = new Individual(false, Multithread);
				}
				newIndividual.calculateObjectiveCost(Multithread);
				saveIndividual(newIndividual);
		}
		Individual lastIndividual = new Individual(true, "none", Multithread);
		saveIndividual(lastIndividual);
	}
	
	//get the population size
	public int size() {
		return individuals.size();
	}
	
	//save a genes into the population
	public void saveIndividual(Individual indiv) {
		individuals.insert(indiv);
	}
	
	//remove a gene from the population
	public void removeIndividual(int num) {
		individuals.randRemove(num);
	}
	
	public void immigrant(int numPool, int numIn,boolean Multithread) {
		if (numPool == 0)
			return;
		
		if(numIn > numPool)
			numIn = numPool;
		
		
		Population immigrationPool = new Population(numPool, Multithread);
		
		for (int i=0; i<numIn; i++) {
			Individual insertImmigration = immigrationPool.individuals.obtainItem(numPool-i);
			if(!this.individuals.isExist(insertImmigration)) {
				removeIndividual(numPool);
				saveIndividual(insertImmigration);
			}
			
		}
		
	}
	
	public void immigrantLarge(int numPool,boolean Multithread) {
		if (numPool == 0)
			return;
		
		
		for (int i=0; i<numPool; i++) {
			removeIndividual(numPool-i);
		}
		
		System.out.println("Individuals left: " + this.size());

		
		Individual insertImmigration;
		
		for (int i=0; i<numPool; ) {
			insertImmigration = new Individual(true, Multithread);
			if(!this.individuals.isExist(insertImmigration)) {
				saveIndividual(insertImmigration);
				i++;
			}			
		}
		System.out.println("Individuals now: " + this.size());
		
	}
	
	//display the population elements
	public void displayIndividuals() {
		System.out.println("All solutions are as follows:");
		individuals.displayAllNode();
	}
	
	
}
