package main;

import connect.ConnectionManager;
import create.CreateTableController;
import sql.SQLController;

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
	
	/**
	 * Vrai si et seulement si la vue graphique est 
	 * déjà ouverte, faux sinon.
	 */
	private boolean mhiMode;
	
	/**
	 * Vrai si et seulement si la vue SQL est déjà
	 * ouverte, faux sinon.
	 */
	private boolean sqlMode;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 * 
	 * @param connector : un objet ConnectionManager.
	 */
	public MainController(ConnectionManager connector)
	{
		this.connector = connector;
		this.mhi = new MainView(this);
		this.mhi.talk("Bienvenue " + this.connector.user());
	}
	
	
	/**
	 * Affiche la vue pour utiliser un SGBD en mode graphique
	 * si et seulement si il n'existe pas déjà d'instance 
	 * de cette dernière, ne fais rien sinon.
	 */
	public void openMhiMode()
	{
		if (!this.mhiMode) {
			this.mhiMode = true;
			new CreateTableController(this.connector);
		}
	}
	
	/**
	 * Affiche la vue pour utiliser un SGBD en mode SQL
	 * si et seulement si il n'existe pas déjà d'instance 
	 * de cette dernière, ne fais rien sinon.
	 */
	public void openSqlMode()
	{
		if (!this.sqlMode) {
			this.sqlMode = true;
			new SQLController(this.connector);
		}
	}
}
