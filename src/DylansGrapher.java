import javax.swing.*;

public class DylansGrapher extends JFrame {
    GrapherPannel pannel;
    final static int HEIGHT = 500;
    final static int WIDTH = 1000;
    DylansGrapher(double[] data, BimodalModel resaults, boolean grafFunc){
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        pannel = new GrapherPannel(this, data, resaults, grafFunc);
        setVisible(true);
        this.add(pannel);
    }
    DylansGrapher(double[] data, BimodalModel resaults, double valueForLine, boolean grafFunc) {
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        pannel = new GrapherPannel(this, data, resaults, valueForLine, grafFunc);
        setVisible(true);
        this.add(pannel);
    }
}
