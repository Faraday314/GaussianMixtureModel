public class BimodalModel {

    private Gaussian blob1, blob2;
    private double weight1, weight2;

    public BimodalModel(Gaussian blob1, Gaussian blob2) {
        this.blob1 = blob1;
        this.blob2 = blob2;

        weight1 = 0.5;
        weight2 = 0.5;
    }

    public double getValue(double x) {
        return weight1*blob1.getValue(x) + weight2*blob2.getValue(x);
    }

    public boolean isUnimodal() {
        return blob1.equals(blob2);
    }
}
