package objets;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Pion {

	private float x, y;
	private int diametre = 180/3;
	private Color couleur;
	private int figure = 0; //0 = pion ; 1 = reine ; 2 = roi;
	private int inPileFigure = 0, inPileNombre = 0;
	static int nbPions = 0;
	private int index;
	private boolean inRobotBleu = false, inRobotRouge = false, inPile = false;
	
	public Pion(float x, float y, int figure) {
		this.x = x/3;
		this.y = y/3;
		this.figure = figure;
		couleur = (figure!=0) ? Color.ORANGE : Color.YELLOW;
		nbPions ++;
		index = nbPions-1;
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
	public int getFigure() {
		return figure; //0 = pion ; 1 = reine ; 2 = roi;
	}
	public void setFigure(int figure) {
		this.figure = figure;
	}
	public float getIndex() {
		return index;
	}
	public float getRayon () {
		return diametre/2*3;
	}
	public boolean isBlockedX (){
		return (3*(x-diametre/2)<2 || 3*(x+diametre/2)>3000);
	}
	public boolean isBlockedY (){
		return (3*(y-diametre/2)<2 || 3*(y+diametre/2)>2100);
	}
	public boolean getInRobot (boolean bleu){
		if (bleu)
			return inRobotBleu;
		return inRobotRouge;
	}
	public void setInRobot (boolean inRobot, boolean bleu){
		if (bleu)
			this.inRobotBleu = inRobot;
		else
			this.inRobotRouge = inRobot;
	}
	public int getInPileNombre (){
		return inPileNombre;
	}
	public int getInPileFigure (){
		return inPileFigure;
	}
	public boolean getInPile (){
		return inPile;
	}
	public void setInPile (int nombre, int figure){
		inPile = true;
		this.inPileNombre = nombre;
		this.inPileFigure = figure;
		couleur = Color.ORANGE;
	}
	
	public void dessiner (Graphics g) {
		g.setColor(couleur);
		g.fillOval((int) (x-diametre/2), (int) (y-diametre/2), diametre, diametre);
		if (inPile) {
			if (inPileFigure == 0) {
				g.setColor(Color.BLACK);
				Font gOld = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString(""+inPileNombre, (int) (x-10), (int) (y+5));
				g.setFont(gOld);	
			}
			else if (inPileFigure == 1){
				g.setColor(Color.BLACK);
				Font gOld = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString(inPileNombre + " Q", (int) (x-20), (int) (y+5));
				g.setFont(gOld);
			}
			else if (inPileFigure == 2){
				g.setColor(Color.BLACK);
				Font gOld = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString(inPileNombre + " K", (int) (x-20), (int) (y+5));
				g.setFont(gOld);
			}
		}
		else {
			if (figure == 1){
				g.setColor(Color.BLACK);
				Font gOld = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("Q", (int) (x-5), (int) (y+5));
				g.setFont(gOld);
			}
			else if (figure == 2){
				g.setColor(Color.BLACK);
				Font gOld = g.getFont();
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("K", (int) (x-5), (int) (y+5));
				g.setFont(gOld);
			}
		}
	}
}
