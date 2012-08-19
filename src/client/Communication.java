package client;

import graphique.PanneauInfo;
import graphique.TableJeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import objets.Pion;
import objets.Robot;

// Classe qui communique avec le serveur : Soumet les actions demandées par l'utilisateur, 
// et transmet à l'interface graphique et aux données stratégiques les mouvements effectifs.

public class Communication extends Thread {
	
	private Robot robots[];
	private Pion pions[];
	private TableJeu tj;
	private PanneauInfo pi;
	private BufferedReader bufR;
	private PrintStream streamW;
	private String status = "success";

	public Communication (Robot robots[], Pion pions[], TableJeu tj, PanneauInfo pi, BufferedReader bufR, PrintStream streamW) {
		this.robots = robots;
		this.pions = pions;
		this.tj = tj;
		this.pi = pi;
		this.bufR = bufR;
		this.streamW = streamW;
	}
	
// Décompte de 3 secondes, puis écoute du serveur
	
	public void run () {
		String message;
		Robot robot;
		Pion pion;
		int n;
		try {
			System.out.println("3");
			sleep(1000);
			System.out.println("2");
			sleep(1000);
			System.out.println("1");
			sleep(1000);
			System.out.println("Go !");
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
// Tant que la socket n'est pas morte et que la partie n'est pas finie
			while ((message = bufR.readLine()) != null && message.compareTo("end") != 0) {
				if ((message.compareTo("active") == 0) || (message.compareTo("success") == 0) || (message.compareTo("failure") == 0)) {
					status = message;
					pi.setStatus(status);
				}
				else {
// Mouvement d'un robot
					if (message.compareTo("robot") == 0) {
						robot = robots[Integer.parseInt(bufR.readLine())];
						if ((message = bufR.readLine()).compareTo("move") == 0) {
							robot.move(Integer.parseInt(bufR.readLine()), Integer.parseInt(bufR.readLine()));
							if (robot.getTenu() != 19) pions[robot.getTenu()].move(robot.getX(), robot.getY());
						}
						else if (message.compareTo("turn") == 0) {
							robot.turn(Integer.parseInt(bufR.readLine()));
						}
						else if (message.compareTo("catch") == 0) {
							pion = pions[n = Integer.parseInt(bufR.readLine())];
							if (robot.getTenu() == 19) {
								robot.setTenu(n);
								pion.setInRobot(true);
								pion.move(robot.getX(), robot.getY());
							}
							else pion.setInPile(pions[robot.getTenu()]);
						}
						else if (message.compareTo("release") == 0) {
							pions[robot.getTenu()].setInRobot(false);
							robot.setTenu(19);
						}
						else System.err.println("robot " + message);
						
					}
// Mouvement d'un pion
					else if (message.compareTo("pion") == 0) {
						pion = pions[n = Integer.parseInt(bufR.readLine())];
						pion.move(Integer.parseInt(bufR.readLine()), Integer.parseInt(bufR.readLine()));
					}
// Anomalie sur la socket
					else System.err.println(message);
					tj.repaint();
				}
			}
// En fin de partie, affichage des scores.
			pi.end(Integer.parseInt(bufR.readLine()), Integer.parseInt(bufR.readLine()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
// Récupération de l'état actuel du robot ("active", "success" ou "failure")
	
	public String getStatus () {
		return status;
	}
	
// Demandes d'action 
	
	public void move (int distance) {
		streamW.println("move " + distance);
	}
	
	public void turn (int angle) {
		streamW.println("turn " + angle);
	}
	
	public void catchPawn () {
		streamW.println("catch");
	}
	
	public void releasePawn () {
		streamW.println("release");
	}

// Demande d'action et attente de l'aboutissement
	
	public boolean moveAndWait (int distance) {
		move(distance);
		for (int i = 0 ; (i < 10) && (!status.equals("active")) ; i ++) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(status.equals("active")) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return status.equals("success");
	}

	public void turnAndWait (int angle) {
		turn(angle);
		for (int i = 0 ; (i < 10) && (!status.equals("active")) ; i ++) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(status.equals("active")) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean catchAndWait () {
		catchPawn();
		for (int i = 0 ; (i < 10) && (!status.equals("active")) ; i ++) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(status.equals("active")) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return status.equals("success");
	}

	public boolean releaseAndWait () {
		releasePawn();
		for (int i = 0 ; (i < 10) && (!status.equals("active")) ; i ++) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(status.equals("active")) {
			try {
				sleep(50) ;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return status.equals("success");
	}
	
}
