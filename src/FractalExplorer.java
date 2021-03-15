import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FractalExplorer {

		private int displaySize;
		private JImageDisplay display;
		private FractalGenerator fractal;
		private Rectangle2D.Double range;
		
		
		public FractalExplorer(int size) {
			displaySize = size;
			
	        fractal = new Mandelbrot();
	        range = new Rectangle2D.Double();
	        fractal.getInitialRange(range);
	        display = new JImageDisplay(displaySize, displaySize);
		}
		
		public void createAndShowGUI() {
			display.setLayout(new BorderLayout());
			JFrame frame = new JFrame("Fractal Explorer");
			frame.add(display, BorderLayout.CENTER);
			
			JButton resetBut = new JButton("Reset Display");
			ResetHandler handler = new ResetHandler();
			resetBut.addActionListener(handler);
			
			frame.add(resetBut, BorderLayout.SOUTH);

			MouseHandler click = new MouseHandler();
			display.addMouseListener(click);
			
			JComboBox comboBox = new JComboBox();
			
	        FractalGenerator mandelbrotFractal = new Mandelbrot();
	        comboBox.addItem(mandelbrotFractal);
	        FractalGenerator tricornFractal = new Tricorn();
	        comboBox.addItem(tricornFractal);
	        FractalGenerator burningShipFractal = new BurningShip();
	        comboBox.addItem(burningShipFractal);
	        
	        ButtonHandler fractalChooser = new ButtonHandler();
	        comboBox.addActionListener(fractalChooser);    
	        
	        JPanel panel = new JPanel();
	        JLabel label = new JLabel("Fractal:");
	        panel.add(label);
	        panel.add(comboBox);
	        frame.add(panel, BorderLayout.NORTH);
	        
	        JButton saveBut = new JButton("Save");
	        JPanel bottomPanel = new JPanel();
	        bottomPanel.add(saveBut);
	        bottomPanel.add(resetBut);
	        frame.add(bottomPanel, BorderLayout.SOUTH);
	        
	        ButtonHandler saveHandler = new ButtonHandler();
	        saveBut.addActionListener(saveHandler);
	        
	        
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
			frame.setResizable(false);			
		}
		
		private void drawFractal() {
			
			for (int x = 0; x < displaySize; x++) {
	            for (int y= 0; y < displaySize; y++) {
	                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x);	                
	                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, displaySize, y);
	                
	                int iteration = fractal.numIterations(xCoord, yCoord);
	                
	                if (iteration == -1) {
	                    display.drawPixel(x, y, 0);
	                } else {
	                    float hue = 0.7f + (float) iteration / 200f;
	                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
	                
	                    display.drawPixel(x, y, rgbColor);
	                }
	                
	            }
	        }
			
	        display.repaint();
	    }
		
	    private class ButtonHandler implements ActionListener
	    {
	    	
		        public void actionPerformed(ActionEvent e) {
		            String command = e.getActionCommand();
		            
		            if (e.getSource() instanceof JComboBox) {
		                JComboBox source = (JComboBox) e.getSource();
		                fractal = (FractalGenerator) source.getSelectedItem();
		                fractal.getInitialRange(range);
		                drawFractal();
		                
		            } else if (command.equals("Reset")) {
		                fractal.getInitialRange(range);
		                drawFractal();
		            }  else if (command.equals("Save")) {
		            	JFileChooser fileChooser = new JFileChooser();
		                
		                FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
		                fileChooser.setFileFilter(extensionFilter);
		                fileChooser.setAcceptAllFileFilterUsed(false);
		                
		                int userSelection = fileChooser.showSaveDialog(display);
		                
		                if (userSelection == JFileChooser.APPROVE_OPTION) {
		                    java.io.File file = fileChooser.getSelectedFile();
		                    String file_name = file.toString();
		                    
		                    try {
		                        BufferedImage displayImage = display.getImage();
		                        javax.imageio.ImageIO.write(displayImage, "png", file);
		                    } catch (Exception exception) {
		                        JOptionPane.showMessageDialog(display, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
		                    }
		                } else return;
		           }
		      }
		 }
		
		private class ResetHandler implements ActionListener {
			
				public void actionPerformed(ActionEvent e) {
					fractal.getInitialRange(range);
					drawFractal();
				}
			
		}
		
		private class MouseHandler extends MouseAdapter {
			
				public void mouseClicked(MouseEvent e) {
					int x = e.getX();
		            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x);
		            
		            int y = e.getY();
		            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, displaySize, y);
		                    
		            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
		                  
		            drawFractal();
				}			
		}
		
		public static void main(String[] args) {
				
			FractalExplorer display = new FractalExplorer(800);
			display.createAndShowGUI();
			display.drawFractal();
			
		}
}