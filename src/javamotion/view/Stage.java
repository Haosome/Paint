//Author: Hao Guo

package javamotion.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.*;

import javamotion.modelInterfaces.*;
import javamotion.Concrete.model.ConcreteModel;
import javamotion.Concrete.model.TextBoxPainter;
import javamotion.command.*;

import javax.swing.*;

/**
 * A Stage is a JComponent for displaying graphical objects.
 * <p>
 * These objects come from a model of type ModelI. Each of these objects must
 * implement interface PainterI.
 * <p>
 * <em>Z-Order</em> The objects will be drawn in the order that they are
 * obtained from the model; thus objects that are later on its list will appear
 * to be in front of those that come earlier.
 * <p>
 * Each Stage object can be associated with one or more ClickListeners. These
 * objects receive information about mouse clicks and other mouse events.
 * <p>
 * Each Stage object can display itself at any level of zoom. A zoom level of 1
 * means that 1 unit the world coordinate system maps to one screen pixel. A
 * zoom level of <i>x</i> means that each unit of world coordinates is mapped to
 * <i>x</i> screen pixels.
 * <p>
 * The (0,0) point in world coordinates is offset both vertically and
 * horizontally my an amount called the "margin". The Stage attempts to size its
 * self so that the same margin also appears at the right and the bottom.
 * <p>
 * <em>Observation</em> Each Stage object acts as an observer of its model. When
 * the model is changed, the Stage reacts by causing itself to be repainted at a
 * later time.
 * <p>
 * The actual painting (when it happens) is done on the GUI thread and is
 * synchronized on the model's lock. Any changes to the model or accesses that
 * require more than a simple read should likewise be synchronized on the
 * model's lock.
 * 
 * @see javamotion.modelInterfaces.ModelI
 * @see javamotion.modelInterfaces.PainterI
 * @see javamotion.modelInterfaces.StageClickListenerI
 * 
 * 
 * 
 * @author Theodore Norvell
 * 
 */
public class Stage extends JScrollPane implements Observer {

	private static final long serialVersionUID = -564005605639032986L;
	// 0:idle,1:adding a object,2:removing a object,3:dragging a object,
	// 4:filling a object,5:changing line or font color;//6:adding line
	// animation
	public int actionFlag;
	public int objectFlag;// 0:rectangle,1:ellipse,2:line,3:text box
	private double worldWidth;
	private double worldHeight;
	private double zoom;
	private double margin;
	private boolean gridOn;
	private int gridSize = 10;
	public Color color;
	public StageMouseListener mouseListener;

	public AffineTransform worldToViewTransform;
	AffineTransform viewToWorldTransform;

	public ModelI model;

	/**
	 * Create a Stage object
	 * 
	 * @param model
	 *            The model
	 * @param worldWidth
	 *            The width of the stage in world units. This does not include
	 *            the margin.
	 * @param worldHeight
	 *            The height of the stage in world units. This does not include
	 *            the margin.
	 * @param zoom
	 *            The zoom level. 1 means no zoom. <em>x</em> means 1 world unit
	 *            equals <em>x</em> screen pixels.
	 * @param gridOn
	 *            Paint a grid under the graphical objects.
	 */
	public Stage(ConcreteModel model, int worldWidth, int worldHeight,
			int zoom, boolean gridOn) {
		actionFlag = 0;
		objectFlag = 0;
		assert worldWidth > 0;
		assert worldHeight > 0;
		assert zoom > 0;
		this.model = model;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.zoom = zoom;
		this.margin = 20;
		this.gridOn = gridOn;
		recomputeWorldToView();
		this.setLayout(null);

		Dimension size = new Dimension((int) (worldWidth * zoom + margin),
				(int) (worldHeight * zoom + margin));
		setMinimumSize(size);
		setPreferredSize(size);
		mouseListener = new StageMouseListener(this);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);

		setOpaque(true);
		setBackground(Color.white);
		model.getObservable().addObserver(this);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private void recomputeWorldToView() {
		worldToViewTransform = new AffineTransform();
		worldToViewTransform.scale(zoom, zoom);
		worldToViewTransform.translate(margin, margin);
		viewToWorldTransform = new AffineTransform(worldToViewTransform);
		try {
			viewToWorldTransform.invert();
		} catch (NoninvertibleTransformException e) {
			// Should not happen
			throw new AssertionError(e);
		}
	}

	/** Get the number of pixels per model unit. */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Set the number of pixels per model unit.
	 * 
	 * @param zoom
	 *            Precondition: zoom > 0
	 */
	public void setZoom(double zoom) {
		assert zoom > 0;
		this.zoom = zoom;
		Dimension size = new Dimension((int) (worldWidth * zoom + 2 * margin),
				(int) (worldHeight * zoom + 2 * margin));
		recomputeWorldToView();
		update(null, null);
		setMinimumSize(size);
		setPreferredSize(size);
		revalidate();
	}

	/**
	 * update() should be called when the model changes its state in some way
	 * that may affect this view.
	 * <p>
	 * Don't confuse this update() with update(Graphics) which is inherited from
	 * Component!
	 * <p>
	 * This schedules a paint on the GUI thread in the future. That paint will
	 * synchronize on the model's lock.
	 * 
	 * @param o
	 *            Not used.
	 * @param arg
	 *            Not used.
	 * @see java.util.Observer#update(Observable, Object)
	 */
	@Override
	public void update(Observable o, Object arg) {

		// The call to repaint causes a future call to paint
		// to be scheduled. This call to paint will cause
		// paintComponent to be called.
		repaint();
	}

	/**
	 * paintComponent is called when the Stage needs to be redisplayed.
	 * 
	 */
	@Override
	protected void paintComponent(Graphics graphics) {

		Graphics2D g2d = (Graphics2D) graphics;
		// Let the UI delegate do its thing, including painting the background.
		super.paintComponent(g2d);
		// Paint the grid
		if (gridOn) {
			for (int x = 0; x < worldWidth; x += gridSize) {
				for (int y = 0; y < worldHeight; y += gridSize) {
					Point2D p2d = new Point2D.Double(x, y);
					Point2D p2d0 = worldToViewTransform.transform(p2d, null);
					g2d.drawRect((int) (p2d0.getX()), (int) (p2d0.getY()), 0, 0);
				}
			}
		}
		// Ask the model's painters to do the rest
		synchronized (model.getLock()) {
			for (PainterI pc : model.getPainterList()) {
				Graphics2D transformedG2D = (Graphics2D) g2d.create();
				transformedG2D.transform(worldToViewTransform);
				pc.paintRequest(transformedG2D);
				transformedG2D.dispose();
			}
			this.removeAll();
			for (TextBoxPainter textbox : model.getTextBoxList()) {
				textbox.getTextBox().setVisible(true);
				this.add(textbox.getTextBox());

			}
		}
	}

	public void playAnimation() {
		synchronized (model.getLock()) {
			for (int i = 0; i < model.getPainterList().size(); i++) {

				PainterI pc = model.getPainterList().get(i);

				DragI drag = (DragI) pc;

				if (drag.getTrackList().size() > 0) {

					Point2D originSv = drag.getOriginPoint();
					Point2D endSv = drag.getEndPoint();

					drag.getTrackList()
							.add(new AppearCommand(drag, null, this));

					for (ConcreteCommand command : drag.getTrackList()) {
						Thread newThread = new Thread(command);
						newThread.run();
					}

					if (originSv != null && endSv != null) {
						drag.setOriginPoint(originSv);
						drag.setEndPoint(endSv);
					}
					model.alert();
				}
			}
		}
	}
}
