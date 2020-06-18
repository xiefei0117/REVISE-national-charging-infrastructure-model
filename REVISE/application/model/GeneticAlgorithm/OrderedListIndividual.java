package application.model.GeneticAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//ordered linked list structure to store genes 
public class OrderedListIndividual {
	
	private Node first;            //pointing to the first node
	private int N;                 //# of nodes
	
	//node to store each gene
	private class Node             
	{
		Individual item;          //item
		Node next;                //pointing to the next node
		
		//constructor with existing genes i 
		private Node(Individual i) {
			item = i;
			next = null;
		}
		
		//constructor with existing genes i, forcing the node to pointing to node tr
		private Node (Individual i, Node tr) {
			item = i;
			next = tr;
		}
	}
	
	//constructor with empty linked list
	public OrderedListIndividual() {
		first = null;
		N = 0;
	}
	
	//check if the linked list is empty
	public boolean isEmpty() {
		return first == null;
	}
	
	//get linked list size
	public int size() {
		return N;
	}
	
	//insert a gene to the linked list
	public void insert(Individual item) {
		Node newNode = new Node(item);
		if(isEmpty()) {                            //if empty, directly insert
			first = newNode;
			N++;
			return;
		}
		else if (item.getObjectiveCost() > first.item.getObjectiveCost()) {            //if the gene has a higher cost than all genes in the list, put the gene in the first
			newNode.next = first;
			first = newNode;
			N++;
		}
		else                                              //insert the gene into a proper place
		{
			Node after = first.next;
			Node before = first;
			while (after != null && item.getObjectiveCost()<after.item.getObjectiveCost()) {
				before = after;
				after = after.next;
			}
			newNode.next = before.next;
			before.next = newNode;
			N++;
		}
	}
	
	//get genes from the list with an index
	public Individual obtainItem(int index) {
		if(index > size() || index <=0) {
			System.out.println("error with boundary");
			return null;
		}
		else {
			if(index == 1) {
				return first.item;
			}
			else {
				Node before = first;
				for (int i=1; i<index; i++) {
					before = before.next;
				}
				return before.item;
			}
		}
	}
	
	//cross over with randomly selected parents from the list
	//randomly select two parents from the list, and crossover to create a new individual
	public Individual crossOver() {
		int firstIndex;
		int secondIndex;
		Individual father;
		Individual mother;
		
		firstIndex = CommonMath.randomIntRange(1, size());
		secondIndex =CommonMath.randomIntRange(1, size());
		
		while (secondIndex == firstIndex) {
			secondIndex =CommonMath.randomIntRange(1, size());
		}
		
		father = obtainItem(firstIndex);
		mother = obtainItem(secondIndex);		
		
		return new Individual(father,mother);
	}
	
	//randomly select 2 of mother, and 2 of fathers, tournament, and crossover if seed true
	public Individual crossOver(boolean seed) {
		if (!seed)
			return crossOver();
		
		Individual father1;
		Individual father2;
		Individual mother1;
		Individual mother2;
		Individual selectedFather;
		Individual selectedMother = new Individual();
		
		boolean sign = true;             //indicate if selectedFather and selectedMother is the same or not
		

		father1 = obtainItem(CommonMath.randomIntRange(1, size()));
		father2 = obtainItem(CommonMath.randomIntRange(1, size()));
		
		if(CommonMath.selectedOrNotWeighted(father1.getObjectiveCost(), father2.getObjectiveCost())) 
			selectedFather = new Individual(father1);
		else
			selectedFather = new Individual(father2);
		
		for (; sign;) {
			mother1 = obtainItem(CommonMath.randomIntRange(1, size()));
			mother2 = obtainItem(CommonMath.randomIntRange(1, size()));
			if(CommonMath.selectedOrNotWeighted(mother1.getObjectiveCost(), mother2.getObjectiveCost())) 
				selectedMother = new Individual(mother1);
			else
				selectedMother = new Individual(mother2);
			
			if(!selectedFather.compare(selectedMother))
				sign = false;
		}
		
		return new Individual(selectedFather,selectedMother);
	}
	
	
	//mutate the gene "temp"
	public Individual mutate(Individual temp) {
		return temp.mutateIndividual(0.05);				
	}
	
	
	//remove the gene from the list with an index
	public void remove(int index) {
		if (index > size() || index <=0) {
			System.out.println("error with boundary");
			return;
		}
		else {
			N--;
			if (index == 1) {
				first = first.next;
				return;
			}
			else {
				Node after = first.next;
				Node before = first;
				for (int i=1; i<index-1; i++) {
					before = after;
					after = after.next;
				}
				before.next = after.next;
			}		
		}					
	}
	
