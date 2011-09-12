package graphiqueObjets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JPanel;

import algorithmique.Fonctions;

import objets.*;

public class TableJeu extends JPanel {
	static final long serialVersionUID = 1;
	Color couleurFond = Color.LIGHT_GRAY;
	private Fonctions fonctions;
	int nbCases = 38;
	private Case tabCases[] = new Case[nbCases];
	private ZoneVerte zone1, zone2;
	int nbPions = 19;
	private Pion tabPions[] = new Pion[nbPions];
	private ArrayList<ArrayList<Pion>> pionsInRobot= new ArrayList<ArrayList<Pion>>();
	private ArrayList<Pion> pionsInRobotBleu = new ArrayList<Pion>();
	private ArrayList<Pion> pionsInRobotRouge = new ArrayList<Pion>();
	Robot robotBleu, robotRouge;
	int nbMurs = 6;
	private Mur tabMurs[] = new Mur[nbMurs];
	
	public TableJeu () {
		//Dimentions de la vraie table : 3000x2100
		//Rapport 3 entre la table réelle et la table virtuelle
		setPreferredSize(new Dimension (3000/3,2100/3));
		setBackground(couleurFond);
		ajoutZonesVertes();
	    ajoutCases();
	    ajoutPions();
	    ajoutRobots();
	    ajoutMurs();
	    this.repaint();

	    pionsInRobot.add(pionsInRobotBleu);
	    pionsInRobot.add(pionsInRobotRouge);
	    fonctions = new Fonctions(this);
	}
	public void ajoutZonesVertes () {
		zone1 = new ZoneVerte(0,400);
		zone2 = new ZoneVerte(2600,400);
	}
	public void ajoutCases () {
		int petitCote = 350;
		int grandCote = 400;
		//Cases départ
		tabCases[36] = new Case(200,200, grandCote, true);//Blue
		tabCases[37] = new Case(2800,200, grandCote, false);//Rouge
		//Damier
		boolean blue=true;
		boolean debutLigne=true;
		for(int i=0;i<=5;i++) {
			debutLigne=true;
			for(int j=0;j<=5;j++) {
				if (blue && !debutLigne) {
					blue=false;
				}
				else if (!debutLigne) {
					blue=true;
				}
				tabCases[6*i+j] = new Case(450+350/2+350*j,350/2+350*i, petitCote, blue);
				debutLigne=false;
			}
		}
		//Bonus
		int[] casesBonus = {7,10,19,22,32,33};
		for (int numeroCase : casesBonus)
			tabCases[numeroCase].setBonus(true);
	}
	public void ajoutPions () {
		//zones vertes
		int tabRand[] = {0,2,0,0,1};
		for (int i=0;i<=4;i++) {
			tabPions[i] = new Pion (200,690+280*i,tabRand[i]);
			tabPions[i+5] = new Pion (2800,690+280*i,tabRand[i]);
		}
		//zone centrale
		//TODO delete
		/*for(int i=10;i<19;i++) {
			tabPions[i] = new Pion (2500,1500,0);
		}*/

		//tabPions[10] = new Pion (400+50+350*2-100,350*2+100,0);
		//tabPions[18] = new Pion (400+50+350*5/2-100,350*5/2+100,0);
		//tabPions[17] = new Pion (400+50+350*7/2,350*5/2,0);

		tabPions[10] = new Pion (400+50+350*5/2,350*1/2,0);
		tabPions[11] = new Pion (400+50+350*5/2,350*3/2,0);
		tabPions[12] = new Pion (400+50+350*1-50,350*1/2,0);
		tabPions[13] = new Pion (400+50+350*2,350*1,0);
		tabPions[14] = new Pion (400+50+350*3/2,350*1/2,0);
		tabPions[15] = new Pion (400+50+350*3/2,3/2*350,0);
		tabPions[16] = new Pion (400+50+350*3,350*3,1);
		tabPions[17] = new Pion (400+50+2*350,2*350,0);
		tabPions[18] = new Pion (3000-50-400-2*350,2*350,0);
	}
	public void ajoutRobots () {
		robotBleu = new Robot(200, 200, 0, true);
		robotRouge = new Robot(2800, 200, 180, false);
	}
	public void ajoutMurs () {
		tabMurs[0] = new Mur(0,0,3000,0);
		tabMurs[1] = new Mur(3000,0,3000,2100);
		tabMurs[2] = new Mur(0,2100,3000,2100);
		tabMurs[3] = new Mur(0,0,0,2100);
		tabMurs[4] = new Mur(0,400,400,400);
		tabMurs[5] = new Mur(2600,400,3000,400);
	}
	public Robot getRobotBleu () {
		return robotBleu;
	}
	public Robot getRobotRouge() {
		return robotRouge;
	}
	public Pion[] getTabPions() {
		return tabPions;
	}
	public ArrayList<ArrayList<Pion>> getPionsInRobot() {
		return pionsInRobot;
	}
	public Fonctions getFonctions() {
		return fonctions;
	}
	//cette methode affiche les objets graphiques 
	public void paintComponent(Graphics g) {
		int tailleMursGros = 10;
		super.paintComponent(g);
		//Zones vertes
		zone1.dessiner(g);
		zone2.dessiner(g);
		//Cases
		for (Case carre: tabCases) {
			carre.dessiner(g);
		}
		//Robots
		robotBleu.dessiner(g);
		robotRouge.dessiner(g);
		//Pions
		for (Pion pion: tabPions) {
			pion.dessiner(g);
		}
		//Murs
		Graphics2D g2D = (Graphics2D) g;
		Stroke s = g2D.getStroke();
		g2D.setStroke(new BasicStroke(tailleMursGros));
		for (Mur mur : tabMurs) {
			mur.dessiner(g);
		}
		g2D.setStroke(s);
	}
}
