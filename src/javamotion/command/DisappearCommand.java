package javamotion.command;

import javamotion.modelInterfaces.DragI;
import javamotion.modelInterfaces.PainterI;
import javamotion.view.Stage;

public class DisappearCommand extends ConcreteCommand {

	public DisappearCommand(DragI object, PainterI painter, Stage stage) {
		super(object, painter, stage);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PainterI tPainter = (PainterI) object;
		try {
			Thread.sleep(500);
		} catch (Exception ex) {
		}
		stage.model.remove(tPainter);
		stage.paint(stage.getGraphics());
		try {
			Thread.sleep(500);
		} catch (Exception ex) {
		}
	}

}
