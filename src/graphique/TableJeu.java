package graphique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

import objets.Mur;
import objets.Pion;
import objets.Robot;

public class TableJeu extends JPanel {

	static final long serialVersionUID = 1;

	public static final int mmParPixel = 3;
	public static final int coteCase = 350 / mmParPixel;
	public static final int largeurMur = 20 / mmParPixel;
	public static final int largeurBande = 400 / mmParPixel;
	public static final int largeurLigne = 50 / mmParPixel;
	public static final int longueurPlateau = 6 * coteCase;
	public static final int largeurPlateau = longueurPlateau + 2*(largeurBande + largeurLigne);
	public static final int longueurBande = longueurPlateau - largeurBande;
	public static final int diametrePoint = 150 / mmParPixel;
	public static final Color couleurMur = Color.DARK_GRAY;
	
	public static final int bonus[][] = {{1,1}, {1,3}, {4,1}, {4,3}, {2,5}, {3,5}};
	
	private Color couleurFond = Color.ORANGE;
	private final int nbRobots = 2;
	private final int nbPions = 19;
	private Robot robots[] = new Robot[nbRobots];
	private Pion pions[] = new Pion[nbPions];
	
	public TableJeu (Robot robots[], Pion pions[]) {
		//Dimentions de la vraie table : 3000x2100
		//Rapport 3 entre la table réelle et la table virtuelle
		setPreferredSize(new Dimension (largeurPlateau, longueurPlateau));
		setBackground(couleurFond);
		this.robots = robots;
		this.pions = pions;
	    this.repaint();
	}

	//cette methode affiche les objets graphiques 
	public void paintComponent(Graphics g) {
		int tailleMursGros = 10;
		super.paintComponent(g);
		dessinerPlateau(g);
		//Robots
		for (Robot robot : robots) {
			dessinerRobot(robot, g);
		}
		//Pions
		for (Pion pion: pions) {
			dessinerPion(pion, g);
		}
		//Murs
		Graphics2D g2D = (Graphics2D) g;
		Stroke s = g2D.getStroke();
		g2D.setStroke(new BasicStroke(tailleMursGros));
		g2D.setStroke(s);
	}
	
	public void dessinerPlateau (Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, largeurBande, largeurBande, longueurBande);
		g.fillRect(largeurPlateau - largeurBande, largeurBande, largeurBande, longueurBande);
		g.setColor(Color.RED);
		g.fillRect(0, 0, largeurBande, largeurBande);
		for (int i = 0 ; i < 6 ; i ++) {
			for (int j = 0 ; j < 3 ; j ++) {
				g.fillRect(largeurBande + largeurLigne + (2*j+((i+1)%2))*coteCase, i*coteCase, coteCase, coteCase);
			}
		}
		g.setColor(Color.BLUE);
		g.fillRect(largeurPlateau - largeurBande, 0, largeurBande, largeurBande);
		for (int i = 0 ; i < 6 ; i ++) {
			for (int j = 0 ; j < 3 ; j ++) {
				g.fillRect(largeurBande + largeurLigne + (2*j+(i%2))*coteCase, i*coteCase, coteCase, coteCase);
				//g.fillRect(x, y, width, height)
			}
		}
		g.setColor(Color.BLACK);
		g.fillRect(largeurBande, 0, largeurLigne, longueurPlateau);
		g.fillRect(largeurPlateau - largeurBande - largeurLigne, 0, largeurLigne, longueurPlateau);
		for (int[] point : bonus) {
			g.fillOval(largeurBande + largeurLigne  + (2*point[0]+1)*coteCase/2 - diametrePoint/2, (2*point[1]+1)*coteCase/2 - diametrePoint/2, diametrePoint, diametrePoint);
		}
		g.setColor(couleurMur);
		g.fillRect(0, 0, largeurMur, longueurPlateau);
		g.fillRect(0, 0, largeurPlateau, largeurMur);
		g.fillRect(0, longueurPlateau - largeurMur, largeurPlateau, largeurMur);
		g.fillRect(largeurPlateau - largeurMur, 0, largeurMur, longueurPlateau);
		for (Mur mur : Mur.MursH) g.fillRect((mur.getX()-Mur.longueurH)/mmParPixel, mur.getY()/mmParPixel, 2*Mur.longueurH/mmParPixel, largeurMur);
		for (Mur mur : Mur.MursV) g.fillRect(mur.getX()/mmParPixel, (mur.getY()-Mur.longueurV)/mmParPixel, largeurMur, 2*Mur.longueurV/mmParPixel);
	}
	
	public void dessinerPion (Pion pion, Graphics g) {
		if (pion.getUtile()) {
			float x = pion.getX()/mmParPixel;
			float y = pion.getY()/mmParPixel;
			int diametre = Pion.diametre/mmParPixel;
			g.setColor(pion.getCouleur());
			g.fillOval((int) (x-diametre/2), (int) (y-diametre/2), diametre, diametre);
			g.setColor(Color.BLACK);
			Font gOld = g.getFont();
			g.setFont(new Font("Arial", Font.BOLD, 20));
			String num = ((pion.getNombre() > 1) ? ("" + pion.getNombre()) : "");
			switch (pion.getFigure()) {
				case 0 : 	g.drawString(num, (int) (x-10), (int) (y+5));
							break;
				case 1 :	g.drawString(num + " Q", (int) (x-20), (int) (y+5));
							break;
				case 2 :	g.drawString(num + " K", (int) (x-20), (int) (y+5));
			}
			g.setFont(gOld);
		}
	}

	public void dessinerRobot (Robot robot, Graphics g) {
		int x = robot.getX()/mmParPixel;
		int y = robot.getY()/mmParPixel;
		int cote = Robot.cote/mmParPixel;
		int epaisseur=5;
		//Rotation
		Graphics2D g2D = (Graphics2D) g;
		Stroke s = g2D.getStroke();
		g2D.rotate(robot.getAngle()*Math.PI/180,x,y);
		//Robot
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int) Math.floor(x-cote/2), (int) Math.floor(y-cote/2), cote, cote);
		g.setColor(robot.getCouleur());
		g.fillRect((int) Math.floor(x-cote/2.5), (int) Math.floor(y-cote*3/8), (int) Math.floor(cote/10), (int) Math.floor(cote*3/4));
		g.setColor(Color.BLACK);
		g2D.setStroke(new BasicStroke(epaisseur));
		g.drawOval((int) Math.floor(x-cote/2), (int) Math.floor(y-cote/2), cote, cote);
		//Réinitialisation
		g2D.setStroke(s);
		g2D.rotate(-robot.getAngle()*Math.PI/180,x,y);
	}
}
