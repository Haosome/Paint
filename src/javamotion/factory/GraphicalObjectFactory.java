//Author: Hao Guo


package javamotion.factory;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javamotion.modelInterfaces.ModelI;
import javamotion.Concrete.model.*;

public class GraphicalObjectFactory {
	public static ConcretePainter CreateObject(int type,double x,double y,ModelI Model ){
		
		switch (type){
		case 0:
			return new RectPainter(new Rectangle2D.Double(x,y,0,0),Model);
		case 1:
			return new EllipsePainter(new Ellipse2D.Double(x,y,0,0),Model);
		case 2:
			return new LinePainter(new Line2D.Double(x,y,x,y),Model);
			default:return null;
		}
	}
}
