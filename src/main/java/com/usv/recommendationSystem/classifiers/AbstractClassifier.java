package com.usv.recommendationSystem.classifiers;

import java.util.Arrays;

import com.usv.recommendationSystem.learningsets.SupervisedLearningSet;
import com.usv.recommendationSystem.learningsets.UnsupervisedLearningSet;

public abstract class AbstractClassifier {
    protected int n;   			// number of patterns
    protected int p;   			// number of features
    protected int M;   			// number of classes
    protected double[][] X;    	// patterns matrix  [n][p]
    protected double[] f;      	// patterns weights (f[i] = the weight of pattern i)
    protected int[] iClass;    	// pattern classes (iClass[i] = index of class of pattern i)
    protected String[] classNames;  // names of classes [M+1], because classNames[0]="")
    protected double[][] w;         // the model of classifier
    
    private int[] iClassCalculated;
    
    abstract public void training();  
    abstract public int predict(double[] z);
    
    public int[] predict(double[][] testSet) {
           return Arrays.stream(testSet)            //Stream<double []>   
    			        .mapToInt(z -> predict(z))  //IntStream  (livreaza int)
    			        .toArray();
    }
    
    private void train(double[][] X, double[] f, int[] iClass, String[] classNames) {
    	if(X==null) 
    		throw new RuntimeException("train(): NO data set provided");
    	this.X = X;
        n = X.length;    //number of patterns
        if(n==0)
        	throw new RuntimeException("train(): No patterns in data set (n=0)");
        p = X[0].length; //number of features (defined in Abstract Classifier)
        if(p==0)
        	throw new RuntimeException("train(): Patterns with no features in data set (p=0)");
    	if(f==null) {
	    	f = new double[n];
	        Arrays.fill(f,1.);
    	}
    	this.f = f;
        // set M, the number of classes (in AbstractClassifier)
		this.iClass = iClass;
        if(iClass!=null){
            M  = (int)Arrays.stream(iClass)
                            .distinct()
                            .count();
    		if(classNames==null) 
    			classNames = SupervisedLearningSet.obtainClassNames(iClass);
        } else {
			M=0;
		}
		this.classNames = classNames;
		training();   // to be overrided in sub-classes
    }
    
    public void train(SupervisedLearningSet supervisedSet) {
    	train(supervisedSet.getX(), supervisedSet.getF(), supervisedSet.getIClass(), 
    			                                          supervisedSet.getClassNames());
    }
    
    public void train(UnsupervisedLearningSet unsupervisedSet) {
        train(unsupervisedSet.getX(), unsupervisedSet.getF(), null, null);
    }
    
    // ---- for compatibility with other 
    // Pattern Recognition/ Machine Learning systems ----
    //
    // supervised context
    public void train(double[][] X, int[] iClass) {
    	train(X, null, iClass , null);
    }
    private void train(double[][] X, double[] f, int[] iClass) {
    	train(X, f, iClass, null);
    }
    // unsupervised context
    public void train(double[][] X) {
    	train(X, null, null, null);
    }
    private void train(double[][] X, double[] f) {
    	train(X, f, null, null);
    }

    public double[][] getClassifierModel() {  
        //return the coefficients of discriminant functions
        return w;
    }
    // ---------------------- Classifier Accuracy Evaluation ------------
    public double evaluateAccuracy(SupervisedLearningSet testSet, boolean print) {
    	return evaluateAccuracy(testSet.getX(), testSet.getIClass(), print);
    }
    
    public double evaluateAccuracy(boolean print) {
    	return evaluateAccuracy (X, iClass, print);
    }

    public double evaluateAccuracy(double X[][], int iClass[], boolean print) {
    	iClassCalculated = predict(X);
    	int nbCorrectClassified = 0;
    	if(print)
    		System.out.println("Classifier performance evaluation \nNr.\tCls.fis\tCls.calc");
    	for (int i=0; i< X.length; i++) {
    		boolean sameClassification = iClass[i] == iClassCalculated[i]; 
    		if(print) {
    			System.out.println((i+1) + ".\t" + classNames[ iClass[i] ] + "\t" + 
    		            classNames[ iClassCalculated[i] ] +
    					(sameClassification?"" : "  *"));
    		}
    		nbCorrectClassified += sameClassification ? 1 : 0;
    	}
    	double accuracy = (double)nbCorrectClassified / X.length;
    	if(print) {
    		System.out.println("\nNr. of patterns correctly classified  ="+ nbCorrectClassified);
    		System.out.println("Accuracy =" + String.format(" %.3f %%", accuracy*100 ));
    	}
    	return accuracy;
      }
    
    public int[] getiClassCalculated() {
		return iClassCalculated;
	}
}
