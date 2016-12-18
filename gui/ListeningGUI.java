package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Cette classe spécialise JFrame sans rien y ajouter.
 * 
 * Cette classe implémente WindowListener et redéfinit toutes les méthodes
 * de cette interface comme si elle ne faisaient rien.
 * 
 * De ce fait créer une classe qui spécialise ceLle-ci permet de créer une JFrame 
 * possèdant déjà des méthodes pour gérer le comportement de l'IHM :
 * plus besoins de toutes les réécrire dans son code si elles ne sont pas nécessaires.
 */
@SuppressWarnings("serial")
public abstract class ListeningGUI 
extends JFrame
implements WindowListener
{
	
	protected ListeningGUI(String name)
	{
		super(name);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}	
}
