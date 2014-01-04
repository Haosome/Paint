package javamotion.command;

import javamotion.modelInterfaces.DragI;
import javamotion.modelInterfaces.PainterI;
import javamotion.view.Stage;

public class AppearCommand extends ConcreteCommand {

	public AppearCommand(DragI object, PainterI painter, Stage stage) {
		super(object, painter, stage);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(500);
		} catch (Exception ex) {
		}

		PainterI tPainter = (PainterI) object;
		stage.model.remove(tPainter);
		stage.paint(stage.getGraphics());
		stage.model.getPainterList().addFirst(tPainter);
		stage.paint(stage.getGraphics());

		try {
			Thread.sleep(500);
		} catch (Exception ex) {
		}
	}

}
