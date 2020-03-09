import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GrapherPannel2 extends JPanel {

    int[] xData;
    int[] yData;
    int[] xCenters;
    int[] yCenters;
    int[] eigX;
    int[] eigY;
    int width;
    int height;
    int dimensionality;
    boolean showCenters;
    JFrame frame;

    public GrapherPannel2(JFrame frame, int width, int height, double[] xData, double[] yData){
        int scaleX = maxVal(xData);
        int scaleY = maxVal(yData);
        this.xData = scaleOtherStuff(Math.max(scaleX,scaleY),width,xData);
        this.yData = scaleOtherStuff(Math.max(scaleX,scaleY),width,yData);
        this.frame = frame;
        this.width = width;
        this.height = height;
        showCenters = false;
    }

    public GrapherPannel2(JFrame frame, int width, int height, double[] xData, double[] yData, double[] centerX, double[] centerY, double[] eigX, double[] eigY, int dimensionality) {
        int scaleX = maxVal(xData);
        int scaleY = maxVal(yData);
        this.xData = scaleOtherStuff(Math.max(scaleX,scaleY),width,xData);
        this.yData = scaleOtherStuff(Math.max(scaleX,scaleY),width,yData);
        this.frame = frame;
        this.width = width;
        this.height = height;
        showCenters = true;
        xCenters =  scaleOtherStuff(Math.max(scaleX,scaleY),width,centerX);
        yCenters =  scaleOtherStuff(Math.max(scaleX,scaleY),width,centerY);
        this.eigX = scaleOtherStuff(Math.max(scaleX,scaleY),width,eigX);
        this.eigY = scaleOtherStuff(Math.max(scaleX,scaleY),width,eigY);
        //this.eigX = mul(width,eigX);
        //this.eigY = mul(height,eigY);
        this.dimensionality = dimensionality;
    }

    public void draw() {
        drawGraph();
    }

    @Override
    public void paintComponent(Graphics g){
        //g.setColor(new Color(255,255,255));
        //g.drawRect(0,0, width, height);
        drawPoints(g);
    }

    public void drawGraph(){
        repaint();
    }

    private void drawPoints(Graphics g) {
        g.setColor(new Color(200,100,0));
        for (int i = 0; i < xData.length; i++) {
            g.fillOval(xData[i],height-yData[i],10,10);
        }
        if(showCenters) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < xCenters.length; i++) {
                g.setColor(new Color(0,255,0));
                g.fillOval(xCenters[i],height-yCenters[i],10,10);
                g2.setColor(new Color(255,0,0));
                for (int j = 0; j < dimensionality; j++) {
                    g2.drawLine(xCenters[i],height-yCenters[i],xCenters[i]+eigX[j],height-(yCenters[i]+eigY[j]));
                }
            }
        }
    }

    public static int maxVal(double[] data) {
        double maxPt = 0;
        for (int i = 0; i < data.length; i++) {
            maxPt = Math.abs(Math.max(maxPt,data[i]));
        }
        return (int) Math.round(maxPt);
    }

    public static int[] mul(int val, double[] data) {
        int[] output = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = (int) Math.round(data[i]*val);
        }
        return output;
    }

    public static int[] scaleOtherStuff(int max, int scaleTo, double[] stuff) {
        int[] output = new int[stuff.length];
        for (int i = 0; i < stuff.length; i++) {
            output[i] = (int) Math.round(scaleTo*(stuff[i]/max));
        }
        return output;
    }

    public static int[] scaleData(int scaleTo, double[] data) {

        int maxPt = maxVal(data);

        int[] output = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = (int) Math.round(scaleTo*(data[i]/maxPt));
        }
        return output;
    }
}