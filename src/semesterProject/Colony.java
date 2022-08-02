package semesterProject;

import dataStructures.HashMap;
import dataStructures.Map;
import AntSimGUI.ColonyView;

/**
 * 1.
The Environment
4.
The remaining squares will initially be unexplored terrain.
5.
Certain ants (scout ants) will be capable of revealing the areas that have not been explored.
6.
All other types of ants will only be allowed to move into squares that have been revealed by scout ants.
7.
Each square can contain one or more of the following, in any combination:
a.
Zero or more enemy ants
b.
Zero or more friendly ants
c.
Zero or more units of food
d.
Zero or more units of pheromone
 * @author SChmielowski
 *
 */
public class Colony {

	/*************
	 *	attributes
	 ************/
	
	Node map[][] = new Node[27][27];
	ColonyView vMap = new ColonyView(27, 27);

	int antCount = 0; // colony's lifetime ant count, it never decreases
	Map colAntHash = new HashMap();
	
	/***************
	 *	constructors
	 **************/
	
	public Colony ()
	{
		initColony();
	}
	
	/*************
	 *	methods
	 ************/
	
	private void initColony () 
	{
		for (int y = 0; y < 27; y++)
		{
			for (int x = 0; x < 27; x++)
			{
				map[x][y] = new Node(x, y);
				vMap.addColonyNodeView(map[x][y].vNode, x, y);
			}
		}
	}
	
	public void addAnt(Ant ant)
	{
		// add ant to colony ant list
		colAntHash.add(ant.id, ant);
		
		// add ant to specific node
		map[ant.x][ant.y].addAnt(ant);

		antCount++;
	}
	
	public void removeAnt(Ant ant)
	{
		// remove from node
		map[ant.x][ant.y].removeAnt(ant);
		
		// remove from master ant list
		colAntHash.remove(ant.id);
	}
	
	public Ant randomEnemy(int x, int y)
	{
		return (Ant) colAntHash.get(map[x][y].balaList.get(Simulation.getRandomNum(map[x][y].balaCount)));		
	}
	
	public Ant randomFriendly(int x, int y)
	{
		return (Ant) colAntHash.get(map[x][y].friendlyList.get(Simulation.getRandomNum(map[x][y].getFriendlyCount())));		
	}
	
	public void resetColony()
	{
		colAntHash.clear();
		antCount = 0;
		for (int y = 0; y < 27; y++)
		{
			for (int x = 0; x < 27; x++)
			{
				map[x][y].resetNode();
			}
		}
	}
}
