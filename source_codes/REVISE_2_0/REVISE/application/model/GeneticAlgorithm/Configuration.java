package application.model.GeneticAlgorithm;

import application.controller.UIParameters;

public class Configuration {
	//network configuration
	private int nodeSize;                   //number of nodes in the network, used in Individual
	private int candidateSize;              //number of candidate sites, used in Individual 
	private int odpairs;                    //number of O-D pairs, used in Individual
	
	private int defaultGeneLength;          //length of genes, used in Individual
	private int numPath;                    //number of paths per OD pair, used in NetworkAnalysis
	private int pathNumNode;                //maximum number of nodes along one path, used in NetworkAnalysis
	private int horizon;                    //time stages, used in Individual
	private double penalty;                 //penalty for penalizing infeasible trip, used in NetworkAnalysis
	
	private int numStates;                  //number of states
	
	
	//technology configuration
	private double batteryCapacity;         //battery size in vehicle range (miles), used in NetworkAnalysis
	private double fullInitialBatterySOC;       //beginning battery SOC in vehicle range (miles) when there is charger available at origins, used in Network Analysis 
	private double partialInitialBatterySOC;    //beginning battery SOC in vehicle range (miles) when there is no charger available at origins, used in Network Analysis 
	
	private double stationCost;            //annual station cost ($/yr)
	private double plugCost;               //present value ($) of each plug
	private double discountRate;           //discount rate
	private double plugLifeTime;           //assumed plug life time (years)
	
	private double fixCapitalCost;          //Station cost per stage ($)
	private double varAnnualCapitalCost;    //Annual station cost per charger ($), used in NetworkAnalysis
	private double CVFuelEconomy;           //Conventional vehicle fuel economy (mpg)
	private double EVFuelConsumptionRate;           //Electric vehicle fuel consumption rate (wh/mile)
	private double chargingPower;           //the charging power of public charging (KW)
	
	//cost configuration
	private double CVFuelCost;        //conventional vehicle fuel cost ($/gallon) 
	private double EVFuelCost;        //public charging electricity cost ($/KWh)
	
	//consumer configuration
	private double maxDailyDrivingDistance;  //maximum daily driving distance (miles), used to divide a total trip distance into number of days
	private double timeValue; 			//time value for waiting for recharging ($/hour)
	private double vehicleOccupancy;  	//average vehicle occupancy (pers/veh)
	
	//charging station service
	double averageSOCwhenCharging;  //average SOC (%) when recharging
	double serviceRate;          //average charging station charging time (hours)
	double waitingTimeMin;          //design waiting time for EV at charging station (mins)
	double serviceLevel;         //design service probability (%) for each charging station
	double hoursServiced;        //design open hours for each chargign station (hours/day)
	int maxNumCharger;        //design maximum number of chargers per sation 
	
	//demographic information
	double [] relativeChangeBEVStockRatio;   //relative change based on imported file on BEV stock
	double relativeChangeHomeChargingAvailRatio;
	
	//objectives
	double txtWeightObj1Value;         //objective weighting factor
	double txtWeightObj2Value;
	
	//GA
	private double timeLimit;
	private int numGenes;
	private int numThreads;
	private long randomSeed;
	
	//file and results
	String fileBEVStockStateValue;  //file for BEV market penetration
	String fileStateHomeChargingAvailabilityValue;
	boolean isInfrastructureOutput;   //indicate if infrastructure outputs are provided
	boolean isSummaryOutput;       //indicate if summary outputs are provided
	String scenarioName;
	
