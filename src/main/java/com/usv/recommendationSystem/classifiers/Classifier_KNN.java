package com.usv.recommendationSystem.classifiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

import com.usv.recommendationSystem.utils.DistanceUtils;
import com.usv.recommendationSystem.utils.IDistance;
import com.usv.recommendationSystem.utils.Neighbour;

public class Classifier_KNN extends AbstractClassifier {
	
	IDistance d;
	int k;
	private boolean debug = true;
	
	public Classifier_KNN(int k, IDistance d) {
		super();
		this.d = d;
		this.k = k;
	}

	public Classifier_KNN(int k) {
		this(k, DistanceUtils::distEuclid);
	}


	@Override
	public void training() {
		if(M==0)
			throw new RuntimeException("train(): No supervised learning set provided (M=0)");
		// all rest were done in super.train(X,F,iClass)
	}

	//TODO: implement predict method
	@Override
	public int predict(double[] z) {
		
		//MY IMPLEMENTATION
		
		/*
		
		PriorityQueue<Neighbour> pq = new PriorityQueue<Neighbour>(k);
	    for (int i = 0; i < X.length; i++) {
	        double distance = d.calculateDistance(X[i], z);
	        pq.offer(new Neighbour(distance, iClass[i]));
	        if (pq.size() > k) {
	            pq.poll();
	        }
	    }
	    Map<Integer, Integer> classOccurenceMap = new HashMap<Integer, Integer>();
	    while (!pq.isEmpty()) {
	        Neighbour currentNeighbour = pq.poll();
	        Integer currentClassIndex = currentNeighbour.getIClass();
	        if (classOccurenceMap.containsKey(currentClassIndex)) {
	            int numberOfOccurences = classOccurenceMap.get(currentClassIndex);
	            classOccurenceMap.put(currentClassIndex, ++numberOfOccurences);
	        } else {
	            classOccurenceMap.put(currentClassIndex, 1);
	        }
	    }
	    int maxOccurrence = -1;
	    int predictedClass = -1;
	    for (Map.Entry<Integer, Integer> entry : classOccurenceMap.entrySet()) {
	        if (entry.getValue() > maxOccurrence) {
	            maxOccurrence = entry.getValue();
	            predictedClass = entry.getKey();
	        }
	    }
	    return predictedClass;
	    */
	    
	    //EXPLANATION    
	    /*
	    we first create a priority queue of Neighbour objects, which will hold the k-nearest neighbours to the test instance. 
	    We loop through all the training instances in X and calculate the distance between the current training instance and the test instance using 
	    the IDistance object d. We then create a new Neighbour object with the class label, distance and index of the current training instance and add it to the priority queue. 
	    If the priority queue size exceeds k, we remove the neighbour with the largest distance.
		Next, we create a Map<Integer, Integer> object to store the class occurrence count for all k-nearest neighbours in the priority queue. 
		We loop through the priority queue and increment the count for each class occurrence. 
		Finally, we iterate over the classOccurenceMap and select the class with the highest occurrence count as the predicted class.	    
	     */
		
	    
	    
	    //IMPLEMENTAREA DE PE CLASSROOM
	    
		
		
	    PriorityQueue<Neighbour> pq = new PriorityQueue<Neighbour>(k);
	    for (int i = 0; i < n; i++){
			double dist = d.calculateDistance(z, X[i]);
			Neighbour neighbour = new Neighbour(dist, iClass[i]);
			if (pq.size() < k) {
				pq.add(neighbour);
			}
			else {
				if(pq.peek().compareTo(neighbour) < 0) //compare distances
				{
					pq.remove();
					pq.add(neighbour);
				}
			}
	    }
	    int[] nrv = new int[M+1];
	    pq.stream().forEach(v -> nrv[v.getIClass()]++);
	    return IntStream.range(1,M+1)
	    .reduce(0,(iprec, icrt)->
	    nrv[icrt] > nrv[iprec] ? icrt : iprec);
	    
	    //EXPLICATII
	     
	    /*
	    
	    The predict method in the KNN classifier is used to classify a new input pattern represented by the feature vector z.
	    
	    The method first initializes a PriorityQueue of size k to store the k-nearest neighbors to the input pattern. 
	    The Neighbour class is defined to hold the distance between the input pattern and a reference pattern, as well as the class label of the reference pattern.
	    
	    The method then iterates through each reference pattern, represented by the row i in the input matrix X, and its corresponding class label, represented by the integer iClass[i].
	    For each reference pattern, the method calculates the Euclidean distance between the input pattern z and the reference pattern X[i] using the calculateDistance method from the d object.
	    
	    The distance and the class label are then added to a new Neighbour object. If the priority queue is not yet full, the new Neighbour object is simply added to the priority queue. 
	    If the priority queue is full, the new Neighbour object is compared to the current maximum distance in the priority queue using the compareTo method. 
	    If the distance of the new Neighbour object is smaller than the maximum distance in the queue, the maximum distance is removed and the new Neighbour object is added to the queue.
	    
	    After all reference patterns have been processed, the method creates an integer array nrv with size M+1, where M is the number of classes. 
	    This array is used to count the number of neighbors belonging to each class. 
	    The stream method is used to iterate through the priority queue and update the count of neighbors for each class in the nrv array.
	    
	    Finally, the method uses the reduce method to find the class with the highest number of neighbors in the nrv array. 
	    The reduce method initializes iprec to 0 and icrt to 1 and compares the number of neighbors for each class. 
	    If the number of neighbors for class icrt is greater than the number of neighbors for class iprec, icrt becomes the new iprec. This process continues until all classes have been compared, 
	    and the method returns the class label iprec with the highest number of neighbors.
	    
	    In summary, the predict method in the KNN classifier computes the distances between the input pattern and each reference pattern, keeps track of the k-nearest neighbors using a priority queue,
	    and determines the class label of the input pattern by counting the number of neighbors belonging to each class and returning the class with the highest count.
	    
	     */
	    
		
		
	}
		
	
    static public void classifyAndDisplayResult(AbstractClassifier classifier, String[] classNames, double[][] testSet) {
    	System.out.println("\nPatterns class:"+ Arrays.deepToString(testSet) + ":");
    	Arrays.stream(classifier.predict(testSet))
    	.mapToObj(k-> (classNames==null ? k :classNames[k]) +" ")
        .forEach(System.out::print);
    }

    public void setDebug(boolean debug) {
		this.debug = debug;
	}

    

}
