package manager.connection;

import java.sql.Connection;
import java.sql.SQLException;

import useful.ConnectionStrings;
import useful.Response;

/**
 * Fournit des méthodes pour se connecter ou se déconnecter à une base de 
 * données puis récupérer des informations sur cette connexion.
 */
public interface I_ConnectionManager {

	//Accesseurs
	/**
	 * @return Le nom de l'utilisateur connecté.
	 */
	public abstract String getUser();

	/**
	 * @return Le SGBD avec lequel est connecté $this, null si n'est pas connecté.
	 */
	public abstract Connection getConnection();

	/**
	 * @return Vrai si et seulement si $this est connecté à un SGBD,
	 * faux sinon.
	 */
	public abstract boolean isConnected();

	//Methods
	/**
	 * Tente d'établir une connexion vers un SGBD en fonction
	 * des informations de connexions de $param.
	 * 
	 * @param param : un objet ConnectionStrings
	 * @return un objet qui décrit la tentative de connexion.
	 */
	public abstract Response connect(ConnectionStrings param);

	/**
	 * Tente de re-établir une connexion vers le SGBD précédement
	 * atteint avec succès, et dont les paramètres de connexions 
	 * sont toujours enregistrés.
	 * 
	 * Ne doit être utilisé que si $this s'est déjà connecté avec succès
	 * au cours de l'exécution de l'application.
	 * 
	 * @return Un objet qui décrit la tentative de connexion.
	 */
	public abstract Response reconnect();

	/**
	 * Ferme proprement la connexion.
	 */
	public abstract void disconnect();

	/**
	 * Interprète une {@code SQLException} issue d'une action sur un SGBD.
	 * @param exception : un objet {@code SQLException}.
	 * @return un {@code String} expliquant l'erreur.
	 */
	public abstract String generateErrorMessage(SQLException exception);

}