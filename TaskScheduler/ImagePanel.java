package TaskScheduler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	private Graphics2D imageGraphics;
	
	public ImagePanel(int width, int height) {
		super();
		setPreferredSize(new Dimension(width, height));
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imageGraphics = (Graphics2D) image.getGraphics();
		imageGraphics.setBackground(Color.WHITE);
		imageGraphics.clearRect(0, 0, width, height);
	}
	
	@Override
	public Graphics getGraphics() {
		return imageGraphics;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
}
