package business;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** Conteneur de tables. */
public class TableSet 
{
	//Attributs
	/** Toutes les tables présentes dans la base de données.*/
	private List<Table> tables;
	
	/** 
	 * Vrai si et seulement si les tables ont toutes été chargées 
	 * depuis la base de données. 
	 */
	private boolean tablesLoaded = false;
	
	
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public TableSet()
	{
		tables = new ArrayList<Table>();
	}
	
	
	//Méthodes
	/**
	 * @return vrai si et seulement si les tables ont toutes été chargées 
	 * depuis la base de données. 
	 */
	public boolean isLoaded(){return this.tablesLoaded;}
	
	
	/**
	 * @return la liste des tables de la base de données.
	 */
	public List<String> getTablesNames(){
		List<String> retour = new ArrayList<String>();
		for (Table table : tables){
			retour.add(table.getName());
		}
		return retour;
	}
	
	
	/**
	 * A n'utiliser qu'au lancement de l'application, pour charger 
	 * une première fois le nom de toutes les tables disponibles.<br/><br/>
	 * 
	 * Charge en mémoire le nom des tables de la base de données,
	 * définit l'ensemble comme étant "chargé".
	 * 
	 * @param tablesNames : le nom des tables disponibles, null interdit.
	 */
	public void loadTables(List<String>tablesNames){
		this.tablesLoaded = true;
		for (String tableName : tablesNames){
			this.tables.add(new Table(tableName));
		}
	}
	
	
	/**
	 * ajoute une table si c'est possible (si aucune autre table de même nom n'existe),
	 * sinon garde l'ancienne.<br>
	 * il est nécessaire d'utiliser {@link business.TableSet#isAddable(String)} avant d'ajouter une table
	 * (de préférence)
	 * 
	 * @param table : nom de la table, null interdit.
	 * @return vrai si et seulement si $table à pu être ajoutée à l'ensemble,
	 * faux sinon.
	 * @see business.TableSet#isAddable(String)
	 */
	public boolean addTable(String table){
		if (this.isAddable(table)){
			return this.tables.add(new Table(table));
		}
		else return false;
	}
	
	
	/**
	 * Ajoute un attribut dans la table spécifié
	 * vérifie les contraintes et si la table existe
	 * ajoute si nécessaire la clé primaire sur l'attribut
	 * Cette méthode part du principe qu'il ne peut y avoir qu'une clé primaire
	 * par table (gère automatiquement les groupes de clé primaires pour
	 * un attribut donné)
	 * 
	 * @param tableName
	 * @param attributeName
	 * @param type
	 * @param size
	 * @param notNull
	 * @param primaryKey
	 * @return boolean
	 */
	public boolean addAttribute(String tableName,String attributeName, String type, int size, boolean notNull,boolean primaryKey){
		for (Table table:tables){
			if (table.getName().equals(tableName) && !table.containsAttributeName(attributeName)){
				Attribute newAttribute = new Attribute(attributeName, type, size, null, tableName, notNull);
				
				if (primaryKey){
					addPrimaryKey(table, newAttribute);
				}
				
				return table.addAttribute(newAttribute);
			}
		}
		return false;
	}
	
	/**
	 * ajoute une clé étrangère si c'est possible
	 * Les attributs doivent exister !!
	 * @param nameOfConstraint
	 * @param tableSourceName
	 * @param attributesSourcesNames
	 * @param tableDestinationName
	 * @param attributesDestinationsNames
	 * @return
	 */
	public boolean addForeignKey(String name, String tableSourceName, String[] attributesSourcesNames, String tableDestinationName, String[] attributesDestinationsNames){
		boolean added = false;
		
		Table tableSource = this.getTableWithName(tableSourceName);
		Table tableDestination = this.getTableWithName(tableDestinationName);
		
		ForeignKeyConstraint fk = new ForeignKeyConstraint();
		fk.setTable(tableSource);
		fk.setTableDestination(tableDestination);
		
		
		
		for (Attribute att : getAttributesFromString(tableSource,attributesSourcesNames)){
			att.addConstraint(fk);
			fk.addAttribute(att);
		}
		
		for (Attribute att : getAttributesFromString(tableDestination,attributesDestinationsNames)){
			att.addConstraint(fk);
			fk.addAttributeDestination(att);
		}
		
		
		fk.setName(name);
		if (name==null){
			fk.createAndSetName();
		}

		tableSource.addConstraint(fk);
		
		
		
		return added;
	}
	
