import java.util.Random;

import static java.lang.Math.sqrt;

public class MainClass {
    public static void main(String[] args) {
        double firstClusterCenter = 0.0;
        double secondClusterCenter = 6.0;

        Random random = new Random();

        int cluster1Size = 250;
        int cluster2Size = 250;

        double cluster1NoiseRange = 0.5;
        double cluster2NoiseRange = 0.5;

        double cluster1Variance = 0.2;
        double cluster2Variance = 0.2;

        double[] data = new double[cluster1Size+cluster2Size];

        //Generate example data
        for (int i = 0; i < cluster1Size; i++) {
            data[i] = Math.max(0,random.nextGaussian() * cluster1Variance + firstClusterCenter);
            data[i] = data[i] > firstClusterCenter + cluster1NoiseRange ? firstClusterCenter + cluster1NoiseRange : Math.max(firstClusterCenter - cluster1NoiseRange, data[i]);
        }
        for (int i = cluster1Size; i < cluster1Size+cluster2Size; i++) {
            data[i] = Math.max(0,random.nextGaussian() * cluster2Variance + secondClusterCenter);
            data[i] = data[i] > secondClusterCenter + cluster2NoiseRange ? secondClusterCenter + cluster2NoiseRange : Math.max(secondClusterCenter - cluster2NoiseRange, data[i]);
        }

        int usedI = random.nextInt(cluster1Size+cluster2Size);
        int nextI = usedI;
        while(nextI == usedI) {
            nextI = random.nextInt(cluster1Size+cluster2Size);
        }

        double variance = 0;
        double mean = 0;
        for(double d: data) {
            mean += d;
        }
        mean /= data.length;

        for(double d : data) {
            variance += (d - mean)*(d - mean);
        }
        variance /= data.length;

        Gaussian blob1Init = new Gaussian(data[usedI],sqrt(variance));
        Gaussian blob2Init = new Gaussian(data[nextI],sqrt(variance));

        System.out.println(data[usedI]);
        System.out.println(data[nextI]);


        new DylansGrapher(data,new BimodalModel(blob1Init, blob2Init));
    }
}
