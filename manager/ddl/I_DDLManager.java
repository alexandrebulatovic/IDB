package manager.ddl;


import useful.Response;
import useful.ResponseData;

/**
 * Fournit un moyen de communication avec le SGBD dans l'optique 
 * de définir les données (LDD).
 */
public interface I_DDLManager 
{
	//Méthodes
	/**
	 * @return la liste des types disponibles dans le SGBD.
	 * Dans l'ordre : chaîne variable, nombre, date et chaîne fixe. 
	 */
	public String [] getDataTypes();
	
	
	/**
	 * Tente de créer une table dans la base de données.
	 * 
	 * @param sql : une requête SQL pour créer une table sans contrainte, 
	 * à l'exception d'un NOT NULL "in line", null interdit.
	 * @return Une réponse personnalisée avec un message de succès si et seulement si
	 * la table est créée, un message détaillant l'erreur sinon.
	 */
	public abstract Response createTable(String sql);

	
	/**
	 * Altère une table dans la base de données.
	 * 
	 * @param sql : une requète SQL "ALTER TABLE...", null interdit.
	 * @return une réponse personnalisée décrivant si l'atltération des
	 * tables & réussie ou ratée. 
	 */
	public abstract Response alterTable(String sql);
	
	
	/**
	 * Supprime $table, si c'est possible.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @param cascade : vrai si et seulement si $table peut être supprimée 
	 * alors qu'elle est référencée par d'autres tables, faux sinon.
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables concernées a réussi ou non.
	 */
	public abstract Response dropTable(String table, boolean cascade);

	
	/**
	 * Supprime $table et toutes les tables de la bases qui utilisent la clée primaire
	 * de $table.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @return une réponse personnalisée qui contient le nom de toutes les tables 
	 * supprimées, ou une réponse vide en cas d'erreur.
	 */
	public abstract ResponseData<String> dropTableDomino(String table);
	
	
	/**
	 * @return Une réponse personnalisée contenant le nom des tables de données
	 * de la base si et seulement si la requête fonctionne, sinon une réponse 
	 * personnalisée détaillant l'erreur survenue.
	 */
	public abstract ResponseData<String> getTables();


	/**
	 * @param table : nom de la table où chercher la clée, null interdit.
	 * @return Une réponse personnalisée contenant les attributs membres
	 * de la clée primaire de $table si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public abstract ResponseData<String> getPrimaryKey(String table);

	
	/**
	 * @param table : table où se trouve les clées étrangères, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * 
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre : <br/>
	 * - le nom d'une table $t,<br/>
	 * - le nom d'un attribut $a, clée primaire de $t,<br/>
	 * - le nom de la contrainte de clée primaire $t($a),<br/>
	 * - l'argument $table,<br/>
	 * - le nom d'un attribut $a2, clée étrangère de $table, qui référence $t($a),<br/>
	 * - le nom de la contrainte de clée étrangère de $table($a2).<br/><br/>
	 * 
	 * Pour résumer : FOREIGN KEY ($a2) REFERENCES $t($a) <br/>
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée. 
	 */ 
	public abstract ResponseData<String[]> getPrimaryFromForeign(String table);


	/**
	 * @param table : table où se les membres de la clée primaire, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * 
	 * Lorsque la récupération réussit, la réponse contient dans l'ordre :<br/>
	 * - l'argument $table, <br/>
	 * - le nom d'un attribut $a2, membre de la clée primaire de $table,<br/>
	 * - le nom de la contrainte de clée primaire de $table,<br/>
	 * - le nom d'une table $t,<br/>
	 * - le nom d'un attribut $a, clée étrangère de $t, qui référence $table($a2),<br/>
	 * - le nom de la contrainte de clée étrangère de $t($a).<br/><br/>
	 * 
	 * Pour résumer : FOREIGN KEY($a) REFERENCES $table($a2)
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public abstract ResponseData<String []> getForeignFromPrimary(String table);


	/**
	 * @param table : table où chercher les attributs avec contrainte unique, null interdit.
	 * @return une réponse personnalisée contenant : <br/>
	 * - Le nom de l'index,<br/>
	 * - le nom des attributs de $table qui sont soumis à une contrainte UNIQUE.
	 */
	public abstract ResponseData<String[]> getUniques(String table);
	
	
	/**
	 * @param table : nom de la table où récupérer les attributs, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre :<br/>
	 * -le nom d'un attribut de $table,<br/>
	 * -le nom de son type SQL,<br/>
	 * -la taille de cet attribut,<br/>
	 * -"NO" si et seulement si cet attribut est NOT NULL.<br/><br/>
	 * 
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public abstract ResponseData<String[]> getAttributes(String table);
	
	
	/**
	 * Supprime la contrainte de clée étrangère nommée $fkName dans $table.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @param fkName : nom de la contrainte, null interdit.
	 * @return une réponse personnalisée décrivant la tentative de suppression
	 * de la contrainte.
	 */
	public abstract Response dropForeignKey(String table, String fkName);
	
	
	/**
	 * Ferme proprement les objets statements.
	 * Ne fait rien en cas d'erreur et n'avertit pas l'utilisateur.
	 */
	public abstract void closeStatement();

	
	public Response addForeignKey(String sql);


	public Response addUnique(String sql);

	
	public Response dropConstraint(String sql);

}