	private List<Attribute> getAttributesFromString(Table tableSource, String[] attributesSourcesNames) {
		ArrayList<Attribute> retour = new ArrayList<Attribute>();
		
		for (String attName : attributesSourcesNames){
			for (Attribute att : tableSource.getAttributes()){
				if (att.getName().equals(attName)){
					retour.add(att);
				}
			}
		}
		return retour;
		
	}


	/**
	 * ajoute une clé étrangère si c'est possible
	 * Le nom de la fk sera automatiquement généré
	 * @see TableSet#addForeignKey(String name, String, String[], String, String[])
	 * 
	 * @param tableSourceName
	 * @param AttributesSourcesNames
	 * @param tableDestinationName
	 * @param attributesDestinationsNames
	 * @return
	 */
	public boolean addForeignKey(String tableSourceName, String[] AttributesSourcesNames, String tableDestinationName, String[] attributesDestinationsNames){
		return this.addForeignKey(null, tableSourceName, AttributesSourcesNames, tableDestinationName, attributesDestinationsNames);
	}
	

	/**
	 * retourne une liste de requettes permettant de modifier la table
	 * passé en paramètres
	 * @exemple [CREATE TABLE table(...),ALTER TABLE table... , ALTER T...,...]
	 * @see business.Table#toCreate(Table)
	 * @param tableName
	 * @return ArrayList<String> sqlString
	 */
	public List<String> getSQLTableToCreate(String tableName){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				return table.toCreate();
			}
		}
		return null;
	}
	
	/**
	 * retourne une liste de requettes permettant de modifier la table
	 * passé en paramètres
	 * @see business.Table#toModify(Table)
	 * @param tableName
	 * @return ArrayList<String> sqlString
	 */
	public ArrayList<String> getSQLTableToModify(String tableName){
		return null;//TODO
	}
	
	
	/**
	 * Supprime une table s'après son nom
	 * et s'occupe de supprimer les sous attributs
	 * et les contraintes associés 
	 * @param table
	 * @return true en cas de succès<br>
	 * false en cas d'erreur
	 */
	public boolean removeTable(String table)
	{
		Table laTable = this.getTableWithName(table);
		laTable.cleanAll();
		
		return this.tables.remove(laTable);
	}
	
	
	/**
	 * @param name : nom de la table a récupérée, null interdit.
	 * @return une table nommée $name si et seulement si elle existe,<br>
	 * null sinon.
	 */
	private Table getTableWithName(String name){		
		for (Table table : tables){
			if (table.getName().equals(name)){
				return table;
			}
		}
		return null;
	}

	
	/**
	 * Définit $att comme étant membre de la clée primaire de $table.
	 * 
	 * @param table : null interdit.
	 * @param att : null interdit.
	 */
	private void addPrimaryKey(Table table, Attribute att) {
		boolean havePk = false;
		for (Attribute attributeActual : table.getAttributes()){
			if (attributeActual.isPk()){
				havePk = true;
				PrimaryKeyConstraint pk = attributeActual.getPk();
				pk.addAttribute(att);
			}
		}
		if (!havePk){
			PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
			pk.addAttribute(att);
			pk.setTable(table);
			att.addConstraint(pk);
			table.addConstraint(pk);
		}
	}


	/**
	 * @param table : nom d'une table à ajouter, null interdit.
	 * @return vrai si et seulement si il est possible d'ajouter
	 * $table à l'ensemble, faux sinon.
	 */
	private boolean isAddable(String table)
	{
		return this.getTableWithName(table)==null;
	}
}
