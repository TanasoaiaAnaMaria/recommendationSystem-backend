package com.usv.recommendationSystem.utils;

import java.util.function.BiFunction;

/*
 * Autor:PSG, feb. 2021    
 */
public class DistanceUtils {
	
	public static double distEuclid ( double x[], double y[] )  {
		if(x.length != y.length) throw new DifferentSpaceSizeException("("+x.length+", "+y.length+")");
		
		double d = 0;
		for(int j=0; j< x.length; j++)
			d += (x[j]-y[j])* (x[j]-y[j]);
	    return Math.sqrt(d);
	}

	public static double distCityBlock ( double x[], double y[] )  {
		if(x.length != y.length) throw new DifferentSpaceSizeException("("+x.length+", "+y.length+")");
		
		double d = 0;
		for(int j=0; j< x.length; j++)
			d += Math.abs(x[j]-y[j]);
	    return d;
	}
	public static double distCanberra ( double x[], double y[] )  {
		if(x.length != y.length) throw new DifferentSpaceSizeException("("+x.length+", "+y.length+")");
		
		double d = 0;
		for(int j=0; j< x.length; j++) {
			double numitor = Math.abs(x[j])+Math.abs(y[j]);
			if (numitor > 0 )
				d += Math.abs(x[j]-y[j])/numitor;
		}
	    return d;
	}

	public static double distCebisev ( double x[], double y[] )  {
		if(x.length != y.length) throw new DifferentSpaceSizeException("("+x.length+", "+y.length+")");
		
		double dmax = 0;
		for(int j=0; j< x.length; j++)
			dmax = Math.max(dmax, Math.abs(x[j]-y[j]));
 	    return dmax;
	}
	public static void main(String[] args) {
	    double [][] matX = new double [][] {{1, 5},{5, 2},{3, 5}, {3, 3.5}};
	    testDistance(matX);
		testDistance (matX, DistanceUtils::distCityBlock, "City Block");
		testDistance (matX, DistanceUtils::distCebisev, "Cebisev");
		testDistance (matX, DistanceUtils::distCanberra, "Canberra");		
		double [] v = new double[] {0};
		System.out.println("d(x,v) ="+distEuclid(matX[0],v));
		
		}

	private static void testDistance (double [][] matX) {
		testDistance(matX, DistanceUtils::distEuclid, "Euclidiana");
	}
		private static void testDistance (double [][] matX, BiFunction<double[], double[], Double> d,
				                          String text) {
			System.out.println("\nDistanta "+text);
			double[] x=matX[0], y=matX[1], z=matX[2], z1 = matX[3];
			System.out.println("d(x,x) ="+d.apply(x,x));
			System.out.println("d(x,y) ="+d.apply(x,y));
			System.out.println("d(y,x) ="+d.apply(y,x));
			System.out.println("d(x,z) + d(z,y)=" + (d.apply(x,z) + d.apply(z,y)) );
			System.out.println("z1 is colinear with x and y");
			System.out.println("d(x,z1) + d(z1,y)=" + (d.apply(x,z1) + d.apply(z1,y)) );
		}
}



class DifferentSpaceSizeException extends RuntimeException{

	public DifferentSpaceSizeException(String message) {
		super(message);
	}
}
