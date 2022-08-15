package main;

import javax.swing.SwingUtilities;

import data.Connect;
import ui.Gui;

public class Main {

	public static void main(String[] args) {
		//Es sind noch ein paar Bugs drin, für die ich aber keine Zeit zum beheben hatte. 
		Connect.connect();
		Connect.getPrioritys();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Gui();
			}
		});
	}

}
