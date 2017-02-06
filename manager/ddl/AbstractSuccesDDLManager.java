package manager.ddl;

/**
 * Contient les messages de succès des méthodes des gestionnaires
 * de connexion vers un SGBD.
 */
public abstract class AbstractSuccesDDLManager 
implements I_DDLManager
{
	//Statiques
	/** Message pour la création d'une table.*/
	protected static final String CREATE_TABLE = "Table créée.";
	
	/** Message pour la suppression d'une table.*/
	protected static final String DROP_TABLE = "Table supprimée.";
	
	/** Message pour la récupération des tables.*/
	protected static final String GET_TABLES = "Tables récupérées.";
	
	/** Message pour la récupération des clées primaires.*/
	protected static final String GET_PRIMARY = "Clées primaires récupérées.";
	
	/** Message pour la récupération des clées étrangères.*/
	protected static final String PRIMARIES_FROM_FOREIGN = "Clées étrangères récupérées.";
	
	/** Message pour la récupération des clées primaires référencées.*/
	protected static final String FOREIGNS_FROM_PRIMARY = "Clées référencées récupérées.";
	
	/** Message pour la récupération des attributs sous contrainte UNIQUE.*/
	protected static final String GET_UNIQUE = "Attributs uniques récupérés.";
	
	/** Message pour la récupération des attributs d'une table.*/
	protected static final String GET_COLUMNS = "Attributs récupérés.";
	
	/** Message pour la suppression en chaîne des tables.*/
	protected static final String DOMINO = "Tables supprimées.";
}
