package main;

import manager.xml.DefaultValueManager;
import facade.HomeFacade;
import factory.MainFactory;
import gui.ConnectionGUI;
import controller.HomeController;


/**
 * Initialise l'application.
 */
public class Launcher {

	private DefaultValueManager dvm;
	private MainFactory factory;
	private HomeController hc;
	private HomeFacade facade;
	
	
	protected Launcher()
	{
		this.initApplication();
	}
	
	
	/**
	 * @param args : aucun argument.
	 */
	public static void main(String[] args) 
	{
		Launcher main = new Launcher();
		main.launchApplication();
	}
	
	
	/**
	 * Lance l' IHM de l'application.
	 */
	private void launchApplication()
	{
		new ConnectionGUI(this.hc);
	}
	
	
	/**
	 * Initialise le backend de l'application.
	 */
	private void initApplication()
	{
		this.dvm = new DefaultValueManager();
		this.factory = new MainFactory(MainFactory.NULL);
		this.facade = new HomeFacade(dvm, factory);
		this.hc = new HomeController(facade);
	}
}
