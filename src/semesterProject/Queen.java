package semesterProject;

public class Queen extends Ant {
	
	/*************
	 *	attributes
	 ************/	

	/***************
	 *	constructors
	 **************/
	
	public Queen (Colony colony)
	{
		super(colony);
		
		lifeSpan = 72800; // 20 years worth of days, in turns is 72800
		
		colony.map[13][13].vNode.setQueen(true);
		colony.map[13][13].vNode.showQueenIcon();
		colony.map[13][13].isHidden = false;
	}
	
	/**********
	 *	methods
	 *********/
	
	@Override
	public void takeTurn(Simulation sim) {
		age++;
		if (age >= lifeSpan)
		{
			death(sim, this);
			sim.endSim();
			return;
		}
		
		eat(sim);	
	}
		
	public void spawnAnt(Colony colony)
	{
		int randNum = Simulation.getRandomNum(1000);
		if (randNum < 500)
			colony.addAnt(new Forager(colony));
		else if (randNum < 750)
			colony.addAnt(new Scout(colony));
		else
			colony.addAnt(new Soldier(colony));
	}
	
	private void eat(Simulation sim)
	{
		if (sim.colony.map[13][13].food == 0)
		{
			death(sim, this);
		}
		else
		{
			sim.colony.map[13][13].food--;
			sim.colony.map[13][13].updateVNode();	
		}
	}
}
