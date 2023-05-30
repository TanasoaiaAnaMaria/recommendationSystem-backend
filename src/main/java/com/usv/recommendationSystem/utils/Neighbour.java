package com.usv.recommendationSystem.utils;

public class Neighbour implements Comparable<Neighbour>{
	double distance;
	int iClass;
	
	/**
	 * 
	 * @param distance
	 * @param iClass
	 */
	public Neighbour(double distance, int iClass) {
		super();
		this.distance = distance;
		this.iClass = iClass;

	}
			
	public double getDistance() {
		return distance;
	}

	public int getIClass() {
		return iClass;
	}
	
	
	@Override
	public int compareTo(Neighbour o) {
		return (-1) * Double.compare(distance, o.getDistance());
	}
}
