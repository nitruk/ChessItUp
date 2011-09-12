package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Serveur {

	public Serveur() {
		System.out.println("-- SERVEUR --\n");
		reception();
	}

	public static void reception() {
		int port[] = {40000, 40001, 40002};
		try {
			PrintStream streamW[] = new PrintStream[2];
			InputStreamReader streamR[] = new InputStreamReader[2];
			BufferedReader bufR[] = new BufferedReader[2];
			for(int i=0;i<=1;i++) {
				ServerSocket ecoute = new ServerSocket(port[0]);
				System.out.println("Waiting for connexion");
				Socket echange = ecoute.accept();
				System.out.print("Redirecting client to port ");
				System.out.println(port[i+1]);
				//ecriture
				streamW[i] = new PrintStream(echange.getOutputStream());
				//lecture
				streamR[i] = new InputStreamReader(echange.getInputStream());
				bufR[i] = new BufferedReader(streamR[i]);
				streamW[i].println(port[i+1]);
				bufR[i].close();
				streamR[i].close();
				streamW[i].close();
				ecoute.close();
				echange.close();
				
				ecoute = new ServerSocket(port[i+1]);
				System.out.println("Waiting for connexion");
				echange = ecoute.accept();
				//ecriture
				streamW[i] = new PrintStream(echange.getOutputStream());
				//lecture
				streamR[i] = new InputStreamReader(echange.getInputStream());
				bufR[i] = new BufferedReader(streamR[i]);
				streamW[i].println("New Player");				
				System.out.println("Sent New Player");
			}
			if((bufR[0].readLine().compareTo("Ready")==0)&&(bufR[1].readLine().compareTo("Ready")==0)) {
				try {
					System.out.println("3");
					Thread.sleep(1000);
					System.out.println("2");
					Thread.sleep(1000);
					System.out.println("1");
					Thread.sleep(1000);
					System.out.println("Go !");
		 			streamW[0].println("Go");
					streamW[1].println("Go");
					Joueur joueur0 = new Joueur(0, bufR[0], streamW[0]);
					Joueur joueur1 = new Joueur(1, bufR[1], streamW[1]);
					joueur0.start();
					joueur1.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException ex) {
		ex.printStackTrace();
		}
		catch (IOException ex) {
		ex.printStackTrace();
		}
	}

}