	//remove the gene with the highest cost from the list
	public void remove() {
		remove(1);
	}
	
	//randomly remove the gene from the first num number of genes in the list
	public void randRemove(int num) {
		int index = num;
		int temp;
		for (int i=0; i<3; i++) {
			temp = CommonMath.randomIntRange(1, num);
			if (index > temp)
				index = temp;
		}
		remove(index);
	}
	
	//check if gene item exists in the list
	public boolean isExist(Individual item) {
		if(isEmpty())
			return false;
		else {
			Node before = first;
			if(before.item.compare(item))
				return true;
			while (before.next != null) {
				before = before.next;
				if(before.item.compare(item))
					return true;
			}
			return false;
		}
	}
	
	//display all the genes in the list
	public void displayAllNode() {
		if(isEmpty()) {
			System.out.println("is empty.");
		}
		else {
			Node before = first;
			before.item.displayGenes();
			while (before.next != null) {
				before = before.next;
				before.item.displayGenes();
			}
		}
					
	}
	
	public void displayBestNode() {
		if(isEmpty()) {
			System.out.println("is empty.");
		}
		else {
			Node before = first;
			while (before.next != null) {
				before = before.next;
			}
			before.item.displayGenes();
		}
	}
	
	public double getBestObjective() {
		if(isEmpty()) {
			System.out.println("is empty.");
			return -1;
		}
		else {
			Node before = first;
			while (before.next != null) {
				before = before.next;
			}
			return before.item.getObjectiveCost();
		}
	}
	
	public void printResults() {
		Node bestNode;
		NetworkAnalysis network;
		Individual bestIndividual;
		boolean sign = false;
		if(isEmpty()) {
			System.out.println("is empty.");
			return;
		}
		else {
			bestNode = first;
			while (bestNode.next != null) {
				bestNode = bestNode.next;			
			}
		}
		
		try {
			File outFile = new File("src/output.txt");
			FileWriter fWriter = new FileWriter (outFile);
			PrintWriter pWriter = new PrintWriter(fWriter);
			network = bestNode.item.Network;
			bestIndividual = bestNode.item;
			
			
//			for (int i=0; i<bestIndividual.getHorizon(); i++) {
//				pWriter.println("Capital Cost for stage " + i + ": " + network.calculateCapitalCost(bestIndividual, i));
//			}
//			for (int i=0; i<bestIndividual.getHorizon(); i++) {
//				pWriter.println("Operational Cost for stage " + i + ": " + network.calculateOperationalCost(bestIndividual, i));
//			}
			pWriter.println("Total Cost: " + bestIndividual.getObjectiveCost());
			pWriter.println("Charger Information: ");
			network.assignChargerExist(bestIndividual);
			for (int i=0; i<network.getNodeSize(); i++) {
				sign = false;
				for (int j=0; j<bestIndividual.getHorizon() && !sign; j++) {
					if (network.chargerExist[i][j]) {
						sign = true;
						pWriter.println(i + ": "+ (j+1));
					}
				}
			}
			
			

			
			pWriter.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printResultsNew() {
		Node bestNode;
		NetworkAnalysis network;
		Individual bestIndividual;
		boolean sign = false;
		if(isEmpty()) {
			System.out.println("is empty.");
			return;
		}
		else {
			bestNode = first;
			while (bestNode.next != null) {
				bestNode = bestNode.next;			
			}
		}
		network = bestNode.item.Network;
		bestIndividual = bestNode.item;
		
		network.printOutput(bestIndividual);
		
		
	}

}
