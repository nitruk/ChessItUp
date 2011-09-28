package objets;


import java.awt.Color;
import java.io.Serializable;

public class Robot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int moveStep = 18, turnStep = 10;
	public static int cote = 300;

	private final String name;
	private int angle;
	private int x, y;
	private Color couleur;
	private int tenu = 19;
	
	public Robot(String name, int x, int y, int angle, boolean couleur) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.couleur = (couleur) ? Color.BLUE: Color.RED;
	}

	public String getName() {
		return name;
	}

	public Color getCouleur() {
		return couleur;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getAngle() {
		return angle;
	}
	
	public int getXAvant() {
		return (int) (x + cote/2*Math.cos(angle*Math.PI/180.));
	}
	
	public int getYAvant() {
		return (int) (y + cote/2*Math.sin(angle*Math.PI/180.));
	}

	public void turn(int angle) {
		this.angle += angle;
	}

	public int getCote() {
		return cote;
	}

	public int getTenu() {
		return tenu;
	}

	public void setTenu(int pion) {
		tenu = pion;
	}
}