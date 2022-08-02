package semesterProject;

import dataStructures.ArrayList;
import dataStructures.List;
import AntSimGUI.ColonyNodeView;

public class Node {

	/*************
	 *	attributes
	 ************/
	
	ColonyNodeView vNode = new ColonyNodeView();
	
	List friendlyList = new ArrayList();
	
	List balaList = new ArrayList();
	
	int idX;
	
	int idY;
	
	int pheromone = 0;
	
	int food = 0;
	
	int soldierCount = 0;
	
	int foragerCount = 0;
	
	int scoutCount = 0;
	
	int balaCount = 0;
	
	boolean isHidden = true;
	
	/***************
	 *	constructors
	 **************/
	
	public Node (int x, int y)
	{
		initNode(x,y);
	}
	
	/*************
	 *	methods
	 ************/
	
	private void initNode(int x, int y)
	{		
		// set ID for square in array and gui
		idX = x+1;
		idY = y+1;
		vNode.setID("" + idX + ", " + idY);
	}
	
	public void openNode()
	{
		isHidden = false;
		
		// 25% chance that food will have between 500 and 1000
		if (Simulation.getRandomNum(1000) < 250)
		{
			food = Simulation.getRandomNum(501) + 500;
		}
		updateVNode();
	}

	public void addAnt(Ant ant)
	{
		if (ant instanceof Bala)
		{
			balaList.add(ant.id);
			balaCount++;
		}
		else
		{
			friendlyList.add(ant.id);
			if (ant instanceof Scout)
				scoutCount++;
			else if (ant instanceof Soldier)
				soldierCount++;
			else if (ant instanceof Forager)
				foragerCount++;
		}
		this.updateVNode();
	}
	
	public void removeAnt(Ant ant)
	{
		if (ant instanceof Bala)
		{
			balaList.remove(balaList.indexOf(ant.id));
			balaCount--;
		}
		else
		{
			friendlyList.remove(friendlyList.indexOf(ant.id));
			if (ant instanceof Scout)
				scoutCount--;
			else if (ant instanceof Soldier)
				soldierCount--;
			else if (ant instanceof Forager)
				foragerCount--;
		}
		this.updateVNode();
	}
	
	public int getFriendlyCount()
	{
		return friendlyList.size();
	}
	
	public void updateVNode()
	{
		vNode.setFoodAmount(food);
		if (pheromone < 0)
			pheromone = 0;
		vNode.setPheromoneLevel(pheromone);
		
		vNode.setSoldierCount(soldierCount);
		if(soldierCount > 0)
			vNode.showSoldierIcon();
		else
			vNode.hideSoldierIcon();
		
		vNode.setScoutCount(scoutCount);
		if(scoutCount > 0)
			vNode.showScoutIcon();
		else
			vNode.hideScoutIcon();
		
		vNode.setForagerCount(foragerCount);
		if(foragerCount > 0)
			vNode.showForagerIcon();
		else
			vNode.hideForagerIcon();
		
		vNode.setBalaCount(balaCount);
		if(balaCount > 0)
			vNode.showBalaIcon();
		else
			vNode.hideBalaIcon();
		
		if (!isHidden)
			vNode.showNode();
		else
			vNode.hideNode();
	}
	
	public void resetNode() 
	{
		isHidden = true;
		pheromone = 0;
		food = 0;
		soldierCount = 0;
		foragerCount = 0;
		scoutCount = 0;
		balaCount = 0;
		
		friendlyList.clear();
		balaList.clear();
		
		// update gui with these defaults
		updateVNode();
	}
}
