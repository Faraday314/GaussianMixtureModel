import java.util.List;
import java.util.Random;

public class RandomDataGenerator {
    private Random random;
    private DataCluster[] clusters;
    public RandomDataGenerator(DataCluster... clusters) {
        random = new Random();
        this.clusters = clusters;
    }

    public double[] genData() {
        int lengthSum = 0;
        double[] data = new double[getDataSize()];
        for(int i = 0; i < clusters.length; i++) {
            for (int j = lengthSum; j < lengthSum+clusters[i].getNumPoints(); j++) {
                data[j] = Math.max(0,random.nextGaussian() * clusters[i].getVariance() + clusters[i].getCenter());
                data[j] = data[j] > clusters[i].getCenter() + clusters[i].getNoiseRange() ? clusters[i].getCenter() + clusters[i].getNoiseRange() : Math.max(clusters[i].getCenter() - clusters[i].getNoiseRange(), data[j]);
            }
            lengthSum =+ clusters[i].getNumPoints();
        }

        return data;
    }

    public int getDataSize() {
        int dataSize = 0;
        for (DataCluster cluster : clusters) {
            dataSize += cluster.getNumPoints();
        }
        return dataSize;
    }
}
