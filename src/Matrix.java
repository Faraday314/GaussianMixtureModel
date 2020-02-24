import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.face.FacemarkKazemi;

import javax.print.attribute.standard.NumberUp;
import java.util.HashSet;
import java.util.Set;

public class Matrix {
    private Mat data;
    private int rows;
    private int cols;

    public enum InvertMethod {

        CHOLESKY(Core.DECOMP_CHOLESKY),
        LU(Core.DECOMP_LU);

        private int value;
        InvertMethod(int value) {
            this.value = value;
        }
    }

    public Matrix(double[]... data) {
        rows = data.length;
        cols = data[0].length;
        this.data = new Mat(rows, cols, CvType.CV_32F);
        for (int i = 0; i < data.length; i++) {
            this.data.put(i, 0, data[i]);
        }
    }

    public Matrix(double... data) {
        this(new double[][] {data});
    }

    public Matrix(Mat data) {
        rows = data.rows();
        cols = data.cols();

        this.data = new Mat(rows, cols, data.type());
        data.copyTo(this.data);
        data.release();
    }

    public Matrix add(Matrix m) {
        Mat outputData = new Mat();
        Mat mData = m.getData();
        Core.add(data,mData,outputData);
        return new Matrix(outputData);
    }

    public Matrix subtract(Matrix m) {
        return add(m.multiplyScalar(-1));
    }

    public Matrix multiplyScalar(double scalar) {
        Mat multiplied = new Mat();
        Core.multiply(data,new Scalar(scalar),multiplied);
        return new Matrix(multiplied);
    }

    public Matrix divideScalar(double scalar) {
        return multiplyScalar(1/scalar);
    }

    public Matrix multiply(Matrix m) {
        Mat multiplied = new Mat();
        Core.gemm(data,m.getData(),1, new Mat(),0, multiplied);
        return new Matrix(multiplied);
    }

    public Matrix inverse(InvertMethod invertMethod) {
        return new Matrix(data.inv(invertMethod.value));
    }

    public Matrix inverse() {
        return inverse(InvertMethod.LU);
    }

    public Matrix transpose() {
        return new Matrix(data.t());
    }

    public double[] calcEigenvalues() {
        Mat vals = new Mat();
        Core.eigen(data,vals);
        Set<Double> eigenVals = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            eigenVals.add(vals.get(i,0)[0]);
        }
        vals.release();
        double[] output = new double[eigenVals.size()];
        for(int i = 0; i < eigenVals.size(); i++) {
            output[i] = (double) eigenVals.toArray()[i];
        }
        return output;
    }

    public Matrix[] calcEigenvectors() {
        Mat vectors = new Mat();
        Core.eigen(data,new Mat(),vectors);
        Matrix output = new Matrix(vectors);
        Matrix[] eigenvectors = output.splitToVectors();
        output.destroy();
        return eigenvectors;
    }

    public Matrix[] splitToVectors() {
        Matrix[] vectors = new Matrix[cols];
        for (int i = 0; i < cols; i++) {
            double[][] vector = new double[3][1];
            for (int j = 0; j < rows; j++) {
                vector[j][0] = data.get(j,i)[0];
            }
            vectors[i] = new Matrix(vector);
        }
        return vectors;
    }

    public double trace() {
        return Core.trace(data).val[0];
    }

    public double determinant() {
        return Core.determinant(data);
    }

    public boolean isSquare() {
        return data.size().width == data.size().height;
    }

    public boolean isZeroMatrix() {
        return Core.countNonZero(data) == 0;
    }

    public boolean isIdentityMatrix() {
        Mat identity = Mat.eye(data.size(),data.type());
        Mat eq = new Mat();
        Core.compare(data,identity,eq,Core.CMP_EQ);
        identity.release();
        int count = Core.countNonZero(eq);
        eq.release();
        return count == data.size().area();
    }

    public static Matrix identityMatrix(int size) {
        double[][] data = new double[size][size];
        int oneLocation = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = j == oneLocation ? 1 : 0;
            }
            oneLocation++;
        }
        return new Matrix(data);
    }

    public static Matrix zeroMatrix(int size) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = 0;
            }
        }
        return new Matrix(data);
    }

    public void destroy() {
        data.release();
    }

    public void put(int row, int col, double datapoint) {
        data.put(row,col,datapoint);
    }

    public double get(int row, int col) {
        return data.get(row,col)[0];
    }

    private Mat getData() {
        return data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public String toString() {
        return data.dump();
    }

    @Override
    protected Matrix clone() throws CloneNotSupportedException{
        super.clone();
        return new Matrix(data);
    }
}