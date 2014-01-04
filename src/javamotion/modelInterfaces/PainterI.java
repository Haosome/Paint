//Author: Hao Guo


package javamotion.modelInterfaces;

import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

/** This interface represents graphical objects to the view system.
 * <p>
 * The view system requires that each graphical object be able to display itself
 * and that it recognize when it intersects a given rectangle.
 * 
 * @author Theodore Norvell
 *
 */
public interface PainterI {

	/** paintRequest is called by the view system to display the object.
	 * The implementor may assume that the Graphics2D object passed in to it
	 * will be immediately disposed of after the call. Thus changes to the
	 * Graphics2D object's state need not be undone. The coordinate
	 * system of the Graphics2D object is in world coordinates.
	 * @param graphics
	 */
	void paintRequest(Graphics2D graphics);
	
	/** Does the given rectangle intersect the area where the object is drawn.
	 * Typically the client will use this method to determine whether the graphical
	 * object is under (or nearly under) the mouse.
	 * 
	 * @param worldRect
	 * @return
	 */
	boolean isHitBy( Rectangle2D worldRect );
	
	
	
	void setOriginPoint(Point2D p);
	
	void setEndPoint(Point2D p);

	Shape getShape();
}
