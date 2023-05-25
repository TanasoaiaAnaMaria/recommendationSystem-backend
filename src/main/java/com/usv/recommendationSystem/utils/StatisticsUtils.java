package com.usv.recommendationSystem.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatisticsUtils {
	
	public static double calculateFeatureAverage(double[] feature)
	{
		double average = 0.0;
		for (int i=0; i<feature.length; i++)
		{
			average += feature[i];
		}
		average = average/feature.length;
		return average;
	}
	

	
	public static Map<Pattern, Double> getPatternsMapFromInitialSet(double[][] patternSet) {
		Map<Pattern, Double> patternsMap = new LinkedHashMap<Pattern, Double>();
		
		for (int i=0;i<patternSet.length; i++)
		{
			Pattern patternObject = new Pattern( patternSet[i]);
			if(patternsMap.containsKey(patternObject))
			{
				double currentWeight = patternsMap.get(patternObject);
				double newWeight = currentWeight +1;
				patternsMap.put(patternObject, newWeight);
			}
			else
			{
				patternsMap.put(patternObject, 1.);
			}
		}
		return patternsMap;
	}


	public static double[] calculateWeightedAverages(Map<Pattern, Double> patternsMap, int numberOfFeatures) {
		double[]  weightedAverages = new double[numberOfFeatures];
		int totalWeight = 0;
		for(Map.Entry<Pattern, Double> entry : patternsMap.entrySet())
		{
			Pattern patternObject = entry.getKey();
			double weight = entry.getValue();
			totalWeight += weight;
			double[] patternValues = patternObject.getPatternValues();
			for (int j=0; j<numberOfFeatures; j++)
			{
				weightedAverages[j] += patternValues[j] * weight;
			}
		}
		for (int j=0; j<numberOfFeatures; j++)
		{
			weightedAverages[j] /= totalWeight;
			System.out.println(weightedAverages[j]);
		}

		return weightedAverages;
	}
	
	public static double[] getPatternWeigths(Map<Pattern, Double> patternsMap) {
		return patternsMap.values().stream()
				.mapToDouble(x -> x.doubleValue() )
				.toArray();		
	}

	public static double[][] getPatterns(Map<Pattern, Double> patternsMap) {
		return patternsMap.keySet().stream()
				.map(Pattern::getPatternValues )
				.toArray(double[][]::new);		
	}

	public static double[] calculateWeightedAverages(double[][] X, double[] f) {
		// avgj = Sigma(xj*fi) / Sigma(fi)
		int n = X.length;
		int p = X[0].length;
		int j;
		double[] avgj = new double [p];
		for(j=0; j<p; j++) {
			avgj[j] = 0.; 
		}
		double sigmafi =0;
		for(int i=0; i<n; i++)  {
			for(j=0; j<p; j++) {
				avgj[j] += X[i][j]*f[i];
		    }
			sigmafi += f[i];
		}
		for(j=0; j<p; j++) {
			avgj[j] /= sigmafi; 
		}
		return avgj;
	}

	public static double[] calculateFeaturesStandardDeviations(double[][] X, double[] f, double[] avgj) {
		// stdj = Sigma[fi(xj - avgj)^2] / Sigma(fi)
		//
		int n = X.length;
		int p = X[0].length;
		int j;
		double[] stdj = new double [p];
		double sigmafi = 0;
		for(j=0; j<p; j++) {
			stdj[j] = 0.; 
		}
		for(int i=0; i<n; i++)  {
			for(j=0; j<p; j++) {
				stdj[j]  += f[i]*(X[i][j] - avgj[j]) * (X[i][j] - avgj[j]);
		    }
			sigmafi += f[i];
		}
		for(j=0; j<p; j++) {
			stdj[j] = Math.sqrt(stdj[j]/(sigmafi-1)); 
		}
		return  stdj;
		
	}

	
	public static double[][] calculateMinMax(double[][] x) {
		double[][] minMax = new double[2][x[0].length];
		double[] min = minMax[0];
		double[] max = minMax[1];
		for (int j = 0; j < x[0].length; j++) {
			max[j] = x[0][j];
			min[j] = x[0][j];
			for (int i = 0; i < x.length; i++) {
				max[j] = x[i][j] > max[j] ? x[i][j] : max[j];
				min[j] = x[i][j] < min[j] ? x[i][j] : min[j];
			
			}
		}
		return minMax;
	}

	
	public static double[] calculateFeaturesStandardDeviations(double[][] X, double[] f) {
		// stdj = Sigma[fi(xj - avgj)^2] / Sigma(fi)
		double avgj[] = calculateWeightedAverages(X, f);
		return calculateFeaturesStandardDeviations(X, f, avgj);
	}
	
	public double[] calculateFeaturesAverages(double[][] X) {
		return calculateWeightedAverages(getPatternsMapFromInitialSet(X), X[0].length);
	}
	

}
