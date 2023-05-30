package com.usv.recommendationSystem.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringInputAdapter {
	
	private double[][] X;
	private int[] iClass;
	private String classNames[];
	
	
	public StringInputAdapter(String fileName) {
		String[][] stringInputMatrix = FileUtils1.readStringMatrixFromFileStream(fileName);
		this.X = getPatternsSetAsDouble(stringInputMatrix);
		
		Map<String, Integer> classMap = getLabelsIClassMap(stringInputMatrix);
		
		//first element of class array will be "" because it coresponds to class 0 that should not exist
		this.classNames = new String[classMap.keySet().size()+1];
		this.classNames[0] = "";
		this.iClass = new int[stringInputMatrix.length];
		for (int i=0; i<stringInputMatrix.length; i++)
		{
			this.iClass[i] = classMap.get(stringInputMatrix[i][stringInputMatrix[0].length-1]);
			this.classNames[iClass[i]] =  stringInputMatrix[i][stringInputMatrix[0].length-1];
		}
	}
	
	
	public double[][] getX() {
		return X;
	}

	public int[] getiClass() {
		return iClass;
	}

	public String[] getClassNames() {
		return classNames;
	}

	public double[][] getPatternsSetAsDouble(String[][] stringInputMatrix)
	{
		return Arrays.stream(stringInputMatrix)
				.map(x -> Arrays.stream(x).limit(x.length - 1).mapToDouble(Double::parseDouble).toArray())
				.toArray(double[][]::new);
	}
	
	private Map<String,Integer> getLabelsIClassMap(String[][] stringInputMatrix)
	{
		 Map<String,Integer> classMap = new LinkedHashMap<String,Integer>();
		//since we look only in the last column, normal iteration is more efficient than streams
		int currentIclass = 0;
		int classNameColumnIndex = stringInputMatrix[0].length-1;
		for (int i=0; i<stringInputMatrix.length; i++)
		{
			String currentClassName = stringInputMatrix[i][classNameColumnIndex];
			if (!classMap.containsKey(currentClassName))
			{
				currentIclass++;
				classMap.put(currentClassName, currentIclass);
			}
		}
		return classMap;
	}

}
