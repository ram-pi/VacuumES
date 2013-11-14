package agent;

import java.awt.Point;

import aima.core.agent.Action;
import aima.core.agent.impl.NoOpAction;

public class VacuumAgentUtils {
	private Action up, down, left, right;
	
	public VacuumAgentUtils(Action up, Action down, Action left, Action right) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	public Action actionFromPoint (Point from, Point to) {
		Action nextAction = NoOpAction.NO_OP;
		
		if (to.getY() > from.getY()) {
			nextAction = down;
		}
		else if (to.getY() < from.getY()) {
			nextAction =  up;
		}
		else if (to.getX() > from.getX()) {
			nextAction = right;
		}
		else if (to.getX() < from.getX()) {
			nextAction = left;
		}
		
		return nextAction;
	}

	public Action getUp() {
		return up;
	}

	public Action getDown() {
		return down;
	}

	public Action getLeft() {
		return left;
	}

	public Action getRight() {
		return right;
	}

	public void setUp(Action up) {
		this.up = up;
	}

	public void setDown(Action down) {
		this.down = down;
	}

	public void setLeft(Action left) {
		this.left = left;
	}

	public void setRight(Action right) {
		this.right = right;
	}
	
}
