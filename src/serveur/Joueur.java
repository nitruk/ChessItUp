package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class Joueur extends Thread{

	int n;
	String name;
	BufferedReader bufR;
	PrintStream streamW;
	ObjectOutputStream objectW;
	Jeu jeu;
	boolean fini = false;
	
// Gère la connexion avec un client.
	
	public Joueur (int n, String name, BufferedReader bufR, PrintStream streamW, ObjectOutputStream objectW){
		this.n = n;
		this.name = name;
		this.bufR = bufR;
		this.streamW = streamW;
		this.objectW = objectW;
	}
	
// Attribution d'un Jeu, et départ
	
	public void start(Jeu jeu) {
		this.jeu = jeu;
		start();
	}
	
	public void run() {
		StringTokenizer mots;
		String received, command;
		try {

// Prévient le client qu'il peut initialiser le décompte.

	 		streamW.println("go");

// Tourne jusqu'à ce que jeu déclare la partie finie et reccueille les requêtes

			while(!fini) {
				received = bufR.readLine();
				mots = new StringTokenizer(received);
				command = mots.nextToken();
				if(command.compareTo("move")==0)
					jeu.move(n, Integer.parseInt(mots.nextToken()));
				else if (command.compareTo("turn")==0)
					jeu.turn(n, Integer.parseInt(mots.nextToken()));
				else if (command.compareTo("catch")==0)
					jeu.catchPion(n);
				else if (command.compareTo("release")==0)
					jeu.releasePion(n);
				else System.out.println("Unknown command from " + name + " : " + command);
			}
			System.out.println("Fin de la partie !");
			streamW.println("end");
			bufR.close();
			streamW.close();
			objectW.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (NullPointerException e) {
			System.out.println("Connection lost with : " + name);
		}
		
	}
	
	public String getPlayerName() {
		return name;
	}
	
	public void write (String s) {
		streamW.println(s);
	}
	
	public void write (int n) {
		streamW.println(n);
	}

// Transmission d'objets sérialisés pour initialiser la partie

	public void writeObject(Object o) {
		try {
			objectW.writeObject(o);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

// Transmission des scores.

	public void end (int score0, int score1) {
		write("end");
		write(score0);
		write(score1);
		fini = true;
	}
}
