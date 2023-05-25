/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usv.recommendationSystem.utils;

import java.awt.print.Printable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DataUtils {
    
	/**
	 * method to print double[][] array
	 * 
	 * @param x - the pattern matrix
	 */
    public static void printMatrix(double[][] x){
    //System.out.println("numRows="+x.length);
        for(double[] lin: x){
            for(double xcrt: lin)
                System.out.print(String.format("%10.2f  \t",xcrt));
            System.out.println("numCols="+lin.length);
        }
    }
    
    /**
     * 
     * method to print double[][] and double[] arrays (patterns and weights)
     * 
     * @param X - the pattern matrix
     * @param f - the weights array
     */
    public static void printPatternsAndWeigthsSet(double[][] X, double[] f){
	IntStream.range(0, X.length)
	.forEach(i-> System.out.println( Arrays.toString(X[i]) + " \t["+ f[i] + "]"));					
    }
    
    /**
     * fetches and prints the Means and Standard Deviations for pattern matrix and weights array 
     * @param X - the pattern matrix
     * @param f - the weights array
     */
    public static void printMeansAndStandardDeviations(double[][] X, double[] f) {
		System.out.println("Features means and standard deviations");
		double[] avg = StatisticsUtils.calculateWeightedAverages(X, f);
		double[] std = StatisticsUtils.calculateFeaturesStandardDeviations(X, f);
		for (int j=0; j<X[0].length; j++)
			System.out.println(String.format("%d %10.6f   %10.6f", j+1, avg[j], std[j])); 
    }
    
    /**
     * normalizes the pattern set (matrix)

     * @return
     */
	public static double[][] normalizeSet(double[][] x) {
		double[][] normalizedLearningSet = new double[x.length][x[0].length];
		double[] max = new double[x[0].length];
		double[] min = new double[x[0].length];
		for (int j = 0; j < x[0].length; j++) {
			max[j] = x[0][j];
			min[j] = x[0][j];
			for (int i = 0; i < x.length; i++) {
				max[j] = x[i][j] > max[j] ? x[i][j] : max[j];
				min[j] = x[i][j] < min[j] ? x[i][j] : min[j];
			
			}
		}
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				normalizedLearningSet[i][j] = (x[i][j] - min[j]) / (max[j] - min[j]);
			}

		}
		return normalizedLearningSet;
	}
    
    /**
     * autoscales the pattern set (matrix)
     * @param X - the pattern matrix
     * @return
     */
	public static double[][] autoScaleSet (double X[][], double f[]){
		double[] avg = StatisticsUtils.calculateWeightedAverages(X, f);
		double[] std = StatisticsUtils.calculateFeaturesStandardDeviations(X, f, avg);
		int n = X.length;
		int p = X[0].length;
		double[][] xAutoScaled = new double [n][p];
		for(int i=0; i<n; i++)  
			for(int j=0; j<p; j++) 
				xAutoScaled[i][j] = (X[i][j] - avg[j]) / std[j];
		return xAutoScaled;
	}
}
