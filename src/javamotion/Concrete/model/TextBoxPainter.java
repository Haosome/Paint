//Author: Hao Guo

//This Class represents text box 
package javamotion.Concrete.model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javamotion.modelInterfaces.DragI;
import javamotion.modelInterfaces.ModelI;
import javamotion.modelInterfaces.PainterI;
import javamotion.view.Stage;

import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

import javamotion.command.*;

public class TextBoxPainter implements DragI {

	private JTextField TextBox;
	private Rectangle2D frame;

	private ArrayList<ConcreteCommand> trackList;
	
	private ModelI model;

	private Stage stage;

	private Point2D currentPoint;

	private Point2D pOrigin;
	private Point2D pEnd;

	protected List<Shape> grabbers = new ArrayList<Shape>();

	public TextBoxPainter(JTextField TextBox, Stage stage, ModelI model) {
		this.stage = stage;
		this.model = model;
		this.TextBox = TextBox;
		pOrigin = new Point2D.Double(TextBox.getX(), TextBox.getY());
		frame = new Rectangle2D.Double(0, 0, 0, 0);
		addGrabbers();
		trackList = new ArrayList<ConcreteCommand>();
	}

	public void addGrabbers() {

		double x0 = TextBox.getX();
		double y0 = TextBox.getY();
		double x1 = x0 + TextBox.getWidth();
		double y1 = y0 + TextBox.getHeight();
		grabbers.add(new Ellipse2D.Double(x0 - 25, y0 - 25, 8, 8));
		grabbers.add(new Ellipse2D.Double(x1 - 25, y1 - 25, 8, 8));
		grabbers.add(new Ellipse2D.Double(x0 - 25, y1 - 25, 8, 8));
		grabbers.add(new Ellipse2D.Double(x1 - 25, y0 - 25, 8, 8));

		TextBox.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent evt) {
			}

			public void mouseDragged(MouseEvent evt) {

				double x0 = evt.getX() - currentPoint.getX();
				double y0 = evt.getY() - currentPoint.getY();

				Move(x0, y0);

				currentPoint = new Point2D.Double(evt.getX(), evt.getY());

			}
		});

		TextBox.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent evt) {
			}

			public void mouseExited(MouseEvent evt) {
			}

			public void mousePressed(MouseEvent evt) {
				currentPoint = new Point2D.Double(evt.getX(), evt.getY());
			}

			public void mouseClicked(MouseEvent evt) {
				if (stage.actionFlag == 2)
					model.remove(getThis());
				else if (stage.actionFlag == 4) {
					getThis().setFill(stage.color);

				} else if (stage.actionFlag == 5) {
					getThis().setColor(stage.color);
				}
			}

			public void mouseReleased(MouseEvent evt) {
				grabbers.clear();
				getThis().addGrabbers();
			}

		});
	}

	@Override
	public int isDraggingGrabber(Rectangle2D worldRect) {
		for (int i = 0; i < grabbers.size(); i++) {
			if (grabbers.get(i).intersects(worldRect))
				return i;
		}
		return -1;
	}

	public JTextField getTextBox() {
		return this.TextBox;
	}

	public boolean isHitBy(Rectangle2D worldRect) {
		// TODO Auto-generated method stub
		if (TextBox.hasFocus() || isDraggingGrabber(worldRect) != -1)
			return true;
		else
			return false;
	}

	public void setOriginPoint(Point2D p) {
		// TODO Auto-generated method stub
		pOrigin = p;

		double width = Math.abs(pOrigin.getX() - pEnd.getX());
		double height = Math.abs(pOrigin.getY() - pEnd.getY());
		TextBox.setBounds((int) pOrigin.getX() + 20, (int) pOrigin.getY() + 20,
				(int) width, (int) height);
		grabbers.clear();
		addGrabbers();
	}

	public void setEndPoint(Point2D p) {
		// TODO Auto-generated method stub
		pEnd = p;

		double x1 = pOrigin.getX();
		double y1 = pOrigin.getY();
		double x2 = pEnd.getX();
		double y2 = pEnd.getY();

		double x0 = Math.min(x1, x2);
		double y0 = Math.min(y1, y2);

		double width = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);

		TextBox.setBounds((int) x0 + 20, (int) y0 + 20, (int) width,
				(int) height);
		frame.setRect(x0 - 5, y0 - 5, width + 10, height + 10);

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
		double x0 = TextBox.getX();
		double y0 = TextBox.getY();
		double width = TextBox.getWidth();
		double height = TextBox.getHeight();
		TextBox.setBounds((int) (x0 + x), (int) (y0 + y), (int) width,
				(int) height);
	}

	@Override
	public void setHighlighted(boolean highlight) {

	}

	@Override
	public void clearGrabbers() {
		grabbers.clear();
	}

	public List<Shape> getGrabbers() {
		return this.grabbers;
	}

	private TextBoxPainter getThis() {
		return this;
	}

	public void setFill(Color fillColor) {
		this.TextBox.setBackground(fillColor);
	}

	public void setColor(Color color) {
		this.TextBox.setForeground(color);
	}
	
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
		
		double x1 = pOrigin.getX();
		double y1 = pOrigin.getY();
		double x2 = pEnd.getX();
		double y2 = pEnd.getY();

		double width = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);
		TextBox.setBounds((int)(p.getX()-width/2), (int)(p.getY() - height/2), (int)width, (int)height);
		pOrigin = new Point2D.Double(p.getX()-width/2, p.getY() - height/2);
		pEnd = new Point2D.Double(p.getX()+width/2,	p.getY()+height/2);
	}
}
