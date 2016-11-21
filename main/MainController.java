package main;

import connect.ConnectionManager;

public class MainController 
{
	//Attributes
	/**
	 * Connexion vers le SGBD.
	 */
	private ConnectionManager connector;
	
	/**
	 * Vue du controleur.
	 */
	private MainView mhi;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 * 
	 * @param connector : un objet ConnectionManager.
	 */
	public MainController(ConnectionManager connector)
	{
		this.connector = connector;
		this.mhi = new MainView();
	}
}
