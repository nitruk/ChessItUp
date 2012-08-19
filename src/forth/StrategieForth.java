package forth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import objets.Robot;
import client.Communication;

public class StrategieForth extends Thread {

	private Forth forth;
	private BufferedReader console;
	
	public StrategieForth (Communication com, Robot robot)
	{
		console = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			forth = new Forth(new FonctionsForth(com, robot));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public void run ()
	{
		try
		{
			sleep(3000);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		while(true)
		{
			try
			{
				forth.process(console.readLine());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (forth.hasResult())
			{
				System.out.println(forth.getHead());
			}
		}
	}
}
