package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class Joueur extends Thread{

	int joueur;
	BufferedReader bufR;
	PrintStream streamW;
	
	public Joueur (int numJoueur, BufferedReader bufR, PrintStream streamW){
		this.joueur = numJoueur;
		this.bufR = bufR;
		this.streamW = streamW;
	}
	
	public void run() {
		long start = System.currentTimeMillis();
		String received, command;

		// boucle tant que la durée de vie du Thread est < à 90 secondes
		try {
			while( System.currentTimeMillis() < ( start + (1000 * 90))) {
				received = bufR.readLine();
				StringTokenizer mots = new StringTokenizer(received);
				command = mots.nextToken();
				if(command.compareTo("Move")==0)
					move(joueur, Integer.parseInt(mots.nextToken()));
				else if (command.compareTo("Turn")==0)
					turn(joueur, Integer.parseInt(mots.nextToken()));
			}
			bufR.close();
			streamW.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (NullPointerException e) {
			System.out.println("fin de connexion du joueur " + joueur);
		}
	}
	public static void move (int player, int distance) {
		System.out.print("Received form ");
		System.out.print(player);
		System.out.print(" : move ");
		System.out.println(distance);
	}
	public static void turn (int player, int angle) {
		System.out.print("Received form ");
		System.out.print(player);
		System.out.print(" : turn ");
		System.out.println(angle);
	}
}
