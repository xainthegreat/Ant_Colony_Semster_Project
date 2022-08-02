package semesterProject;

public class Soldier extends Ant {

	/*************
	 *	attributes
	 ************/
		
	/***************
	 *	constructors
	 **************/
	
	public Soldier (Colony colony)
	{
		super(colony);
	}
	
	/**********
	 *	methods
	 *********/

	@Override
	void takeTurn(Simulation sim) 
	{
		age++;
		if (age >= lifeSpan)
		{
			death(sim, this);
			return;
		}		
		if (sim.colony.map[x][y].balaCount > 0)
			attack(sim, sim.colony.randomEnemy(x, y));
		else
			move(sim.colony);
	} // end takeTurn()

	@Override
	void move(Colony colony) 
	{
		// looping through nodes around soldier looking for bala ants
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (x == 0 && i == -1)
					continue;
				else if (x == 26 && i == 1)
					continue;
				else if (y == 0 && j == -1)
					continue;
				else if (y == 26 && j == 1)
					continue;
				else if (colony.map[x+i][y+j].isHidden) // if node is hidden
					continue;
				else
				{
					if(colony.map[x + i][y + j].balaCount > 0)
					{
						colony.map[x][y].removeAnt(this);
						x = x + i;
						y = y + j;
						colony.map[x][y].addAnt(this);
						return;
					}
				}
			}
		}

		// if there are no bala move randomly
		super.move(colony);
	} // end move()
} // end Soldier
