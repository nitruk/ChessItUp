package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import objets.Pion;
import objets.Robot;

public class Strategie extends Thread implements KeyListener {

	private Robot robots[];
	private Pion pions[];
	private Communication com;
	
	private int cmd;
	
	public Strategie (Robot robots[], Pion pions[], Communication com) {
		this.robots = robots;
		this.pions = pions;
		this.com = com;
	}
	
	public void run () {
		try {
			sleep(3000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (cmd) {
			case 1 : 	com.move(Robot.moveStep);
						break;
			case 2 : 	com.move(-Robot.moveStep);
						break;
			case 3 : 	com.turn(Robot.turnStep);
						break;
			case 4 : 	com.turn(-Robot.turnStep);
						break;
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			cmd = 1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN){
			cmd = 2;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			cmd = 3;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			cmd = 4;
		}
		else if (e.getKeyCode() == KeyEvent.VK_Z) {
			cmd = 1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S) {
			cmd = 2;
		}
		else if (e.getKeyCode() == KeyEvent.VK_D) {
			cmd = 3;
		}
		else if (e.getKeyCode() == KeyEvent.VK_Q) {
			cmd = 4;
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE){
			com.catchPawn();
		}
		else if (e.getKeyCode() == KeyEvent.VK_CONTROL){
			com.releasePawn();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() != KeyEvent.VK_SPACE && e.getKeyCode() != KeyEvent.VK_CONTROL) {
			cmd = 0;
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}
