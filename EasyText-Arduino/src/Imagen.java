import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
class Imagen extends javax.swing.JPanel {

	public Imagen() {
		this.setSize(300, 400); //se selecciona el tamaño del panel
	}

	public void paint(Graphics grafico) {
		Dimension height = getSize();
		ImageIcon Img = new ImageIcon(getClass().getResource("help.png")); 
		grafico.drawImage(Img.getImage(), 0, 0, height.width, height.height, null);

		setOpaque(false);
		super.paintComponent(grafico);
	}
}