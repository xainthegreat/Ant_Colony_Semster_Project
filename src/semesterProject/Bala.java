package semesterProject;

public class Bala extends Ant {
	
	/*************
	 *	attributes
	 ************/

	/***************
	 *	constructors
	 **************/
	
	public Bala (Colony colony)
	{
		super(colony);
		
		int randomNum = Simulation.getRandomNum(105);

		if (randomNum <= 26)
		{
			x = randomNum;
			y = 0;
		}
		else if (randomNum <= 52)
		{
			x = 26;
			y = randomNum-26;
		}
		else if (randomNum <= 78)
		{
			x = 0;
			y = randomNum-52;
		}
		else
		{
			x = randomNum-78;
			y = 26;
		}
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

		if (sim.colony.map[x][y].getFriendlyCount() > 0)
			attack(sim, sim.colony.randomFriendly(x, y));
		else
			super.move(sim.colony);
	} // end takeTurn()
}
