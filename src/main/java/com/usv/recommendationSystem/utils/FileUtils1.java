package com.usv.recommendationSystem.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils1 
{
	private static String inputFileValuesSeparator = "\\s+"; // pentru a permite utilizarea a 2 separatori
	private static final String outputFileValuesSeparator = ",";
	
	public static void setinputFileValuesSeparator(String separator) {
		inputFileValuesSeparator = separator;
	}

	public static void setinputFileValuesSeparator() {
		inputFileValuesSeparator = "\\s+";
	}

	protected static String[][] readStringMatrixFromFileStream(String fileName) {
	     try (Stream<String> stream = Files.lines(Paths.get(fileName)))
	    {
	            return stream.map(Line -> Line.split(inputFileValuesSeparator))
	                                  .toArray(String[][]::new);
	    } catch (IOException e) {
	             e.printStackTrace();
	             return null;
	    }
	}


//	protected static String[][] readStringMatrixFromFileStream(String fileName) {
//		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
//			return stream.map(line -> {
//				String[] values = line.split(inputFileValuesSeparator);
//				for (int i = 0; i < values.length; i++) {
//					try {
//						double numericValue = Double.parseDouble(values[i]);
//						values[i] = String.valueOf(numericValue);
//					} catch (NumberFormatException e) {
//						// Ignore non-numeric values
//					}
//				}
//				return values;
//			}).toArray(String[][]::new);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	
   public static double[][] readMatrixFromFileStream(String fileName)
	{
		// read file into stream, try-with-resources
                double[][] matrice=null;
		try (Stream<String> streamLiniiFisier = Files.lines(Paths.get(fileName))) {
                    matrice = streamLiniiFisier
                    		         .map(linie -> 
                                                  Arrays.stream(linie.split(inputFileValuesSeparator))
                                                        .filter(x -> !x.isEmpty())
                                                        .mapToDouble(Double::parseDouble)  
                                                        .toArray()
                                         )
                                     .filter(linMatrice -> linMatrice.length!=0)
                                     .toArray(double[][]::new);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matrice;
	}
	
	public static double[][] convertToBiDimensionalArray(List<ArrayList<Double>> learningSetList ) {
            return learningSetList.stream()
                        .map((ArrayList<Double> lin) -> ((ArrayList<Double>)lin).stream()
                                .mapToDouble(Double::doubleValue)
                                .toArray()
                             )
                        .toArray(double[][]::new);
	}
	
	
	public static void writeLearningSetToFile(String fileName, double[][] patternMatrix, String fieldSeparator)
	{
            // first create the output to be written
            StringBuilder stringBuilder = new StringBuilder();
		for(double [] lineSet: patternMatrix) //for each row
		{
			//for each column
			 for(double val: lineSet) 
			 {
				//append to the output string
				 stringBuilder.append(val).append(fieldSeparator);
			 }
            //replace separatorCampuri at the end of the row by \n
            stringBuilder.replace(stringBuilder.lastIndexOf(fieldSeparator),
                                              stringBuilder.length(), "\n");
		}		
        try {
                  Files.write(Paths.get(fileName), stringBuilder.toString().getBytes());
        } catch (IOException e) {
			e.printStackTrace();
        }

	}

	public static void writeLearningSetToFile(String fileName, String[][] matrix, String fieldSeparator)
	{
            // first create the output to be written
            StringBuilder stringBuilder = new StringBuilder();
		for(String [] lineSet: matrix) //for each row
		{
			//for each column
			 for(String val: lineSet) 
			 {
				//append to the output string
				 stringBuilder.append(val).append(fieldSeparator);
			 }
            //replace separatorCampuri at the end of the row by \n
            stringBuilder.replace(stringBuilder.lastIndexOf(fieldSeparator),
                                              stringBuilder.length(), "\n");
		}		
        try {
                  Files.write(Paths.get(fileName), stringBuilder.toString().getBytes());
        } catch (IOException e) {
			e.printStackTrace();
        }

	}

}

