public class DataCluster {
    private double center, noiseRange, variance;
    private int numPoints;
    public DataCluster(double center, double noiseRange, double variance, int numPoints) {
        this.center = center;
        this.noiseRange = noiseRange;
        this.variance = variance;
        this.numPoints = numPoints;
    }

    public double getCenter() {
        return center;
    }

    public double getNoiseRange() {
        return noiseRange;
    }

    public double getVariance() {
        return variance;
    }

    public int getNumPoints() {
        return numPoints;
    }
}
