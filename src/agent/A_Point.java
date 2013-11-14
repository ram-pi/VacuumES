package agent;

import java.awt.Point;

public class A_Point {
	
	private double g, h;
	private Point position;
	private Point father;
	
	public A_Point() {
	}
	
	public A_Point (double g, double h, Point position, Point father) {
		this.g = g;
		this.h = h;
		this.position = position;
		this.father = father;
	}

	public double getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double d) {
		this.h = d;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	public double getF () {
		return g+h;
	}
	
	public Point getFather() {
		return father;
	}

	public void setG(double g) {
		this.g = g;
	}

	public void setFather(Point father) {
		this.father = father;
	}

	@Override
	public boolean equals(Object obj) {
		A_Point a = (A_Point) obj;
		if (this.position.getX() == a.getPosition().getX() && this.position.getY() == a.getPosition().getY())
			return true;
		return false;
	}
	
}
