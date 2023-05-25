package com.usv.recommendationSystem.learningsets;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import javax.sound.sampled.Line;

import com.usv.recommendationSystem.utils.DataUtils;
import com.usv.recommendationSystem.utils.FileUtils1;
import com.usv.recommendationSystem.utils.Pattern;
import com.usv.recommendationSystem.utils.StatisticsUtils;

public class UnsupervisedLearningSet {
    protected double[][] X;  // the pattern matrix
    protected double[]   f;  // the weight of each pattern 
    protected int n;         // number of patterns
    protected int p;         // number of features
    protected int M;         // number of classes (=0 for unsupervised set)
    //

    public UnsupervisedLearningSet(double[][] X) {
        fillFieldValues(X, null);
    }
    public UnsupervisedLearningSet(double[][] X, double[] f) {
        fillFieldValues(X, f);
    }

    public UnsupervisedLearningSet(String dataFileName) {
        X = FileUtils1.readMatrixFromFileStream(dataFileName);
        fillFieldValues(X, null);
    }

    protected UnsupervisedLearningSet() {
    }

    protected void fillFieldValues(double[][] X, double[] f) {
        this.X = X;
        n = X==null?0:X.length;
        p = X==null || X[0]==null ? 0 : X[0].length;
        // TODO check if all the patterns have the same number of features
        //            if not throw an Exception
        if (f == null) {
            f = calculateWeightsValues();
        }
        this.f = f;
    }

    public double[] calculateWeightsValues(){
        double[] f = new double[X.length];
        Arrays.fill(f,1.);  // TODO: replace it by counting identical pattern
        // if a reduced set is produce update tve value of n
        return f;
    }

    public void normalize() {
        this.X = DataUtils.normalizeSet(X);
    }

    public void autoScale() {
        this.X = DataUtils.autoScaleSet(X, f);
    }

    public int getN() {
        return n;
    }

    public int getP() {
        return p;
    }

    public int getM() {
        return M;
    }
    public double[][] getX() {
        return X;
    }

    public double[] getF() {
        return f;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + ": n="+n+", p=" + p + ", M=" + M);
        sb.append("\nNr.crt. ,  X ,  [f]:\n");
        for(int i=0; i<n; i++) {
            sb.append(String.format("%d.  ", i+1));
            for( double elemCrt: X[i])
                sb.append(String.format("%5.2f  ", elemCrt));
            sb.append(String.format("   [%5.2f]", f[i])).append("\n");
        }
        return sb.toString();
    }



}
