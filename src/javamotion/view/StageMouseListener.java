/**
 * Author: Hao Guo

 */
package javamotion.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javamotion.factory.GraphicalObjectFactory;
import javamotion.main.gui.Main;
import javamotion.modelInterfaces.PainterI;
import javamotion.modelInterfaces.DragI;
import javamotion.Concrete.model.*;
import javamotion.command.*;

import javax.swing.JTextField;

/**
 * A class to relay mouse events to listeners Objects of this class listen for
 * mouse events on a Stage and forwards them to a the Stage's listeners.
 * 
 * @author theo
 * 
 */
public class StageMouseListener implements MouseListener, MouseMotionListener {

	private Stage stage;
	public DragI currentObject;
	private Point2D currentPoint;
	private int grabber;

	/**
	 * @param stage
	 */
	StageMouseListener(Stage stage) {
		this.stage = stage;
		currentObject = null;
	}

	public void mouseClicked(MouseEvent evt) {
		Point2D worldPoint = getWorldPoint(evt);
		Rectangle2D worldRect = getWorldRect(worldPoint);

		stage.requestFocus();

		if (stage.actionFlag == 2) {
			DragI toBeDelete = getPaintersHit(worldRect);
			if (toBeDelete != null) {
				
				for(ConcreteCommand command:toBeDelete.getTrackList()){
					stage.model.remove(command.painter);
				}
				
				stage.model.remove((PainterI) toBeDelete);
			}
		} else if (stage.actionFlag == 4) {
			if (currentObject instanceof ConcretePainter) {
				currentObject.setFill(stage.color);
			}
		} else if (stage.actionFlag == 5) {
			if (currentObject instanceof ConcretePainter) {
				currentObject.setColor(stage.color);
			}
		}

	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mousePressed(MouseEvent evt) {
		Point2D worldPoint = getWorldPoint(evt);
		Rectangle2D worldRect = getWorldRect(worldPoint);

		double x0 = worldPoint.getX();
		double y0 = worldPoint.getY();

		if (stage.actionFlag == 1 || stage.actionFlag == 6) {
			if (stage.objectFlag == 3) {

				JTextField newTextBox = new JTextField();
				newTextBox.setBounds((int) x0, (int) y0, 0, 0);
				TextBoxPainter TextBox = new TextBoxPainter(newTextBox, stage,
						stage.model);
				stage.model.add(TextBox);
			} else {
				ConcretePainter newObject = GraphicalObjectFactory
						.CreateObject(stage.objectFlag, x0, y0, stage.model);
				stage.model.add(newObject);
				if (stage.actionFlag == 6 && currentObject != null)
					currentObject.getTrackList().add(
							new MoveCommand(currentObject, newObject, stage));
			}
		} else if (stage.actionFlag == 0 || stage.actionFlag == 4
				|| stage.actionFlag == 5) {

			if (currentObject instanceof ConcretePainter) {
				ConcretePainter temp = (ConcretePainter) currentObject;
				temp.setHighlighted(false);
			}

			currentObject = getPaintersHit(worldRect);

			if (currentObject instanceof ConcretePainter) {
				ConcretePainter tempPainter = (ConcretePainter) currentObject;
				tempPainter.setHighlighted(true);
			}
		}
		currentPoint = worldPoint;

		if (currentObject != null) {

			if (currentObject.mousePosition(worldRect) && stage.actionFlag == 0) {

				stage.actionFlag = 3;
				grabber = currentObject.isDraggingGrabber(worldRect);
			}
		}

		if (currentObject != null) {
			Main.lineAnimaitonButton.setEnabled(true);
			Main.disappearButton.setEnabled(true);
			Main.appearButton.setEnabled(true);
		} else {
			Main.lineAnimaitonButton.setEnabled(false);
			Main.disappearButton.setEnabled(false);
			Main.appearButton.setEnabled(false);
		}
	}

	public void mouseReleased(MouseEvent evt) {

		if (stage.actionFlag == 3) {
			stage.actionFlag = 0;
			grabber = -1;
		}

	}

	public void mouseDragged(MouseEvent evt) {
		Point2D worldPoint = getWorldPoint(evt);

		if (stage.actionFlag == 1 || stage.actionFlag == 6) {
			if (stage.objectFlag == 3) {
				stage.model.getTextBoxList()
						.get(stage.model.getTextBoxList().size() - 1)
						.setEndPoint(worldPoint);
			} else {
				stage.model.getPainterList()
						.get(stage.model.getPainterList().size() - 1)
						.setEndPoint(worldPoint);

			}
			stage.model.alert();
		} else if (stage.actionFlag == 3) {

			if (currentObject != null) {

				Point2D pOrigin = currentObject.getOriginPoint();
				Point2D pEnd = currentObject.getEndPoint();
				if (grabber == -1) {
					double x = worldPoint.getX() - currentPoint.getX();
					double y = worldPoint.getY() - currentPoint.getY();

					currentObject.setHighlighted(false);
					currentObject.clearGrabbers();
					currentObject.Move(x, y);
					currentObject.addGrabbers();
					currentObject.setHighlighted(true);
					currentPoint = worldPoint;
				} else {

					double x0 = worldPoint.getX();
					double y0 = worldPoint.getY();

					double x1 = currentObject.getOriginPoint().getX();
					double y1 = currentObject.getOriginPoint().getY();

					double x2 = currentObject.getEndPoint().getX();
					double y2 = currentObject.getEndPoint().getY();

					if (grabber == 0) {
						if (x0 <= x2 && y0 <= y2 || stage.objectFlag == 2)
							currentObject.setOriginPoint(worldPoint);
					} else if (grabber == 1) {
						if (x0 >= x1 && y0 >= y1 || stage.objectFlag == 2)
							currentObject.setEndPoint(worldPoint);
					} else if (grabber == 2) {

						if (x0 <= x2 && y0 >= y1 || stage.objectFlag == 2) {
							currentObject.setOriginPoint(new Point2D.Double(
									worldPoint.getX(), pOrigin.getY()));
							currentObject.setEndPoint(new Point2D.Double(pEnd
									.getX(), worldPoint.getY()));
						}
					} else if (grabber == 3) {

						if (x0 >= x1 && y0 <= y2 || stage.objectFlag == 2) {
							currentObject.setEndPoint(new Point2D.Double(
									worldPoint.getX(), pEnd.getY()));
							currentObject.setOriginPoint(new Point2D.Double(
									pOrigin.getX(), worldPoint.getY()));
						}
					}
				}
				stage.model.alert();
			}
		}

	}

	public void mouseMoved(MouseEvent evt) {
	}

	private Point2D getWorldPoint(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		Point2D viewPoint = new Point2D.Double(x, y);
		return this.stage.viewToWorldTransform.transform(viewPoint, null);
	}

	private Rectangle2D getWorldRect(Point2D worldPoint) {
		double x0 = worldPoint.getX();
		double y0 = worldPoint.getY();
		return new Rectangle2D.Double(x0 - 5, y0 - 5, 10, 10);
	}

	private DragI getPaintersHit(Rectangle2D worldPoint) {

		DragI currentObject = null;

		synchronized (this.stage.model.getLock()) {

			for (int i = stage.model.getTextBoxList().size() - 1; i >= 0; i--) {
				if (stage.model.getTextBoxList().get(i).isHitBy(worldPoint)) {
					currentObject = stage.model.getTextBoxList().get(i);
					break;
				}
			}
			if (currentObject == null) {
				for (int i = stage.model.getPainterList().size() - 1; i >= 0; i--) {
					if (stage.model.getPainterList().get(i).isHitBy(worldPoint)) {
						currentObject = (DragI) stage.model.getPainterList()
								.get(i);
						break;
					}
				}

			}
		}
		return currentObject;
	}
}