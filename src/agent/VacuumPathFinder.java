/* This is a path finder that implements the A* Algorithm for searching the minimum distance to a cell
 * and next find the shortest path with the nearest neighbor algorithm
 *  */

package agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VacuumPathFinder {

	private List<A_Point> closedList;
	private List<A_Point> openList;
	private A_Point current;
	private List<A_Point> path;

	public VacuumPathFinder() {
		this.closedList = new LinkedList<A_Point>();
		this.openList = new LinkedList<A_Point>();
		this.current = new A_Point();
		this.path = new ArrayList<A_Point>(); 	
	}

	public void clearLists () {
		this.getOpenList().clear();
		this.getClosedList().clear();
		this.getPath().clear();
		this.current = new A_Point();
	}

	public void astar (Point start, Point finish, int size, List<Point> obs) {
		boolean finishPointReached = false;
		List<A_Point> walkableFromCurrent = new LinkedList<A_Point>();

		// Starting to set the starting node as the current node
		clearLists();
		current.setG(0);
		current.setH(manhattanDistance(start, finish));
		current.setPosition(start);
		current.setFather(start);

		closedList.add(current);
		walkableFromCurrent = getAdjForOpenList(getAdjacent(current.getPosition(), obs, size), current, finish);
		openList.addAll(walkableFromCurrent);

		while (!finishPointReached) {

			// Verify if I reached the destination
			if (current.getPosition().equals(finish)) {
				finishPointReached = true;
				continue;
			}

			// Get the square S on the openlist which has the lowest F
			current = findPointLowestF();
			//			System.out.println("Choose the square : " + current.getPosition() + " with F value -> " + current.getF() );

			// Remove S from openList and add it to closedList
			openList.remove(current);
			closedList.add(current);

			if (current == null)
				System.out.println("current is null");


			// Find the adjacent tiles to current position
			walkableFromCurrent = getAdjForOpenList(getAdjacent(current.getPosition(), obs, size), current, finish);

			// Foreach Tiles T adjacent to current if T is in the closed list ignore it and if it is not in the openlis add it
			for (A_Point ap : walkableFromCurrent) {
				List<Point> closedListPoint = this.getListOfPoint(closedList);
				List<Point> openListPoint = this.getListOfPoint(openList);
				if (!closedListPoint.contains(ap.getPosition()) && !openListPoint.contains(ap.getPosition())) {
					openList.add(ap);
				}
				// If T is in the openlist check if F score is lower and update his parent with his path
				else if (openListPoint.contains(ap.getPosition())) {
					checkPath(ap);
				}
			}

		}

		this.buildPath();
	}

	public void buildPath () {
		A_Point curr = this.getClosedList().get(this.getClosedList().size()-1);

		while (!curr.getPosition().equals(curr.getFather())) {
			this.getPath().add(curr);
			curr = searchInClosedByPosition(curr.getFather());
		}

		//		this.getPath().add(curr);
		Collections.reverse(this.getPath());
	}

	public A_Point searchInClosedByPosition (Point p) {
		for (A_Point ap : this.getClosedList()) {
			if (ap.getPosition().equals(p))
				return ap;
		}
		return null;
	}

	public List<Point> getListOfPoint (List<A_Point> l) {
		List<Point> res = new LinkedList<Point>();
		for (A_Point ap : l) {
			res.add(ap.getPosition());
		}
		return res;
	}

	public void checkPath (A_Point adjP) {

		// Get the A_Point already in the openList and check if it is lower then the adjP
		int fromOpenIndex = this.getOpenList().indexOf(adjP);
		A_Point fromOpen = this.getOpenList().get(fromOpenIndex);
		double fromOpenF = fromOpen.getF();
		if (fromOpenF >= adjP.getF()) {
			this.getOpenList().remove(fromOpen);
			this.getOpenList().add(adjP);
			return;
		}
	}

	public int getIndexFromList (List<A_Point> l, A_Point a) {
		int index = -1;
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).equals(a))	
				return i;
		}
		return index;
	}

	public A_Point findPointLowestF () {
		double lowestF;
		A_Point result;
		List<A_Point> sel = this.getOpenList();

		if (sel.isEmpty())
			return null;

		lowestF = sel.get(0).getF();
		result = sel.get(0);
		for (A_Point ap : sel) {
			if (ap.getF() < lowestF) {
				lowestF = ap.getF();
				result = ap;
			}
		}
		return result;
	}

	public static void printApointList (List<A_Point> l) {
		System.out.println("Printing the list...");
		for (A_Point a : l) {
			System.out.println("The position is : " + a.getPosition() + " and the " + a.getG() + " + " + a.getH() + " = " + a.getF() );
		}
	}

	public static List<A_Point> getAdjForOpenList (List<Point> adj, A_Point father, Point destination) {
		List<A_Point> adjOpen = new LinkedList<A_Point>();
		double g = father.getG()+0.5;
		double h;
		Point f = father.getPosition();
		A_Point tmp;

		for (Point point : adj) {
			h = manhattanDistance(point, destination);
			tmp = new A_Point(g, h, point, f);
			adjOpen.add(tmp);
		}

		return adjOpen;
	}

	public static List<Point> getAdjacent (Point p, List<Point> obstacles, int s) {
		LinkedList<Point> adjacent = new LinkedList<Point>();

		Point left, right, up, down;
		left = new Point((int) p.getX()-1, (int) p.getY());
		right = new Point((int) p.getX()+1, (int) p.getY());
		up = new Point((int) p.getX(), (int) p.getY()-1);
		down = new Point((int) p.getX(), (int) p.getY()+1);
		
		if (!obstacles.contains(left) && (left.getX() >= 0)) 
			adjacent.add(left);
		if (!obstacles.contains(down) && (down.getY() < s))
			adjacent.add(down);
		if (!obstacles.contains(up) && (up.getY() >= 0))
			adjacent.add(up);
		if (!obstacles.contains(right) && (right.getX() < s))
			adjacent.add(right);

		return adjacent;
	}
	
	public void printPath (List<Point> l) {
		System.out.println("The path is :");
		for (Point point : l) {
			System.out.println(point);
		}
	}

	public static double manhattanDistance (Point a, Point b) {

		double x = Math.abs(a.getX() - b.getX());
		double y = Math.abs(a.getY() - b.getY());

		return x+y;
	}
	
	public List<Point> getPointPath () {
		List<Point> l = new LinkedList<Point>();
		for (A_Point ap : this.getPath()) {
			l.add(ap.getPosition());
		}
		return l;
	}
	
	// Perform the Nearest Neighbor  
	public List<Point> getPathToNN (Point start, List<Point> dirty, int size, List<Point> obstacles) {
		List<Point> minimumPath = null;
		for (Point dpoint : dirty) {
			astar(start, dpoint, size, obstacles);
			if (minimumPath == null || this.getPointPath().size() < minimumPath.size()) {
				minimumPath = this.getPointPath();
			}
		}
		return minimumPath;
	}
	
	public List<Point> getPathToDijkstra (Point start, List<Point> dirty, int size, List<Point> obstacles) {
		List<Point> minimumPath = null;
		return minimumPath;
	}

	public List<A_Point> getClosedList() {
		return closedList;
	}

	public List<A_Point> getOpenList() {
		return openList;
	}

	public A_Point getCurrent() {
		return current;
	}

	public List<A_Point> getPath() {
		return path;
	}

	public void setClosedList(List<A_Point> closedList) {
		this.closedList = closedList;
	}

	public void setOpenList(List<A_Point> openList) {
		this.openList = openList;
	}

	public void setCurrent(A_Point current) {
		this.current = current;
	}

	public void setPath(List<A_Point> path) {
		this.path = path;
	}

	public static void main(String[] args) {
		int size = 8;
		LinkedList<Point> obs = new LinkedList<Point>();
		Point o1 = new Point(5, 7);
		Point o2 = new Point(6, 6);
		obs.add(o1);
		obs.add(o2);
		Point start = new Point(3, 6);
		Point finish = new Point(6, 7);

		System.out.println("Begin to find a path...");
		VacuumPathFinder finder = new VacuumPathFinder();
		finder.astar(start, finish, size, obs);
		for (A_Point ap : finder.getClosedList()) {
			System.out.println(ap.getPosition() + " father -> " + ap.getFather());
		}
		System.out.println("Building path...");
		List<Point> point_path = finder.getPointPath();
		for (Point pp : point_path) {
			System.out.println(pp);
		}
	}

}
