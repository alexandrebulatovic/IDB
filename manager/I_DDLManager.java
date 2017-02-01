package manager;

import java.util.ArrayList;
import java.util.List;

import useful.Response;
import useful.ResponseData;
import business.Attribute;

public interface I_DDLManager {

	//Méthodes
	/**
	 * @return la liste des types disponibles dans le SGBD.
	 */
	public String [] getAttributeTypes();
	
	
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
	 * Tente de supprimer une table dans la base de données.
	 * 
	 * @param table : une requête SQL pour supprimer une table, null interdit.
	 * @return Une réponse personnalisée avec un message de succès si et seulement si
	 * la table est supprimée, un message détaillant l'erreur sinon.
	 */
	public abstract Response dropTable(String table, boolean cascade);

	
	/**
	 * @param table : nom de la table où chercher la clée, null interdit.
	 * @return Une réponse personnalisée contenant les attributs membres
	 * de la clée primaire de $table si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public abstract ResponseData<String> getPrimaryKey(String table);

	
	/**
	 * @return Une réponse personnalisée contenant le nom des tables de données
	 * de la base si et seulement si la requête fonctionne, sinon une réponse 
	 * personnalisée détaillant l'erreur survenue.
	 */
	public abstract ResponseData<String> getTables();

	
	public abstract List<Attribute> getAttributes(String table);

	
	/**
	 * @param table : table où chercher les attributs avec contrainte unique, null interdit.
	 * @return une réponse personnalisée contenant le nom des attributs de $table
	 * qui sont soumis à une contrainte UNIQUE.
	 */
	public ResponseData<String> getUniqueAttribute(String table);
	
	
	/**
	 * TODO : voir s'il faut ajouter le nom de la contrainte dans le résultat.
	 * @param table : table où se trouve les clées étrangères.
	 * @return une réponse personnalisée.
	 * Lorsque la récupération réussit, la réponse contient dans l'ordre : <br/>
	 * - le nom d'une table $t,<br/>
	 * - le nom d'un attribut $a, clée primaire de $t,<br/>
	 * - le nom d'un attribut de $table soumit à une contrainte FOREIGN KEY
	 * qui utilise $t($a) comme référence. <br/>
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée. 
	 */ 
	public abstract ResponseData<String[]> getImportedKey(String table);


	/**
	 * @param table : table où chercher les clées primaires.
	 * @return une réponse personnalisée.
	 * Lorsque la récupération réussit, la réponse contient dans l'ordre :<br/>
	 * - le nom d'une table $t, <br/>
	 * - le nom d'un attribut $a, clée étrangère de $t
	 */
	public ResponseData<String []> getExportedKey(String table);

	
	/**
	 * Ferme proprement les objets statements.
	 * Ne fait rien en cas d'erreur et n'avertit pas l'utilisateur.
	 */
	public abstract void closeStatement();

}