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
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(cluster1, cluster2,cluster3);

        double[] xData = randomDataGenerator.genData();

        double[] yData = new double[xData.length];
        Random r = new Random();
        for (int i = 0; i < xData.length; i++) {
            yData[i] = xData[i]+r.nextGaussian();
        }

        Matrix data = new Matrix(xData,yData);

        GaussianMixtureModel model = new GaussianMixtureModel(3,2);
        model.train(0.001,data);

        Matrix[] eigs1 = model.getGaussian(0).getCovarianceMatrix().calcEigenvectorsNonNorm();
        Matrix[] eigs2 = model.getGaussian(1).getCovarianceMatrix().calcEigenvectorsNonNorm();


        Gaussian[] gaussians = model.getGaussians();
        double[] x = new double[gaussians.length];
        double[] y = new double[gaussians.length];
        double[] eigsX = new double[gaussians.length*2];
        double[] eigsY = new double[gaussians.length*2];
        int eigTrack = 0;
        for (int i = 0; i < gaussians.length; i++) {
            x[i] = model.getGaussian(i).getMean().get(0,0);
            y[i] = model.getGaussian(i).getMean().get(1,0);
            Matrix[] eigs = model.getGaussian(i).getCovarianceMatrix().calcEigenvectors();
            for (int j = 0; j < eigs.length; j++) {
                Matrix vector = eigs[j];
                eigsX[eigTrack] = vector.get(0,0);
                eigsY[eigTrack] = vector.get(1,0);
                eigTrack++;
            }
        }

        ColesGrapher grapher = new ColesGrapher(xData,yData,x,y,eigsX,eigsY,2);
        grapher.draw();

/*
        System.out.println(model.getGaussian(0).getMean());
        System.out.println(model.getGaussian(1).getMean());
        System.out.println(Arrays.toString(eigs1));
        System.out.println();
        System.out.println(Arrays.toString(eigs2));


        /*
        GaussianMixtureModel model = new GaussianMixtureModel(3);
        model.train(data,DELTA_THRESH);

        double divider = (model.getGaussian(0).getMean() + model.getGaussian(1).getMean()) / 2.0;

        new DylansGrapher(data,new BimodalModel(new Gaussian(0,90), new Gaussian(17,90)),true);
        System.out.println(divider);*/
    }

    public static int[] scaleData(int scaleTo, double[] data) {
        double maxPt = 0;
        for (int i = 0; i < data.length; i++) {
            maxPt = Math.abs(Math.max(maxPt,data[i]));
        }

        int[] output = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = (int) Math.round(scaleTo*(data[i]/maxPt));
        }
        return output;
    }
}