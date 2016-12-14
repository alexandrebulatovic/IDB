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
	//Attributs
	/**
	 * Nom du fichier xml contenant les valeurs de connexion par défaut.
	 */
	private static final String 
	DEFAULT_VALUE_FILENAME = "default_connection.xml";
	
	/**
	 * Contient les couples (clef, valeur) pour
	 * se connecter à un SGBD.
	 */
	private Properties properties;
	
	/**
	 * Chemin d'accès absolu du répertoire 
	 * courant de la ligne de commande.
	 */
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
	 * Retourne l'URL de connexion par défaut.
	 * 
	 * @return String
	 */
	public String getUrl(){return this.defaultValue("url");}
	
	
	/**
	 * Retourne le nom d'utilisateur par défaut.
	 * 
	 * @return String
	 */
	public String getUser(){return this.defaultValue("user");}
	
	
	/**
	 * Retourne le nom de la base de données par défaut.
	 * 
	 * @return String
	 */
	public String getDataBase(){return this.defaultValue("db");}
	
	
	/**
	 * Retourne le nom du port par défaut.
	 * 
	 * @return String
	 */
	public String getPort(){return this.defaultValue("port");}
	
	
	/**
	 * Définit la valeur par défaut de l'URL comme étant $value.
	 * 
	 * @param value : l'url d'un sgbd valide.
	 */
	public void setUrl(String value){this.properties.setProperty("url", value);}
	
	
	/**
	 * Définit la valeur par défaut de l'utilisateur comme étant $value.
	 * 
	 * @param value : un nom d'utilisateur valide.
	 */
	public void setUser(String value){this.properties.setProperty("user", value);}
	
	
	/**
	 * Définit la valeur par défaut de la base de données comme étant $value.
	 * 
	 * @param value : un nom de base de données valide.
	 */
	public void setDataBase(String value){this.properties.setProperty("db", value);}
	
	
	/**
	 * Définit la valeur par défaut de la base de données comme étant $value.
	 * 
	 * @param value : un numéro de port valide.
	 */
	public void setPort(String value){this.properties.setProperty("port", value);}
	
	
	/**
	 * Enregistre les informations par défaut dans le répertoire en cours.
	 */
	public void save()
	{
		try{
			OutputStream defaultFile = new FileOutputStream(
				this.currentDir + "/" + DEFAULT_VALUE_FILENAME);
			this.properties.storeToXML(defaultFile, "");
			defaultFile.close();
		}
		//TODO : Gérer ou non ces exceptions.
		catch(FileNotFoundException err){}
		catch(IOException err){}
	}
	
	/**
	 * Retourne une chaine de caractères qui décrit $this.
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("Répertoire courant : " + this.currentDir + "\n");
		result.append("Nom du (futur?) fichier : " 
				+ DEFAULT_VALUE_FILENAME + "\n");
		result.append("Couples clef-valeur :\n");
		result.append(this.properties.toString());
		return result.toString();
	}
	
	
	//Privates
	/**
	 * Retourne la valeur par défaut du champ $key.
	 * 
	 * @param key : clef pour récupérer une valeur par défaut
	 * @return String
	 */
	private String defaultValue(String key)
	{
		String result = this.properties.getProperty(key);
		if (result == null) {
			return "";
		}
		else{
			return result;
		}
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
					this.currentDir + "/" + DEFAULT_VALUE_FILENAME);
			this.properties.loadFromXML(defaultFile);
			this.exists = true;
			defaultFile.close();
		}
		//TODO : Gérer ou non ces exceptions.
		catch(FileNotFoundException err){this.exists = false;}
		catch(IOException err){this.exists = false;}
	}
}
