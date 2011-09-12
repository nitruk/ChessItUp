package algorithmique;

public class ExceptionWaitUntilEnd extends Exception{

	private static final long serialVersionUID = 1L;
	boolean couleur;

	public ExceptionWaitUntilEnd(boolean couleur) {
		this.couleur = couleur;
		if(couleur)
			System.out.println("Exception lancée par Bleu : WaitUntilEnd");
		else
			System.out.println("Exception lancée par Rouge : WaitUntilEnd");
	}
}
