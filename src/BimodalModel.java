import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BimodalModel {

    private Gaussian blob1, blob2;
    private double weight1, weight2;
    private double gammaSum1, gammaProductSum1, gammaVarianceSum1;
    private double gammaSum2, gammaProductSum2, gammaVarianceSum2;

    private double maxDelta;

    public BimodalModel(Gaussian blob1, Gaussian blob2) {
        this.blob1 = blob1;
        this.blob2 = blob2;

        weight1 = 0.5;
        weight2 = 0.5;

        maxDelta = Double.POSITIVE_INFINITY;
    }

    public double getValue(double x) {
        return weight1*blob1.getValue(x) + weight2*blob2.getValue(x);
    }

    public void updateModel(double x) {
        double gamma1 = getBlob1Gamma(x);

        gammaSum1 += gamma1;
        gammaProductSum1 += gamma1*x;
        gammaVarianceSum1 += gamma1*(x-blob1.getMean())*(x-blob1.getMean());

        double gamma2 = getBlob2Gamma(x);
        gammaSum2 += gamma2;
        gammaProductSum2 += gamma2*x;
        gammaVarianceSum2 += gamma2*(x-blob2.getMean())*(x-blob2.getMean());
    }

    public void finishUpdate(int numberDatapoints) {
        calcMaxDelta(numberDatapoints);

        System.out.println("primary");
        System.out.println(getBlob1());
        System.out.println(getBlob2());

        weight1 = gammaSum1/numberDatapoints;
        weight2 = gammaSum2/numberDatapoints;

        blob1.setMean(gammaProductSum1/gammaSum1);
        blob2.setMean(gammaProductSum2/gammaSum2);

        blob1.setVariance(gammaVarianceSum1/gammaSum1);
        blob2.setVariance(gammaVarianceSum2/gammaSum2);

        System.out.println("secondary");
        System.out.println(getBlob1());
        System.out.println(getBlob2());

        resetSums();
    }

    public Gaussian getBlob1() {
        return blob1;
    }

    public Gaussian getBlob2() {
        return blob2;
    }

    public double getMaxDelta() {
        return maxDelta;
    }

    public boolean isUnimodal() {
        return blob1.equals(blob2);
    }

    private double getBlob1Gamma(double x) {
        return (weight1*blob1.getValue(x))/getValue(x);
    }

    private double getBlob2Gamma(double x) {
        return (weight2*blob2.getValue(x))/getValue(x);
    }

    private void resetSums() {
        gammaSum1 = 0;
        gammaSum2 = 0;

        gammaProductSum1 = 0;
        gammaProductSum2 = 0;

        gammaVarianceSum1 = 0;
        gammaVarianceSum2 = 0;
    }

    private void calcMaxDelta(int numberDatapoints) {
        double[] deltas = new double[] {
                abs(weight1 - gammaSum1 / numberDatapoints),
                abs(weight2 - gammaSum2 / numberDatapoints),

                abs(blob1.getMean() - gammaProductSum1 / gammaSum1),
                abs(blob2.getMean() - gammaProductSum2 / gammaSum2),

                abs(blob1.getVariance() - gammaVarianceSum1 / gammaSum1),
                abs(blob2.getVariance() - gammaVarianceSum2 / gammaSum2)
        };

        double maxDelta = 0;
        for(double delta : deltas) {
            if(delta > maxDelta) {
                maxDelta = delta;
            }
        }

        this.maxDelta = maxDelta;
    }
}
