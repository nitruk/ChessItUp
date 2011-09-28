package objets;


import java.awt.Color;
import java.io.Serializable;

public class Pion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y;
	public static int diametre = 200;
	private Color couleur;
	private int figure = 0; //0 = pion ; 1 = reine ; 2 = roi;
	private int nombre = 1;
	static int nbPions = 0;
	private int index;
	private boolean inRobot = false, utile = true;
	
	public Pion(int x, int y, int figure) {
		this.x = x;
		this.y = y;
		this.figure = figure;
		couleur = (figure!=0) ? Color.ORANGE : Color.YELLOW;
		nbPions ++;
		index = nbPions-1;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getCouleur() {
		return couleur;
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getFigure() {
		return figure; //0 = pion ; 1 = reine ; 2 = roi;
	}
	
	public float getIndex() {
		return index;
	}
	
	public float getRayon () {
		return diametre/2;
	}
	
	public boolean isBlockedX (){
		return ((x-diametre/2)<2 || (x+diametre/2)>3000);
	}
	
	public boolean isBlockedY (){
		return ((y-diametre/2)<2 || (y+diametre/2)>2100);
	}
	
	public boolean getInRobot (){
		return inRobot;
	}
	
	public void setInRobot (boolean inRobot){
		this.inRobot = inRobot;
	}
	
	public int getNombre (){
		return nombre;
	}
	
	public boolean getinPile (){
		return nombre > 1;
	}
	
	public void setInPile (Pion pile){
		utile = false;
		pile.incrPile();
	}
	
	public void incrPile (){
		nombre ++;
	}
	
	public boolean getUtile (){
		return utile;
	}
}
