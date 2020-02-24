import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

import java.util.*;

import static java.lang.Math.*;

public class MainClass {

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static final double DELTA_THRESH = 1e-6;

    public static final DataCluster cluster1 = new DataCluster(5,100,0.5,100);
    public static final DataCluster cluster2 = new DataCluster(11,100,0.5,100);
    public static final DataCluster cluster3 = new DataCluster(17,100,0.5,100);
    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(cluster1);

        double[] xData = randomDataGenerator.genData();
        double[] yData = randomDataGenerator.genData();

        Matrix wham = new Matrix(new double[][]{
                {1,0,0},
                {0,1,0},
                {0,0,1},
        });

        Matrix kabam = new Matrix(new double[][]{
                {2,2,2},
                {1,3,3},
                {3,3,4}
        });

        Matrix[] vectors = kabam.splitToVectors();
        Gaussian g = new Gaussian(vectors[0],wham);
        g.getValue(vectors[1]);

        /*
        GaussianMixtureModel model = new GaussianMixtureModel(3);
        model.train(data,DELTA_THRESH);

        double divider = (model.getGaussian(0).getMean() + model.getGaussian(1).getMean()) / 2.0;

        new DylansGrapher(data,new BimodalModel(new Gaussian(0,90), new Gaussian(17,90)),true);
        System.out.println(divider);*/
    }
}