package semesterProject;

public abstract class Ant {
	
	/*************
	 *	attributes
	 ************/
		
	int id;
	
	int lifeSpan = 3640; // one year worth of turns
	
	int age = 0;
	
	int x = 13;
	
	int y = 13;
	
	/***************
	 *	constructors
	 **************/
	
	public Ant (Colony colony)
	{
		id = colony.antCount;
	}
	
	/**********
	 *	methods
	 *********/

	abstract void takeTurn (Simulation sim);
	
	void move (Colony colony)
	{
		int attempts = 0;
		int tempX = x;
		int tempY = y;
				
		// rolls random direction, if it can move update x and y
		boolean again = true;
		while (again && attempts < 10)
		{
			switch (Simulation.getRandomNum(1600)%8)
			{
				case 0: // move up and left
					if (x > 0 && y > 0)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x-1][y-1].isHidden)
							{
								attempts++;
								break;
							}
						}

						again = false;
						tempX--;
						tempY--;
					}
					break;
				case 1: // move up
					if (y > 0)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x][y-1].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempY--;
					}
					break;
				case 2: // move up and right
					if (x < 26 && y > 0)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x+1][y-1].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempX++;
						tempY--;
					}
					break;
				case 3: // move left
					if (x > 0)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x-1][y].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempX--;
					}
					break;
				case 4: // move right
					if (x < 26)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x+1][y].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempX++;
					}
					break;
				case 5: // move down and left
					if (x > 0 && y < 26)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x-1][y+1].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempX--;
						tempY++;
					}
					break;
				case 6: // move down
					if (y < 26)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x][y+1].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempY++;
					}
					break;
				case 7: // move down and right
					if (x < 26 && y < 26)
					{
						if (this instanceof Soldier)
						{
							if (colony.map[x+1][y+1].isHidden)
							{
								attempts++;
								break;
							}
						}
						
						again = false;
						tempX++;
						tempY++;
					}
					break;
			} // end switch
		} // end while (again)
		
		if (tempX != x || tempY != y)
		{
			colony.map[x][y].removeAnt(this);
			x = tempX;
			y = tempY;
			colony.map[x][y].addAnt(this);
		}
		else
			return;
	}
	
	void attack (Simulation sim, Ant target)
	{
		// During an attack, there is a 50% chance it kills the ant it attacks; otherwise, the attack misses and the ant that is attacked survives.
		if (Simulation.getRandomNum(1000) < 500)
			death(sim, target);
	} // end attack()
	
	void death (Simulation sim, Ant ant)
	{
		if (ant instanceof Forager)
			if(!((Forager) ant).forageMode)
				((Forager) ant).depositFood(sim.colony);
		
		if(ant instanceof Queen)
			sim.endSim();
		
		sim.colony.removeAnt(ant);
	}
}
