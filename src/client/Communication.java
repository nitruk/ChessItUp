package client;

import graphique.PanneauInfo;
import graphique.TableJeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import objets.Pion;
import objets.Robot;

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
			while ((message = bufR.readLine()) != null && message.compareTo("end") != 0) {
				if ((message.compareTo("active") == 0) || (message.compareTo("success") == 0) || (message.compareTo("failure") == 0)) {
					status = message;
					pi.setStatus(status);
				}
				else {
					if (message.compareTo("robot") == 0) {
						robot = robots[Integer.parseInt(bufR.readLine())];
						if ((message = bufR.readLine()).compareTo("move") == 0) {
							System.out.println("received move");
							robot.move(Integer.parseInt(bufR.readLine()), Integer.parseInt(bufR.readLine()));
							if (robot.getTenu() != 19) pions[robot.getTenu()].move(robot.getX(), robot.getY());
						}
						else if (message.compareTo("turn") == 0) {
							System.out.println("received turn");
							robot.turn(Integer.parseInt(bufR.readLine()));
						}
						else if (message.compareTo("catch") == 0) {
							pion = pions[n = Integer.parseInt(bufR.readLine())];
							System.out.println("received catch " + n);
							if (robot.getTenu() == 19) {
								robot.setTenu(n);
								pion.setInRobot(true);
								pion.move(robot.getX(), robot.getY());
							}
							else pion.setInPile(pions[robot.getTenu()]);
						}
						else if (message.compareTo("release") == 0) {
							System.out.println("received release");
							pions[robot.getTenu()].setInRobot(false);
							robot.setTenu(19);
						}
						else System.err.println("robot " + message);
						
					}
					else if (message.compareTo("pion") == 0) {
						int i, j;
						pion = pions[n = Integer.parseInt(bufR.readLine())];
						pion.move(i = Integer.parseInt(bufR.readLine()), j = Integer.parseInt(bufR.readLine()));
						System.out.println("received pion "  + n + " " + i + " " + j);
					}
					else System.err.println(message);
					tj.repaint();
				}
			}
			pi.end(Integer.parseInt(bufR.readLine()), Integer.parseInt(bufR.readLine()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getStatus () {
		return status;
	}
	
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
