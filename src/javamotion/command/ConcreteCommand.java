package javamotion.command;

import javamotion.modelInterfaces.DragI;
import javamotion.modelInterfaces.*;
import javamotion.view.*;

public abstract class ConcreteCommand implements Runnable {

	protected DragI object;
	public PainterI painter;
	protected ModelI model;
	protected Stage stage;

	ConcreteCommand(DragI object, PainterI painter,Stage stage){
		this.object = object;
		this.painter = painter;
		this.model = model;
		this.stage = stage;
	}

}
