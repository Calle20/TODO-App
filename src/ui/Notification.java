package ui;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

import logic.TODO;

public class Notification {

    public static void dateIsOver(TODO todo) throws AWTException {
        if (SystemTray.isSupported()) {
        	Notification td = new Notification();
        	td.displayTray(todo);
            
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public void displayTray(TODO todo) throws AWTException {
       
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "TODO-App Benachrichtigung");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("Eine Aufgabe ist bis heute zu erledigen!");
        tray.add(trayIcon);

        trayIcon.displayMessage(todo.title, todo.description+" bis "+todo.date, MessageType.INFO);
    }
 }