package graphique;
import graphiqueObjets.PanneauInfo;
import graphiqueObjets.TableJeu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class PanneauGeneral extends JPanel{
	static final long serialVersionUID = 1;
	Color couleurFond = Color.LIGHT_GRAY;
	private TableJeu table ;
	private PanneauInfo info;
	
	public PanneauGeneral () {	
		setPreferredSize(new Dimension (1210,700));
		setBackground(couleurFond);
		
		table = new TableJeu();
		info = new PanneauInfo(this);

		//on crée une Box (dans laquelle on met nos JPanel horizontalement) qu'on ajoute au JPanel
		setLayout(new BorderLayout());
	    add(info, BorderLayout.WEST);
	    add(table, BorderLayout.EAST);
	}
	public TableJeu getTableJeu() {
		return table;
	}
}