	public Configuration () {
		//network configuration
		this.nodeSize = 4301;
		this.candidateSize = 4301;
		this.odpairs = 29241;
		
		this.defaultGeneLength = 4301;
		this.numPath = 1;
		this.pathNumNode = 219;
		this.horizon = 5;
		this.penalty = UIParameters.gettxtRentalCarCostValue();
		
		this.numStates = 51;
		
		//technology configuration
		this.batteryCapacity = UIParameters.gettxtBEVRangeValue();          //miles    Base: 200
		this.fullInitialBatterySOC = this.batteryCapacity;        //miles  Base: 200
		this.partialInitialBatterySOC = this.batteryCapacity*
				UIParameters.getsliderInitialSOCNoHomeChargerValue()/100;        //miles Base: 100
		
		this.plugCost = UIParameters.gettxtPlugCostValue();               
		this.discountRate = UIParameters.gettxtDiscountRateValue()/100;
		this.plugLifeTime = UIParameters.gettxtPlugLifespanValue();
		
		this.stationCost = UIParameters.gettxtStationCostValue();
		this.fixCapitalCost = this.stationCost*5;
		this.varAnnualCapitalCost = this.plugCost*
				this.discountRate*Math.pow(1+this.discountRate, this.plugLifeTime)/
				(Math.pow(1+this.discountRate, this.plugLifeTime)-1)*5;    //$/plugs, Base: 47014.30 cite 2018 corridor charging paper
		this.CVFuelEconomy = UIParameters.gettxtRentalCarFuelEconomyValue();           // Base: 22.4, 2016 weighted average fuel economy across all cars and light trucks, Table 4.3, ref: 2019 TEDB
		this.EVFuelConsumptionRate = UIParameters.gettxtBEVFuelConsumptionValue();    //wh/mile, Base: 320
		this.chargingPower = UIParameters.gettxtChargingPowerValue();   //Base: 150
		
		//cost configuration
		this.CVFuelCost = UIParameters.gettxtGasolinePriceValue();      //$/gallon, Base: 2.42      2017, US average gasoline price, 2017 TEDB
		this.EVFuelCost = UIParameters.gettxtChargingCostValue();                     //, Base: 0.59, 0.49/kWh to 0.69/kWh , Blink, https://www.plugincars.com/ultimate-guide-electric-car-charging-networks-126530.html
		
		//consumer configuration
		this.maxDailyDrivingDistance = UIParameters.gettxtMaximumDailyRangeValue();  //Base: 400
		this.timeValue = UIParameters.gettxtTimeValueValue();           //Base: 25
		this.vehicleOccupancy = UIParameters.gettxtVehicleOccupancyValue();                //all purpose, 2017 NHTS, Base: 1.7
		
		//charging station service
		this.averageSOCwhenCharging = UIParameters.getsliderSOCWhenChargeValue()/100;      //will cite, Joann's data, Base: 0.35
		this.serviceRate = (1-this.averageSOCwhenCharging)*this.batteryCapacity*this.EVFuelConsumptionRate/1000/this.chargingPower;
		this.waitingTimeMin = UIParameters.gettxtWaitingTimeValue();  //mins
		this.serviceLevel = UIParameters.gettxtProbabilityThresholdValue()/100;  
		this.hoursServiced = 18;
		this.maxNumCharger = 999;
		
		relativeChangeBEVStockRatio = new double[this.horizon]; 
		relativeChangeBEVStockRatio[0] = UIParameters.getsliderBEVStock2020Value()/
				UIParameters.getoldsliderBEVStock2020Value();
		relativeChangeBEVStockRatio[1] = UIParameters.getsliderBEVStock2025Value()/
				UIParameters.getoldsliderBEVStock2025Value();
		relativeChangeBEVStockRatio[2] = UIParameters.getsliderBEVStock2030Value()/
				UIParameters.getoldsliderBEVStock2030Value();
		relativeChangeBEVStockRatio[3] = UIParameters.getsliderBEVStock2035Value()/
				UIParameters.getoldsliderBEVStock2035Value();
		relativeChangeBEVStockRatio[4] = UIParameters.getsliderBEVStock2040Value()/
				UIParameters.getoldsliderBEVStock2040Value();
		
		relativeChangeHomeChargingAvailRatio = UIParameters.getsliderNationalHomeChargingAvailabilityValue()/
				UIParameters.getoldsliderNationalHomeChargingAvailabilityValue();
		
		//objectives
		this.txtWeightObj1Value = UIParameters.gettxtWeightObj1Value();
		this.txtWeightObj2Value = UIParameters.gettxtWeightObj2Value();
		
		//file path
		this.fileBEVStockStateValue = UIParameters.getfileBEVStockStateValue();
		this.fileStateHomeChargingAvailabilityValue = UIParameters.getfileStateHomeChargingAvailabilityValue();
		
		this.isInfrastructureOutput = UIParameters.gettoggleInfrastructureOutputSelected();   
		this.isSummaryOutput = UIParameters.gettoggleSummaryOutputSelected();       
		
		if(UIParameters.getTxtScenarioNameMainValue().equals("")) {
			this.scenarioName = "Base";
		}
		else {
			this.scenarioName = UIParameters.getTxtScenarioNameMainValue();
		}
		
		//GA
		this.timeLimit = UIParameters.gettxtTimeLimitValue();
		this.numGenes = UIParameters.gettxtNumGenesValue();
		this.numThreads = UIParameters.getprocessorsValue();
		this.randomSeed = UIParameters.gettxtRandomSeedValue();
		
	}
	
