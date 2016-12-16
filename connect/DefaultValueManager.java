package connect;

import java.util.Properties;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Gère la lecture et l'écriture des valeurs de connexion par défaut dans un fichier XML.
 * 
 * @author UGOLINI Romain
 */
public class DefaultValueManager 
{
	//Static
	/**Nom du fichier xml contenant les valeurs de connexion par défaut.*/
	private static final String FILENAME = "default_connection.xml";
	
	/** Clée du pilote.*/
	private static final String DRIVER = "driver";
	
	/** Clée de l'URL.*/
	private static final String URL = "url";
	
	/** Clée de l'utilisateur.*/
	private static final String USER = "user";
	
	/** Clée de la base de données.*/
	private static final String DB = "db";
	
	/** Clée du numéro de port.*/
	private static final String PORT = "port";
	
	
	//Attributs
	/** Contient les couples (clef, valeur) pour se connecter à un SGBD.*/
	private Properties properties;
	
	/** Chemin d'accès absolu du répertoire courant de la ligne de commande.*/
	private String currentDir;
	
	/**
	 * Vrai si et seulement si il existe déjà un fichier 
	 * qui stocke les valeurs par défaut, faux sinon.
	 */
	private boolean exists;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public DefaultValueManager()
	{
		this.properties = new Properties();
		this.currentDir = System.getProperty("user.dir");
		this.loadDefaultValue();
	}
	
	
	//Méthods
	/**
	 * Retourne vrai si et seulement si $this possède des
	 * propriétés à l'instant 't', faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean hasProperty(){return this.exists;}
	
	
	/**
	 * Retourne le nom du pilote par défaut.
	 * 
	 * @return String
	 */
	public String getDriver(){return this.getDefaultValue(DRIVER);}
	
	
	/**
	 * Retourne l'URL de connexion par défaut.
	 * 
	 * @return String
	 */
	public String getUrl(){return this.getDefaultValue(URL);}
	
	
	/**
	 * Retourne le nom d'utilisateur par défaut.
	 * 
	 * @return String
	 */
	public String getUser(){return this.getDefaultValue(USER);}
	
	
	/**
	 * Retourne le nom de la base de données par défaut.
	 * 
	 * @return String
	 */
	public String getDataBase(){return this.getDefaultValue(DB);}
	
	
	/**
	 * Retourne le nom du port par défaut.
	 * 
	 * @return String
	 */
	public String getPort(){return this.getDefaultValue(PORT);}
	
	
	/**
	 * Définit la valeur par défaut de pilote comme étant $value.
	 * 
	 * @param value : le nom du pilote, null interdit.
	 */
	public void setDriver(String value){this.setDefaultValue(DRIVER, value);}
	
	
	/**
	 * Définit la valeur par défaut de l'URL comme étant $value.
	 * 
	 * @param value : l'url d'un sgbd valide, null interdit.
	 */
	public void setUrl(String value){this.setDefaultValue(URL, value);}
	
	
	/**
	 * Définit la valeur par défaut de l'utilisateur comme étant $value.
	 * 
	 * @param value : un nom d'utilisateur valide, null interdit.
	 */
	public void setUser(String value){this.setDefaultValue(USER, value);}
	
	
	/**
	 * Définit la valeur par défaut de la base de données comme étant $value.
	 * 
	 * @param value : un nom de base de données valide, null interdit.
	 */
	public void setDataBase(String value){this.setDefaultValue(DB, value);}
	
	
	/**
	 * Définit la valeur par défaut de la base de données comme étant $value.
	 * 
	 * @param value : un numéro de port valide, null interdit.
	 */
	public void setPort(String value){this.setDefaultValue(PORT, value);}
	
	
	/**
	 * Enregistre les informations par défaut dans le répertoire en cours.
	 */
	public void save()
	{
		try{
			OutputStream defaultFile = new FileOutputStream(
				this.currentDir + "/" + FILENAME);
			this.properties.storeToXML(defaultFile, "");
			defaultFile.close();
		}
		//TODO : Gérer ou non ces exceptions.
		catch(FileNotFoundException err){}
		catch(IOException err){}
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("Répertoire courant : " + this.currentDir + "\n");
		result.append("Nom du (futur?) fichier : " 
				+ FILENAME + "\n");
		result.append("Couples clef-valeur :\n");
		result.append(this.properties.toString());
		return result.toString();
	}
	
	
	//Privates
	/**
	 * Retourne la valeur par défaut du champ $key.
	 * 
	 * @param key : clef pour récupérer une valeur par défaut, null interdit.
	 * @return String
	 */
	private String getDefaultValue(String key)
	{
		String result = this.properties.getProperty( this.keyPrefix(key) + key);
		return result == null ? "" : result;
	}
	
	
	/**
	 * Enregistre le couple $key, $value dans le buffer de $this.
	 * 
	 * @param key : clée du couple, null interdit.
	 * @param value : valeur du couple, null interdit.
	 */
	private void setDefaultValue(String key, String value)
	{
		this.properties.setProperty(this.keyPrefix(key) + key, value);
	}
	
	
	/**
	 * Charge les couples (clef, valeur) du fichier qui contient 
	 * les informations de connexion par défaut s'il existe.
	 * Ne fait rien sinon.
	 */
	private void loadDefaultValue()
	{
		try{
			InputStream defaultFile = new FileInputStream(
					this.currentDir + "/" + FILENAME);
			this.properties.loadFromXML(defaultFile);
			this.exists = true;
			defaultFile.close();
		}
		catch(FileNotFoundException err){this.exists = false;}
		catch(IOException err){this.exists = false;}
	}
	
	
	/**
	 * Retourne le nom du pilote par défaut suivit d'un underscore
	 * si et seulement si $key != DRIVER, retourne une chaîne vide sinon.
	 * 
	 * @param key : nom de la clef, null interdit.
	 * @return String
	 */
	private String keyPrefix(String key)
	{
		return (key == DRIVER) ? "" : (this.getDriver() + "_");
	}
}
