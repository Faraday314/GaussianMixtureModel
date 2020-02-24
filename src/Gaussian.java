import static java.lang.Math.*;
import static java.lang.StrictMath.sqrt;

public class Gaussian {
    private Matrix mean, covariance;
    private int dimensionality;
    public Gaussian(Matrix mean, Matrix covariance) {
        this.mean = mean;
        this.covariance = covariance;
        dimensionality = mean.getRows();
    }

    public double getValue(Matrix x) {
        return 1.0/(pow(2*PI,dimensionality/2.0)*sqrt(covariance.determinant()))*exp(-0.5*(x.subtract(mean).transpose().multiply(covariance.inverse()).multiply(x.subtract(mean)).trace()));
    }

    public Matrix getMean() {
        return mean;
    }

    public Matrix getCovarianceMatrix() {
        return covariance;
    }

    public void setCovarianceMatrix(Matrix covariance) {
         this.covariance = covariance;
    }

    public void setMean(Matrix mean) {
        this.mean = mean;
    }

    @Override
    public String toString() {
        return "mean: "+mean+" covariance: "+covariance;
    }
}