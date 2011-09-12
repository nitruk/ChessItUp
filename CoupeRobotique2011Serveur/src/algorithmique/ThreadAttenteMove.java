package algorithmique;

import objets.Robot;

public class ThreadAttenteMove extends Thread{
	 
	private Fonctions fonctions;
	Robot robot;
	float xFinal, yFinal;
	boolean couleur;
	int vitesse = 13; // vitesse du move, de 0 à 20

	public ThreadAttenteMove(Fonctions fonctions, Robot robot, float xFinal, float yFinal) {
		this.fonctions = fonctions;
		this.robot = robot;
		this.couleur = robot.getColor();
		this.xFinal = xFinal;
		this.yFinal = yFinal;
	}

	public void run() {
		try {
			Thread.sleep(21-vitesse);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			fonctions.movePasAPas(robot,xFinal,yFinal);
		} catch (ExceptionWaitUntilEnd e) {
			fonctions.coupSuivant(couleur);
		}
	}
}
