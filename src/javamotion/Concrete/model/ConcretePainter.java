//Modifier: Hao Guo

package javamotion.Concrete.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javamotion.modelInterfaces.PainterI;
import javamotion.modelInterfaces.ModelI;

/** A base class for graphical objects.
 * One thing you'll find is missing from this is
 * the ability to move, stretch or rotate objects.
 * @author Theodore Norvell
 *
 */
abstract public class ConcretePainter implements PainterI {
	protected Color drawColor = Color.BLACK;
	protected boolean fill = false ;
	protected Color fillColor = Color.WHITE;
	protected boolean highlighted = false ;
	protected List<Shape> grabbers = new ArrayList<Shape>() ;
	protected ModelI model ;
	protected Point2D pOrigin;
	protected Point2D pEnd;
	
	ConcretePainter( ModelI model ) {
		this.model = model ;
	}
	
	public abstract Shape getShape() ;

	@Override
	public boolean isHitBy(Rectangle2D worldRect) {
		return getShape().intersects( worldRect ) ; 
	}

	@Override
	public void paintRequest(Graphics2D graphics) {
		
		Shape shape = getShape() ;
		if( fill ){
			graphics.setColor( fillColor ) ;
			graphics.fill( shape ) ; }
		graphics.setColor( drawColor ) ;
		graphics.draw( shape ) ;
		if( highlighted ){
			graphics.setColor( Color.BLACK ) ;
			for( Shape grabber : grabbers ) graphics.draw( grabber ) ;
		}
	}
	
	public void setColor( Color color ) {
		synchronized( model.getLock() ) { this.drawColor = color ; }
		model.alert() ;
	}
	
	public void setFill( Color fillColor ) {
		synchronized( model.getLock() ) {
			this.fillColor = fillColor ;
			this.fill = true ; }
		model.alert() ;
	}
	
	public void setHighlighted( boolean highlight ) {
		synchronized( model.getLock() ) { this.highlighted = highlight ; }
		model.alert() ;
	}
	
	public boolean getHighlighted() {
		return highlighted;
	}
	
	
}
