//Author: Hao Guo

package javamotion.Concrete.model;

import java.util.*;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javamotion.modelInterfaces.ModelI;
import javamotion.modelInterfaces.DragI;

import javamotion.command.*;

public class EllipsePainter extends ConcretePainter implements DragI {

	private Ellipse2D shape;
	private ArrayList<ConcreteCommand> trackList;

	public EllipsePainter(Ellipse2D shape, ModelI model) {
		super(model);
		this.shape = shape;
		pOrigin = new Point2D.Double(shape.getX(), shape.getY());
		addGrabbers();
		trackList = new ArrayList<ConcreteCommand>();
	}

	public void addGrabbers() {
		double x0 = shape.getMinX();
		double y0 = shape.getMinY();
		double x1 = shape.getMaxX();
		double y1 = shape.getMaxY();
		grabbers.add(new Ellipse2D.Double(x0 - 4, y0 - 4, 8, 8));
		grabbers.add(new Ellipse2D.Double(x1 - 4, y1 - 4, 8, 8));
		grabbers.add(new Ellipse2D.Double(x0 - 4, y1 - 4, 8, 8));
		grabbers.add(new Ellipse2D.Double(x1 - 4, y0 - 4, 8, 8));
	}

	@Override
	public int isDraggingGrabber(Rectangle2D worldRect) {
		for(int i=0;i<grabbers.size();i++){
			if(grabbers.get(i).intersects( worldRect ))
				return i;
		}
		return -1;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void setOriginPoint(Point2D p) {
		pOrigin = p;

		double width = Math.abs(pOrigin.getX() - pEnd.getX());
		double height = Math.abs(pOrigin.getY() - pEnd.getY());
		shape.setFrame(pOrigin.getX(), pOrigin.getY(), width, height);
		grabbers.clear();
		addGrabbers();
	}
	
	public boolean isHitBy(Rectangle2D worldRect) {
		return shape.getBounds().intersects( worldRect ); 
	}

	@Override
	public void setEndPoint(Point2D p) {
		pEnd = p;

		double x1 = pOrigin.getX();
		double y1 = pOrigin.getY();
		double x2 = pEnd.getX();
		double y2 = pEnd.getY();

		double x0 = Math.min(x1, x2);
		double y0 = Math.min(y1, y2);

		pOrigin.setLocation(x0, y0);
		pEnd.setLocation(Math.max(x1, x2), Math.max(y1, y2));

		double width = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);

		shape.setFrame(x0, y0, width, height);

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
		double x0 = shape.getX();
		double y0 = shape.getY();
		double width = shape.getWidth();
		double height = shape.getHeight();
		shape.setFrame(x0 + x, y0 + y, width, height);
	}

	@Override
	public void clearGrabbers() {
		grabbers.clear();
	}
	
	@Override
	public List<ConcreteCommand> getTrackList(){
		return this.trackList;
	}
	
	@Override
	public void setVisibleTrack(boolean flag){
		if(flag == true){
			for(ConcreteCommand command:trackList){
				model.add(command.painter);
			}
		}
		else{
			for(ConcreteCommand command:trackList){
				model.remove(command.painter);
			}
		}
	}
	
	@Override
	public void centerLocate(Point2D p){
		double width = shape.getWidth();
		double height = shape.getHeight();
		shape.setFrame(p.getX()-width/2, p.getY() - height/2, width, height);
		pOrigin = new Point2D.Double(p.getX()-width/2, p.getY() - height/2);
		pEnd = new Point2D.Double(p.getX()+width/2,	p.getY()+height/2);
	}
}
