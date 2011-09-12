package algorithmique;

import java.util.ArrayList;
import java.util.Iterator;


public class Vecteur {
 
	private ArrayList<Double> coordonees = new ArrayList<Double>();
 
	public Vecteur(Double x, Double y){
		this.coordonees.add(x);
		this.coordonees.add(y);
	}

	public double produitScalaire(Vecteur vecteur) {
		double res = 0;
		for (Iterator<Double> iter1 = this.coordonees.iterator(),
				iter2 = vecteur.coordonees.iterator(); iter1.hasNext() && iter2.hasNext();) {
			res += iter1.next() * iter2.next();
		}
		return res;
	}
 }