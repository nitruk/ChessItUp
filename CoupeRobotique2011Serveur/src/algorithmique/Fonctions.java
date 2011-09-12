package algorithmique;

import java.util.ArrayList;

import graphiqueObjets.TableJeu;
import objets.Pion;
import objets.Robot;

public class Fonctions {
	
	private TableJeu table;
	private Robot bleu, rouge;
	private boolean mouvement[] = {false, false};
	private int compteurBleu = 0;
	private int compteurRouge = 0;
	private Pion[] tabPions;
	private ArrayList<ArrayList<Pion>> pionsInRobot;
	
	public Fonctions (TableJeu table) {
		this.table = table;
		this.bleu = table.getRobotBleu();
		this.rouge = table.getRobotRouge();
		this.tabPions = table.getTabPions();
		this.pionsInRobot = table.getPionsInRobot();
	}

	public void move (Robot robot, int distance) throws ExceptionWaitUntilEnd{
		int couleur = robot.getColor()?0:1;
		mouvement[couleur]=true;
		float xFinal = (float) (robot.getX() + distance*Math.cos(robot.getAngle()*Math.PI/180));
		float yFinal = (float) (robot.getY() + distance*Math.sin(robot.getAngle()*Math.PI/180));
		movePasAPas(robot, xFinal, yFinal);
	}
		
	public void movePasAPas(Robot robot, float xFinal, float yFinal) throws ExceptionWaitUntilEnd {
		//pousser pions
		int couleur = robot.getColor()?0:1;
		int cote = robot.getCote();
		float x0 = robot.getX(), y0 = robot.getY();//, angleRobot = robot.getAngle() % 360;
		boolean robotBloqueX = false, robotBloqueY = false; //vrai si le robot est bloque par au moins un pion
		boolean robotBloquePionX = false, robotBloquePionY = false;//vrai si le robot est bloque par le pion considéré
		boolean pion1BloqueX, pion1BloqueY, pion2BloqueX, pion2BloqueY;
		double x1, y1, x2, y2;
		float newX, newY;
		double distMin, distCentres, rayon = table.getTabPions()[0].getRayon();
		for (Pion pion : tabPions) {
			if (!(pion.getInRobot(true) || pion.getInRobot(false))) {
				robotBloquePionX = false;
				robotBloquePionY = false;
				x1 = pion.getX();
				y1 = pion.getY();
				distCentres = Math.sqrt(Math.pow((x1-x0),2)+Math.pow((y1-y0),2));
				distMin = distCentres - rayon - cote/2;
				//Si le robot touche le pion
				if(distMin<2) {
					robotBloquePionX = pion.isBlockedX();
					robotBloquePionY = pion.isBlockedY();
					if (!robotBloquePionX){
						pion.setX((float)(pion.getX() + 2*(x1-x0)/(rayon+cote/2)));
						if(distMin<0) {//il faut parfois ratraper un "retard" du au fait qu'on soit sur un bord et que le pion avance donc moins vite que le robot
							pion.setX((float)(pion.getX() + 3*(x1-x0)/(rayon+cote/2)));
						}
						if(distMin<-1) { //il faut parfois ratraper un "retard" du au fait qu'on soit sur un bord et que le pion avance donc moins vite que le robot
							pion.setX((float)(pion.getX() + 4*(x1-x0)/(rayon+cote/2)));
						}
					}
					if (!robotBloquePionY){
						pion.setY((float)(pion.getY() + 2*(y1-y0)/(rayon+cote/2)));
						if(distMin<0) //il faut parfois ratraper un "retard" du au fait qu'on soit sur un bord et que le pion avance donc moins vite que le robot
							pion.setY((float)(pion.getY() + 2*(y1-y0)/(rayon+cote/2)));
					}
				}
				for (Pion pion2 : tabPions) {
					if (!(pion.getInRobot(true) || pion.getInRobot(false))) {
						x2 = pion2.getX();
						y2 = pion2.getY();
						distCentres = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
						distMin = distCentres - 2*rayon;
						//Si les deux pions se touchent
						if(pion.getIndex()!=pion2.getIndex() && distMin<5 && distCentres>rayon/10) {
							pion1BloqueX = pion.isBlockedX();
							pion1BloqueY = pion.isBlockedY();
							pion2BloqueX = pion2.isBlockedX();
							pion2BloqueY = pion2.isBlockedY();
							/*if (!pion1BloqueX){
								pion.setX((float)(pion.getX() + 2*(x2-x1)/(2*rayon)));
								if(distMin<0)
									pion.setX((float)(pion.getX() + 2*(x2-x1)/(2*rayon)));
							}
							if (!pion1BloqueY){
								pion.setY((float)(pion.getY() + 2*(y2-y1)/(2*rayon)));
								if(distMin<0)
									pion.setY((float)(pion.getY() + 2*(y2-y1)/(2*rayon)));
							}*/
							if (!pion2BloqueX){
								pion2.setX((float)(pion2.getX() + 2*(x2-x1)/(2*rayon)));
								if(distMin<0)
									pion2.setX((float)(pion2.getX() + 2*(x2-x1)/(2*rayon)));
							}
							if (!pion2BloqueY){
								pion2.setY((float)(pion2.getY() + 2*(y2-y1)/(2*rayon)));
								if(distMin<0)
									pion2.setY((float)(pion2.getY() + 2*(y2-y1)/(2*rayon)));
							}
						}
					}
				}
				robotBloqueX = robotBloqueX || robotBloquePionX;
				robotBloqueY = robotBloqueY || robotBloquePionY;
			}
		}
		//Si rien ne l'en empèche, le robot avance
		if((Math.abs(robot.getX()-xFinal)>0.1 && !robotBloqueX) || (Math.abs(robot.getY()-yFinal)>0.1 && !robotBloqueY)) {
			if(Math.abs(robot.getX()-xFinal)>0.1 && !robotBloqueX) {
				newX = (float)(robot.getX() + 2*Math.cos(robot.getAngle()*Math.PI/180));
				robot.setX(newX);
				for(Pion pionInRobot : pionsInRobot.get(couleur))
					pionInRobot.setX(newX);
			}
			if(Math.abs(robot.getY()-yFinal)>0.1 && !robotBloqueY) {
				newY = (float)(robot.getY() + 2*Math.sin(robot.getAngle()*Math.PI/180));
				robot.setY(newY);
				for(Pion pionInRobot : pionsInRobot.get(couleur))
					pionInRobot.setY(newY);
			}
			table.repaint();
			ThreadAttenteMove attente = new ThreadAttenteMove (this, robot, xFinal, yFinal);
            attente.start();
		}
		else {
			throw new ExceptionWaitUntilEnd(robot.getColor());
		}
	}

