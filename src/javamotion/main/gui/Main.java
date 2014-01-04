package javamotion.main.gui;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javamotion.Concrete.model.ConcreteModel;
import javamotion.modelInterfaces.DragI;
import javamotion.view.*;
import javamotion.command.*;

import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.JColorChooser;

/**
 * This class is just used to test the Stage and associated classes.
 * 
 * @author theo
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = -5870471398418882533L;

	final Stage stage;
	final ConcreteModel mainModel = new ConcreteModel();
	private JPanel colorPanel;
	private Color color;

	public static JButton lineAnimaitonButton;
	public static JButton disappearButton;
	public static JButton appearButton;

	private Main() {

		stage = new Stage(mainModel, 200, 200, 1, false);
		stage.setBackground(new Color(230, 230, 255));
		final JScrollPane mainScrollPane = new JScrollPane(stage);
		final double ZOOM_FACTOR = 1.6;

		colorPanel = new JPanel();

		JButton zoomIn = new JButton("Zoom in");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double zoom = stage.getZoom();
				stage.setZoom(ZOOM_FACTOR * zoom);
			}
		});

		JButton zoomOut = new JButton("Zoom out");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double zoom = stage.getZoom();
				stage.setZoom(zoom / ZOOM_FACTOR);
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(500, 300));
		mainPanel.add(mainScrollPane, BorderLayout.CENTER);
		mainPanel.add(zoomIn, BorderLayout.NORTH);
		mainPanel.add(zoomOut, BorderLayout.SOUTH);

		Icon rectIcon = new ImageIcon("Icon/retanglec.png");
		Icon ellipseIcon = new ImageIcon("Icon/Ellipsec.png");
		Icon lineIcon = new ImageIcon("Icon/linec.png");
		Icon textIcon = new ImageIcon("Icon/textc.png");
		Icon mouseIcon = new ImageIcon("Icon/selectc.png");
		Icon removeIcon = new ImageIcon("Icon/deletec.png");
		Icon paletteIcon = new ImageIcon("Icon/pickc.png");
		Icon fillIcon = new ImageIcon("Icon/fillc.png");
		Icon lineColorIcon = new ImageIcon("Icon/changec.png");

		JButton rectButton = new JButton(rectIcon);
		JButton ellipseButton = new JButton(ellipseIcon);
		JButton lineButton = new JButton(lineIcon);
		JButton textButton = new JButton(textIcon);
		JButton mouseButton = new JButton(mouseIcon);
		JButton removeButton = new JButton(removeIcon);
		JButton paletteButton = new JButton(paletteIcon);
		JButton fillButton = new JButton(fillIcon);
		JButton lineColorButton = new JButton(lineColorIcon);
		JButton playButton = new JButton("Play");
		lineAnimaitonButton = new JButton("Path");
		disappearButton = new JButton("Disappear");
		appearButton = new JButton("Appear");

		lineAnimaitonButton.setEnabled(false);
		appearButton.setEnabled(false);
		disappearButton.setEnabled(false);

		rectButton.setPreferredSize(new Dimension(80, 80));
		ellipseButton.setPreferredSize(new Dimension(80, 80));
		lineButton.setPreferredSize(new Dimension(80, 80));
		textButton.setPreferredSize(new Dimension(80, 80));
		mouseButton.setPreferredSize(new Dimension(80, 80));
		removeButton.setPreferredSize(new Dimension(80, 80));
		paletteButton.setPreferredSize(new Dimension(80, 80));
		fillButton.setPreferredSize(new Dimension(80, 80));
		lineColorButton.setPreferredSize(new Dimension(80, 80));
		lineAnimaitonButton.setPreferredSize(new Dimension(80, 80));
		playButton.setPreferredSize(new Dimension(80, 80));
		appearButton.setPreferredSize(new Dimension(80, 80));
		disappearButton.setPreferredSize(new Dimension(80, 80));

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 2;
			}
		});

		rectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 1;
				stage.objectFlag = 0;
				stage.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});

		ellipseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 1;
				stage.objectFlag = 1;
				stage.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});

		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 1;
				stage.objectFlag = 2;
				stage.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});

		textButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 1;
				stage.objectFlag = 3;
				stage.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		});

		mouseButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 0;
				stage.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		paletteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JColorChooser chooser = new JColorChooser(); // 实例化颜色选择器
				color = chooser.showDialog(Main.this, "选取颜色", Color.lightGray); // 得到选择的颜色
				if (color == null) // 如果未选取
					color = Color.gray; // 则设置颜色为灰色
				colorPanel.setBackground(color); // 改变面板的背景色
			}
		});

		fillButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 4;
				stage.setColor(color);
			}
		});

		lineColorButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 5;
				stage.setColor(color);
			}
		});

		lineAnimaitonButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.actionFlag = 6;
				stage.objectFlag = 2;
			}
		});

		playButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.playAnimation();
			}
		});

		appearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stage.mouseListener.currentObject.getTrackList().add(
						new AppearCommand(stage.mouseListener.currentObject,
								null, stage));
				
				
			}
		});

		disappearButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				{
					stage.mouseListener.currentObject.getTrackList().add(
							new DisappearCommand(stage.mouseListener.currentObject,
									null, stage));
				}
			}
		});

		JPanel palette = new JPanel();
		palette.setLayout(new GridLayout(10, 1, 2, 10));

		
		JPanel commandPanel = new JPanel();
		
		
		
		final JScrollPane paletteScroll = new JScrollPane(palette);

		palette.add(rectButton);
		palette.add(ellipseButton);
		palette.add(lineButton);
		palette.add(textButton);
		palette.add(mouseButton);
		palette.add(removeButton);
		palette.add(paletteButton);
		palette.add(fillButton);
		palette.add(lineColorButton);
		palette.add(lineAnimaitonButton);
		palette.add(playButton);
		palette.add(disappearButton);
		palette.add(appearButton);
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				paletteScroll, mainPanel);
		mainPane.setDividerLocation(0.1);
		add(mainPane, BorderLayout.CENTER);

		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}
}
