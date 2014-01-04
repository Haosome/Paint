//Author: Hao Guo

package javamotion.Concrete.model;

import java.util.*;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javamotion.modelInterfaces.DragI;
import javamotion.modelInterfaces.ModelI;
import javamotion.modelInterfaces.PainterI;

import javamotion.command.*;

/** A graphical object representing lines */
public class LinePainter extends ConcretePainter implements DragI {

	private Line2D shape;
	private ArrayList<ConcreteCommand> trackList;

	public LinePainter(Line2D shape, ModelI model) {
		super(model);
		this.shape = shape;
		trackList = new ArrayList<ConcreteCommand>();
		pOrigin = new Point2D.Double(shape.getX1(), shape.getY1());
		addGrabbers();
	}

	public void addGrabbers() {
		Line2D line = shape;
		double x0 = line.getX1();
		double y0 = line.getY1();
		double x1 = line.getX2();
		double y1 = line.getY2();
		grabbers.add(new Ellipse2D.Double(x0 - 4, y0 - 4, 8, 8));
		grabbers.add(new Ellipse2D.Double(x1 - 4, y1 - 4, 8, 8));
	}

	@Override
	public int isDraggingGrabber(Rectangle2D worldRect) {
		for (int i = 0; i < grabbers.size(); i++) {
			if (grabbers.get(i).intersects(worldRect))
				return i;
		}
		return -1;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	public void setOriginPoint(Point2D p) {
		pOrigin = p;

		shape.setLine(pOrigin, pEnd);
		grabbers.clear();
		addGrabbers();
	}

	public void setEndPoint(Point2D p) {
		pEnd = p;

		shape.setLine(pOrigin, pEnd);

		grabbers.clear();
		addGrabbers();
	}

	@Override
	public boolean mousePosition(Rectangle2D worldRect) {
		return this.isHitBy(worldRect);
	}

	@Override
	public Point2D getOriginPoint() {
		return pOrigin;
	}

	@Override
	public Point2D getEndPoint() {
		return pEnd;
	}

	@Override
	public void Move(double x, double y) {
		double x1 = shape.getX1();
		double y1 = shape.getY1();
		double x2 = shape.getX2();
		double y2 = shape.getY2();
		shape.setLine(x1 + x, y1 + y, x2 + x, y2 + y);
	}

	@Override
	public void clearGrabbers() {
		grabbers.clear();
	}

	@Override
	public List<ConcreteCommand> getTrackList() {
		return this.trackList;
	}

	@Override
	public void setVisibleTrack(boolean flag) {
		if (flag == true) {
			for (ConcreteCommand command : trackList) {
				model.add(command.painter);
			}
		} else {
			for (ConcreteCommand command : trackList) {
				model.remove(command.painter);
			}
		}
	}

	@Override
	public void centerLocate(Point2D p) {

		double x0 = p.getX();
		double y0 = p.getY();
		double x1 = shape.getP1().getX();
		double y1 = shape.getP1().getY();
		double x2 = shape.getP2().getX();
		double y2 = shape.getP2().getY();

		shape.setLine((x1 - x2) / 2 + x0, (y1 - y2) / 2 + y0, (x2 - x1) / 2
				+ x0, (y2 - y1) / 2 + y0);
		pOrigin = new Point2D.Double((x1 - x2) / 2 + x0, (y1 - y2) / 2 + y0);
		pEnd = new Point2D.Double((x2 - x1) / 2 + x0, (y2 - y1) / 2 + y0);
	}
}
