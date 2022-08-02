package semesterProject;

import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import AntSimGUI.AntSimGUI;
import AntSimGUI.SimulationEvent;
import AntSimGUI.SimulationEventListener;
import dataStructures.ArrayQueue;
import dataStructures.List;
import dataStructures.Queue;


public class Simulation implements ActionListener, SimulationEventListener{

	/*************
	 *	attributes
	 ************/
	
	AntSimGUI gui = new AntSimGUI();

	int year;
	
	int day;
	
	int turn;
	
	boolean nodeChange;
	
	Colony colony = new Colony();
	
	Timer timer;
	
	static Random rand = new Random();
	
	/***************
	 *	constructors
	 **************/
	
	public Simulation ()
	{
		// add array map to the gui
		gui.initGUI(colony.vMap);
		
		// add event listeners to simulation
		gui.addSimulationEventListener(this);
		
		// Initialize timer
		timer = new Timer(1000, this);
		
		initSim();
	}
	
	/**********
	 *	methods
	 *********/
	
	private void initSim()
	{
		// initialize turn/day and add to gui
		turn = 1;
		day = 1;
		year = 0;
		gui.setTime(this.getTime());
		
		// add queen
		colony.addAnt(new Queen(this.colony));
		
		/////////////// Colony Entrance Starting "Supplies" ////////////////////////////
		// starting food is 1000
		colony.map[13][13].food = 1000;
		
		// add 10 starting soldiers
		for (int i = 0; i < 10; i++)
			colony.addAnt(new Soldier(colony));
		
		// add 50 starting foragers
		for (int i = 0; i < 50; i++)
			colony.addAnt(new Forager(colony));
		
		// add 4 starting scouts
		for (int i = 0; i < 4; i++)
			colony.addAnt(new Scout(colony));
		/////////////// Colony Entrance Starting Supplies //////////////////////////////
		
		colony.map[13][13].updateVNode();
	}
	
	/**
	 * a method to run the simulation for a single turn
	 * 
	 * updates turn and day and updates gui
	 * on first turn of day, queen spawns a new ant and all pheromone is halved.
	 * 
	 */
	private void takeTurn()
	{
		// after turn 10 day increases and turn reverts to 1
		if (turn == 10)
		{
			day++;
			if (day == 365)
			{
				year++;
				day = 1;
			}
			turn = 1;
			
			// queen spawns ant on first turn of every day
			((Queen) colony.colAntHash.get(0)).spawnAnt(colony);
			
			// pheromone levels in each square drop by half on first turn of every day
			for (int y = 0; y < 27; y++)
			{
				for (int x = 0; x < 27; x++)
				{
					int temp = colony.map[x][y].pheromone;
					temp = temp / 2;
					colony.map[x][y].pheromone = temp;
					colony.map[x][y].vNode.setPheromoneLevel(temp);
					colony.map[x][y].updateVNode();
				}
			}
		}
		else
			turn++;
		
		// update gui with new turn information
		gui.setTime(getTime());
		
		// 3% chance a Bala will spawn in colony // TODO turn back on
		if (getRandomNum(1000) < 30)
		{
			colony.addAnt(new Bala(colony));
		}
		
		runAntTurns();
	} // end takeTurn()
	
	/**
	 * method to get current year, day and turn of simulation
	 * @return String containing day and turn
	 */
	private String getTime()
	{
		return "Year " + year + ". Day " + day + ". Turn " + turn + ".";
	} // end getTime()
	
	/**
	 * method to get a random number
	 * @param max int that is highest possible return value
	 * @return int random number
	 */
	public static int getRandomNum(int max)
	{
		return rand.nextInt(max);
	} // end getRandomNum
	
	/**
	 * a method that creates a psuedo-random order and takes each ants turn
	 * 
	 * pulls list of keys from the master HashMap, a random item is chosen from the key list,
	 * placed into a queue, then deleted. Process is repeated until all keys are placed in the queue.
	 * queue is dequeued until empty, taking each ants turn.
	 */
	private void runAntTurns()
	{
		// queue that will be the list of keys in random order
		Queue turnOrder = new ArrayQueue();
		// list of keys for every ant still in colony
		List availableKeys = colony.colAntHash.keyList();
		// randomly pick a key, add to turn order, and remove from list to prevent repeats
		while (!availableKeys.isEmpty())
		{
			int pick = Simulation.getRandomNum(availableKeys.size());
			turnOrder.enqueue(availableKeys.get(pick));
			availableKeys.remove(pick);
		}
		
		// take all turns
		while (!turnOrder.isEmpty() && colony.colAntHash.get(0) != null)
		{
			int next = (int) turnOrder.dequeue();
			if (colony.colAntHash.get(next) != null)
				((Ant) colony.colAntHash.get(next)).takeTurn(this);
		}
	} // end runAntTurns()
	
	public void endSim ()
	{
		timer.stop();
		JOptionPane.showMessageDialog(null, "The Queen has died!", "Game Over", JOptionPane.PLAIN_MESSAGE);
	} // end endSim()

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(colony.colAntHash.get(0) == null)
			timer.stop();
		else
			takeTurn();
	}

	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {
		if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT)
		{ // set up the simulation for normal operation }
			timer.stop();
			timer.setDelay(100);
			colony.resetColony();
			initSim();
		}
		else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT)
		{ // set up simulation for testing the queen ant }
			timer.setDelay(1);
		}
		else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT)
		{ // set up simulation for testing the scout ant }
			timer.setDelay(100);
		}
		else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT)
		{ // set up simulation for testing the forager ant }
			timer.setDelay(1000);
		}
		else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT)
		{ // set up simulation for testing the soldier ant }
			timer.stop();		
		}
		else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT)
		{ // run the simulation continuously }
			timer.start();
		}
		else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT)
		{ // run the next turn of the simulation }
			timer.stop();
			if(colony.colAntHash.get(0) != null)
				takeTurn();
		}
		else
		{ // invalid event occurred - probably will never happen }
			
		}
	}
}
