package application.model.GeneticAlgorithm;

public class NodeIndividual {
	public Individual current;
	public Individual pre;
	public Individual next;
	
	public NodeIndividual(Individual indiv) {
		current = indiv;		
	}
	
	public void assignPre (Individual indiv) {
		pre = indiv;
	}
	
	public void assignNext (Individual indiv) {
		next = indiv;
	}

}
