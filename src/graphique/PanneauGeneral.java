package graphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import objets.Pion;
import objets.Robot;
import client.Communication;
import client.Strategie;


public class PanneauGeneral extends JPanel{

	static final long serialVersionUID = 1;

	final String address = "127.0.0.1";
	final int port = 4343;
	final String name = "Nitruk";
	

	Color couleurFond = Color.LIGHT_GRAY;
	private TableJeu table ;
	private PanneauInfo info;
	private Communication com;
	
	public PanneauGeneral () {	
		//setPreferredSize(new Dimension (1210,700));
		setBackground(couleurFond);
		
		Robot robots[] = new Robot[2];
		Pion pions[] = new Pion[19];
		
		try {
			Socket s = new Socket(address, port);
			PrintStream streamW = new PrintStream(s.getOutputStream());
			BufferedReader bufR = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ObjectInputStream objectR = new ObjectInputStream(s.getInputStream());
			streamW.println(name);
			System.out.println("Nom envoye (" + name + ")");
			for (int n = 0 ; n < 2 ; n ++) robots[n] = (Robot) objectR.readObject();
			for (int n = 0 ; n < 19 ; n ++) pions[n] = (Pion) objectR.readObject();
			
			table = new TableJeu(robots, pions);
			info = new PanneauInfo(robots);
			com = new Communication(robots, pions, table, info, bufR, streamW);

			//on crée une Box (dans laquelle on met nos JPanel horizontalement) qu'on ajoute au JPanel
			setLayout(new BorderLayout());
		    add(info, BorderLayout.WEST);
		    add(table, BorderLayout.EAST);
		    Strategie strat = new Strategie(robots, pions, com);
		    addKeyListener(strat);
			if (bufR.readLine().compareTo("go") == 0) {
				com.start();
				strat.start();
				setFocusable(true);
				requestFocus();
			}
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