	public String get_inputString() {
		String toPrint = "Key configurations of model:\n";
		
		toPrint += "    Scenario setup and configuration: \n";
		toPrint += "        Scenario name: " + this.scenarioName + "\n";
		toPrint += "        GA algorithm random seed: " + this.randomSeed + "\n";
		toPrint += "        GA algorithm time limit (secs): " + this.timeLimit +"\n";
		toPrint += "        GA genes population pool size: " + this.numGenes + "\n";
		toPrint += "        Gene length: " + this.defaultGeneLength + "\n";
		
		toPrint += "    Station related parameters:\n";
		toPrint += "        Station cost ($/yr) " + this.stationCost + "\n";
		toPrint += "        Plug cost ($/plug) " + this.plugCost + "\n";
		toPrint += "        Design charging power (KW): " + this.chargingPower + "\n";
		toPrint += "        Design plug lifetime (years): " + this.plugLifeTime + "\n";
		toPrint += "        Discount rate: " + this.discountRate + "\n";		
		toPrint += "        Station cost per time stage ($/station/5 yrs): " + this.fixCapitalCost+"\n";
		toPrint += "        Plug cost per time stage ($/plug/5yrs): " + this.varAnnualCapitalCost + "\n";
		toPrint += "        Public charging cost ($/kWh): " + this.EVFuelCost + "\n";
		toPrint += "        Gasoline fuel cost ($/gallon): " + this.CVFuelCost + "\n";
		toPrint += "        Charger design waiting time threshold (mins): " + this.waitingTimeMin + "\n";
		toPrint += "        Charger design service probability threshold: " + this.serviceLevel + "\n";
		toPrint += "        Charger design service hours per day: " + this.hoursServiced + "\n";
		toPrint += "        Average charger service rate (hrs/charging session): " + String.format("%.2f",this.serviceRate) + "\n";
		
		toPrint += "    Vehicle related parameters:\n";
		toPrint += "        Relative change in BEV stock compared to input file: " + 
				+ relativeChangeBEVStockRatio[0] + " " 
				+ relativeChangeBEVStockRatio[1] + " " 
				+ relativeChangeBEVStockRatio[2] + " "
				+ relativeChangeBEVStockRatio[3] + " "
				+ relativeChangeBEVStockRatio[3] + "\n"; 
		toPrint += "        Design BEV vehicle range (miles):  " + this.batteryCapacity + "\n";
		
		toPrint += "    Traveler related parameters:\n";
		toPrint += "        Relative change in home charging availability compared to input file: " + 
				+ relativeChangeHomeChargingAvailRatio + "\n";
		toPrint += "        Initial SOC at beginning with home charging (miles): " + this.fullInitialBatterySOC + "\n";
		toPrint += "        Initial SOC at beginning without home charging (miles): " + this.partialInitialBatterySOC + "\n";
		toPrint += "        Average vehicle occupancy (passengers/veh): " + this.vehicleOccupancy + "\n";
		toPrint += "        Average time value ($/hr): " + this.timeValue + "\n";
		toPrint += "        Maximum daily driving range (miles): " + this.maxDailyDrivingDistance + "\n";
		toPrint += "        Alternative daily rental car cost ($/day): " + this.penalty + "\n";
		toPrint += "        Average SOC when BEV got recharging (%): " + this.averageSOCwhenCharging + "\n";
		toPrint += "        CV rental car fuel economy (mpg): " + this.CVFuelEconomy + "\n";
		toPrint += "        EV fuel consumption rate (wh/mile): " + this.EVFuelConsumptionRate + "\n";
		
		toPrint += "    Network related parameters:\n";
		toPrint += "        Number of nodes: " + this.nodeSize + "\n";
		toPrint += "        Number of candidate sites: " + this.candidateSize + "\n";
		toPrint += "        Number of O-D pairs: " + this.odpairs + "\n";		
		toPrint += "        Number of paths: " + this.numPath + "\n";
		toPrint += "        Number of stages: " + this.horizon + "\n";
		toPrint += "        Number of states: " + this.numStates + "\n";
		
		toPrint += "    Objective function related parameters:\n";
		toPrint += "        Weighting factor for objective 1: " + this.txtWeightObj1Value + "\n";
		toPrint += "        Weighting factor for objective 2: " + this.txtWeightObj2Value + "\n";
	
		this.timeLimit = UIParameters.gettxtTimeLimitValue();
		this.numGenes = UIParameters.gettxtNumGenesValue();
		this.numThreads = UIParameters.getprocessorsValue();
		this.randomSeed = UIParameters.gettxtRandomSeedValue();

		toPrint += "\n"
				+ get_numThreads() + " threads are used for calculation.\n";

		return toPrint;
	}
	
