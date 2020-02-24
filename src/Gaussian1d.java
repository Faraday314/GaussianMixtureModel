import static java.lang.Math.*;
import static java.lang.StrictMath.sqrt;

public class Gaussian1d {
    private static final double EQUALITY_THRESHOLD = 0.001;
    private double mean, standardDeviation;
    public Gaussian1d(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public double getValue(double x) {
        return (1.0/(standardDeviation*sqrt(2*PI)))*exp(-0.5*pow((x-mean)/standardDeviation,2));
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public double getVariance() {
        return standardDeviation*standardDeviation;
    }

    public void setVariance(double variance) {
        standardDeviation = sqrt(variance);
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @Override
    public boolean equals(Object obj) {
        Gaussian1d gaussian;
        if(!(obj instanceof Gaussian1d)) {
            return false;
        }
        else {
            gaussian = (Gaussian1d) obj;
        }
        return abs(mean-gaussian.getMean()) < EQUALITY_THRESHOLD && abs(standardDeviation-gaussian.getStandardDeviation()) < EQUALITY_THRESHOLD;
    }

    @Override
    public String toString() {
        return "mean: "+mean+" standard deviation: "+standardDeviation;
    }
}