package serveur;
import graphique.TableJeu;

import java.util.concurrent.Semaphore;

import objets.Mur;
import objets.Pion;
import objets.Robot;

public class Jeu extends Thread {

	private static int rayonRobot = Robot.cote / 2;
	private static int rayonRobot2 = rayonRobot * rayonRobot;
	private static int rayonPion = Pion.diametre / 2;
	private static int rayonPion2 = rayonPion * rayonPion;
	private static int margeRobot2 = Robot.cote * Robot.cote;
	private static int margePion = rayonRobot + rayonPion;
	private static int margePion2 = margePion * margePion;
	private static int margePionPion = 2 * rayonPion;
	private static int margePionPion2 = margePionPion * margePionPion;
	private static final int BLOCKED = -1;
	private static final int FREE = 0;
	private static final int BLOCK_X = 1;
	private static final int BLOCK_Y = 2;
	private static final int BLOCK_ROBOT = 3;
	private static final int BLOCK_WALLV = 5;
	private static final int BLOCK_WALLH = 6;
	private static final int BLOCK_PAWN = 7;
	
	private Joueur joueurs[] = new Joueur[2];
	private Robot robots[] = new Robot[2];
	private int moves[] = new  int[2], turns[] = new  int[2];
	private int r;
	private Pion pions[] = new Pion[19];
	private int distributions[][] = {{0,1}, {0,2}, {0,3}, {0,4},
			 {1,0}, {1,2}, {1,3}, {1,4},
			 {2,0}, {2,1}, {2,3}, {2,4},
			 {3,0}, {3,1}, {3,2}, {3,4},
			 {4,0}, {4,1}, {4,2}, {4,3}};
	private Semaphore S = new Semaphore(1);
	
	public Jeu (Joueur joueurs[]) {
		this.joueurs = joueurs;
		int figure;
		r = (int) (19 * Math.random());
		for (int j = 0 ; j < 5 ; j ++) {
			if (distributions[r][0] == j) figure = Pion.KING;
			else if (distributions[r][1] == j) figure = Pion.QUEEN;
			else figure = Pion.PAWN;
			pions[j] = new Pion(200, 690 + j*280, figure);
			pions[5+j] = new Pion(2800, 690 + j*280, figure);
		}
		pions[10] = new Pion(1500, 1050, Pion.PAWN);
		for (int i = 0 ; i <2 ; i ++) {
			r = (int) (19 * Math.random());
			for (int j = 0 ; j < 2 ; j ++) {
				pions[11+2*i+j] = new Pion(800+i*350, (1+distributions[r][j])*350, Pion.PAWN);
				pions[15+2*i+j] = new Pion(2200-i*350, (1+distributions[r][j])*350, Pion.PAWN);
			}
		}
		r = (int) (2 * Math.random());
		for (int i = 0 ; i < 2 ; i ++) {
			int j = (i + r) % 2;
			robots[i] = new Robot(joueurs[i].getPlayerName(), j*2800+(1-j)*200, 200, j*180, j==1);
		}
		Joueur joueur;
		for (int n = 0 ; n < 2 ; n ++) {
			joueur = joueurs[n];
			joueur.writeObject(robots[n]);
			joueur.writeObject(robots[1-n]);
			for (int i = 0 ; i < 19 ; i ++) {
				joueur.writeObject(pions[i]);
			}
		}
		start();
	}
	
