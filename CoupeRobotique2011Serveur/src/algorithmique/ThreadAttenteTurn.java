package algorithmique;

import objets.Robot;

public class ThreadAttenteTurn extends Thread{
	 
	private Fonctions fonctions;
	Robot robot;
	boolean couleur;
	int angleFinal;
	int vitesse = 15; //vitesse du turn, de 0 à 20

	public ThreadAttenteTurn(Fonctions fonctions, Robot robot, int angleFinal) {
		this.fonctions = fonctions;
		this.robot = robot;
		this.couleur = robot.getColor();
		this.angleFinal = angleFinal;
	}

	public void run() {
		try {
			Thread.sleep(21-vitesse);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			fonctions.turnPasAPas(robot,angleFinal);
		} catch (ExceptionWaitUntilEnd e) {
			fonctions.coupSuivant(couleur);
		}
	}
}
