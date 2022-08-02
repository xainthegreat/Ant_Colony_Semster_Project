package semesterProject;

import dataStructures.ArrayList;
import dataStructures.HashSet;
import dataStructures.LinkedStack;
import dataStructures.List;
import dataStructures.Set;
import dataStructures.Stack;

public class Forager extends Ant {
	
	/*************
	 *	attributes
	 ************/
	// the actual path home
	Stack stackHome = new LinkedStack();
	// to check if they have been in the node already
	Set setHome = new HashSet();
	
	Coords lastPos = new Coords(-1, -1);
		
	boolean forageMode;
	
	boolean hasFood;

	/***************
	 *	constructors
	 **************/
	
	public Forager (Colony colony)
	{
		super(colony);
		forageMode = true;
		hasFood = false;
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
			depositFood(sim.colony);
			death(sim, this);
			return;
		}			
		
		if (forageMode)
		{
			move(sim.colony);
			
			if (sim.colony.map[x][y].food > 0)
				pickUpFood(sim.colony);
		}
		else
		{
			returnHome(sim.colony);
		}
		
//		System.out.println(id + "has: " + setHome.toString());
	} // end takeTurn()
	
	private void pickUpFood(Colony colony)
	{
		colony.map[x][y].food--;
		forageMode = false;
		hasFood = true;
	}
	
	public void depositFood(Colony colony)
	{
		if (hasFood)
			colony.map[x][y].food++;
		
		hasFood = false;
		forageMode = true;
		lastPos = new Coords(-1, -1);
		setHome.clear();
	}
	
	private void returnHome(Colony colony)
	{
		if (hasFood)
		{
			if(x!=13 && y!=13) 
			{
				// add 10 pheromone to each node on way back if < 1000
				if (colony.map[x][y].pheromone < 1000)
					colony.map[x][y].pheromone+=10;
			}
		}	
			// move to previous node
			Coords temp = (Coords) stackHome.pop();
			colony.map[x][y].removeAnt(this);
			x = temp.getX();
			y = temp.getY();
			colony.map[x][y].addAnt(this);
			
			// if ant made it back to queen deposit food if it has it
			if (stackHome.isEmpty())
				depositFood(colony);
	}

	@Override
	void move(Colony colony) 
	{
		int tempX = x;
		int tempY = y;
		int highestPher = 0;
		List coordList = new ArrayList();
		
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				// skip current node if...
				if (x == 0 && i == -1) // if node doesn't exist, west side
					continue;
				else if (x == 26 && i == 1) // if node doesn't exist, east side
					continue;
				else if (y == 0 && j == -1) // if node doesn't exist, north side
					continue;
				else if (y == 26 && j == 1) // if node doesn't exist, south side
					continue;
				else if (colony.map[x+i][y+j].idX == 14 && colony.map[x+i][y+j].idY == 14) // if node is queens square
					continue;
				else if (colony.map[x+i][y+j].isHidden) // if node is hidden
					continue;
				else if (i == 0 && j == 0) // if node is current node
					continue;
				else if (lastPos.getX() == (x+i) && lastPos.getY() == (y+j)) // if node was previous node
					continue;
				else
				{
					int checkPher = colony.map[x+i][y+j].pheromone;
					if (highestPher < checkPher)
					{
						highestPher = checkPher;
						coordList.clear();
						tempX = x + i;
						tempY = y + j;
					}
					else if (highestPher == checkPher)
					{
						coordList.add(new Coords(x + i, y + j));
					}
				}
			}
		}
		
		colony.map[x][y].removeAnt(this);
		lastPos = new Coords(x, y);
		stackHome.push(lastPos);
		
		if(!coordList.isEmpty())
		{
			Coords temp = (Coords) coordList.get(Simulation.getRandomNum(coordList.size()));
			x = temp.getX();
			y = temp.getY();
		}
		else
		{
			x = tempX;
			y = tempY;
		}

		colony.map[x][y].addAnt(this);
		
		// this checks to see if a node has been visited before on the same food run, if so return home and start again.
		Coords temp = new Coords(x,y);
		if (!setHome.add(temp.toString()))
		{
			forageMode = false;
			setHome.clear();
		}			
	} // end move()
	
	private class Coords
	{
		/*************
		 *	attributes
		 ************/
		
		int x;
		
		int y;
		
		/***************
		 *	constructors
		 **************/
		
		public Coords (int inputX, int inputY)
		{
			x = inputX;
			y = inputY;
		}
		
		/**********
		 *	methods
		 *********/
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		public String toString()
		{
			return "(" + (x+1) + ", " + (y+1) + ")";
		}
		
	} // end Coords
}