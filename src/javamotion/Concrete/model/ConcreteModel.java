//Modifier: Hao Guo

package javamotion.Concrete.model;

import java.util.*;

import javamotion.modelInterfaces.ModelI;
import javamotion.modelInterfaces.PainterI;
import javamotion.Concrete.model.TextBoxPainter;

/** A simple model for testing the Stage class.
 * <p>
 * Note the use of synchronization for every mutator.
 * <p> It is assumed that any method processing the list returned
 * from getPainterList() is synchronized on this object while it
 * gets and then processes the list.
 * @see javamotion.modelInterfaces.ModelI
 * */
public class ConcreteModel extends Observable implements ModelI {

	LinkedList<PainterI> list = new LinkedList<PainterI>() ;
	List<TextBoxPainter> TextBoxList = new ArrayList<TextBoxPainter>();
	
	@Override
	public LinkedList<PainterI> getPainterList() {
		return list;
	}
	
	public List<TextBoxPainter> getTextBoxList(){
		return this.TextBoxList;
	}
	
	public void add(PainterI painter) {
		synchronized(this) { list.add( painter ) ; }
		alert() ;
	}
	
	public void add(TextBoxPainter TextBox){
		synchronized(this) { TextBoxList.add( TextBox ) ; }
		alert() ;
	}
	
	public void remove(PainterI painter) {
		synchronized(this) { list.remove( painter ) ; }
		alert() ;
	}
	
	public void remove(TextBoxPainter textBox){
		synchronized(this) { TextBoxList.remove( textBox ) ; }
		alert() ;
	}
	
	public void alert() {
		setChanged() ; notifyObservers() ;
	}

	@Override
	public Observable getObservable() {
		return this;
	}

	@Override
	public Object getLock() {
		return this;
	}

}