	public int get_nodeSize() {
		return this.nodeSize;
	}
	
	public int get_candidateSize() {
		return this.candidateSize;
	}
	
	public int get_odpairs() {
		return this.odpairs;
	}
	
	public int get_defaultGeneLength() {
		return this.defaultGeneLength;
	}
	
	public int get_numPath() {
		return this.numPath;
	}
	
	public int get_pathNumNode(){
		return this.pathNumNode;
	}
	
	public int get_horizon(){
		return this.horizon;
	}
	
	public double get_penalty(){
		return this.penalty;
	}
	
	public int get_numStates(){
		return this.numStates;
	}
	
	public double get_batteryCapacity(){
		return this.batteryCapacity;
	}
	
	public double get_initialBatterySOC(boolean d) {
		if (d)
			return this.fullInitialBatterySOC;
		else
			return this.partialInitialBatterySOC;
	}
	
	public double get_varAnnualCapitalCost(){
		return this.varAnnualCapitalCost;
	}
	
	public double get_CVFuelEconomy(){
		return this.CVFuelEconomy;
	}
	
	public double get_EVFuelConsumptionRate(){
		return this.EVFuelConsumptionRate;
	}
	
	public double get_chargingPower(){
		return this.chargingPower;
	}
	
	public double get_CVFuelCost(){
		return this.CVFuelCost;
	}
	
	public double get_EVFuelCost(){
		return this.EVFuelCost;
	}
	
	public double get_maxDailyDrivingDistance(){
		return this.maxDailyDrivingDistance;
	}
	
	public double get_timeValue(){
		return this.timeValue;
	}
	
	public double get_vehicleOccupancy() {
		return this.vehicleOccupancy;
	}
	
	
	public double get_serviceRate() {
		return this.serviceRate;
	}
	
	public double get_waitingTimeMin() {
		return this.waitingTimeMin;
	}
	
	public double get_serviceLevel() {
		return this.serviceLevel;
	}
	
	public double get_hoursServiced() {
		return this.hoursServiced;
	}
	
	public int get_maxNumCharger() {
		return this.maxNumCharger;
	}
	
	public double [] get_relativeChangeBEVStockRatio() {
		return this.relativeChangeBEVStockRatio;
	}
	
	public double get_relativeChangeHomeChargingAvailRatio() {
		return this.relativeChangeHomeChargingAvailRatio;
	}
	
	public double get_txtWeightObj1Value() {
		return this.txtWeightObj1Value;
	}
	
	public double get_txtWeightObj2Value() {
		return this.txtWeightObj2Value;
	}
	
	public String get_fileBEVStockStateValue() {
		return this.fileBEVStockStateValue;
	}
	
	public String get_fileStateHomeChargingAvailabilityValue() {
		return this.fileStateHomeChargingAvailabilityValue;
	}
	
	public String get_scenarioName() {
		return this.scenarioName;
	}
	
	public boolean get_isInfrastructureOutput() {
		return this.isInfrastructureOutput;
	}
	public boolean get_isSummaryOutput() {
		return this.isSummaryOutput;
	}
	
	public int get_numThreads() {
		return this.numThreads;
	}
	
	public long get_randomSeed() {
		return this.randomSeed;
	}

}
