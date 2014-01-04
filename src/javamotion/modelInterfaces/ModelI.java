
//Author: Hao Guo

package javamotion.modelInterfaces;

import java.util.*;

import javamotion.Concrete.model.TextBoxPainter;

/**
 * A model consisting at least of a list of graphical objects. Each such object
 * is represented by an item of type PainterI.
 * 
 * @author Theodore Norvell
 * 
 */
public interface ModelI {

	/**
	 * A list of objects to be painted. Clients should paint the objects from
	 * first to last and thus objects at the end of the list will appear to be
	 * in front of those at the back.
	 * <p>
	 * The client must not use this iterator to delete any elements.
	 * <p>
	 * A client should only use this method and process the list as while
	 * synchronized on this object's lock.
	 * 
	 * @see #getLock()
	 * 
	 * @return an iterator listing all PainterI objects held by the model.
	 */
	public LinkedList<PainterI> getPainterList();

	/**
	 * Normally this just returns the model object itself. However it may also
	 * return its delegate.
	 * 
	 * @return
	 */
	public Observable getObservable();

	/**
	 * This method should consistently return the same object. Normally this
	 * just returns the model object itself. Clients should own (i.e. be
	 * synchronized on) the returned object while processing the list of
	 * painters returned from getPainterList(). Likewise all access to the model
	 * should be synchronized on this object.
	 * 
	 * @return
	 */
	public Object getLock();

	public void alert();

	public void add(PainterI painter);

	public void remove(PainterI painter);
	
	public void remove(TextBoxPainter textBox);

	public List<TextBoxPainter> getTextBoxList();

	public void add(TextBoxPainter TextBox);
}