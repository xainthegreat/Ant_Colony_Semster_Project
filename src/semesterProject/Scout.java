package semesterProject;

public class Scout extends Ant {

	/***************
	 *	constructors
	 **************/
	
	public Scout (Colony colony)
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
		
		move(sim.colony);		
	} // end takeTurn()
	
	@Override
	void move (Colony colony)
	{
		super.move(colony);

		// if square is hidden, open it
		if (colony.map[x][y].isHidden)
			colony.map[x][y].openNode();
	} // end move()
} // end Scout