	public void turn (Robot robot, int angle) throws ExceptionWaitUntilEnd{
		int couleur = robot.getColor()?0:1;
		mouvement[couleur]=true;
		int angleFinal = robot.getAngle()+angle;
		turnPasAPas(robot, angleFinal);
	}
	
	public void turnPasAPas(Robot robot, int angleFinal) throws ExceptionWaitUntilEnd {
		if(robot.getAngle() != angleFinal) {
			if(robot.getAngle() < angleFinal)
				robot.setAngle(robot.getAngle()+1);
			else
				robot.setAngle(robot.getAngle()-1);
			table.repaint();
			ThreadAttenteTurn attente = new ThreadAttenteTurn (this, robot, angleFinal);
            attente.start();
		}
		else {
			throw new ExceptionWaitUntilEnd(robot.getColor());
		}
	}
	
	public void catchPawn (Robot robot) throws ExceptionWaitUntilEnd{
		//si le robot n'est pas plein et si un pion est suffisament proche, on le prend
		int couleur = robot.getColor()?0:1;
		int remplissage = pionsInRobot.get(couleur).size();
		int distanceMin = 180;
		int cote = robot.getCote();
		Pion pionAAttraper = tabPions[0];
		float pionAAttraperX, pionAAttraperY;
		double distanceIntermediaire, distance = 10000;
		double avantRobotX = robot.getX()+cote/2*Math.cos(Math.toRadians(robot.getAngle())), avantRobotY = robot.getY()+cote/2*Math.sin(Math.toRadians(robot.getAngle()));
		ArrayList<Pion> pionsProches = new ArrayList<Pion>();
		ArrayList<Pion> listePionsAAttraper = new ArrayList<Pion>();
		double distCentres, rayon = table.getTabPions()[0].getRayon();
		float pionX, pionY;
		//on regarde les pions proches
		for(Pion pion : tabPions) {
			if (!(pion.getInRobot(true) || pion.getInRobot(false))) {
				distanceIntermediaire = Math.sqrt(Math.pow((pion.getX()-avantRobotX),2)+Math.pow((pion.getY()-avantRobotY),2));
				if(distanceIntermediaire<distance){
					distance = distanceIntermediaire;
					pionAAttraper = pion;
				}
				if(distanceIntermediaire<distanceMin)
					pionsProches.add(pion);
			}
		}
		if(distance<distanceMin && (remplissage==0||pionAAttraper.getFigure()==0)) {
			listePionsAAttraper.add(pionAAttraper);
			pionsProches.remove(pionAAttraper);
			if (pionAAttraper.getInPile() && (remplissage==0||pionAAttraper.getInPileFigure()==0)){
				pionAAttraperX = pionAAttraper.getX();
				pionAAttraperY = pionAAttraper.getY();
				for(Pion pion : pionsProches) {
					pionX = pion.getX();
					pionY = pion.getY();
					distCentres = Math.sqrt(Math.pow((pionX-pionAAttraperX),2)+Math.pow((pionY-pionAAttraperY),2));
					//Si les pions sont dans la même pile
					if(distCentres<rayon/10)
						listePionsAAttraper.add(pion);
				}
			}
			for (Pion pion1 : listePionsAAttraper) {
				pionsInRobot.get(couleur).add(pion1);
				pion1.setX(robot.getX());
				pion1.setY(robot.getY());
				pion1.setInRobot(true, robot.getColor());
				System.out.println("Capture du pion : " + pionsInRobot.get(couleur).size());
			}
			if (remplissage>0 || listePionsAAttraper.size()>1) {
				int figureInPile = 0;
				for (Pion pion : pionsInRobot.get(couleur))
					figureInPile = Math.max(figureInPile, pion.getFigure());
				for (Pion pion : pionsInRobot.get(couleur))
					pion.setInPile(pionsInRobot.get(couleur).size(), figureInPile);
			}
		}
		else {
			System.out.println(distance);
			System.out.println("Echec de la prise de pion !");
		}
		ThreadAttenteCatchPawn attente = new ThreadAttenteCatchPawn (this, robot);
		attente.start();
	}

