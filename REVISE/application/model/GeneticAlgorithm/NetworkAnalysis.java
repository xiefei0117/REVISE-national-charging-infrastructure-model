package application.model.GeneticAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class NetworkAnalysis {
	static Configuration Config = GA.Config;
	int nodeSize;
	int candidateSize;
	int stage;
	int odpairs;
	int mappingCandidate[];
	int ODOrigin[];
	int ODDestination[];
	int ODReverse[];
	int ODtrip[][];
	int numNode[];  //National model
	
	double serviceRate; //National model
	double waitingTime; //National model
	double serviceLevel; //National model
	double hoursServiced; //National model
	
	double chargerCapacity[] ; //National model                  new double[121];
	static final int numPath = Config.get_numPath();
	static final int pathNumNode = Config.get_pathNumNode();
	static final double batteryCapacity = Config.get_batteryCapacity();            ///////////
	final double fullInitialBatterySOC = Config.get_initialBatterySOC(true);
	final double partialInitialBatterySOC = Config.get_initialBatterySOC(false);
	static final double penalty = Config.get_penalty();
	
	static final int numThreads = Config.get_numThreads();
	
	boolean chargerExist[][];
	//double chargerCapitalCost[][];
	double distance[][];
	double trafficFlow[][];
	double fixedAnnualCapitalCost = 0;
	static final double varAnnualCapitalCost = Config.get_varAnnualCapitalCost();
	short isCovered[][];          //=0; both partial and full initial SOC are covered, =1; full is covered, but partial is not, = -1 both are not covered
	double chargeTimes[][];
	int numberCharger[][];
	
	final double pathDistance[];     //National model, record the total distance of a path (miles)
	double fullPenaltyCost[];    //National model, the full penalty cost for each o-d pair if an alternative rental car is used, considering fuel cost, time cost, and rental car cost
	//double tempSOC[];
	
	double statePenetration[][];     //National model, state penetration, states start from 1, 0 is the dummy state
	double stateHomeChargingAvail[];  //National model, state home charging availability %
	double tripOriginChargingAvail[]; //National model, trip origin charging availability %
	final String stateName[];
	final int ODOriginStates[];          //National model, to record which state each OD pair's origin is located
	final int ODDestinationStates[];     //National model, to record which state each OD pair's destination is located
	
	final double txtWeightObj1Value;
	final double txtWeightObj2Value;
	
	final boolean isInfrastructureOutput;   //indicate if infrastructure outputs are provided
	final boolean isSummaryOutput;       //indicate if summary outputs are provided
	
	//variables for multithread
	
	public int getNodeSize() {
		return nodeSize;
	}
	
	public NetworkAnalysis(int nodeSize, int candidateSize, int odpairs, int stage) {
		this.nodeSize = nodeSize;                              //#of nodes in the network
		this.candidateSize = candidateSize;                    //#of locations for charging
		this.stage = stage;
		this.odpairs = odpairs;
		
		
		mappingCandidate = new int[candidateSize];
		ODOrigin = new int[odpairs];
		ODDestination = new int[odpairs];
		ODReverse = new int[odpairs];                                 //The reverse of the OD trip 
		ODtrip = new int[odpairs][pathNumNode];              //The sequence of nodes traversed along each path of the OD trip
		numNode = new int[odpairs];                          //number of network nodes along the path
		
		serviceRate = Config.get_serviceRate(); //National model
		waitingTime = Config.get_waitingTimeMin()/60;     //National model, mins to hours
		serviceLevel = Config.get_serviceLevel(); //National model
		hoursServiced = Config.get_hoursServiced();   //National model
		chargerCapacity = new double [Config.get_maxNumCharger()+1];
		
		chargerExist = new boolean [nodeSize][stage];                 //Indicate weather or not there is a charger at the node
		//chargerCapitalCost = new double [nodeSize][stage];
		distance = new double[nodeSize][nodeSize];
		trafficFlow = new double[odpairs][stage];
		//annualCapitalCost = new double[nodeSize];
		isCovered = new short[odpairs][stage];
		chargeTimes = new double [nodeSize][stage];
		numberCharger = new int[nodeSize][stage];
		//tempSOC = new double[pathNumNode];
		pathDistance = new double[odpairs];      //National Model
		fullPenaltyCost = new double[odpairs];    //National Model
		statePenetration = new double[Config.get_numStates()+1][stage]; //National model, state penetration, states start from 1, 0 is the dummy state
		stateName = new String[Config.get_numStates()+1]; //National Model
		stateHomeChargingAvail =new double[Config.get_numStates()+1]; //National model
		tripOriginChargingAvail = new double[odpairs];  //National
		ODOriginStates = new int[odpairs];
		ODDestinationStates = new int [odpairs];
		
		txtWeightObj1Value = Config.get_txtWeightObj1Value();
		txtWeightObj2Value = Config.get_txtWeightObj2Value();
		isInfrastructureOutput = Config.get_isInfrastructureOutput();   
		isSummaryOutput = Config.get_isSummaryOutput();       
		
		readDataMappingCandidate();
		readDataODtrip();
		readDataDistance();
		readStatePenetration();
		readOriginChargingAvail();
		
		readDataTrafficFlow();
		ErlangStationCapacityforAll();
		this.countNumNodes();
		//readDataCapitalcost();
		
		
		calculatePathDistance ();            //National Model
		calculateFullPenaltyCost();          //National Model
		
	}
	
	
	// ----------------------reading data --------------------------// 
	
	//map candidate node to all nodes
	public void readDataMappingCandidate() {
		try {
			Scanner input = new Scanner (new File("REVISE/application/data/candidateMapping.txt"));
			for(int i=0; i<candidateSize; i++)
				mappingCandidate[i] = input.nextInt();
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public void readDataODtrip() {
		try
		{
			Scanner input = new Scanner(new File("REVISE/application/data/odpair.txt"));
			for (int i=0; i<odpairs; i++) {
				ODOrigin[i] = input.nextInt();
				ODDestination[i] = input.nextInt();
			}
			input.close();		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//National model to read states info for each O and D
		try
		{
			Scanner input = new Scanner(new File("REVISE/application/data/ODstates.txt"));
			for (int i=0; i<odpairs; i++) {
				this.ODOriginStates[i] = input.nextInt();
				this.ODDestinationStates[i] = input.nextInt();
			}
			input.close();		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		for (int i=0; i<odpairs; i++) {
			ODReverse[i] = -1;
			for (int j=0; j<odpairs; j++) {
				if(ODOrigin[j]==ODDestination[i] && ODDestination[j]==ODOrigin[i]) {
					ODReverse[i] = j;
				}
			}
		}
		
		try 
		{
			Scanner input = new Scanner (new File("REVISE/application/data/ODtrip.txt"));
			for(int i=0; i<odpairs; i++) {

				for (int n=0; n<pathNumNode; n++)
					if(input.hasNextInt())
						ODtrip[i][n] = input.nextInt();
													
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readDataDistance() {
		try {
			int origin;
			int destination;
			
			for(int i=0; i<nodeSize; i++)
				for(int j=0; j<nodeSize; j++) {
					distance[i][j] = 1000000;
					if (i == j) {
						distance[i][j] = 0;            //National model if origin and destination are at the same point, then distance equal 0
					}
				}

			
			Scanner input = new Scanner (new File("REVISE/application/data/distance.txt"));
			while (input.hasNext()) {
				origin = input.nextInt();
				destination = input.nextInt();
				distance[origin][destination] = input.nextDouble();
			}
			
			
			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public void readDataCapacity() {
		try {
			int i=0;
			Scanner input = new Scanner (new File("src/chargerCapacity.txt"));
			while (input.hasNext()) {
				chargerCapacity[i] = input.nextDouble()*365*5;
				i++;
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/*public void readDataCapitalcost() {
		try {
			Scanner input = new Scanner (new File("src/capitalcost.txt"));
			
			for (int i=0; i<nodeSize; i++)
					annualCapitalCost[i] = input.nextDouble();	

			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void readDataTrafficFlow() {
		
		try {
			Scanner input = new Scanner (new File("REVISE/application/data/trafficFlow.txt"));       //unit: annual person trips
			
			double vehicleOccupancy = Config.get_vehicleOccupancy();
			for (int i=0; i<odpairs; i++)
				for (int m=0; m<stage; m++) {
						trafficFlow[i][m] = input.nextDouble()/vehicleOccupancy*5*(this.statePenetration[this.ODOriginStates[i]][m]+this.statePenetration[this.ODDestinationStates[i]][m])/2;         //National Model, modified with the EV penetration and vehicle occupancy
				}
			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void readStatePenetration() {
		try {
			Scanner input = new Scanner (new File(Config.get_fileBEVStockStateValue()));
			
			double [] relativeChangeBEVStockRatio = Config.get_relativeChangeBEVStockRatio();
			
			for (int i=1; i<Config.get_numStates()+1; i++) {
				this.stateName[i] = input.next();
				for (int j=0; j<stage; j++) {
					this.statePenetration[i][j] = input.nextDouble()*relativeChangeBEVStockRatio[j];
				}
			}
			
			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readOriginChargingAvail() {
		try {
			Scanner input = new Scanner (new File(Config.get_fileStateHomeChargingAvailabilityValue()));
			double relativeChangeHomeChargingAvailRatio = Config.get_relativeChangeHomeChargingAvailRatio();
			
			
			for (int i=1; i<Config.get_numStates()+1; i++) {
				if (this.stateName[i].equals(input.next()))
					this.stateHomeChargingAvail[i] = input.nextDouble()*relativeChangeHomeChargingAvailRatio;
				else
					throw new StringMisMatchException("state does not match when reading state home charging availability");
			}
			
			input.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StringMisMatchException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		for (int i=0; i<odpairs; i++) {
			tripOriginChargingAvail[i] = this.stateHomeChargingAvail[this.ODOriginStates[i]];
		}
		
	}

	// ----------------------reading data (end) --------------------------//
	
	// ---------------------print data --------------------------------//
	public void printDataChargerExist() {
		for (int i=0; i<nodeSize; i++) {
			for (int j=0; j<stage; j++) {
				if (chargerExist[i][j] == true)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.print("\n");
		}
	}
	
	public void printDataDistance() {
		for (int i=0; i<nodeSize; i++)
			for (int j=0; j<nodeSize; j++) 
				if(distance[i][j] < 100000)
					System.out.print(i + " " + j + " " + distance[i][j] + "\n");
		
	}
	
	public void printDataTrafficFlow(int num) {
		for (int i=0; i<num; i++) {
			
			System.out.print ("origin: " + ODOrigin[i] + " destination: " + ODDestination[i] + "    ");
				for (int m=0; m<stage; m++) {
					System.out.print(trafficFlow[i][m] + " ");
				}
				System.out.print("\n");
			}
	}
	//National Model, print the total distance of each path, and total cost
	public void printPathDistanceAndCost(int num){
		for (int m=0; m<num; m++) {
			System.out.print(pathDistance[m] + " ");
			System.out.print (", total penalty cost is: ");
			System.out.print(this.fullPenaltyCost[m]);
			System.out.print("\n");
		}
	}
	
	//National Model, print state penetration
	public void printStatePenetration(int num) {
		System.out.print("The penetration level at the first " + num + " states are:\n");
		for (int i=1; i<num+1; i++) {
			System.out.print(this.stateName[i] +": ");
			for (int j=0; j<this.stage; j++) {
				System.out.print(" "+ this.statePenetration[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	//National Model, print charger capacity
	public void printChargerCapacityAll (int num) {
		System.out.print ("charger capacity level as follows: \n");
		for (int i=0; i<num; i++) {
			System.out.print(this.chargerCapacity[i]/365/5 + " ");
			if (i%10 == 0) 
				System.out.print("\n");
		}
	}
	
	
	// ---------------------print data (end) ---------------------------//
	
	//National Model, count # of nodes along each path
	public void countNumNodes() {
		for (int i=0; i<odpairs; i++) {
			for (int j=1; this.ODtrip[i][j] != -1; j++) {
				this.numNode[i] = j;
			}
		}
	}
	
	
	
	//National Model, calculate the total distance of each path
	public void calculatePathDistance () {
		int before = 0;
		int after = 0;
		for (int m=0; m<odpairs; m++) {
			pathDistance[m] = 0;
			for (int i=1; i<pathNumNode && ODtrip[m][i] != -1; i++) {
				before = ODtrip[m][i-1];
				after = ODtrip[m][i];
				pathDistance[m] += distance[before][after];
			}
			
		}
	}
	
	//National Model, calculate full penalty cost for each o-d pair
	public void calculateFullPenaltyCost() {
		double CVFuelCost = Config.get_CVFuelCost();
		double EVFuelCost = Config.get_EVFuelCost();
		double CVFuelEconomy = Config.get_CVFuelEconomy();
		double EVFuelConsumptionRate = Config.get_EVFuelConsumptionRate();
		double maxDailyDrivingDistance = Config.get_maxDailyDrivingDistance();
		double timeValue = Config.get_timeValue();
		double rentalCarCost = Config.get_penalty();
		double chargingPower = Config.get_chargingPower();

		for (int i=0; i<odpairs; i++) {
			this.fullPenaltyCost[i] = Math.max(0,rentalCarCost*Math.ceil(this.pathDistance[i]/maxDailyDrivingDistance)+
					CVFuelCost*this.pathDistance[i]/CVFuelEconomy-
					EVFuelCost*EVFuelConsumptionRate*this.pathDistance[i]/1000-
					timeValue*EVFuelConsumptionRate*this.pathDistance[i]/1000/chargingPower);
		}
				
	}
	
	
	//National Model, calculate factorial
	private double factorial(int N) {
		if (N==0)
			return 1;
		double ans = 1.0;
		for (int i=1; i<=N; i++) {
			ans *= i;
		}
		return ans;
	}
	
	//National Model, part of the ErLang formula
	private double tempSumFact(int m, double u) {
		double temp = 0;
		for (int k=0; k<m; k++) {
			temp+= Math.pow(u, k) / this.factorial(k);
		}
		return temp;
	}
	
	//National Model, calculate probablity for Erlang C
	private double calculateProbability(double arrival, int chargers) {
		double lemada = arrival;
		int m = chargers;
		double Ts = this.serviceRate;
		double u = lemada*Ts;
		double row = u/m;
		double Ec = (Math.pow(u, m)/factorial(m))/((Math.pow(u, m) / factorial(m)) + (1 - row)*tempSumFact(m, u));
		double Tw = Ec*Ts/m/(1-row);
		return 1-Ec*Math.exp(-(m-u)*this.waitingTime/Ts);
	}
	
	
	//National Model, calculate capacity (vehs/hour) for each charging station based on number of charger
	public double ErLangStationCapacity(int chargers) {
		double minArrival = 0;
		double maxArrival = chargers / this.serviceRate;
		double capacity = 0;
		double currentArrival = 0;
		double bestEstimateArrival = 0;
		for (; (maxArrival-minArrival)>0.0001;) {
			currentArrival = (maxArrival + minArrival)/2;
			if(this.calculateProbability(currentArrival, chargers) > this.serviceLevel) {
				bestEstimateArrival = currentArrival;
				minArrival = currentArrival;				
			}
			else {
				maxArrival = currentArrival;
			}		
		}
		return bestEstimateArrival;
	}
	
	public void ErlangStationCapacityforAll() {
		this.chargerCapacity[0] = 0;
		for (int i =1; i<= 120; i++) {
			chargerCapacity[i] = this.ErLangStationCapacity(i)*this.hoursServiced*365*5;
		}
		
		double incrementalCapacity = this.chargerCapacity[120] - this.chargerCapacity[119];
		
		for (int i = 121; i<= Config.get_maxNumCharger(); i++) {
			this.chargerCapacity[i] = this.chargerCapacity[i-1] + incrementalCapacity;
		}
		
	}
	
	
	
	//Check if path k from origin i to j is covered in year m  return true or false
	public boolean checkIfCovered(int odpair, int year, double initialSOC) {
		double SOC = initialSOC;           //National Model, allow a different beginning SOC for EV
		int before;
		int after=0;
		
		if (pathDistance[odpair] < SOC)
			return true;
		
		if (fullPenaltyCost[odpair]<=0)
			return false;
		
		try {
			for (int i=1; i<pathNumNode && ODtrip[odpair][i] != -1; i++) {
				before = ODtrip[odpair][i-1];
				after = ODtrip[odpair][i];
				
				SOC = SOC - distance[before][after];
				if (SOC < 0)
					return false;
				if (chargerExist[after][year])
					SOC = batteryCapacity-0.0001;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("charger Exist error" + after + " " + year);
			System.exit(1);
		}
		if (SOC < 0)
			return false;
		return true;		
	}
	
	public void assignedODCoveredMultiThreadLessEfficient() {
		
		int numThreads = 4;
		int step1 = odpairs/4;
		int step2 = 2*odpairs/4;
		int step3 = 3*odpairs/4;
		
		Thread t1 = new Thread (new Runnable(){
			public void run() {
				for (int i=0; i<step1; i++) {
					
					for (int m=0; m<stage; m++) {
						if(checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						/*
						else if (checkIfCovered(i,m,2)) {
							isCovered[i][m] = 2;
						}*/
						else {
							isCovered[i][m]=-1;
						}
					}
				}
			}
		});
		
		Thread t2 = new Thread (new Runnable(){
			public void run() {
				for (int i=step1; i<step2; i++) {
										
					for (int m=0; m<stage; m++) {
						if(checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						/*
						else if (checkIfCovered(i,m,2)) {
							isCovered[i][m] = 2;
						}*/
						else {
							isCovered[i][m]=-1;
						}
					}
				}
			}
		});
		
		Thread t3 = new Thread (new Runnable(){
			public void run() {
				for (int i=step2; i<step3; i++) {
					
					for (int m=0; m<stage; m++) {
						if(checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						/*
						else if (checkIfCovered(i,m,2)) {
							isCovered[i][m] = 2;
						}*/
						else {
							isCovered[i][m]=-1;
						}
					}
				}
			}
		});
		
		Thread t4 = new Thread (new Runnable(){
			public void run() {
				for (int i=step3; i<odpairs; i++) {
					
					for (int m=0; m<stage; m++) {
						if(checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						/*
						else if (checkIfCovered(i,m,2)) {
							isCovered[i][m] = 2;
						}*/
						else {
							isCovered[i][m]=-1;
						}
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.print(" assignedODCovered ");   //toremove
		
	}
	
	
	public void assignedODCoveredMultiThread() {
		int step = odpairs/this.numThreads;
		
		Thread myThreads[] = new Thread[this.numThreads];
		
		class AssignedODCoveredOneThread implements Runnable {
			int startOD;
			int endOD;
			AssignedODCoveredOneThread(int s, int e) {startOD = s; endOD = e;}
			public void run() {
				for (int i=startOD; i<endOD; i++) {
					
					//stage 1, initial check
					if(checkIfCovered(i,0,partialInitialBatterySOC)) {
						isCovered[i][0] = 1;
					}
					else if (checkIfCovered(i,0,fullInitialBatterySOC)) {
						isCovered[i][0] = 0;
					}
					else {
						isCovered[i][0]=-1;
					}
					
					//later stages
					for (int m=1; m<stage; m++) {
						if(isCovered[i][m-1] == 1) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (isCovered[i][m-1] == 0) {
							isCovered[i][m] = 0;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						else {
							isCovered[i][m] = -1;
						}
					}
				}
			}
			
		}
		
		//the first n-1 threads
		for (int j = 0; j<this.numThreads-1; j++) {
			myThreads[j] = new Thread(new AssignedODCoveredOneThread(j*step, (j+1)*step));
		}
		//the n thread
		myThreads[this.numThreads-1] = new Thread(new AssignedODCoveredOneThread((this.numThreads-1)*step, odpairs));
		
		for(int j=0; j<this.numThreads; j++) {
			myThreads[j].start();
		}
		for(int j=0; j<this.numThreads; j++) {
			try {
				myThreads[j].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void assignedODCoveredMultiThreadOld2() {  //fixed number of threads = 4
		
		int numThreads = 4;
		int step1 = odpairs/4;
		int step2 = 2*odpairs/4;
		int step3 = 3*odpairs/4;
		
		Thread t1 = new Thread (new Runnable(){
			public void run() {
				for (int i=0; i<step1; i++) {
					
					//stage 1, initial check
					if(checkIfCovered(i,0,partialInitialBatterySOC)) {
						isCovered[i][0] = 1;
					}
					else if (checkIfCovered(i,0,fullInitialBatterySOC)) {
						isCovered[i][0] = 0;
					}
					else {
						isCovered[i][0]=-1;
					}
					
					//later stages
					for (int m=1; m<stage; m++) {
						if(isCovered[i][m-1] == 1) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (isCovered[i][m-1] == 0) {
							isCovered[i][m] = 0;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						else {
							isCovered[i][m] = -1;
						}
					}
				}
			}
		});
		
		Thread t2 = new Thread (new Runnable(){
			public void run() {
				for (int i=step1; i<step2; i++) {
					
					//stage 1, initial check
					if(checkIfCovered(i,0,partialInitialBatterySOC)) {
						isCovered[i][0] = 1;
					}
					else if (checkIfCovered(i,0,fullInitialBatterySOC)) {
						isCovered[i][0] = 0;
					}
					else {
						isCovered[i][0]=-1;
					}
					
					//later stages
					for (int m=1; m<stage; m++) {
						if(isCovered[i][m-1] == 1) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (isCovered[i][m-1] == 0) {
							isCovered[i][m] = 0;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						else {
							isCovered[i][m] = -1;
						}
					}
				}
			}
		});
		
		Thread t3 = new Thread (new Runnable(){
			public void run() {
				for (int i=step2; i<step3; i++) {
					
					//stage 1, initial check
					if(checkIfCovered(i,0,partialInitialBatterySOC)) {
						isCovered[i][0] = 1;
					}
					else if (checkIfCovered(i,0,fullInitialBatterySOC)) {
						isCovered[i][0] = 0;
					}
					else {
						isCovered[i][0]=-1;
					}
					
					//later stages
					for (int m=1; m<stage; m++) {
						if(isCovered[i][m-1] == 1) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (isCovered[i][m-1] == 0) {
							isCovered[i][m] = 0;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						else {
							isCovered[i][m] = -1;
						}
					}
				}
			}
		});
		
		Thread t4 = new Thread (new Runnable(){
			public void run() {
				for (int i=step3; i<odpairs; i++) {
					
					//stage 1, initial check
					if(checkIfCovered(i,0,partialInitialBatterySOC)) {
						isCovered[i][0] = 1;
					}
					else if (checkIfCovered(i,0,fullInitialBatterySOC)) {
						isCovered[i][0] = 0;
					}
					else {
						isCovered[i][0]=-1;
					}
					
					//later stages
					for (int m=1; m<stage; m++) {
						if(isCovered[i][m-1] == 1) {
							isCovered[i][m] = 1;
						}
						else if (checkIfCovered(i,m,partialInitialBatterySOC)) {
							isCovered[i][m] = 1;
						}
						else if (isCovered[i][m-1] == 0) {
							isCovered[i][m] = 0;
						}
						else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
							isCovered[i][m] = 0;
						}
						else {
							isCovered[i][m] = -1;
						}
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.print(" assignedODCovered ");   //toremove
		
	}
	//not used
	public void assignedODCovered() {
		
		for (int i=0; i<odpairs; i++) {
			
			for (int m=0; m<stage; m++) {
				if(checkIfCovered(i,m,partialInitialBatterySOC)) {
					isCovered[i][m] = 0;
				}
				else if (checkIfCovered(i,m,fullInitialBatterySOC)) {
					isCovered[i][m] = 1;
				}
				/*
				else if (checkIfCovered(i,m,2)) {
					isCovered[i][m] = 2;
				}*/
				else {
					isCovered[i][m]=-1;
				}
			}
		}
		
	}
	
	public int determineNumCharger(double flow) {
		for (int i=0; i<Config.get_maxNumCharger()+1; i++) {
			if(flow <= chargerCapacity[i]) {
				return i;
			}
		}
		return 9999;
	}
	
	
	//reset charger exist matrix, let all locations to have no chargers
	public void resetChargerExist() {
		for (int i=0; i<nodeSize; i++)
			for (int j=0; j<stage; j++) {
				chargerExist[i][j] = false;
				chargeTimes[i][j] = 0;
				numberCharger[i][j] = 0;				
			}				
	}
	
	//based on the Individual, determine where charger is located
	public void assignChargerExist(Individual solution) {
		resetChargerExist();
		for (int i=0; i<candidateSize; i++) {
			if (solution.displayGeneElement(i) > 0) {
				int year = solution.displayGeneElement(i)-1;
				int j = mappingCandidate[i];
				for (int m=year; m<stage; m++)
					chargerExist[j][m] = true;
			}
		}
	}
	/*
	public void displaySOC(int maxNumNode) {
		for (int i=0; i<= maxNumNode; i++) {
			System.out.printf("%.1f  -> ", tempSOC[i]);
		}
		System.out.print("\n");
	}
	*/
	
	public void countChargeTimes(int od, int stage, double initialSOC, double popShare) {
		
		double[] tempSOC = new double[this.numNode[od]+1];
		
		//if(numPath == -1)
		//	return;
		
		int currentNode = 0;
		double currentCharge = 0;
		
		tempSOC[0] = initialSOC;
		for (int i=1; i<=this.numNode[od]; i++) {
			tempSOC[i]=tempSOC[i-1] - distance[ODtrip[od][i-1]][ODtrip[od][i]];
		}
		//System.out.print("a");  //toremove
		
		int tempCount = 0; ///
		
		for (;tempSOC[this.numNode[od]]<0;) {
			tempCount++;    ////
			for (int i=this.numNode[od]; i>=currentNode; i--) {
				if(tempSOC[i]>=0 && chargerExist[ODtrip[od][i]][stage]) {
					currentNode = i;
					/*if(tempCount <=5)
						System.out.println(currentNode);        ////
*/					currentCharge = batteryCapacity - tempSOC[i];
					synchronized (chargeTimes[ODtrip[od][i]]) {
						chargeTimes[ODtrip[od][i]][stage] += trafficFlow[od][stage]*popShare;
					}
					for (int j=currentNode; j<=this.numNode[od]; j++) {
						tempSOC[j] += currentCharge;
					}
				}
				
			}
			/*if(tempCount ==1000) {
				for (int m = maxNumNode; m>=currentNode; m--) {
					System.out.println(m + ": " + tempSOC[m] + ", chargerExist - " + chargerExist[ODtrip[od][numPath][m]][stage]);
				}
			}*/
				//System.out.print(od + " " + stage + " " + maxNumNode + " " + currentCharge + " "+ tempSOC[maxNumNode] + " " + currentNode + " "+ tempSOC[currentNode] + tempSOC[currentNode-1] + "\n");         ////
		}
	//	System.out.print(" tempCount ");  //toremove
		//displaySOC(maxNumNode);         ////
	}
	

	public void countChargeTimesFullandPartial(int od, int stage, double initialSOCPartial, double popShareFull) {
		
		double[] tempSOCPartial = new double[this.numNode[od]+1];
		double[] tempSOCFull = new double[this.numNode[od]+1];
		double PartialShare = 1-popShareFull;
		double FullShare = popShareFull;
		
		boolean flag = false;
		boolean flag2 = false;
		int currentNode = 0;
		double currentChargeFull = 0;
		double currentChargePartial =0;
		
		tempSOCPartial[0] = initialSOCPartial;
		tempSOCFull[0] = batteryCapacity;
		
		for (int i=1; i<=this.numNode[od]; i++) {
			tempSOCPartial[i]=tempSOCPartial[i-1] - distance[ODtrip[od][i-1]][ODtrip[od][i]];
			tempSOCFull[i]=tempSOCFull[i-1] - distance[ODtrip[od][i-1]][ODtrip[od][i]];
		}
		//System.out.print("a");  //toremove
		
		for(;tempSOCPartial[this.numNode[od]]<0;) {
			for (int i=this.numNode[od]; i>=currentNode; i--) {
				
				//if both veh_full and veh_par share the same charging locations from the location i to destination
				if (flag) {
					//if there is charging need for veh_full at location i
					if(tempSOCFull[i] >=0 && chargerExist[ODtrip[od][i]][stage]) {
						currentChargeFull = batteryCapacity - tempSOCFull[i];
						synchronized (chargeTimes[ODtrip[od][i]]) {
							chargeTimes[ODtrip[od][i]][stage] += trafficFlow[od][stage];
						}
						          //both veh_full and veh_par charge there
						for (int j=i; j<=this.numNode[od]; j++) {
							tempSOCFull[j] += currentChargeFull;
							tempSOCPartial[j] = tempSOCFull[j];
						}
						currentNode = i;
					}
				}
				// if veh_full has already been charged at some location later (no need to consider previous locations for veh_full)
				else if (flag2) {
					//if there is charging need for veh_par at location i
					if(tempSOCPartial[i] >=0 && chargerExist[ODtrip[od][i]][stage]) {
						currentChargePartial = batteryCapacity - tempSOCPartial[i];
						synchronized (chargeTimes[ODtrip[od][i]]) {
							chargeTimes[ODtrip[od][i]][stage] += trafficFlow[od][stage]*PartialShare;  //veh_par charge there
						}
						for (int j=i; j<=this.numNode[od]; j++) {
							tempSOCPartial[j] += currentChargePartial;
						}
						currentNode = i;						//end this iteration
						flag2 = true;                           //reset flag2
					}
				}
				
				//still need to consider the charging need for veh_full in this iteration
				else {
					//if there is a charging need for veh_full at location i
					if(tempSOCFull[i] >=0  && chargerExist[ODtrip[od][i]][stage]) {
						//if there is also a charging need for veh_par
						if(tempSOCPartial[i] >=0) {
							//veh_full and veh_par could share the same charging points from location i 
							flag = true;
							
							currentChargeFull = batteryCapacity - tempSOCFull[i];
							synchronized (chargeTimes[ODtrip[od][i]]) {
								chargeTimes[ODtrip[od][i]][stage] += trafficFlow[od][stage];
							}
							for (int j=i; j<=this.numNode[od]; j++) {
								tempSOCFull[j] += currentChargeFull;
								tempSOCPartial[j] = tempSOCFull[j];
							}
							currentNode = i;	
						}
						
						else {
							currentChargeFull = batteryCapacity - tempSOCFull[i];
							synchronized (chargeTimes[ODtrip[od][i]]) {
								chargeTimes[ODtrip[od][i]][stage] += trafficFlow[od][stage]*FullShare;
							}
							for (int j=i; j<=this.numNode[od]; j++) {
								tempSOCFull[j] += currentChargeFull;
							}
							flag2 = true;
							
						}
					}
				}
			}
		}

	}
	

	
	
	
	public void determineNumChargers() {
		for (int i=0; i<nodeSize; i++) {
			for (int j=0; j<stage; j++) {
				if (j==0) {
					numberCharger[i][j] = Math.max(determineNumCharger(chargeTimes[i][j]),((chargerExist[i][j]==true)?1:0));					
				}
				else {
					numberCharger[i][j] = Math.max(Math.max(determineNumCharger(chargeTimes[i][j]), numberCharger[i][j-1]),((chargerExist[i][j]==true)?1:0)); 
				}
			}
		}
	}
	
	public void printOutput(Individual solution) {
		
		assignChargerExist(solution);
		assignedODCoveredMultiThread();
		countChargeTimesAllMultiThread(); 

		determineNumChargers();
		try{
			if (this.isInfrastructureOutput) {
		///////to implement		
				Scanner input = new Scanner (new File("REVISE/application/data/stationCoordinates.txt"));
				
				String stationID[] = new String[this.candidateSize];
				double xCoordinate[] = new double[this.candidateSize];
				double yCoordinate[] = new double[this.candidateSize];
				int numCapacity[][] = new int[this.candidateSize][this.stage];
				
				for(int i=0; i<this.candidateSize; i++) {
					stationID[i] = input.next();
					xCoordinate[i] = input.nextDouble();
					yCoordinate[i] = input.nextDouble();
				}
				input.close();	
				
				
				File outFile = new File("REVISE/" + Config.get_scenarioName() + "_station_results.csv");
				FileWriter fWriter = new FileWriter (outFile);
				PrintWriter pWriter = new PrintWriter(fWriter);
				
				
				pWriter.print("StationID, CoordinateX, CoordinateY, 2020-2024, 2025-2029, 2030-2034, 2035-2039, 2040-2044\n");
				
				for (int i=0; i<this.candidateSize; i++) {
					pWriter.print(stationID[i] + ", " + xCoordinate[i] + ", " + yCoordinate[i]);
					int j=mappingCandidate[i];
					for (int m=0; m<stage; m++) {
						pWriter.print(", "+numberCharger[j][m]);
					}
					pWriter.print("\n");
				}
				pWriter.close();
			}
			
			if (this.isSummaryOutput) {
				double capitalCost = 0;
				double operationalCost = 0;
				
				double electricVMT = 0;
				double nonElectricVMT = 0;
				
				File outFile = new File("REVISE/" + Config.get_scenarioName() + "_summary.csv");
				FileWriter fWriter = new FileWriter (outFile);
				PrintWriter pWriter = new PrintWriter(fWriter);
				
				pWriter.print("Stage, Period, Infrastructure_Cost, Traveler_Cost, non-EVMT, EVMT, gasoline (mmBTU), electricity (mmBTU)\n");
				String periodName[] = new String[]{"2020-2024","2025-2029", "2030-2034", "2035-2039", "2040-2044"};
				
				for (int j=0; j<stage; j++) {
					//stage
					pWriter.print(j + ", ");
					//period
					pWriter.print(periodName[j] + ", ");
					//Infrastructure_Cost
					capitalCost = 0;
					for (int i=0; i<nodeSize; i++) {
						if(chargerExist[i][j])
							capitalCost += fixedAnnualCapitalCost;
							capitalCost += numberCharger[i][j]*varAnnualCapitalCost;
					}
					pWriter.print(capitalCost + ", ");
					//Traveler_Cost
					operationalCost = 0;
					for (int i=0; i<odpairs; i++) {
						if(isCovered[i][j] == -1)
							operationalCost += this.fullPenaltyCost[i]*trafficFlow[i][j];
						else if (isCovered[i][j] == 0)
							operationalCost +=this.fullPenaltyCost[i]*trafficFlow[i][j]*(1-this.tripOriginChargingAvail[i]);
						
					}
					pWriter.print(operationalCost + ", ");
					//other
					electricVMT = 0;
					nonElectricVMT = 0;
					
					for (int i=0; i<odpairs; i++) {
						if(isCovered[i][j] == -1)
							nonElectricVMT += this.pathDistance[i]*trafficFlow[i][j];
						else if (isCovered[i][j] == 0) {
							nonElectricVMT += this.pathDistance[i]*trafficFlow[i][j]*(1-this.tripOriginChargingAvail[i]);
							electricVMT += this.pathDistance[i]*trafficFlow[i][j]*this.tripOriginChargingAvail[i];
						}
						else
							electricVMT += this.pathDistance[i]*trafficFlow[i][j];				
					}
					pWriter.print(nonElectricVMT + ", " + electricVMT + ", " +
							nonElectricVMT/Config.get_CVFuelEconomy()*114000/1000000 + ", " +
							electricVMT*Config.get_EVFuelConsumptionRate()/1000*3412.14/1000000);
					pWriter.print("\n");
				}						
				
				pWriter.close();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//multithread countCharger
	
public void countChargeTimesAllMultiThread() {
	int step = odpairs/this.numThreads;
	
	Thread myThreads[] = new Thread[this.numThreads];
	
	class CountChargeTimesAllOneThread implements Runnable {
		int startOD;
		int endOD;
		CountChargeTimesAllOneThread(int s, int e) {startOD = s; endOD = e;}
		public void run() {
			for (int i=startOD; i<endOD; i++) {
				
				for (int m=0; m<stage; m++) {
					if (isCovered[i][m] == 1) {
						countChargeTimesFullandPartial(i,m,partialInitialBatterySOC, tripOriginChargingAvail[i]);			
					}
					else if (isCovered[i][m] == 0) {
						countChargeTimes(i,m,fullInitialBatterySOC,tripOriginChargingAvail[i]);
					}
				}
			}
		}
	}
	
	//the first n-1 threads
			for (int j = 0; j<this.numThreads-1; j++) {
				myThreads[j] = new Thread(new CountChargeTimesAllOneThread(j*step, (j+1)*step));
			}
			//the n thread
			myThreads[this.numThreads-1] = new Thread(new CountChargeTimesAllOneThread((this.numThreads-1)*step, odpairs));
			
			for(int j=0; j<this.numThreads; j++) {
				myThreads[j].start();
			}
			for(int j=0; j<this.numThreads; j++) {
				try {
					myThreads[j].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
}
	
	

public void countChargeTimesAllMultiThreadOld2() {   //fixed number of threads, = 4
		
		int numThreads = 4;
		int step1 = odpairs/4;
		int step2 = 2*odpairs/4;
		int step3 = 3*odpairs/4;
		
		Thread t1 = new Thread (new Runnable(){
			public void run() {
				for (int i=0; i<step1; i++) {
					
					for (int m=0; m<stage; m++) {
						if (isCovered[i][m] == 1) {
							countChargeTimesFullandPartial(i,m,partialInitialBatterySOC, tripOriginChargingAvail[i]);			
						}
						else if (isCovered[i][m] == 0) {
							countChargeTimes(i,m,fullInitialBatterySOC,tripOriginChargingAvail[i]);
						}
					}
				}
			}
		});
		
		Thread t2 = new Thread (new Runnable(){
			public void run() {
				for (int i=step1; i<step2; i++) {
					
					for (int m=0; m<stage; m++) {
						if (isCovered[i][m] == 1) {
							countChargeTimesFullandPartial(i,m,partialInitialBatterySOC, tripOriginChargingAvail[i]);					
						}
						else if (isCovered[i][m] == 0) {
							countChargeTimes(i,m,fullInitialBatterySOC,tripOriginChargingAvail[i]);
						}
					}
				}
			}
		});
		
		Thread t3 = new Thread (new Runnable(){
			public void run() {
				for (int i=step2; i<step3; i++) {
					
					for (int m=0; m<stage; m++) {
						if (isCovered[i][m] == 1) {
							countChargeTimesFullandPartial(i,m,partialInitialBatterySOC, tripOriginChargingAvail[i]);				
						}
						else if (isCovered[i][m] == 0) {
							countChargeTimes(i,m,fullInitialBatterySOC,tripOriginChargingAvail[i]);
						}
					}
				}
			}
		});
		
		Thread t4 = new Thread (new Runnable(){
			public void run() {
				for (int i=step3; i<odpairs; i++) {
					
					for (int m=0; m<stage; m++) {
						if (isCovered[i][m] == 1) {
							countChargeTimesFullandPartial(i,m,partialInitialBatterySOC, tripOriginChargingAvail[i]);				
						}
						else if (isCovered[i][m] == 0) {
							countChargeTimes(i,m,fullInitialBatterySOC,tripOriginChargingAvail[i]);
						}
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void countChargeTimesAll() {
		for (int i=0; i<odpairs; i++) {
			for (int m=0; m<stage; m++) {
				if (isCovered[i][m] == 1) {
					countChargeTimes(i,m,partialInitialBatterySOC, 1-this.tripOriginChargingAvail[i]);
					countChargeTimes(i,m,fullInitialBatterySOC,this.tripOriginChargingAvail[i]);					
				}
				else if (isCovered[i][m] == 0) {
					countChargeTimes(i,m,fullInitialBatterySOC,this.tripOriginChargingAvail[i]);
				}
			}			
		}
		//System.out.print(" countCountCharge \n");   //toremove
	}
	
	
	
	
	public double calculateTotalCost(Individual solution, boolean Multithread) {
		double capitalCost = 0;
		double operationalCost = 0;
		//System.out.println("assignChargerExist");        /////
		assignChargerExist(solution);
		
		//System.out.println(Multithread);        /////
		if(Multithread) {
			//System.out.println("assignedODCovered");        /////
			assignedODCoveredMultiThread();
			//assignedODCoveredMultiThreadLessEfficient();
			//System.out.println("count charger times");        /////
			//countChargeTimesAll();
			countChargeTimesAllMultiThread(); 
		}
		else {
			assignedODCovered();             //needs multithread
			countChargeTimesAll();
		}
		//countChargeTimesAll();
		///System.out.println("ccc:");        /////
		
		
		
		
		//System.out.println("determine num chargers");        /////
		determineNumChargers();
		
	
		
		//System.out.println("calculate objective cost");        /////
			for (int i=0; i<nodeSize; i++) {
				for (int j=0; j<stage; j++) {
					if (chargerExist[i][j])
						capitalCost += fixedAnnualCapitalCost;
						capitalCost += numberCharger[i][j]*varAnnualCapitalCost;
				}
			}
			
			for (int i=0; i<odpairs; i++) {
				for (int m=0; m<stage; m++) {
					if(isCovered[i][m] == -1)
						operationalCost += this.fullPenaltyCost[i]*trafficFlow[i][m];
					else if (isCovered[i][m] == 0)
						operationalCost +=this.fullPenaltyCost[i]*trafficFlow[i][m]*(1-this.tripOriginChargingAvail[i]);
				}			
			}
		
		//	System.out.println("reset charger");        /////
		resetChargerExist();
		
		return txtWeightObj1Value*capitalCost + txtWeightObj2Value*operationalCost;
				
	}
	
}

