package agent;

import instanceXMLParser.Instance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.corba.se.idl.Noop;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;	
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.NoOpAction;
import aima.gui.framework.MessageLogger;
import core.LocalVacuumEnvironmentPercept;
import core.VacuumEnvironment.LocationState;
import core.VacuumEnvironmentState;

public class VacuumAgentNaive extends AbstractAgent {
	
	public java.util.List<Point> dirtyPoints;
	public java.util.List<Point> obstacles;
	public static Map<Point, LocationState> state;
	public VacuumEnvironmentState env;
	public Action suck, left, right, up, down;
	public List<Point> path;
	
	public VacuumAgentNaive() {
		
		this.program = new AgentProgram() {
			
			@Override
			public Action execute(Percept percept) {
				final LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				final Set<Action> actionsKeySet = vep.getActionEnergyCosts().keySet();
				
				// Initialize the set of action we can do
				init(actionsKeySet);
				
				// Gets the list of dirtyPoints and obstacles in the istance
				state = vep.getState();
				obstacles = getObstacles();
				dirtyPoints = getDirty();
				
				// Gets out if the floo is clean
				if (dirtyPoints.isEmpty()) {
					
				}
				
				// Select an action toDo
				Action toDo = chooseAction(vep);
				//Action toDo = findAction(vep);
				
				return toDo;
			}
		};
	}
	
//	public Action findAction (LocalVacuumEnvironmentPercept vep) {
//		if (path.isEmpty()) {
//			path = VacuumPathFinder.pathFinder(vep.getAgentLocation(), dirtyPoints, obstacles, up, down, left, right, suck);
//		}
//		return path.remove(0);
//	}
	
	public Action chooseAction (LocalVacuumEnvironmentPercept vep) {
		Point agentLoc = vep.getAgentLocation();
		
		// If we are already in a dirty position we suck 
		if (state.get(agentLoc).equals(LocationState.Dirty)) {
			return suck;
		}
		
		// Find the neaerest dirty point
		double minimum_distance = 1000;
		double temp_distance;
		Point pointToReach = null;
		for (int i = 0; i < dirtyPoints.size(); i++) {
			Point tmp = dirtyPoints.get(i);
			temp_distance = agentLoc.distance(tmp);
			if (temp_distance < minimum_distance) {
				minimum_distance = temp_distance;
				pointToReach = tmp;
			}
		}
		
		// Find the better action to reach the nearest point 
		System.out.println("The nearest point is " + pointToReach.getY() + " " + pointToReach.getX());
		return findAction(agentLoc, pointToReach);
	}
	
	public Action findAction (Point from, Point to) {
		
		Action nextAction = null;
		Point nextPosition = new Point();
		
		if (to.getY() > from.getY() && !obstacles.contains(new Point((int) from.getY()+1, (int) from.getX()))) {
			nextAction = down;
			nextPosition = new Point((int) from.getY()+1, (int) from.getX());
		}
		else if (to.getY() < from.getY() && !obstacles.contains(new Point((int) from.getY()-1, (int) from.getX()))) {
			nextAction =  up;
			nextPosition = new Point((int) from.getY()-1, (int) from.getX());
		}
		else if (to.getX() > from.getX() && !obstacles.contains(new Point((int) from.getY(), (int) from.getX()+1))) {
			nextAction = right;
			nextPosition = new Point((int) from.getY(), (int) from.getX()+1);
		}
		else if (to.getX() < from.getX() && !obstacles.contains(new Point((int) from.getY(), (int) from.getX()+1))) {
			nextAction = left;
			nextPosition = new Point((int) from.getY()+1, (int) from.getX()-1);
		}
		
		System.out.println("The Agent is moving on " + nextPosition.getY() + " " + nextPosition.getX());
		if (obstacles.contains(nextPosition)) {
			System.out.println("The next position is an obstacle!");
		}
		
		return nextAction;
	}
	
	public void init (Set<Action> actionKeySet) {
//		Instance instance = new Instance();
//		String path = "/Users/pierpaolo/Documents/workspaceJava/VacuumCleaner_Editor/environment/prova1.xml";
//		instance.buildINstanceJDom(path);
//		env = new VacuumEnvironmentState(instance, null);
//		
//		left = env.getActionFromName("left");
//		right = env.getActionFromName("right");
//		up = env.getActionFromName("up");
//		down = env.getActionFromName("down");
//		suck = env.getActionFromName("suck");
//		
//		left = new DynamicAction("left");
//		right = new DynamicAction("right");
//		up = new DynamicAction("up");
//		down = new DynamicAction("down");
//		suck = new DynamicAction("suck");
		
		suck = (Action) actionKeySet.toArray()[0];
		left = (Action) actionKeySet.toArray()[1];
		down = (Action) actionKeySet.toArray()[2];
		right = (Action) actionKeySet.toArray()[3];
		up = (Action) actionKeySet.toArray()[4];
	}
	
	public java.util.List<Point> getDirty() {
		java.util.List<Point> dp = new ArrayList<Point>();
		
		try {
			Iterator<Map.Entry<Point, LocationState>> entries = state.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<Point, LocationState> entry = entries.next();
				if (entry.getValue().toString().equals("Dirty") && !obstacles.contains(entry.getKey()) ) {
					dp.add(entry.getKey());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return dp;
	}
	
	public java.util.List<Point> getObstacles () {
		java.util.List<Point> op = new ArrayList<Point>();
		
		try {
			Iterator<Map.Entry<Point, LocationState>> entries = state.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<Point, LocationState> entry = entries.next();
				if (entry.getValue().toString().equals("Obstacle")) 
					op.add(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return op;
	}
}
