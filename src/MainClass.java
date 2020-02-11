import java.util.*;

import static java.lang.Math.*;

public class MainClass {

    public static final double DELTA_THRESH = 1e-6;
    public static final double MEAN_MIN_DELTA = 3;
    public static final int ITERS = 20;

    public static final DataCluster cluster1 = new DataCluster(9,100,0.5,500);
    public static final DataCluster cluster2 = new DataCluster(6,100,0.5,500);

    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(cluster1,cluster2);
        double[] data = randomDataGenerator.genData();

        //Init stage, select 2 data points as means, then set the variance to the variance of the data.
        double variance = 0;
        double mean = 0;
        double maxVal = 0;
        double minVal = Double.POSITIVE_INFINITY;
        for(double d : data) {
            mean += d;
            maxVal = max(d, maxVal);
            minVal = min(d, minVal);
        }
        mean /= data.length;

        for(double d : data) {
            variance += (d - mean)*(d - mean);
        }
        variance /= data.length;

        double avg = (maxVal+minVal)/2.0;
        double init1 = avg + sqrt(variance);
        double init2 = avg - sqrt(variance);


        double avgDivider = 0;
        int iterations = 0;
        while(iterations < ITERS) {

            double[] randomVals = getRandomVals(data,2);

            System.out.println(randomVals[0]);
            System.out.println(randomVals[1]);


            Gaussian blob1Init = new Gaussian(randomVals[0], sqrt(variance));
            Gaussian blob2Init = new Gaussian(randomVals[1], sqrt(variance));

            BimodalModel model = new BimodalModel(blob1Init, blob2Init);

            //Expectation-Maximization Stage, calculation the probability that each data point belongs to each distribution.
            while (model.getMaxDelta() < DELTA_THRESH) {
                for (double datapoint : data) {
                    model.updateModel(datapoint);
                }

                model.finishUpdate(data.length);
            }
            if(abs(model.getBlob1().getMean() - model.getBlob2().getMean()) < MEAN_MIN_DELTA) {
                continue;
            }

            double divider = (model.getBlob1().getMean() + model.getBlob2().getMean()) / 2.0;
            new DylansGrapher(data,model,divider,true);

            avgDivider += divider;
            iterations++;
        }
        avgDivider /= ITERS;

        System.out.println(avgDivider);

    }

    public static double[] getRandomVals(double[] data, int numVals ) {
        Random random = new Random();
        Set<Double> nonDuplicateSet = new HashSet<>();
        for(double datapoint : data) {
            nonDuplicateSet.add(datapoint);
        }
        List<Double> nonDuplicates = new ArrayList<>(nonDuplicateSet);

        double[] outputVals = new double[numVals];
        for (int i = 0; i < numVals ; i++) {
            int randIdx = random.nextInt(nonDuplicates.size());
            outputVals[i] = nonDuplicates.get(randIdx);
            nonDuplicates.remove(randIdx);
        }
        return outputVals;
    }
}
