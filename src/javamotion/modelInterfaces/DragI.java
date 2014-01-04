//Author: Hao Guo

package javamotion.modelInterfaces;

import java.util.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

import javamotion.command.*;

public interface DragI {

	// false mean is not on a grabber, other means the index of the grabber on
	// the list
	public int isDraggingGrabber(Rectangle2D worldRect);

	// set upper left point of the graphical object
	public void setOriginPoint(Point2D p);

	// set lower right point of the graphical object
	public void setEndPoint(Point2D p);

	// get upper left point of the graphical object
	public Point2D getOriginPoint();

	// get lower right point of the graphical object
	public Point2D getEndPoint();

	public void setHighlighted(boolean highlight);

	// return false means mouse is out of the object, true mouse is on the
	// object
	public boolean mousePosition(Rectangle2D worldRect);

	// move the object by x and y
	public void Move(double x, double y);

	public void addGrabbers();

	public void clearGrabbers();

	public void setFill(Color fillColor);

	public void setColor(Color color);
	
	public List<ConcreteCommand> getTrackList();
	
	public void setVisibleTrack(boolean flag);
	
	public void centerLocate(Point2D p);
}
