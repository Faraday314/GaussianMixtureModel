import java.util.*;

import static java.lang.Math.*;

public class MainClass {

    public static final double DELTA_THRESH = 0.01;
    public static final int MAX_ITERS = 150;

    public static final DataCluster cluster1 = new DataCluster(1,100,0.1,250);
    public static final DataCluster cluster2 = new DataCluster(6,100,0.1,250);

    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(cluster1,cluster2);
        double[] data = randomDataGenerator.genData();
        double[] randomVals = getRandomVals(data,2);

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
        double init1 = (minVal+avg)/2.0;
        double init2 = (maxVal+avg)/2.0;

        Gaussian blob1Init = new Gaussian(init1,sqrt(variance));
        Gaussian blob2Init = new Gaussian(init2,sqrt(variance));

        BimodalModel model = new BimodalModel(blob1Init,blob2Init);

        //Expectation-Maximization Stage, calculation the probability that each data point belongs to each distribution.
        int iterations = 0;
        while(model.getMaxDelta() < DELTA_THRESH && iterations < MAX_ITERS) {
            for (double datapoint : data) {
                model.updateModel(datapoint);
            }

            model.finishUpdate(data.length);
            iterations++;
        }

        double divider = (model.getBlob1().getMean() + model.getBlob2().getMean())/2.0;


        System.out.println(divider);

        new DylansGrapher(data,model,divider);
    }

    public static double[] getRandomVals(double[] data, int numVals) {
        Random random = new Random();
        Set<Double> nonDuplicateSet = new HashSet<>();
        for(double datapoint : data) {
            nonDuplicateSet.add(datapoint);
        }
        List<Double> nonDuplicates = new ArrayList<>(nonDuplicateSet);

        double[] outputVals = new double[numVals];
        for (int i = 0; i < numVals ; i++) {
            int randIdx = random.nextInt(nonDuplicates.size());
            outputVals[i] = nonDuplicates.get(i);
            nonDuplicates.remove(i);
        }
        return outputVals;
    }
}
