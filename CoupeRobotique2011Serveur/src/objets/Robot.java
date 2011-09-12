package objets;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Robot {

	private int angle;
	private float x, y;
	private int cote = 250/3;
	private Color couleurRobot;
	boolean couleur;
	
	public Robot(float x, float y, int angle, boolean couleur) {
		this.x = x/3;
		this.y = y/3;
		this.angle = angle;
		this.couleur = couleur;
		couleurRobot = (couleur) ? Color.BLUE: Color.RED;
	}
	public boolean getColor() {
		return couleur;//True = Bleu ; False = Rouge
	}
	public float getX() {
		return 3*x;
	}
	public float getY() {
		return 3*y;
	}
	public void setX(float x) {
		this.x = x/3;
	}
	public void setY(float y) {
		this.y = y/3;
	}
	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public int getCote() {
		return 3*cote;
	}
	public void dessiner (Graphics g) {
		int epaisseur=5;
		//Rotation
		Graphics2D g2D = (Graphics2D) g;
		Stroke s = g2D.getStroke();
		g2D.rotate(angle*3.14/180,x,y);
		//Robot
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int) Math.floor(x-cote/2), (int) Math.floor(y-cote/2), cote, cote);
		g.setColor(couleurRobot);
		g.fillRect((int) Math.floor(x-cote/2.5), (int) Math.floor(y-cote/3), (int) Math.floor(cote/10), (int) Math.floor(cote*2/3));
		g.setColor(Color.BLACK);
		g2D.setStroke(new BasicStroke(epaisseur));
		g.drawOval((int) Math.floor(x-cote/2), (int) Math.floor(y-cote/2), cote, cote);
		//Réinitialisation
		g2D.setStroke(s);
		g2D.rotate(-angle*3.14/180,x,y);
	}
}
