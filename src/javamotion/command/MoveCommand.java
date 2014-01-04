package javamotion.command;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

import javamotion.view.*;

import javamotion.modelInterfaces.*;

public class MoveCommand extends ConcreteCommand {

	public MoveCommand(DragI object, PainterI painter, Stage stage) {
		super(object, painter, stage);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Line2D temp = (Line2D) painter.getShape();

		Point2D p1 = temp.getP1();
		Point2D p2 = temp.getP2();


		object.centerLocate(p1);
		double x, y;

		x = p2.getX() - p1.getX();
		y = p2.getY() - p1.getY();

		double x0 = x / 100;
		double y0 = y / 100;

		for (int i = 0, j = 0; i < 100 && j < 100; i += 1, j += 1) {
			object.setOriginPoint(new Point2D.Double(object.getOriginPoint()
					.getX() + x0, object.getOriginPoint().getY() + y0));

			object.setEndPoint(new Point2D.Double(object.getEndPoint().getX()
					+ x0, object.getEndPoint().getY() + y0));
			stage.paint(stage.getGraphics());
			try {
				Thread.sleep(5);
			} catch (Exception ex) {
			}

		}
		stage.paint(stage.getGraphics());

	}

}