	public void catchPawnPasAPas(Robot robot) throws ExceptionWaitUntilEnd {
		throw new ExceptionWaitUntilEnd(robot.getColor());
	}
	
	public void releasePawn (Robot robot) throws ExceptionWaitUntilEnd{
		int couleur = robot.getColor()?0:1;
		int remplissage = pionsInRobot.get(couleur).size();
		int cote = robot.getCote();
		double rayon = table.getTabPions()[0].getRayon();
		if (remplissage>0) {
			float pionX = (float) (robot.getX()+(cote/2+rayon)*1.1*Math.cos(Math.toRadians(robot.getAngle()))), pionY = (float) (robot.getY()+(cote/2+rayon)*1.1*Math.sin(Math.toRadians(robot.getAngle())));
			for (Pion pion : pionsInRobot.get(couleur)) {
				pion.setX(pionX);
				pion.setY(pionY);
				pion.setInRobot(false, robot.getColor());
			}
			pionsInRobot.get(couleur).clear();
		}
		ThreadAttenteReleasePawn attente = new ThreadAttenteReleasePawn (this, robot);
		attente.start();
	}

	public void releasePawnPasAPas(Robot robot) throws ExceptionWaitUntilEnd {
		throw new ExceptionWaitUntilEnd(robot.getColor());
	}
	
	public void coupSuivant(boolean couleur) {
		if(couleur) {
			switch (compteurBleu) {
			case 0 :
				try {
					move(bleu, 1200);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 1 :
				try {
					turn(bleu, 85);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 2 :
				try {
					move(bleu, 800);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 3 :
				try {
					turn(bleu, 50);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 4 :
				try {
					move(bleu, 150);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 5 :
				try {
					turn(bleu, 100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 6 :
				try {
					move(bleu, 100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 7 :
				try {
					move(bleu, 200);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 8 :
				try {
					turn(bleu, 160);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 9 :
				try {
					move(bleu, 300);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 10 :
				try {
					turn(bleu, 45);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 11 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 12 :
				try {
					turn(bleu, 160);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 13 :
				try {
					move(bleu, 200);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 14 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 15 :
				try {
					move(bleu,100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 16 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 17 :
				try {
					turn(bleu,20);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 18 :
				try {
					move(bleu,200);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 19 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 20 :
				try {
					move(bleu,100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 21 :
				try {
					releasePawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 22 :
				try {
					move(bleu,100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 23 :
				try {
					turn(bleu,90);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 24 :
				try {
					move(bleu,300);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 25 :
				try {
					turn(bleu,-150);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 26 :
				try {
					move(bleu,500);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 27 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 28 :
				try {
					move(bleu,200);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 29 :
				try {
					turn(bleu,-90);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 30 :
				try {
					releasePawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 31 :
				try {
					move(bleu,300);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 32 :
				try {
					turn(bleu,-100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 33 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 34 :
				try {
					move(bleu,1000);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 35 :
				try {
					catchPawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 36 :
				try {
					turn(bleu,100);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 37 :
				try {
					move(bleu,800);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 38 :
				try {
					turn(bleu,-30);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 39 :
				try {
					move(bleu,400);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 40 :
				try {
					releasePawn(bleu);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 41 :
				try {
					move(bleu,10);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			}
			compteurBleu++;
			//System.out.println("x = " + bleu.getX() + " | y = " + bleu.getY() + " | angle = " + bleu.getAngle());
			//Faire des boucles et des petits angles pour régler le pb de la précision de l'arret du robot
			//changer le test d'arret pour move, car on sait  qu'on avance de 2 en 2 en PasAPas et qu'il faut
			//aller jusqu'à distance => il suffit de compter le nb de "2"
		}
		else {
			switch (compteurRouge) {
			case 0 :
				try {
					move(rouge, 400);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 1 :
				try {
					turn(rouge, -90);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 2 :
				try {
					move(rouge, 800);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 3 :
				try {
					turn(rouge, 50);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 4 :
				try {
					move(rouge, 150);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 5 :
				try {
					turn(rouge, 90);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 6 :
				try {
					move(rouge, 400);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 7 :
				try {
					catchPawn(rouge);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 8 :
				try {
					turn(rouge, 60);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			case 9 :
				try {
					releasePawn(rouge);
				} catch (ExceptionWaitUntilEnd e) {}
				break;
			}
			System.out.println(compteurRouge);
			compteurRouge++;
		}
	}
}
