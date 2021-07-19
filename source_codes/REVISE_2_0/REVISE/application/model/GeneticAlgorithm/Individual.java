package application.model.GeneticAlgorithm;


// class - Individual: object to store each candidate solution with objective cost and identification 
public class Individual {

	//defining parameters
	static Configuration Config = GA.Config;
	static private int defaultGeneLength = Config.get_defaultGeneLength();             //gene solution length 
	static private int horizon = Config.get_horizon();                       //planning horizon
	private double objectiveCost = 100000000;              //cost with the solution
	private long hashID1 = 0;                              //identity 1
	private long hashID2 = 0;                              //identity 2
	static NetworkAnalysis Network = new NetworkAnalysis(Config.get_nodeSize(), Config.get_candidateSize(), Config.get_odpairs(), Config.get_horizon());
	
	private short[] genes = new short[defaultGeneLength];  //gene solution array
	
	//change gene solution length  ---- not used
	public static void setDefaultGeneLength(int length) {
		defaultGeneLength = length;
	}
	
	
	//change planning horizon ----not used
	public static void setHorizon(int time) {
		horizon = time;
	}
	
	public int getHorizon() {
		return horizon;
	}
	
	//constructor no argument without calculating cost objective
	public Individual() {
		generateIndividual();                //randomly generate solution
		assignHashID();                      //calculate identities 1,2
	}
	
	//constructor with if or not calculating cost objective
	public Individual(boolean sign, boolean Multithread){
		
		//System.out.println("generateIndividual");   //remove
		generateIndividual();
		//System.out.println("assignHashID");   //remove
		assignHashID();		
		
		//System.out.println("objective");   //remove
		if (sign) {
			calculateObjectiveCost(Multithread);        //calculate cost objective
		}
		//System.out.println("done");   //remove
	}
	
	public Individual(boolean sign, String A, boolean Multithread) {
		if (A == "all") {
			for (int i=0; i<size(); i++)
				genes[i]=1;
		}
		else if (A == "none") {
			for (int i=0; i<size(); i++)
				genes[i]=0;
		}
		else {
			generateIndividual();			
		}
		assignHashID();
		if(sign) {
			calculateObjectiveCost(Multithread);
		}
	}
	
	//constructor by copying all attributes from another individual object
	public Individual(Individual copy) {
		for (int i=0; i<size(); i++) {
			genes[i] = copy.genes[i];
		}
		hashID1 = copy.hashID1;
		hashID2 = copy.hashID2;
		objectiveCost = copy.objectiveCost;
	}
	
	//crossover with father and mother without calculating the objective cost
	public Individual(Individual father, Individual mother) {
		generateIndividual(father,mother);
	}
	
	public Individual (boolean sign, boolean sign2, boolean Multithread) {
		if (sign == true && sign2 == true) {
			for (int i=0; i<size(); i++) {
				genes[i] = 1;
			}
			calculateObjectiveCost(Multithread); 
		}
		else {
			generateIndividual();
			assignHashID();		
			
			if (sign) {
				calculateObjectiveCost(Multithread);        //calculate cost objective
			}				
		}
	}
	
	//randomly generate solutions for the current Individual object
	public void generateIndividual(){
		for (int i=0; i<size(); i++) {
			boolean open = CommonMath.selectedOrNot();
			if(!open)
				genes[i] = 0;
			else
				genes[i] = CommonMath.randomIntRange(1, horizon);						
		}
	}
	
	//crossover generate solutions for the current object with father and mother
	public void generateIndividual(Individual father, Individual mother) {
		for (int i=0; i<size(); i++) {
			if (father.genes[i] != mother.genes[i]) {
				boolean sign = CommonMath.selectedOrNotWeighted(father.getObjectiveCost(), mother.getObjectiveCost());
				if (sign)
					genes[i]=father.displayGeneElement(i);
				else
					genes[i]=mother.displayGeneElement(i);
			}
			else
				genes[i]=father.displayGeneElement(i);
				
		}
	}
	
	
	//mutation with probability threshold, return a mutated genes
	public Individual mutateIndividual(double threshold) {
		Individual newIndividual = new Individual(this);
		for (int i=0; i<size(); i++) {
			if (CommonMath.selectedOrNot(threshold)) {
				if(newIndividual.genes[i] == 0)
					newIndividual.genes[i] = CommonMath.randomIntRange(1, horizon);
				else {
					boolean open = CommonMath.selectedOrNot();
					if(!open)
						newIndividual.genes[i] = 0;
					else
						newIndividual.genes[i] = CommonMath.randomIntRange(1, horizon);
				}
			}
		}
		return newIndividual;
	}
	
	//calculate objective cost                                        ---------------------------------------<------------------
	public void calculateObjectiveCost(boolean Multithread) {
				
		objectiveCost = Network.calculateTotalCost(this, Multithread);
	}
	
	//calculate identities with hashing functions
	public void assignHashID() {
		long ID1 = 0;
		long ID2 = 0;
		
		if (genes[0] == 0) {
			for (int i=0; i<size(); i++) {
				ID1 = ID1 + genes[i]*(i+1) - Math.round(Math.log((genes[i]+1)*(i+1)*(i+1))); 
			}
		}
		else {
			for (int i=0; i<size(); i++) {
				ID1 = ID1 - genes[i]*(i+1) + Math.round(Math.log((genes[i]+1)*(i+1)*(i+1)));
			}
			
		}
		
		if (genes[1] == 0) {
			for (int i=0; i<size(); i++) {
				ID2 = ID2 + genes[i]*(i+1)*(i+1) + genes[i]*(genes[i]-i);
			}
		}
		else {
			for (int i=0; i<size(); i++) {
				ID2 = ID2 - genes[i]*(i+1)*(i+1) - genes[i]*(genes[i]-i);
			}
		}
		hashID1 = ID1;
		hashID2 = ID2;
	}
	
	//Check if two Individual items are identical
	public boolean compare(Individual item) {
		if(this.hashID1 != item.getHashID1() || this.hashID2 != item.getHashID2())
			return false;
		else {
			boolean notStop = true;
			for (int i=0; i<this.size() && notStop; i++) {
				if(this.displayGeneElement(i) != item.displayGeneElement(i))
					notStop = false;
			}
			if (notStop == true)
				return true;
			else
				return false;
		}
			
	}
	
	//get the solution length
	public int size() {
		return genes.length;
	}
	
	//display gene solution , ID , and cost 
	public void displayGenes () {
		String display = "";
		for (int i =0; i < size(); i++) {
			display += genes[i];
			display +=" ";
		}
		display += "    ";
		display += hashID1;
		display += " ";
		display += hashID2;
		display += " ";
		display += objectiveCost;
		System.out.println(display);
	}
	
	// return solution element with index
	public short displayGeneElement (int index) {
		return genes[index];
	}
	
	//get objective cost
	public double getObjectiveCost() {
		return objectiveCost;
	}
	
	//get ID1
	public long getHashID1(){
		return hashID1;
	}
	
	//get ID2
	public long getHashID2(){
		return hashID2;
	}
	
	public void displayAll(){
		Network.printOutput(this);
		Network.printPathDistanceAndCost(10);
		Network.printStatePenetration(10);
		Network.printDataTrafficFlow(10);
		Network.printChargerCapacityAll(999);
	}
	
}
