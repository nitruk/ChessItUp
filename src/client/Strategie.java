package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import objets.Robot;

public class Strategie extends Thread implements KeyListener {

// Les objets à utiliser

	private Communication com;
	
	private int cmd;
	
	public Strategie (Communication com) {
		this.com = com;
	}
	
// Une stratégie basique consistant à observer l'utilisation du clavier. :D
	
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
			com.catchAndWait();
		}
		else if (e.getKeyCode() == KeyEvent.VK_CONTROL){
			com.releaseAndWait();
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
