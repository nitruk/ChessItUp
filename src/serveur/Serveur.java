package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Serveur {

	public static void main(String args[]) {
		System.out.println("-- SERVEUR --\n");
		int port = 4343;
		try {
			PrintStream streamW[] = new PrintStream[2];
			BufferedReader bufR[] = new BufferedReader[2];
			ObjectOutputStream objectW[] = new ObjectOutputStream[2];
			ServerSocket ecoute = new ServerSocket(port);
			String name[] = new String[2];
			Joueur joueur[] = new Joueur[2];
			Jeu jeu;
			while (true) {
				for(int i=0;i<2;i++) {
					System.out.println("Waiting for connection with player " + (i+1));
					Socket echange = ecoute.accept();
					System.out.println("Connected with player " + (i+1));
					//ecriture
					streamW[i] = new PrintStream(echange.getOutputStream());
					//lecture
					bufR[i] = new BufferedReader(new InputStreamReader(echange.getInputStream()));
					objectW[i] = new ObjectOutputStream(echange.getOutputStream());
					name[i] = bufR[i].readLine();
					joueur[i]= new Joueur(i, name[i], bufR[i], streamW[i], objectW[i]);
					System.out.println("Player " + (i+1) + " (" + name[i] + ") connected");
					
				}
				try {
					jeu = new Jeu(joueur);
					for (int i = 0 ; i < 2 ; i ++) joueur[i].start(jeu);
					System.out.println("3");
					Thread.sleep(1000);
					System.out.println("2");
					Thread.sleep(1000);
					System.out.println("1");
					Thread.sleep(1000);
					System.out.println("Go !");
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
