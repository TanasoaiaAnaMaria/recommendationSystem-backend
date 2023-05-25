package com.usv.recommendationSystem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.usv.recommendationSystem.learningsets.SupervisedLearningSet;

public class SetUtils {
	public static SupervisedLearningSet[] splitSet(SupervisedLearningSet supervisedLearningSet, int percentageForTestSet )
	{
		double[][] learningSet = supervisedLearningSet.getX();
		int numberOfPatterns = supervisedLearningSet.getN();
		int numberOFFeatures =supervisedLearningSet.getP();
		int[] iClass = supervisedLearningSet.getIClass();		
		String[] classNames = supervisedLearningSet.getClassNames()	;	
		Map<Integer, Integer> classesMap = new HashMap<Integer, Integer>();
				
		//create map with distinct classes and number of occurence for each class
		for (int i=0; i<numberOfPatterns; i++)
		{
			int clazz = iClass[i];
			if (classesMap.containsKey(clazz))
			{
				Integer nrOfClassPatterns = classesMap.get(clazz);
				classesMap.put(clazz, nrOfClassPatterns + 1);
			}
			else
			{
				classesMap.put(clazz, 1);
			}
		}
		Random random = new Random();
		//map that keeps for each class the random patterns selected for test set
		Map<Integer, List<Integer>> classesTestPatterns = new HashMap<Integer, List<Integer>>();
		Integer testSetSize = 0;
		for (Map.Entry<Integer, Integer> entry: classesMap.entrySet())
		{
			Integer currentIClass = entry.getKey();
			Integer classMembers = entry.getValue();
			Integer testPatternsNr = Math.round(classMembers * percentageForTestSet/100);
			testSetSize += testPatternsNr;
			List<Integer> selectedPatternsForTest = new ArrayList<Integer>();
			for (int i=0; i<testPatternsNr; i++)
			{
				Integer patternNr = random.nextInt(classMembers ) +1;
				while (selectedPatternsForTest.contains(patternNr))
				{
					patternNr = random.nextInt(classMembers ) +1;
				}
				selectedPatternsForTest.add(patternNr);
			}
			classesTestPatterns.put(currentIClass, selectedPatternsForTest);			
		}
				
		double[][] testSetPatterns = new double[testSetSize][numberOFFeatures];
		double[][] trainingSetPatterns = new double[numberOfPatterns-testSetSize][numberOFFeatures];
		int[] testSetIclass = new int[testSetSize];
		int[] trainingSetIclass = new int[numberOfPatterns-testSetSize];
		int testSetIndex = 0;
		int trainingSetIndex = 0;
		Map<Integer, Integer> classCurrentIndex = new HashMap<Integer, Integer>();
		

		for (int i=0; i<numberOfPatterns; i++)
		{
			int currentIClass = iClass[i];
			if (classCurrentIndex.containsKey(currentIClass))
			{
				int currentIndex = classCurrentIndex.get(currentIClass);
				classCurrentIndex.put(currentIClass, currentIndex+1);
			}
			else
			{
				classCurrentIndex.put(currentIClass, 1);
			}
			if (classesTestPatterns.get(currentIClass).contains(classCurrentIndex.get(currentIClass)))
			{
				testSetPatterns[testSetIndex] = learningSet[i];
				testSetIclass[testSetIndex] = currentIClass;
				testSetIndex++;
			}
			else
			{
				trainingSetPatterns[trainingSetIndex] = learningSet[i];
				trainingSetIclass[trainingSetIndex] = currentIClass;
				trainingSetIndex++;
			}
		}
		
		SupervisedLearningSet[] sets = new SupervisedLearningSet[2];
		sets[0] = new SupervisedLearningSet(trainingSetPatterns, trainingSetIclass, classNames);
		sets[1] = new SupervisedLearningSet(testSetPatterns, testSetIclass, classNames);
		
		return sets;
		//FileUtils1.writeLearningSetToFile("test.txt", testSet," ");
		//FileUtils1.writeLearningSetToFile("train.txt", trainingSet," ");

	}

}
