package application.model.GeneticAlgorithm;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import application.controller.CalculationController;
import javafx.application.Platform;

import java.text.DecimalFormat;

public class GA {
	
	Population pool; 
	public static Configuration Config;
	int populationSize;
	private final CalculationController controller1;	//userinterface
	
	
	public GA(int populationSize, boolean Multithread, CalculationController controller1) {
		this.controller1 = controller1;
		
		this.controller1.JFXTextAreaAppendText("Initializing population pool......\n");
		
		Config = new Configuration();
		this.populationSize = populationSize;                  //#of genes in the pool
		
		pool = new Population(populationSize, Multithread, this.controller1);		
				
		
	}
	
	// numIteration: num of total iterations
	// tournament Parents: randomly select two mothers, and two fathers and tournament first
	//numPool: the candidate selection population size for immigrants
	//numIn: num of immigrants each time
	//largePool: large immigrants size
	//numLargeTimes: total numIteration/numLargeTimes is the toleration iterations for unchanged
	
	
	public void GARunWithTime(double limitedTime, boolean tournamentParents, int numPool, int numIn, int largePool, double timeToLarge,boolean Multithread) {
		Config = new Configuration();
		this.controller1.JFXTextAreaAppendText(Config.get_inputString());
		this.controller1.JFXTextAreaAppendText("");
		
		long startTime = System.nanoTime();
		long shortDisplayTime = startTime;
		long longDisplayTime = startTime;
		long UnchangedTime = startTime;
		long currentTime = startTime;
		double currentBestObjective = pool.individuals.getBestObjective();
		double firstBestObjective = currentBestObjective;
		double previousBestObjective = currentBestObjective;
		double convertTime = 1000000000.0;
		double elapseTimeTotal = 0;
		double elapseTimeShortDisplay = 0;
		double elapseTimeLongDisplay = 0;
		double elapseUnchangedTime = 0;
		Individual child = new Individual();  ///
		boolean sign = false;
		long iteration = 0;
		long longDisplayTimeCondition = 0;
		DecimalFormat df= new DecimalFormat("#.##%");
		//File outFile = new File("src/process.txt");
		DecimalFormat decimalFormat0 = new DecimalFormat("#");
		
		this.controller1.JFXTextAreaAppendText("\nGA algorithm starts......\n");
		for (;elapseTimeTotal < limitedTime & (!controller1.isCancelled());) {
			final double elapseTimeTotalFinal = elapseTimeTotal;
			Platform.runLater(new Runnable() {
				
	            @Override public void run() {
	            	
	            	controller1.setProgressBar(elapseTimeTotalFinal/limitedTime);
	            	controller1.setProgressLabel(elapseTimeTotalFinal/limitedTime);
	            }
	        });
			
			
			sign = false;
			for(; !sign;) {
				child = pool.individuals.crossOver(tournamentParents);
				child = pool.individuals.mutate(child);
				child.assignHashID();
				child.calculateObjectiveCost(Multithread);
				if(!pool.individuals.isExist(child)) {
					sign = true;
					iteration++;
				}
			}			
			pool.removeIndividual(populationSize-1);
			pool.saveIndividual(child);
			
			currentTime = System.nanoTime();
			currentBestObjective = pool.individuals.getBestObjective();
			
			if(currentBestObjective != previousBestObjective) {
				UnchangedTime = currentTime;
			}
			
			elapseTimeTotal = (currentTime-startTime)/convertTime;
			elapseTimeShortDisplay = (currentTime-shortDisplayTime)/convertTime;
			elapseTimeLongDisplay = (currentTime-longDisplayTime)/convertTime;
			elapseUnchangedTime = (currentTime-UnchangedTime)/convertTime;
			
			
			
			if(elapseTimeShortDisplay > 5 || iteration == 1) {
				this.controller1.JFXTextAreaAppendText("Iter. - " + iteration + ", Time Elaspsed: " + decimalFormat0.format(elapseTimeTotal) + "s" + ", Obj. Value: " + decimalFormat0.format(currentBestObjective)
						+ ", Incr. Change: " + df.format((currentBestObjective-previousBestObjective)/previousBestObjective) +
						", Total Change: " + df.format(((currentBestObjective-firstBestObjective)/firstBestObjective))+"\n");
				shortDisplayTime = currentTime;
				previousBestObjective = currentBestObjective;
			}
			if(elapseTimeLongDisplay > longDisplayTimeCondition) {                       ////////////////////////////////////////////////////////////////////////////// 10
				//pool.individuals.displayBestNode();
				if (longDisplayTimeCondition >= 60) {
					//System.out.println("new " + numIn + " seeds out of " + numPool + " candidate seeds join the pool");
					pool.immigrant(numPool, numIn, Multithread); 
				}
				longDisplayTime = currentTime;
				longDisplayTimeCondition++;
				//pWriter.println(iteration + ", " + elapseTimeTotal + ", " + currentBestObjective + ", " + 
				//df.format(((currentBestObjective-firstBestObjective)/firstBestObjective)));
				//pWriter.flush();
			}
			
			
			
			if(elapseUnchangedTime > timeToLarge) {
				System.out.println("New Immigration with " + largePool + " individuals");
				pool.immigrantLarge(largePool, Multithread);
				UnchangedTime = currentTime;
			}
			
			
				
		}
		if (!controller1.isCancelled()) {
			Platform.runLater(new Runnable() {
				
	            @Override public void run() {
	            	
	            	controller1.setProgressBar(1);
	            	controller1.setProgressLabel(1);
	            }
	        });
		}
		
		this.controller1.JFXTextAreaAppendText("printing results......");
		//System.out.println("The final results are:");
		pool.individuals.printResultsNew();
		
		File outFile = new File("REVISE/" + Config.get_scenarioName() + "_process.txt");
		FileWriter fWriter;
		try {
			fWriter = new FileWriter (outFile);
			PrintWriter pWriter = new PrintWriter(fWriter);
	
			pWriter.print(controller1.JFXTextAreaGetText());
			pWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.controller1.JFXTextAreaAppendText("done");
		
		//pool.individuals.displayBestNode();
		//pWriter.close();
	}

	
	
	

	
}

	
