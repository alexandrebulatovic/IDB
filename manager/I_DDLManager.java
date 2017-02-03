package manager;

import java.util.ArrayList;
import java.util.List;

import useful.Response;
import useful.ResponseData;
import business.Attribute;

public interface I_DDLManager 
{

	//Méthodes
	/**
	 * @return la liste des types disponibles dans le SGBD.
	 */
	public String [] getAttributeTypes();
	
	
	/**
	 * @return vrai si et seulement si le SGBD permet de "droper" une 
	 * table avec l'option "CASCADE", faux sinon.
	 */
	public boolean allowsDropCascade();
	
	
	/**
	 * Tente de créer une table dans la base de données.
	 * 
	 * @param sql : une requête SQL pour créer une table, null interdit.
	 * @return Une réponse personnalisée avec un message de succès si et seulement si
	 * la table est créée, un message détaillant l'erreur sinon.
	 */
	public abstract Response createTable(String sql);

	
	public abstract ArrayList<Response> modifyTable(ArrayList<String> sqls);

	
	/**
	 * Supprime $table, si c'est possible.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @param cascade : vrai si et seulement si $table peut être supprimée 
	 * alors qu'elle est référencée par d'autres tables, faux sinon.
	 * @param chain : vrai si et seulement si toutes les tables qui référencent
	 * $table doivent être supprimées aussi.
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables concernées a réussi ou non.
	 */
	public abstract Response dropTable(String table, boolean cascade, boolean chain);

	
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
	 * TODO : ne gère pas les groupes d'uniques...
	 * @param table : table où chercher les attributs avec contrainte unique, null interdit.
	 * @return une réponse personnalisée contenant le nom des attributs de $table
	 * qui sont soumis à une contrainte UNIQUE.
	 */
	public abstract ResponseData<String> getUniqueAttribute(String table);
	
	
	/**
	 * @param table : nom de la table où récupérer les attributs, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre :<br/>
	 * -le nom d'un attribut de $table,<br/>
	 * -la taille de cet attribut,<br/>
	 * -le nom de son type SQL,<br/>
	 * -"NO" si et seulement si cet attribut est NOT NULL.<br/><br/>
	 * 
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public abstract ResponseData<String[]> getAttributes(String table);
	


	/**
	 * Ferme proprement les objets statements.
	 * Ne fait rien en cas d'erreur et n'avertit pas l'utilisateur.
	 */
	public abstract void closeStatement();

}