	public void run () {
		Robot robot;
		Pion pion;
		int step, x, y, dx, dy, r, r2, retour;
		double angle;
		float  stepx, stepy, d;
		boolean libre, done;
		long start = System.currentTimeMillis();
		try {
			Thread.sleep(3000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (System.currentTimeMillis() < ( start + (1000 * 90))) {
			try {
				sleep(50) ;
				for (int n = 0 ; n < 2 ; n ++) {
					robot = robots[n];
					if (turns[n] != 0) {
						if (Math.abs(turns[n]) > Robot.turnStep) {
							step = (turns[n] > 0) ? Robot.turnStep : -Robot.turnStep;
							joueurs[n].write("active");
						}
						else {
							step = turns[n];
							joueurs[n].write("success");
						}
						turns[n] -= step;
						robot.turn(step);
						S.acquire();
						writeAll("robot");
						writeAllNumber(n);
						writeAll("turn");
						writeAll(step);
						S.release();
					}
					else if (moves[n] != 0) {
						if (Math.abs(moves[n]) >= Robot.moveStep) step = (moves[n] > 0) ? Robot.moveStep : -Robot.moveStep;
						else step = moves[n];
						libre = true;
						angle = robot.getAngle()/180.*Math.PI;
						stepx = step * (float) Math.cos(angle);
						stepy = step * (float) Math.sin(angle);
						x = robot.getX() + (int) stepx;
						y = robot.getY() + (int) stepy;
						int i = 10;
						do {
							done = true;
							if (x < rayonRobot || x > 3*TableJeu.largeurPlateau - rayonRobot) {
								done = false;
								x = robot.getX();
							}
							if (y < rayonRobot || y > 3*TableJeu.longueurPlateau - rayonRobot){
								done = false;
								y = robot.getY();
							}
							for (Mur mur : Mur.MursV) {
								if (Math.abs(mur.getX() - x) < rayonRobot) {
									d = Math.abs(y - mur.getY()) - Mur.longueurV;
									if (d < 0)
										x = robot.getX();
									else {
										dx = mur.getX() - x;
										dy = mur.getY() - Mur.longueurV - y;
										if (dx*dx + dy*dy < rayonRobot2) {
											done = false;
											d = (dx * stepy - dy * stepx)/(dx*dx + dy*dy);
											stepx = - d * dy;
											stepy = d * dx;
											dx = (int)stepx + robot.getX() - robots[1-n].getX();
											dy = (int)stepy + robot.getY() - robots[1-n].getY();
											while(dx * dx + dy*dy < rayonRobot2) {
												stepx += (x > 0) ? -1 : 1;
												stepy += (y > 0) ? -1 : 1;
												dx = (int) stepx + robot.getX() -  robots[1-n].getX();
												dy = (int) stepy + robot.getY() -  robots[1-n].getY();
											}
											x = robot.getX() + (int) stepx;
											y = robot.getY() + (int) stepy;
										}
									}
								}
							}
							for (Mur mur : Mur.MursH) {
								if (Math.abs(mur.getY() - y) < rayonRobot) {
									d = Math.abs(x - mur.getX()) - Mur.longueurH;
									if (d < 0)
										y = robot.getY();
									else {
										dx = mur.getX() + ((mur == Mur.MursH[0]) ?  Mur.longueurH : -Mur.longueurH) - x;
										dy = mur.getY() - y;
										if (dx*dx + dy*dy < rayonRobot2) {
											done = false;
											d = (dx * stepy - dy * stepx)/(dx*dx + dy*dy);
											stepx = - d * dy;
											stepy = d * dx;
											dx = (int)stepx + robot.getX() - robots[1-n].getX();
											dy = (int)stepy + robot.getY() - robots[1-n].getY();
											while(dx * dx + dy*dy < rayonRobot2) {
												stepx += (x > 0) ? -1 : 1;
												stepy += (y > 0) ? -1 : 1;
												dx = (int) stepx + robot.getX() -  robots[1-n].getX();
												dy = (int) stepy + robot.getY() -  robots[1-n].getY();
											}
											x = robot.getX() + (int) stepx;
											y = robot.getY() + (int) stepy;
										}
									}
								}
							}
							dx = robots[1-n].getX() - x;
							dy = robots[1-n].getY() - y;
							if (dx*dx + dy*dy < margeRobot2) {
								done = false;
								d = (dx * stepy - dy * stepx)/(dx*dx + dy*dy);
								stepx = - d * dy;
								stepy = d * dx;
								dx = (int)stepx + robot.getX() - robots[1-n].getX();
								dy = (int)stepy + robot.getY() - robots[1-n].getY();
								while(dx * dx + dy*dy < margeRobot2) {
									stepx += (x > 0) ? -1 : 1;
									stepy += (y > 0) ? -1 : 1;
									dx = (int) stepx + robot.getX() -  robots[1-n].getX();
									dy = (int) stepy + robot.getY() -  robots[1-n].getY();
								}
								x = robot.getX() + (int) stepx;
								y = robot.getY() + (int) stepy;
							}
						} while (!done && --i > 0);
						if ((x == robot.getX() && y == robot.getY()) || (i == 0)) libre = false;
						else {
							for (i = 0 ; i < 19 && libre ; i ++) {
								pion = pions[i];
								if (pion.getUtile() && !pion.getInRobot()) {
									dx = pion.getX() - x;
									dy = pion.getY() - y;
									r2 = dx*dx + dy*dy;
									if (r2 < margePion2) {
										r = (int) Math.sqrt(r2);
										d = (margePion - r) / (float) r;
										retour = movePion(i, (int) (pion.getX() + Math.ceil(d * dx)), (int) (pion.getY() +  Math.ceil(d * dy)));
										if (!(libre = (retour == FREE)) && retour != BLOCKED) libre = movePionMur(i, x, y, retour, rayonRobot);
									}
								}
							}
						}
						if (libre) {
							robot.move(x, y);
							if (robot.getTenu() != 19) pions[robot.getTenu()].move(robot.getX(), robot.getY());
							moves[n] -= step;
							S.acquire();
							writeAll("robot");
							writeAllNumber(n);
							writeAll("move");
							writeAll(x);
							writeAll(y);
							S.release();
							joueurs[n].write((moves[n] == 0) ? "success" : "active");
						}
						else {
							S.acquire();
							joueurs[n].write("failure");
							S.release();
						}
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		end();
	}

	public void move (int n, int distance) {
		turns[n] = 0;
		moves[n] = distance;
	}

	public void turn (int n, int angle) {
		moves[n] = 0;
		turns[n] = angle;
	}

	public void catchPion (int n) {
		turns[n] = 0;
		moves[n] = 0;
		Robot robot = robots[n];
		Pion pion;
		float x = robot.getXAvant();
		float y = robot.getYAvant() ;
		int marge = Pion.diametre/2 + 100;
		marge = marge * marge;
		float dx, dy;
		int i = 0;
		do {
			pion = pions[i];
			dx = pion.getX() - x;
			dy = pion.getY() - y;
		}
		while ((!pion.getUtile() || pion.getInRobot() || ((robot.getTenu() != 19 && (pion.getFigure() != Pion.PAWN))) || (dx*dx + dy*dy > marge)) && (++i < 19));
		if (i < 19) {
			if (robot.getTenu() == 19) {
				robot.setTenu(i);
				pion.setInRobot(true);
				pion.move(robot.getX(), robot.getY());
			}
			else pion.setInPile(pions[robot.getTenu()]);
			try {
				S.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			writeAll("robot");
			writeAllNumber(n);
			writeAll("catch");
			writeAll(i);
			joueurs[n].write("success");
			S.release();
		}
		else {
			try {
				S.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			joueurs[n].write("failure");
			S.release();
		}
	}

	public void releasePion (int n) {
		turns[n] = 0;
		moves[n] = 0;
		Robot robot = robots[n];
		int p;
		if ((p= robot.getTenu()) != 19) {
			Pion pion = pions[p];
			int x = (int) (robot.getX() + Math.cos(robot.getAngle() * Math.PI / 180) * (margePion+10));
			int y = (int) (robot.getY() + Math.sin(robot.getAngle() * Math.PI / 180) * (margePion+10));
			if (movePion(p, x, y) == FREE) {
				robot.setTenu(19);
				pion.setInRobot(false);
				try {
					S.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				writeAll("robot");
				writeAllNumber(n);
				writeAll("release");
				joueurs[n].write("success");
				S.release();
			}
			else {
				try {
					S.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				joueurs[n].write("failure");
				S.release();
			}
		}
	}

	private int movePion (int n, int x, int y) {
		int dx, dy, r, r2, retour;
		int etat = FREE;
		float d;
		Pion pion = pions[n], autre;
		if ((x < rayonPion) || (x > 3*TableJeu.largeurPlateau - rayonPion)) {
			if (y == pion.getY()) etat = BLOCKED;
			else etat = BLOCK_X;
		}
		if ((y < rayonPion) || (y > 3*TableJeu.longueurPlateau - rayonPion)) {
			if ((etat != FREE) || (x == pion.getX())) etat = BLOCKED;
			else etat = BLOCK_Y;
		}
		for (int i = 0 ; i < 2 ; i ++) {
			dx = robots[i].getX() - x;
			dy = robots[i].getY() - y;
			if (dx*dx + dy*dy < margePion2) {
				//System.out.println(robots[i].getCouleur().toString());
				if ((etat != FREE) || (dx*(y-pion.getY()) - dy*(x-pion.getX()) == 0)) etat = BLOCKED;
				else etat = BLOCK_ROBOT;
			}
		}
		for (Mur mur : Mur.MursV) {
			if (Math.abs(mur.getX() - x) < rayonPion) {
				d = Math.abs(y - mur.getY()) - Mur.longueurV;
				if (d < 0) {
					if (y == pion.getY()) etat = BLOCKED;
					else etat = BLOCK_X;
				}
				else {
					dx = mur.getX() - x;
					dy = mur.getY() - Mur.longueurV - y;
					if (dx*dx + dy*dy < rayonPion2) {
						if (x == pion.getX()) etat = BLOCKED;
						else etat = BLOCK_WALLV;
					}
				}
			}
		}
		for (Mur mur : Mur.MursH) {
			if (Math.abs(mur.getY() - y) < rayonPion) {
				d = Math.abs(x - mur.getX()) - Mur.longueurH;
				if (d < 0) {
					if (x == pion.getX()) etat = BLOCKED;
					else etat = BLOCK_Y;
				}
				else {
					dx = mur.getX() + ((mur == Mur.MursH[0]) ?  Mur.longueurH : -Mur.longueurH) - x;
					dy = mur.getY() - y;
					if (dx*dx + dy*dy < rayonPion2) {
						if (x == pion.getX()) etat = BLOCKED;
						else etat = BLOCK_WALLH;
					}
				}
			}
		}
		if (etat != FREE) return etat;
		else {
			for (int i = 0 ; i < 19 && (etat != BLOCKED) ; i ++) {
				if (n != i) {
					autre = pions[i];
					if (autre.getUtile() && !autre.getInRobot()) {
						dx = autre.getX() - x;
						dy = autre.getY() - y;
						r2 = dx*dx + dy*dy;
						if (dx*dx + dy*dy < margePionPion2) {
							r = (int) Math.sqrt(r2);
							d = (margePionPion - r) / (float) r;
							retour = movePion(i, (int) (autre.getX() + d * dx), (int) (autre.getY() +  d * dy));
							if (retour != FREE) {
								if ((retour == BLOCKED) || !movePionMur(i, x, y, retour, rayonPion)) {
									if (etat == FREE) etat = BLOCK_PAWN + i;
									else etat = BLOCKED;
								}
							}
						}
					}
				}
			}
		}
		if (etat == FREE) {
			pion.move(x, y);
			try {
				S.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			writeAll("pion");
			writeAll(n);
			writeAll(x);
			writeAll(y);
			S.release();
		}
		return etat;
	}

	private boolean movePionMur (int n, int x0, int y0, int etat, int rayon) {
		int x, y, dx, dy;
		float stepx, stepy, dX, dY, d;
		Pion pion = pions[n];
		x = pion.getX();
		y = pion.getY();
		dx = x - x0;
		dy = y - y0;
		stepx = stepy = 0;
		switch (etat) {
		case BLOCK_X :		stepx = 0;
							stepy = (dy > 0) ? 1 : -1;
							break;
		case BLOCK_Y :		stepx = (dx > 0) ? 1 : -1;
							stepy = 0;
							break;
		case BLOCK_ROBOT :
		case BLOCK_ROBOT+1 ://System.out.println(etat-BLOCK_ROBOT);
							stepx = robots[etat-BLOCK_ROBOT].getY() - y;
							stepy = x - robots[etat-BLOCK_ROBOT].getX();
							d = ((stepx*(x-x0)+stepy*(y-y0)>0) ? 1 : -1) * (float) Math.sqrt(stepx*stepx + stepy*stepy);
							stepx /= d;
							stepy /= d;
							break;
		case BLOCK_WALLV :	for (Mur mur : Mur.MursV) {
								if (Math.abs(mur.getX() - x) > rayonPion) continue;
								stepx = mur.getY() - Mur.longueurV - y;
								stepy = x - mur.getX();
								d = ((stepx*(x-x0)+stepy*(y-y0)>0) ? 1 : -1) * (float) Math.sqrt(stepx*stepx + stepy*stepy);
								stepx /= d;
								stepy /= d;
							}
							break;
		case BLOCK_WALLH :	for (Mur mur : Mur.MursH) {
								if (Math.abs(mur.getY() - y) > rayonPion) continue;
								stepx = mur.getY() - y;
								stepy = x - (mur.getX() + ((mur == Mur.MursH[0]) ?  Mur.longueurH : -Mur.longueurH));
								d = ((stepx*(x-x0)+stepy*(y-y0)>0) ? 1 : -1) * (float) Math.sqrt(stepx*stepx + stepy*stepy);
								stepx /= d;
								stepy /= d;
							}
							break;
		default :			stepx = pions[etat - BLOCK_PAWN].getY() - y;
							stepy = x - pions[etat - BLOCK_PAWN].getX();
							d = ((stepx*(x-x0)+stepy*(y-y0)>0) ? 1 : -1) * (float) Math.sqrt(stepx*stepx + stepy*stepy);
							stepx /= d;
							stepy /= d;
		}
		dX = Math.abs(dx * stepy - dy * stepx);
		dY = Math.abs(dx * stepx + dy * stepy);
		d = rayon + rayonPion;
		d = (float) Math.sqrt(d*d - dX*dX) - dY;
		x += d * stepx;
		y += d * stepy;
		return movePion(n, x, y) == FREE;
	}
	
	public void writeAll (String s) {
		for (int n = 0 ; n < 2 ; n ++) joueurs[n].write(s);
	}
	
	public void writeAll (int i) {
		for (int n = 0 ; n < 2 ; n ++) joueurs[n].write(i);
	}
	
	public void writeAllNumber (int i) {
		for (int n = 0 ; n < 2 ; n ++) joueurs[n].write((n + i) % 2);
	}
	
	public void end () {
		int x, y, valeur;
		int points[] = new int[2];
		for (Pion pion : pions) {
			if (pion.getUtile() && !pion.getInRobot() && (x = pion.getX() - 447) > 0 && x < 2088 && x%348 > 80 && x%348 < 268 && (y=pion.getY())%348 > 80 && y%348 < 268) {
				System.out.println("-----------------------------------------------");
				System.out.println(x + " " + y);
				valeur = pion.getFigure() * 10;
				System.out.println(valeur);
				if (valeur > 10) valeur *= Math.min(pion.getNombre(), 3);
				System.out.println(valeur);
				x /= 348;
				y /= 348;
				System.out.println(x + " " + y);
				if ((((x==1)||(x==4))&&((y==1)||(y==3)))||(((x==2)||(x==3))&&(y==5))) valeur += 30;
				System.out.println(valeur);
				points[(1-((x + y) % 2)+r) % 2] +=  valeur;
				System.out.println((1-((x + y) % 2)+r) % 2);
			}
		}
		for (int i = 0 ; i < 2 ; i ++) {
			System.out.println(joueurs[i].getPlayerName() + " : " + points[i]);
			joueurs[i].end(points[i], points[1-i]);
		}
	}
}
