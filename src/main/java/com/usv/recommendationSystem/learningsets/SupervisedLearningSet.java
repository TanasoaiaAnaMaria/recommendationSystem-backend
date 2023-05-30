package com.usv.recommendationSystem.learningsets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.usv.recommendationSystem.utils.DataUtils;
import com.usv.recommendationSystem.utils.StringInputAdapter;
public class SupervisedLearningSet  extends UnsupervisedLearningSet {
	
    // X[][], f[], n, p are defined in super class
    private int[] iClass;         // the label (index) class of each pattern  
    private String[] classNames;  // an array of M+1 elements containing the names of the classes
                                  // because iClass[i] is in [1, M] (iClass[i]=0 means unknown class) 
                                  // classNames[0]="" (reserved, not used),
                                  // classNames[1]= the name of the first class,
                                  // classNames[2]= the name of the second class, ...
                                  // classNames[M] = the name of the M-th class
    
    public SupervisedLearningSet(double[][] X, int[] iClass) {
        fillSupervisedFieldsValues(X, iClass, null);
    }

    public SupervisedLearningSet(double[][] X, int[] iClass, String[] classNames) {
        fillSupervisedFieldsValues(X, iClass, classNames);
    }

    // This constructor is designed for cases where the user
    // has used numbers to indicate class labels and the last 
    // column of X (the matrix returned by the UnsupervisedLearningSet constructor)
    // contains the class labels for each pattern
    // Optionally may be indicated the classes names or null
    //
     public SupervisedLearningSet(String dataFileName, String[] classNames) {
        super(dataFileName);
        // extract the numerical class labels from X
        double[][] newX = new double[n][];
        iClass = new int[n];
        for(int i=0; i<n; i++){
            newX[i] = Arrays.copyOf(X[i], p-1);  //without class label
            iClass[i] = (int) X[i][p-1];
        }
        fillSupervisedFieldsValues(newX, iClass, classNames);
    }

    public SupervisedLearningSet(String dataFileName) {
		StringInputAdapter stringInputAdapter = new StringInputAdapter(dataFileName);
    	fillSupervisedFieldsValues(stringInputAdapter.getX(),
				stringInputAdapter.getiClass(), stringInputAdapter.getClassNames());
    }
    
    public double[] calculateWeightsValues(){
    	double[] f = new double[X.length];
        Arrays.fill(f,1.);  // TODO: replace it by counting identical patterns
        					// check if 2 identical patterns has same class (must be!)
                            // if a reduced set is produce update the value of n
        return f;
    }
    
    public void printMeansAndStandardDeviations() {
    	DataUtils.printMeansAndStandardDeviations(X, f);
    }
    
    private void fillSupervisedFieldsValues(double[][] X, int[] iClass, String[] classNames) {
    	this.X = X;
    	n = X==null?0:X.length;
    	p = X==null || X[0]==null ? 0 : X[0].length;  
    	// TODO check if all the patterns have the same number of features
    	//            if not throw an Exception
        //       Use the method you will write in super class.
        if (iClass!= null) {
            this.iClass = iClass;
            this.classNames = classNames!=null ? classNames : obtainClassNames(iClass);  
            M = this.classNames.length - 1;  // because classNames[0]="" (reserved, not used)
        } else {
            iClass = new int[n];
            Arrays.fill(iClass, 0);
            M = 0;    // no classes defined; in fact is not a supervised set
        }
        this.f = calculateWeightsValues();
    }
    
    public static String[] obtainClassNames(int[] iClass) {
    	List<String> lstClassNames = Arrays.stream(iClass)
			        .distinct()
			        .mapToObj(String::valueOf)   // conversion from int number to String
			        .collect(Collectors.toList());
    	lstClassNames.add("");   // added for obtaining directly the class names directly as
    	                         //     "classNames [ iClass[i] ]", where iClass[i] is in [1, M]
    	                         // (not with "iClass[i]-1")
    	String[] classNames = new String[lstClassNames.size()];   //M+1 because is already added a ""
    	lstClassNames.toArray(classNames);
    	Arrays.sort(classNames);
    	return classNames;
    }


    public int[] getIClass() {
        return iClass;
    }

    public String[] getClassNames() {
        return classNames;
    }
       
    public void doSameClassIndexAs(SupervisedLearningSet trainingSet) {
    	if(this.M > trainingSet.M)
    		throw new RuntimeException("Test set has a greater number of classes than training set ("+M +">"+ trainingSet.M);
    	List<String> classNamesTrainingSetList = Arrays.asList(trainingSet.classNames);
    	int index;
    	for(int i=0; i<n; i++) {
    		if( iClass[i]==0)
    			continue;
    		index = classNamesTrainingSetList.indexOf(classNames[iClass[i]]);
    		if( index <0 )
    			throw new RuntimeException("In the test set there is a class \""+ 
    					classNames[iClass[i]] + "\" that is not in the training set");
    	       iClass[i] = index;
    	}
    	this.classNames= Arrays.copyOf(trainingSet.classNames, M+1);
    }
    
    @Override
    public String toString() {
       	String[] linii = 	super.toString().split("\n");
    	StringBuilder sBuilder = new StringBuilder(linii[0]).append("\n");
    	for(int i=0;i<n;i++)
    		sBuilder.append(linii[i+2])
    		        .append(String.format("   <%s>  - %d", classNames[iClass[i]], iClass[i])).append("\n");
        return sBuilder.toString();  
    }
   
 
}
