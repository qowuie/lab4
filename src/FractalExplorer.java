import javax.management.remote.JMXConnectorFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.util.Objects;

public class FractalExplorer{
    private int size;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    FractalExplorer(int size){
        this.size = size;
        this.range = new Rectangle2D.Double();
        this.fractal = new Mandelbrot();

        fractal.getInitialRange(range);
        display = new JImageDisplay(size, size);
    }

    private void createAndShowGUI(){
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MouseListener mouseListener = new MouseListener();
        display.addMouseListener(mouseListener);
        frame.add(display, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Display");
        resetButton.setActionCommand("reset fractal");
        ActionListener actionListener = new ButtonListener();
        resetButton.addActionListener(actionListener);
        frame.add(resetButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }

    private void drawFractal(){
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);

                int iterations = fractal.numIterations(xCoord, yCoord);
                if(iterations == -1){
                    display.drawPixel(x, y, 0);
                }
                else {
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        display.repaint();
    }


    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getActionCommand().equals("reset fractal")){
                fractal.getInitialRange(range);
                drawFractal();
            }
        }
    }

    private class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args){
        FractalExplorer explorer = new FractalExplorer(800);
        explorer.createAndShowGUI();
        explorer.drawFractal();

    }
}
