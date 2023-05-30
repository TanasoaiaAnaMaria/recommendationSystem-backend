package com.usv.recommendationSystem.utils;

import java.util.Arrays;

/**
 * 
 * class for calculating and storing the triangle distance matrix
 *
 */
public class DistanceMatrixTriangle {
	private double mat[][];
	private int n;
	private double[][] x;
	IDistance d;
	
	/**
	 * constructor that uses by default the euclidian distance

	 */
	public DistanceMatrixTriangle(double[][] x) {
		this(x, DistanceUtils::distEuclid);
	}

	/**
	 * 
	 * @constructor that allows to specify what distance should be used for calculating the distance matrix
	 * @param d - interface for distance
	 */
	public DistanceMatrixTriangle(double[][] x, IDistance d){ 
		this.d = d;
		this.n = x.length;
		this.x = x;
		if (n<=10000) {
			mat = new double[n][n];
			calculateDistanceMatrix( d );
		} else {
			mat = null;
		}
	}	
	
	/**
	 * method to calculate the Distance matrix
	 * 
	 * @param d - interface for distance
	 */
	private void calculateDistanceMatrix(IDistance d) {
		mat = new double[n-1][];
		for(int i=1; i<n; i++) {
			mat[i-1] = new double[i];
			for(int j=0; j<i; j++)
				mat[i-1][j]=d.calculateDistance(x[i],x[j] );
		}
	}

	/**
	 * method to re-calculate the Distance matrix
	 * 
	 * @param d - interface for distance
	 */
	private void reCalculateDistanceMatrix(IDistance d) {
		if(n != x.length)
			throw new UnsupportedOperationException("The initial number of patterns ("+ n +
					") is no longer equal to the current number of lines of pattern matrix.");
		    // poate sa apara aceasta situatie ?
		for(int i=1; i<n; i++) {
			for(int j=0; j<i; j++)
				mat[i-1][j]=d.calculateDistance(x[i],x[j] );
		}
	}

	/**
	 * method to get distance between two patterns
	 * 
	 * @param i - frist pattern
	 * @param j - second pattern
	 * @return distance between i and j
	 */
	public double getDistance(int i, int j) {
		if(mat==null)
			return d.calculateDistance(x[i],x[j] );
		else
			return i==j?0 : 
				(i>j? mat[i-1][j] : mat[j-1][i]);
	}
	
	@Override
	public String toString() {
		return "DistanceMatrixTriangle [mat=" + Arrays.deepToString(mat) + "]";
	}
	
	/**
	 * method to display complete distance matrix
	 * 
	 * @return string representation of complete distance matrix
	 */
	public String toStringComplete() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++)
				sb.append(String.format("%5.2f  ", getDistance(i,j)));
			sb.append("\n");
		}
		return sb.toString();		
	}
	
	/**
	 * method to display triangle distance matrix
	 * 
	 * @return string representation of triangle distance matrix
	 */
	public String toStringTriangle() {
		StringBuilder sb = new StringBuilder();
		for(double[] linieCrt: mat) {
			for( double elemCrt: linieCrt)
				sb.append(String.format("%5.2f  ", elemCrt));
			sb.append("\n");
		}
		return sb.toString();

	}

	/**
	 * main method for testing purposes
	 * @param args
	 */
	public static void main(String[] args) {
		double [][] x = new double[][] {
			{1,5}, {5,2}, {3, 5}, {3, 3.5} };
			DistanceMatrixTriangle mdt = new DistanceMatrixTriangle(x);
			//System.out.println(mdt);
			System.out.println(mdt.toStringComplete());
/* --------------------------------------------------------------------------
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<x.length; i++) {
				for(int j=0; j<x.length; j++)
					sb.append(String.format("%5.2f  ", mdt.getDistance(i,j)));
				sb.append("\n");
			}
			System.out.println("\nMatricea complet\n" + sb);
			
			System.out.println("\n mat[1,0]=" + mdt.getMat()[1][0]);
			mdt.getMat()[1][0] = 17;
			System.out.println("\n mat[1,0]=" + mdt.getMat()[1][0]);
// --------------------------------------------------------------------------
 */
			mdt.reCalculateDistanceMatrix(DistanceUtils::distCityBlock);
			System.out.println("\nMatricea recalculata\n" + mdt.toStringComplete());
			
	}

	public double[][] getMat() {
		return mat;
	}

	

}
