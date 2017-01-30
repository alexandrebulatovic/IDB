package manager;

import java.util.ArrayList;
import java.util.List;

import useful.Response;
import useful.ResponseData;
import business.Attribute;

public interface I_DDLManager {

	//Méthodes
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
	 * @param table : table où chercher les clées étrangères.
	 * @return Une réponse personnalisée qui contient les clées étrangères
	 * de $table et leurs références si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public abstract ResponseData<String[]> getImportedKey(String table);

	/**
	 * Ferme proprement les objets statements.
	 * Ne fait rien en cas d'erreur et n'avertit pas l'utilisateur.
	 */
	public abstract void closeStatement();

}