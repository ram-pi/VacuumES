package agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;	
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import core.LocalVacuumEnvironmentPercept;
import core.VacuumEnvironment.LocationState;
import core.VacuumEnvironmentState;

public class VacuumAgentES extends AbstractAgent {
	
	public java.util.List<Point> dirtyPoints;
	public java.util.List<Point> obstacles;
	public static Map<Point, LocationState> state;
	public VacuumEnvironmentState env;
	public Action suck, left, right, up, down;
	public final List<Point> path = new LinkedList<Point>();
	public VacuumPathFinder finder;
	public Point agentLocation;
	
	public VacuumAgentES() {
		
		this.program = new AgentProgram() {
			
			@Override
			public Action execute(Percept percept) {
				final LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				final Set<Action> actionsKeySet = vep.getActionEnergyCosts().keySet();
				
				// Initialize the set of action we can do
				init(actionsKeySet);
				
				// Gets the list of dirtyPoints and obstacles in the istance and location of the agent
				state = vep.getState();
				obstacles = getObstacles();
				dirtyPoints = getDirty();
				agentLocation = vep.getAgentLocation();
				
				// Gets out if the floor is clean and the agent is at the base
				if (dirtyPoints.isEmpty() && !agentLocation.equals(vep.getBaseLocation())) {
					dirtyPoints.add(vep.getBaseLocation());
				} else if (dirtyPoints.isEmpty() && agentLocation.equals(vep.getBaseLocation())) {
					return NoOpAction.NO_OP;
				}
				
				// Select an action toDo
				Action toDo = findAction(vep);
				
				return toDo;
			}
		};
	}
	
	public Action findAction (LocalVacuumEnvironmentPercept vep) {
		// Clean if the agent is on a dirty cell
		if (dirtyPoints.contains(agentLocation)) {
			path.clear();
			return suck;	
		} else if (dirtyPoints.isEmpty()) {
			return NoOpAction.NO_OP;
		}
		
		// If there is no path for any dirty cell the agent creates it 
		if (path.isEmpty()) {
			finder = new VacuumPathFinder();
//			finder.astar(agentLocation, dirtyPoints.get(0), 8, obstacles);
//			path.addAll(finder.getPointPath());
			path.addAll(finder.getPathToNN(agentLocation, dirtyPoints, 8, obstacles));
			finder.printPath(path);
		}
			
		
		Point to = path.remove(0);
		VacuumAgentUtils utils = new VacuumAgentUtils(up, down, left, right);
		return utils.actionFromPoint(agentLocation, to);
	}
	
	public void init (Set<Action> actionKeySet) {
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
