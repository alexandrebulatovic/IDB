package business;

import java.util.ArrayList;
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
	public boolean isLoaded(){
		return this.tablesLoaded;
	}
	
	
	/**
	 * @param table : nom d'une table, null interdit.
	 * @return vrai ssi $table a déjà été chargée depuis le SGBD,
	 * faux sinon.
	 */
	public boolean isLoaded(String table)
	{
		Table t = this.getTableByName(table);
		if (t.getAttributes().size()<=0){
			return false;
		}
		return true;
	}
	
	
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
//				System.out.println("att : "+newAttribute.getName()+" is pk ?  : "+newAttribute.isPk());
				
				return table.addAttribute(newAttribute);
			}
		}
		return false;
	}
	
	
	/**
	 * Ajoute une clée étrangère dans les classes métiers.
	 * 
	 * @param constraintName : nom de la contrainte, null interdit.
	 * @param foreignTable : nom de la table qui contient la clée étrangère, null interdit.
	 * @param foreignAttributes : nom des attributs membres de la clée étrangère, null interdit.
	 * @param primaryTable : nom de la table contenant la clée primaire référencée.
	 * @param primaryAttributes : nom des attributs membres de la clée primaire référencées, null interdit.
	 * @return le nom de la contrainte de clée étrangère.
	 */
	public String addForeignKey(
			String constraintName, 
			String foreignTable, 
			String[] foreignAttributes, 
			String primaryTable, 
			String[] primaryAttributes){
		
		Table tableSource = this.getTableByName(foreignTable);
		Table tableDestination = this.getTableByName(primaryTable);
		
		ForeignKeyConstraint fk = new ForeignKeyConstraint();
		fk.setTable(tableSource);
		fk.setTableDestination(tableDestination);
		
		for (Attribute att : getAttributesFromString(tableSource,foreignAttributes)){
			att.addConstraint(fk);
			fk.addAttribute(att);
		}
		for (Attribute att : getAttributesFromString(tableDestination,primaryAttributes)){
			att.addConstraint(fk);
			fk.addAttributeDestination(att);
		}
		
		fk.setName(constraintName);
		if (constraintName==null){
			fk.createAndSetName();
		}
		tableSource.addConstraint(fk);
		return fk.getName();
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
	public String addForeignKey(String tableSourceName, String[] AttributesSourcesNames, String tableDestinationName, String[] attributesDestinationsNames){
		return this.addForeignKey(null, tableSourceName, AttributesSourcesNames, tableDestinationName, attributesDestinationsNames);
	}
	
	
	/**
	 * ajoute une contrainte unique pouvant porter sur plusieurs attributs d'une meme table
	 * @param uniqueName
	 * @param tableName
	 * @param attributesNames
	 * @return nom de la contrainte généré ou null en cas d'échec
	 */
	public String addUnique(String uniqueName, String tableName, String[] attributesNames){
		Table table = this.getTableByName(tableName);
		UniqueConstraint un = new UniqueConstraint();
		un.setTable(table);
		
		for (String attributeName : attributesNames){//controle de l'intégrité de la contrainte
			Attribute attribute = this.getAttributeWithName(tableName, attributeName);
			if (attribute.getPk()!= null){
				return null;
			}
			for (Constraint c : attribute.getConstraints()){
				if (c.getName().equals(un.getName())){
					return null;
				}
			}
		}
		
		for (String attributeName : attributesNames){//controle de l'intégrité de la contrainte
			Attribute attribute = this.getAttributeWithName(tableName, attributeName);
			un.addAttribute(attribute);
			attribute.addConstraint(un);
		}
		
		
		this.getTableByName(tableName).addConstraint(un);
		
		if (uniqueName==null){
			un.createAndSetName();
		}
		else{
			un.setName(uniqueName);
		}

		return un.getName();
	}
	
	/**
	 * ajoute une contrainte unique pouvant porter sur plusieurs attributs d'une meme table
	 * @param tableName
	 * @param attributesNames
	 * @return nom de la contrainte généré ou non
	 */
	public String addUnique(String tableName, String[] attributesNames){
		return addUnique(null, tableName, attributesNames);
	}
	
	
	public void removeConstraint(String tableName, String attributeName, String constraintName){
		Table table = this.getTableByName(tableName);
		Constraint c = this.getConstraintWithName(tableName, constraintName);
		System.out.println(c);
		table.dropConstraint(c);
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
	 * @param oldTableName : ancien nom de la table, null interdit.
	 * @param newTableName : nouveau nom de la table, null interdit.
	 * @param attributes : liste décrivant les attributes : 
	 * 		-le nom String
	 * 		-type String
	 * 		-taille int
	 * 		-isNotNull boolean
	 * 		-isPrimaryKey boolean
	 * @return une liste de requêtes SQL pour altérer la table $oldname.
	 */
	public List<String> getSQLTableToModify(String oldTableName, String newTableName, List<Object[]> attributes)
	{
		Table tmpTable = new Table(oldTableName);
		Table oldTable = this.getTableByName(oldTableName);
		
		List<Attribute> attributesPk = new ArrayList<Attribute>();
		
		for (Object[] att : attributes){
			String name = (String) att[0];
			String type = (String) att[1];
			
			int size = (int) att[2];
			boolean isNotNull = (boolean) att[3];
			
			Attribute a = new Attribute(name, type, size, null, type, isNotNull);
			
			
			if ((boolean) att[4]){//if is pk
				attributesPk.add(a);
			}
			
			tmpTable.addAttribute(a);
		}
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setTable(tmpTable);
		
		PrimaryKeyConstraint oldPk = oldTable.getPk();
		if (oldPk != null){
			pk.setName(oldPk.getName());
		}
		
		for (Attribute a : attributesPk){
			pk.addAttribute(a);
			a.addConstraint(pk);
		}
		tmpTable.addConstraint(pk);
		
		
		
		tmpTable.setName(oldTableName);
		
//		List<String> toModifySQL = tmpTable.toModify(this.getTableByName(oldTable));
		
		
		
		
		List<String> toModifySQL = oldTable.toModify(tmpTable);
		oldTable.setName(newTableName);
		this.tables.remove(oldTable);
		this.tables.add(tmpTable);
		return toModifySQL;
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
		Table laTable = this.getTableByName(table);
		if (laTable != null) {
			laTable.cleanAll();
		}
		return this.tables.remove(laTable);
	}
	

	/**
	 * @param tableName : nom de la table, null interdit.
	 * @return tous les attributs de $tables, avec :<br/>
	 * - le nom de l'attribut,<br/>
	 * - le type de l'attribut,<br/>
	 * - la taille de l'attribut,<br/>
	 * - "NOTNULL" ssi l'attribut est sous contrainte NOT NULL,<br/>
	 * - "PRIMARY" ssi l'attribut est membre de la clée primaire.
	 */
	public List<String[]> getAttributes(String tableName){
		
		Table table = this.getTableByName(tableName);
		String[] attribute;
		List<String[]> result = new ArrayList<String[]>();
		
		for (Attribute att : table.getAttributes()){
			attribute = new String[5];
			attribute[0] = att.getName();
			attribute[1] = att.type;
			attribute[2] = String.valueOf(att.size);
			attribute[3] = att.isNotNull() ? "NOTNULL" : "";
			attribute[4] = att.isPk() ? "PRIMARY" : "";
			result.add(attribute);
		}
		return result;
	}
	
	
	/**
	 * retourne un tableau composé de (voir @return)
	 * @param tableName
	 * @return
	 * 0 - name
	 * 1 - type
	 * 2 - size
	 * 3 - 'NOT NULL'/null
	 * 4 - 'PRIMARY KEY'/null
	 */
	public String[] getAttribute(String tableName, String attributeName){
		Attribute attribute = this.getAttributeWithName(tableName, attributeName);
		String[] result = new String[5];

		result[0] = attribute.getName();
		result[1] = attribute.type;
		result[2] = String.valueOf(attribute.size);
		result[3] = attribute.isNotNull() ? "NOTNULL" : null;
		result[4] = attribute.isPk() ? "PRIMARY KEY" : null;					
			
		return result;
	}
	
	/**
	 * retourne une liste d'object<br>
	 * la première permet de sélectionner un attribut d'une table<br>
	 * la seconde de récupérer les informations de l'attribut<br>
	 * voir @return pour le contenu de la description de chaque attribut
	 * @param tableName
	 * @return
	 * 0 - name string
	 * 1 - type string
	 * 2 - size int
	 * 3 - null/nomConstrainte : String
	 * 4 - null/liste fk composé de 
	 * 		0 - nomFk String
	 * 		1 - tableSource String
	 * 		2 - liste<String> attSources String
	 * 		3 - tableDest String 
	 * 		4 - listattDest String
	 */
	public List<Object> getTableWithJustComplexConstraintsAndBaseInformationsAttributes(String tableName, String attributeName){
		Attribute attribute = this.getAttributeWithName(tableName, attributeName);
		List<Object> attributeReturn = new ArrayList<Object>();
		
		attributeReturn.add(attribute.getName());
		attributeReturn.add(attribute.type);
		attributeReturn.add(attribute.size);
		attributeReturn.add(attribute.isUnique());
		
		List<Object> fk = attribute.getFk().toListOfString();
		
		attributeReturn.add(fk);
		
		return attributeReturn;
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
	 * @return
	 * list of<br>
	 * 	name,<br>
	 * 	unique desc : list of <br>
	 * 		attributes<String>
	 */
	public List<Object> getUniqueConstraint(String tableSource){
		Table table = this.getTableByName(tableSource);

		
		List<Object> retour = new ArrayList<Object>();
		
		for (UniqueConstraint u : table.getUniques()){
			ArrayList<Object> unStr = new ArrayList<Object>();
			unStr.add(u.getName());
			unStr.add(u.getAttributesNames());
			retour.add(unStr);
			unStr.clear();
		}
		return retour;
	}
	
	
	/**
	 * @return
	 * list of<br>
	 * 	name,<br>
	 * 	fk desc : list of <br>
	 * 		tableSrc
	 * 		attributesSrc
	 * 		attributesDest<String>
	 * 		tableDest
	 */
	public List<Object> getFkConstraint(String tableSource){
		Table table = this.getTableByName(tableSource);

		
		List<Object> retour = new ArrayList<Object>();
		
		for (ForeignKeyConstraint fk : table.getFks()){
			ArrayList<Object> fkStr = new ArrayList<Object>();
			
			fkStr.add(tableSource);
			fkStr.add(fk.getAttributesNames());
			fkStr.add(fk.getTableDestination().getName());
			fkStr.add(fk.getAttributesDestinationNames());
			retour.add(fkStr);
			fkStr.clear();
		}
		return retour;
	}
	
	
	/**
	 * @exemple ALTER TABLE tableName ADD CONSTRAINT un_tableName_att UNIQUE(att)
	 * @param tableName
	 * @param attributeName
	 * @param ConstraintName
	 * @return
	 */
	public String getSQLADDConstraint(String tableName,String attributeName, String ConstraintName){
		Constraint c = this.getConstraintWithName(tableName, ConstraintName);
		return c.toAddConstraintSQL();
	}
	
	public String getSQLDropConstraint(String tableName, String attributeName, String ConstraintName){
		Constraint c = this.getConstraintWithName(tableName, ConstraintName);
		if(c == null){
			return null;
		}else{
			return c.toDropConstraintSQL();
		}
	}
	


	private Constraint getConstraintWithName(String tableName, String constraintName) {
		for (Constraint c : this.getTableByName(tableName).getConstraints()){
			if (c.getName().equals(constraintName)){
				return c;
			}
		}
		return null;
		
	}


	/**
	 * @param name : nom de la table a récupérée, null interdit.
	 * @return une table nommée $name si et seulement si elle existe,<br>
	 * null sinon.
	 */
	private Table getTableByName(String name){		
		for (Table table : tables){
			if (table.getName().equals(name)){
				return table;
			}
		}
		return null;
	}
	
	private Attribute getAttributeWithName(String tableName, String attributeName){
		for (Attribute a : this.getTableByName(tableName).getAttributes()){
			if (a.getName().equals(attributeName)){
				return a;
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
				att.addConstraint(pk);
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
		return this.getTableByName(table)==null;
	}
}
