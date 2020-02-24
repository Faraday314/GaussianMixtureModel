import java.util.*;

import static java.lang.Math.*;

public class GaussianMixtureModel1d {
    private double maxDelta;
    private int k;
    private double[] weights, gammaSums, gammaProductSums, gammaVarianceSums;
    private Gaussian1d[] gaussians;
    public GaussianMixtureModel1d(int k) {
        this.k = k;

        gaussians = new Gaussian1d[k];
        weights = new double[k];
        gammaSums = new double[k];
        gammaProductSums = new double[k];
        gammaVarianceSums = new double[k];

        for (int i = 0; i < k; i++) {
            gaussians[i] = new Gaussian1d(0,0);
            weights[i] = 1.0/k;
            gammaSums[i] = 0;
            gammaProductSums[i] = 0;
            gammaVarianceSums[i] = 0;
        }

        maxDelta = Double.POSITIVE_INFINITY;
    }

    public double getValue(double x) {
        double sum = 0;
        for(int i = 0; i < k; i++) {
            sum += weights[i]*gaussians[i].getValue(x);
        }
        return sum;
    }

    public void updateModel(double x) {
        for (int i = 0; i < k; i++) {
            double gamma = getBlobGamma(x, i);

            gammaSums[i] += gamma;
            gammaProductSums[i] += gamma*x;
            gammaVarianceSums[i] += gamma*(x-gaussians[i].getMean())*(x-gaussians[i].getMean());
        }
    }

    public void finishUpdate(int numberDatapoints) {
        calcMaxDelta(numberDatapoints);
        for (int i = 0; i < k; i++) {
            weights[i] = gammaSums[i]/numberDatapoints;
            gaussians[i].setMean(gammaProductSums[i]/gammaSums[i]);
            gaussians[i].setVariance(gammaVarianceSums[i]/gammaSums[i]);
        }
        resetSums();
    }

    public void train(double[] data, double deltaThreshold) {
        double mean = 0;
        for(double d : data) {
            mean += d;
        }
        mean /= data.length;

        double variance = 0;
        for(double d : data) {
            variance += (d - mean)*(d - mean);
        }
        variance /= data.length;

        double[] randomValues = selectNRandomValues(data, k);
        for (int i = 0; i < k; i++) {
            gaussians[i].setMean(randomValues[i]);
            gaussians[i].setVariance(variance);
        }

        while(maxDelta > deltaThreshold) {
            for(double datapoint : data) {
                updateModel(datapoint);
            }
            finishUpdate(data.length);
        }
    }

    private double[] selectNRandomValues(double[] data, int n) {
        Random random = new Random();
        Set<Double> nonDuplicateSet = new HashSet<>();
        for(double datapoint : data) {
            nonDuplicateSet.add(datapoint);
        }
        List<Double> nonDuplicates = new ArrayList<>(nonDuplicateSet);

        double[] outputVals = new double[n];
        for (int i = 0; i < n ; i++) {
            int randIdx = random.nextInt(nonDuplicates.size());
            outputVals[i] = nonDuplicates.get(randIdx);
            nonDuplicates.remove(randIdx);
        }
        return outputVals;
    }

    private double getBlobGamma(double x, int i) {
        return (weights[i]*gaussians[i].getValue(x))/getValue(x);
    }

    private void resetSums() {
        for(int i = 0; i < k; i++) {
            gammaSums[i] = 0;
            gammaProductSums[i] = 0;
            gammaVarianceSums[i] = 0;
        }
    }

    private void calcMaxDelta(int numberDatapoints) {
        double maxDelta = 0;
        for(int i = 0; i < k; i++) {
            maxDelta = max(maxDelta, abs(weights[i] - gammaSums[i]/numberDatapoints));
            maxDelta = max(maxDelta, abs(gaussians[i].getMean() - gammaProductSums[i]/gammaSums[i]));
            maxDelta = max(maxDelta, abs(gaussians[i].getVariance() - gammaVarianceSums[i]/gammaSums[i]));
        }
        this.maxDelta = maxDelta;
    }

    public int getK() {
        return k;
    }

    public Gaussian1d getGaussian(int i) {
        return gaussians[i];
    }

    public Gaussian1d[] getGaussians() {
        return gaussians;
    }
}
