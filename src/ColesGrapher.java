import javax.swing.*;

public class ColesGrapher extends JFrame {
    GrapherPannel2 pannel;
    final static int HEIGHT = 500;
    final static int WIDTH = 500;
    public ColesGrapher(double[] xData, double[] yData) {
        setSize(WIDTH+100,HEIGHT+100);
        setResizable(false);
        pannel = new GrapherPannel2(this,WIDTH,HEIGHT, xData, yData);
        setVisible(true);
        this.add(pannel);
    }
    public ColesGrapher(double[] xData, double[] yData, double[] xCenters, double[] yCenters, double[] eigX, double[] eigY, int dimensionality) {
        setSize(WIDTH+100,HEIGHT+100);
        setResizable(false);
        pannel = new GrapherPannel2(this,WIDTH,HEIGHT, xData, yData, xCenters, yCenters,eigX,eigY, dimensionality);
        setVisible(true);
        this.add(pannel);
    }
    public void draw() {
        pannel.draw();
    }
}
