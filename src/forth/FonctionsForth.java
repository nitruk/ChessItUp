package forth;

import objets.Robot;

import client.Communication;


public class FonctionsForth
{
	Communication com;
	Robot robot;
	
	public FonctionsForth (Communication com, Robot robot)
	{
		this.com = com;
		this.robot = robot;
	}

	public void move (int d)
	{
		com.move(d);
	}

	public void turn (int d)
	{
		com.turn(d);
	}

	public void catchPawn ()
	{
		com.catchPawn();
	}

	public void release ()
	{
		com.releasePawn();
	}
	
	public int getX ()
	{
		return robot.getX();
	}
	
	public int getY() 
	{
		return robot.getY();
	}

}
