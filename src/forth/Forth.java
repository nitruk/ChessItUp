package forth;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Forth {
	
	private Stack<Integer> pile;
	private ArrayList<Method> fonctions;
	private Object gestionnaire;
	
	public Forth (Object gestionnaire) throws Exception
	{
		pile = new Stack<Integer>();
		fonctions = new ArrayList<Method>();
		this.gestionnaire = gestionnaire;
		
		for (Method fonction : gestionnaire.getClass().getDeclaredMethods())
		{
			if (!addMethod(fonction))
			{
				Exception e = new Exception("Le prototype de " + fonction.getName() + " est incorrect.");
				throw e;
			}
		}
	}
	
	public void process (String str)
	{
		StringTokenizer strtk = new StringTokenizer(str, " ");
		int n = strtk.countTokens();
		for (int i = 0 ; i < n ; i ++)
			enforth(strtk.nextToken());
	}
	
	public boolean hasResult ()
	{
		return !pile.isEmpty();
	}
	
	public int getHead ()
	{
		return pile.pop();
	}
	
	private void enforth (String str)
	{
		for (Method fonction : fonctions)
		{
			if (str.equals(fonction.getName()))
			{
				int n = fonction.getParameterTypes().length;
				Integer[] args = new Integer[n];
				for (int i = 0 ; i < n ; i ++)
					args[n-1-i] = pile.pop();
				try
				{
					if (fonction.getReturnType().equals(void.class))
					{
						fonction.invoke(gestionnaire, (Object[]) args);
						return;
					}
					else
					{
						pile.push((Integer) fonction.invoke(gestionnaire, (Object[]) args));
						return;
					}
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		pile.push(Integer.parseInt(str));
	}
	
	private boolean addMethod (Method fonction)
	{
		Class<?>[] args = fonction.getParameterTypes();
		for (int i = 0; i < args.length ; i ++)
			if (!args[i].equals(int.class))
				return false;
		Class<?> rt = fonction.getReturnType();
		if (rt != void.class && rt != int.class)
			return false;
		fonctions.add(fonction);
		return true;
	}

}
