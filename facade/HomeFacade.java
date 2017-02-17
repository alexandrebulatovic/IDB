package facade;

import java.sql.Connection;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.xml.DefaultValueManager;
import useful.ConnectionStrings;
import useful.Response;
import factory.MainFactory;

/**
 * Façade entre les différents controleurs et les gestionnaires.
 */
public class HomeFacade 
{
	// Attributs
	/** Gestionnaire de paramètres de connexion par défaut.*/
	private DefaultValueManager dvm;
	
	/** Fabrique principale de l'application.*/
	private MainFactory factory;
	
	/** Gestionnaire de connexion.*/
	private I_ConnectionManager connector;
	
	/** Tables disponibles.*/
	private TableSet tables;
	
	
	//Constructeurs
	/**
	 * Constructeur lambda.
	 * 
	 * @param dvm : null interdit.
	 * @param factory : null interdit.
	 * @param tables : null interdit.
	 */
	public HomeFacade(DefaultValueManager dvm, MainFactory factory, TableSet tables)
	{
		this.dvm = dvm;
		this.factory = factory;
		this.tables = tables;
	}
	
	
	//Méthodes
	/**
	 * @return la liste des différents SGBD disponibles.
	 */
	public String[] getAvailableDBMS()
	{
		return this.factory.getAvailableDBMS();
	}
	
	
	/**
	 * Force à relire le fichier XML.
	 */
	public void reloadDefaultValue()
	{
		this.dvm.reload();
	}
	
	
	/**
	 * @return les informations de la dernière connexion valide
	 * du SGBD actuellement référencé dans les paramètres de connexion par défaut.
	 */
	public ConnectionStrings getDefaultValues()
	{
		return new ConnectionStrings(
				this.dvm.getDriver(), 
				this.dvm.getUrl(), 
				this.dvm.getUser(), 
				"", //mot de passe
				this.dvm.getDataBase(), 
				this.dvm.getPort());
	}
	
	
	/**
	 * Enregistre $driver comme étant le dernier SGBD connecté.
	 * 
	 * @param dbms : nom du pilote de SGBD, null interdit.
	 * @return Les informations de la dernière connexion valide
	 * avec le pilote $dbms.
	 */
	public ConnectionStrings getDefaultValues(String dbms) 
	{
		this.dvm.setDriver(dbms);
		return this.getDefaultValues();
	}
	
	
	/**
	 * Tente d'établir une connexion vers un SGBD
	 * en utilisant les informations de connexion de $parameters. <br/>
	 * Lorsque la connexion est établie, la fabrique de la façade est mutée
	 * pour correspondre au SGBD connecté.
	 * 
	 * @param parameters : un objet ConnectionStrings, null interdit.
	 * @return Une réponse personnalisée qui décrit la tentative de connexion.
	 */
	public Response connect(ConnectionStrings parameters)
	{
		this.factory.setDBMS(parameters.driver);
		this.connector = this.factory.getConnectionManager();
		return this.connector.connect(parameters);
	}
	
	
	/**
	 * Pré-requis : utilisation de la méthode connect(), avec pour retour
	 * une réponse personnalisée décrivant le succès de la connexion.
	 * 
	 * @return la connexion établie.
	 */
	public Connection getConnection() {return this.connector.getConnection();}
	
	
	/**
	 * Enregistre certaines informations de connexion de $parameters
	 * dans un fichier xml  situé dans le répertoire courant.
	 * Le fichier est créé s'il n'existe pas.<br/><br/>
	 * 
	 * Après appel de cette méthode, il n'est plus possible d'utiliser
	 * le gestionnaire de valeur par défaut jusqu'au redémarrage de l'application.
	 * 
	 * @param param : les informations de connexions, null interdit.
	 */
	public void saveDefaultValue(ConnectionStrings param)
	{
		this.dvm.setDriver(param.driver);
		this.dvm.setUrl(param.url);
		this.dvm.setUser(param.user);
		this.dvm.setPort(param.port);
		this.dvm.setDataBase(param.baseName);
		this.dvm.save();
	}
	
	
	/**
	 * Pré-requis : utilisation de la méthode connect(), avec pour retour
	 * une réponse personnalisée décrivant le succès de la connexion.
	 * 
	 * @return Le nom de l'utilisateur si et seulement si l'application
	 * est connectée à un SGBD, null sinon.
	 */
	public String getUser(){return this.connector.getUser();}
	
	
	/**
	 * Ferme proprement la connexion.<br/>
	 * Ne soulève pas d'erreur s'il n'existe aucune connexion.
	 */
	public void disconnect()
	{
		if (this.connector != null) this.connector.disconnect();
	}
	
	
	/**
	 * Pré-requis : doit être appelée après que la connexion soit faite.
	 * 
	 * @return une facade pour la définition des données.
	 */
	public DDLFacade getDDLFacade()
	{
		return new DDLFacade
				(this.factory.getDDLManager(this.getConnection()), 
				 this.factory, this.tables);
	}
	
	
	/**
	 * Pré-requis : doit être appelée après que la connexion soit faite.
	 * 
	 * @return une facade pour le CRUD des données.
	 */
	public SQLFacade getCRUDFacade()
	{
		return new SQLFacade(this.factory.getDDLManager(this.getConnection()), connector, tables);
	}
}
