package algorithmique;

import objets.Robot;

public class ThreadAttenteCatchPawn extends Thread{
	 
	private Fonctions fonctions;
	Robot robot;
	float xFinal, yFinal;
	boolean couleur;
	int attente = 1000; //attente lors de la prise d'un pion

	public ThreadAttenteCatchPawn(Fonctions fonctions, Robot robot) {
		this.fonctions = fonctions;
		this.robot = robot;
		this.couleur = robot.getColor();
	}

	public void run() {
		try {
			Thread.sleep(attente);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			fonctions.catchPawnPasAPas(robot);
		} catch (ExceptionWaitUntilEnd e) {
			fonctions.coupSuivant(couleur);
		}
	}
}
