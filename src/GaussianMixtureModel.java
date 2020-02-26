import java.util.*;

import static java.lang.Math.*;

public class GaussianMixtureModel {
    private double maxDelta;
    private int k, dimensionality;
    private double logLikelihoodSum, logLikelihood;
    private double[] weights, gammaSums;
    private Matrix[] gammaProductSums, gammaVarianceSums;
    private Gaussian[] gaussians;
    public GaussianMixtureModel(int k, int dimensionality) {
        this.k = k;
        this.dimensionality = dimensionality;

        logLikelihoodSum = 0;
        logLikelihood = Double.NEGATIVE_INFINITY;

        gaussians = new Gaussian[k];
        weights = new double[k];
        gammaSums = new double[k];
        gammaProductSums = new Matrix[k];
        gammaVarianceSums = new Matrix[k];

        for (int i = 0; i < k; i++) {
            weights[i] = 1.0/k;
            gammaSums[i] = 0;
            gammaProductSums[i] = Matrix.zeroVector(dimensionality);
            gammaVarianceSums[i] = Matrix.zeroMatrix(dimensionality);
        }

        maxDelta = Double.POSITIVE_INFINITY;
    }

    public double getValue(Matrix x) {
        double sum = 0;
        for(int i = 0; i < k; i++) {
            sum += weights[i]*gaussians[i].getValue(x);
        }
        return sum;
    }

    public void updateModel(Matrix x) {
        for (int i = 0; i < k; i++) {
            double gamma = getBlobGamma(x, i);

            gammaSums[i] += gamma;
            gammaProductSums[i] = gammaProductSums[i].add(x.multiplyScalar(gamma));
            gammaVarianceSums[i] = gammaVarianceSums[i].add(x.subtract(gaussians[i].getMean()).multiply(x.subtract(gaussians[i].getMean()).transpose()).multiplyScalar(gamma));

            logLikelihoodSum += log(getValue(x));
        }
    }

    public void finishUpdate(int numberDatapoints) {
        //calcMaxDelta(numberDatapoints);
        for (int i = 0; i < k; i++) {
            weights[i] = gammaSums[i]/numberDatapoints;
            gaussians[i].setMean(gammaProductSums[i].divideScalar(gammaSums[i]));
            gaussians[i].setCovarianceMatrix(gammaVarianceSums[i].divideScalar(gammaSums[i]));
        }
        logLikelihood = logLikelihoodSum;
        resetSums();
    }

    public void train(double deltaThreshold, Matrix data) {
        //Each column is a data point

        int numberPoints = data.getCols();

        double[][] meanVal = new double[dimensionality][1];

        for (int i = 0; i < numberPoints; i++) {
            for (int j = 0; j < dimensionality; j++) {
                meanVal[j][0] += data.get(j,i);
            }
        }
        Matrix mean = new Matrix(meanVal);
        mean = mean.divideScalar(numberPoints); //sample vs population


        Matrix[] points = data.splitToVectors();
        Matrix covariance = Matrix.zeroMatrix(dimensionality);
        for (int i = 0; i < numberPoints; i++) {
            Matrix score = points[i].subtract(mean);
            covariance = covariance.add(score.multiply(score.transpose()));
            score.destroy();
        }
        covariance = covariance.divideScalar(numberPoints);

        Matrix[] randomlySelected = new Matrix[k];
        List<Integer> usedIndexes = new ArrayList<>();
        int i = 0;
        while(usedIndexes.size() < k) {
            int randomIdx = getRandomIndex(numberPoints);
            if(!usedIndexes.contains(randomIdx)) {
                usedIndexes.add(randomIdx);
                gaussians[i] = new Gaussian(points[randomIdx],covariance);
                i++;
            }
        }

        double delta = Double.POSITIVE_INFINITY;
        while(delta > deltaThreshold) {
            for (Matrix datapoint : points) {
                updateModel(datapoint);
            }
            double prevLogLikelihood = logLikelihood;
            finishUpdate(points.length);
            delta = logLikelihood - prevLogLikelihood;
            System.out.println(logLikelihood);
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

    private int getRandomIndex(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    private double getBlobGamma(Matrix x, int i) {
        return (weights[i]*gaussians[i].getValue(x))/getValue(x);
    }

    private void resetSums() {
        for(int i = 0; i < k; i++) {
            gammaSums[i] = 0;
            gammaProductSums[i] = Matrix.zeroVector(dimensionality);
            gammaVarianceSums[i] = Matrix.zeroMatrix(dimensionality);
        }
        logLikelihoodSum = 0;
    }
/*
    private void calcMaxDelta(int numberDatapoints) {
        double maxDelta = 0;
        for(int i = 0; i < k; i++) {
            maxDelta = max(maxDelta, abs(weights[i] - gammaSums[i]/numberDatapoints));
            maxDelta = max(maxDelta, abs(gaussians[i].getMean() - gammaProductSums[i]/gammaSums[i]));
            maxDelta = max(maxDelta, abs(gaussians[i].getVariance() - gammaVarianceSums[i]/gammaSums[i]));
        }
        this.maxDelta = maxDelta;
    }
*/
    public int getK() {
        return k;
    }

    public Gaussian getGaussian(int i) {
        return gaussians[i];
    }

    public Gaussian[] getGaussians() {
        return gaussians;
    }
}
