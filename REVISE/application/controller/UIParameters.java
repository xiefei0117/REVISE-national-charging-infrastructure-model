package application.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UIParameters {
	
	private static double [] stateWeight = {0.016490142,0.002314702,0.021779087,0.010012443,
			0.103841853,0.016921609,0.011204443,0.003102411,0.002281178,0.068128142,
			0.030947346,0.003962693,0.005091698,0.039829135,0.021155019,0.01016256,
			0.009334935,0.014582254,0.014990905,0.005445124,0.01810211,0.021185809,
			0.034074123,0.017836295,0.009662179,0.020473964,0.003657835,0.006076932,
			0.008937149,0.004642498,0.026943253,0.006815629,0.061188638,0.033088602,
			0.002555013,0.038583484,0.012667318,0.012680713,0.041905401,0.00347613,
			0.01625456,0.002788276,0.021391964,0.077224557,0.007575936,0.002439104,
			0.025695682,0.022049352,0.006622214,0.019828865,0.001998737
			};
	
	
	//main page
	private static String txtScenarioNameMainValue = "";
	
	//vehicle page
	private static boolean radioBEVStockNationalSelected = true;
	
	private static double oldsliderBEVStock2020Value = 1.2;        //not change, unless reimport file
	private static double oldsliderBEVStock2025Value = 6.1;		//not change, unless reimport file
	private static double oldsliderBEVStock2030Value = 14.6;	//not change, unless reimport file
	private static double oldsliderBEVStock2035Value = 26.1;	//not change, unless reimport file
	private static double oldsliderBEVStock2040Value = 34.3;	//not change, unless reimport file	
	
	
	private static double sliderBEVStock2020Value = 1.2;  //
	private static double sliderBEVStock2025Value = 6.1;  //
	private static double sliderBEVStock2030Value = 14.6;  //
	private static double sliderBEVStock2035Value = 26.1;  //
	private static double sliderBEVStock2040Value = 34.3;  //
	
	private static File BEVStockFile = new File ("REVISE/application/data/EVpenetration.txt");	
	private static String fileBEVStockStateValue = BEVStockFile.getAbsolutePath();  //
	private static double txtBEVFuelConsumptionValue = 320;   //
	private static double txtRentalCarFuelEconomyValue = 22.4; //
	private static double txtBEVRangeValue = 200;
	
	//traveler page
	private static boolean radioHomeChargingNationalSelected = true;
	private static double oldsliderNationalHomeChargingAvailabilityValue = 64.1;      //not change, unless reimport file
	private static double sliderNationalHomeChargingAvailabilityValue = 64.1;  //
	
	private static File stateHomeChargingFile = new File ("REVISE/application/data/charging_availability.txt");
	private static String fileStateHomeChargingAvailabilityValue = stateHomeChargingFile.getAbsolutePath(); //
	
	private static double sliderInitialSOCNoHomeChargerValue = 50;  //
	
	private static double txtVehicleOccupancyValue = 1.7; //
	private static double txtTimeValueValue = 25;   //
	private static double txtMaximumDailyRangeValue = 400;	//
	private static double txtRentalCarCostValue = 50;  //
	private static double sliderSOCWhenChargeValue = 35;  //
	
	
	//Infrastructure page
	private static double txtStationCostValue = 0;
	private static double txtPlugCostValue = 50000;        //
	private static double txtChargingPowerValue = 150;     //
	private static double txtPlugLifespanValue = 10;       //
	private static double txtDiscountRateValue = 7;        //
	private static double txtChargingCostValue = 0.59;     //
	private static double txtGasolinePriceValue = 2.42;    //
	private static double txtWaitingTimeValue = 10;    //
	private static double txtProbabilityThresholdValue = 95;  //
	
	//Objective page
	private static double txtWeightObj1Value = 1;   //
	private static double txtWeightObj2Value = 1;   //
	
	//network page
	private static File ODpairFile = new File ("REVISE/application/data/odpair.txt");
	private static String fileODpairValue = ODpairFile.getAbsolutePath();
	private static File distanceFile = new File ("REVISE/application/data/distance.txt");
	private static String fileDistanceValue = distanceFile.getAbsolutePath();
	private static File pathNodesFile = new File ("REVISE/application/data/ODtrip.txt");
	private static String filePathNodesValue = pathNodesFile.getAbsolutePath();
	
	//setup
	private static double txtTimeLimitValue = 10800;   //done
	private static int processors = Runtime.getRuntime().availableProcessors();
	private static int processorsValue = Runtime.getRuntime().availableProcessors();
	private static boolean toggleTournamentSelected = false;   //done
	private static boolean toggleInfrastructureOutputSelected = true;
	private static boolean toggleSummaryOutputSelected = true;
	
	private static long txtRandomSeedValue = System.currentTimeMillis();
	private static int txtNumGenesValue = 100;
	private static boolean checkRandomSeedChooseValue = true;
	
	//main page
	public static void setTxtScenarioNameMainValue (String value) {
		txtScenarioNameMainValue = value;
	}
	
	public static String getTxtScenarioNameMainValue () {
		return txtScenarioNameMainValue;
	}
	
	//vehicle page
	public static void setradioBEVStockNationalSelected (boolean value) {
		radioBEVStockNationalSelected = value;
	}
	public static boolean getradioBEVStockNationalSelected () {
		return radioBEVStockNationalSelected;
	}
	public static void setoldsliderBEVStock2020Value(double value) {
		oldsliderBEVStock2020Value = value;
		sliderBEVStock2020Value = value;
	}
	public static void setsliderBEVStock2020Value(double value) {
		sliderBEVStock2020Value = value;
	}
	public static double getsliderBEVStock2020Value() {
		return sliderBEVStock2020Value;
	}
	public static double getoldsliderBEVStock2020Value() {
		return oldsliderBEVStock2020Value;
	}
	public static void setsliderBEVStock2025Value(double value) {
		sliderBEVStock2025Value = value;
	}
	public static void setoldsliderBEVStock2025Value(double value) {
		oldsliderBEVStock2025Value = value;
		sliderBEVStock2020Value= value;
	}
	public static double getsliderBEVStock2025Value() {
		return sliderBEVStock2025Value;
	}
	public static double getoldsliderBEVStock2025Value() {
		return oldsliderBEVStock2025Value;
	}
	public static void setsliderBEVStock2030Value(double value) {
		sliderBEVStock2030Value = value;
	}
	public static void setoldsliderBEVStock2030Value(double value) {
		oldsliderBEVStock2030Value = value;
		sliderBEVStock2030Value = value;
	}
	public static double getsliderBEVStock2030Value() {
		return sliderBEVStock2030Value;
	}
	public static double getoldsliderBEVStock2030Value() {
		return oldsliderBEVStock2030Value;
	}
	public static void setsliderBEVStock2035Value(double value) {
		sliderBEVStock2035Value = value;
	}
	public static void setoldsliderBEVStock2035Value(double value) {
		oldsliderBEVStock2035Value = value;
		sliderBEVStock2035Value = value;
	}
	public static double getsliderBEVStock2035Value() {
		return sliderBEVStock2035Value;
	}
	public static double getoldsliderBEVStock2035Value() {
		return oldsliderBEVStock2035Value;
	}
	public static void setsliderBEVStock2040Value(double value) {
		sliderBEVStock2040Value = value;
	}
	public static void setoldsliderBEVStock2040Value(double value) {
		oldsliderBEVStock2040Value = value;
		sliderBEVStock2040Value = value;
	}
	public static double getsliderBEVStock2040Value() {
		return sliderBEVStock2040Value;
	}
	public static double getoldsliderBEVStock2040Value() {
		return oldsliderBEVStock2040Value;
	}
	public static void setfileBEVStockStateValue(String value) {
		fileBEVStockStateValue = value;
	}
	public static String getfileBEVStockStateValue() {
		return fileBEVStockStateValue;
	}

	public static void settxtBEVFuelConsumptionValue(double value) {
		txtBEVFuelConsumptionValue = value;
	}
	public static double gettxtBEVFuelConsumptionValue() {
		return txtBEVFuelConsumptionValue;
	}
	
	public static void settxtRentalCarFuelEconomyValue(double value) {
		txtRentalCarFuelEconomyValue = value;
	}
	public static double gettxtRentalCarFuelEconomyValue() {
		return txtRentalCarFuelEconomyValue;
	}
	
	public static void settxtBEVRangeValue(double value) {
		txtBEVRangeValue = value;
	}
	public static double gettxtBEVRangeValue() {
		return txtBEVRangeValue;
	}
	
	//Traveler page
	public static void setradioHomeChargingNationalSelected(boolean value) {
		radioHomeChargingNationalSelected = value;
	}
	public static void setoldsliderNationalHomeChargingAvailabilityValue(double value) {
		oldsliderNationalHomeChargingAvailabilityValue = value;
		sliderNationalHomeChargingAvailabilityValue = value;
	}
	public static void setsliderNationalHomeChargingAvailabilityValue(double value) {
		sliderNationalHomeChargingAvailabilityValue = value;
	}
	public static void setfileStateHomeChargingAvailabilityValue (String value) {
		fileStateHomeChargingAvailabilityValue = value;
	}
	public static void setsliderInitialSOCNoHomeChargerValue (double value) {
		sliderInitialSOCNoHomeChargerValue = value;
	}
	public static void settxtVehicleOccupancyValue (double value) {
		txtVehicleOccupancyValue = value;
	}
	public static void settxtTimeValueValue (double value) {
		txtTimeValueValue = value;
	}
	public static void settxtMaximumDailyRangeValue (double value) {
		txtMaximumDailyRangeValue = value;
	}
	public static void settxtRentalCarCostValue (double value) {
		txtRentalCarCostValue = value;
	}
	public static void setsliderSOCWhenChargeValue (double value) {
		sliderSOCWhenChargeValue = value;
	}
	

	public static boolean getradioHomeChargingNationalSelected() {
		return radioHomeChargingNationalSelected;
	}
	public static double getoldsliderNationalHomeChargingAvailabilityValue() {
		return oldsliderNationalHomeChargingAvailabilityValue;
	}
	public static double getsliderNationalHomeChargingAvailabilityValue() {
		return sliderNationalHomeChargingAvailabilityValue;
	}
	public static String getfileStateHomeChargingAvailabilityValue() {
		return fileStateHomeChargingAvailabilityValue;
	}
	public static double getsliderInitialSOCNoHomeChargerValue() {
		return sliderInitialSOCNoHomeChargerValue;
	}
	public static double gettxtVehicleOccupancyValue() {
		return txtVehicleOccupancyValue;
	}
	public static double gettxtTimeValueValue() {
		return txtTimeValueValue;
	}
	public static double gettxtMaximumDailyRangeValue() {
		return txtMaximumDailyRangeValue;
	}
	public static double gettxtRentalCarCostValue() {
		return txtRentalCarCostValue;
	}
	public static double getsliderSOCWhenChargeValue() {
		return sliderSOCWhenChargeValue;
	}
	
	
	//Infrastructure page
	
	public static void settxtStationCostValue (double value) {
		txtStationCostValue = value;
	}
	public static void settxtPlugCostValue (double value) {
		txtPlugCostValue = value;
	}
	public static void settxtChargingPowerValue (double value) {
		txtChargingPowerValue = value;
	}
	public static void settxtPlugLifespanValue (double value) {
		txtPlugLifespanValue = value;
	}
	public static void settxtDiscountRateValue (double value) {
		txtDiscountRateValue = value;
	}
	public static void settxtChargingCostValue (double value) {
		txtChargingCostValue = value;
	}
	public static void settxtGasolinePriceValue (double value) {
		txtGasolinePriceValue = value;
	}
	public static void settxtWaitingTimeValue (double value) {
		txtWaitingTimeValue = value;
	}
	public static void settxtProbabilityThresholdValue (double value) {
		txtProbabilityThresholdValue = value;
	}
	
	
	public static double gettxtStationCostValue() {
		return txtStationCostValue;
	}
	public static double gettxtPlugCostValue() {
		return txtPlugCostValue;
	}
	public static double gettxtChargingPowerValue() {
		return txtChargingPowerValue;
	}
	public static double gettxtPlugLifespanValue() {
		return txtPlugLifespanValue;
	}
	public static double gettxtDiscountRateValue() {
		return txtDiscountRateValue;
	}
	public static double gettxtChargingCostValue() {
		return txtChargingCostValue;
	}
	public static double gettxtGasolinePriceValue() {
		return txtGasolinePriceValue;
	}
	public static double gettxtWaitingTimeValue() {
		return txtWaitingTimeValue;
	}
	public static double gettxtProbabilityThresholdValue() {
		return txtProbabilityThresholdValue;
	}
	
	
	//objective page
	public static void settxtWeightObj1Value (double value) {
		txtWeightObj1Value = value;
	}
	public static void settxtWeightObj2Value (double value) {
		txtWeightObj2Value = value;
	}
	
	public static double gettxtWeightObj1Value() {
		return txtWeightObj1Value;
	}
	public static double gettxtWeightObj2Value() {
		return txtWeightObj2Value;
	}
	
	//network page
	public static String getfileODpairValue() {
		return fileODpairValue;
	}
	
	public static String getfileDistanceValue() {
		return fileDistanceValue;
	}
	
	public static String getfilePathNodesValue() {
		return filePathNodesValue;
	}
	
	//Setup
	public static void settxtTimeLimitValue (double value) {
		txtTimeLimitValue = value;
	}
	public static void setprocessorsValue(int value) {
		processorsValue = value;
	}
	public static void settxtRandomSeedValue(long value) {
		txtRandomSeedValue =value;
	}
	public static void settxtNumGenesValue(int value) {
		txtNumGenesValue = value;
	}
	public static void setcheckRandomSeedChooseValue(boolean value) {
		checkRandomSeedChooseValue = value;
	}
	public static void settoggleTournamentSelected (boolean value) {
		toggleTournamentSelected = value;
	}
	public static void settoggleInfrastructureOutputSelected (boolean value) {
		toggleInfrastructureOutputSelected = value;
	}
	public static void settoggleSummaryOutputSelected (boolean value) {
		toggleSummaryOutputSelected = value;
	}
	
	
	public static double gettxtTimeLimitValue() {
		return txtTimeLimitValue;
	}
	public static int getprocessors() {
		return processors;
	}
	public static int getprocessorsValue() {
		return processorsValue;
	}
	public static long gettxtRandomSeedValue() {
		return txtRandomSeedValue;
	}
	public static int gettxtNumGenesValue() {
		return txtNumGenesValue;
	}
	public static boolean getcheckRandomSeedChooseValue() {
		return checkRandomSeedChooseValue;
	}	
	public static boolean gettoggleTournamentSelected() {
		return toggleTournamentSelected;
	}
	public static boolean gettoggleInfrastructureOutputSelected() {
		return toggleInfrastructureOutputSelected;
	}
	public static boolean gettoggleSummaryOutputSelected() {
		return toggleSummaryOutputSelected;
	}
	
	public static double calculateStateWeightedAverage(double [] stateValue) {		
		double sum = 0;
		for (int i=0; i< 51; i++) {
			sum+=stateValue[i]*stateWeight[i]*100;
		}		
		return sum;
	}
	
}
