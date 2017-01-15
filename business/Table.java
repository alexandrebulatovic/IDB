package business;
import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import manager.SQLManager;



public class Table {
	
	//Attributs
	/** Nom de la table.*/
	private String name;
	
	/** Un ensemble d'attributs (pas de doublons).*/
	private LinkedHashSet<Attribute> attributes;
	
	/**
	 * Vrai si et seulement si la table doit être supprimée
	 * en cascade, faux sinon.
	 */
	private boolean cascade;

	

	//Constructeurs
	/**
	 * Constructeur vide.
	 * Instancie un ensemble vide.
	 */
	protected Table(){
		this.attributes = new LinkedHashSet<Attribute>();
	}
	
	
	
	/**
	 * Construit une table nommée $name.
	 * Recopie les attributs de $attributes dans $this.
	 * 
	 * @param name : le nom de la table.
	 * @param attributes : un ensemble d'attributs, peut être vide.
	 */
	public Table(String name, LinkedHashSet<Attribute> attributes){
		this();
		this.name=name;	
		this.copyAttributes(attributes);
	}

	
	/**
	 * Construit une table nommée $name.
	 * Si $cascade est vrai, la table doit être supprimée avec 
	 * l'option CASCADE CONSTRAINT dans le SGBD.
	 * 
	 * @param name : le nom de la table.
	 * @param cascade : un ensemble d'attributs, peut être vide.
	 */
	public Table(String name, boolean cascade)
	{
		this();
		this.name = name;
		this.cascade = cascade;
	}
	
	/**
	 * Constructeur par recopie profonde.
	 * 
	 * @param copy : une table à recopier.
	 */
	public Table(Table copy)
	{
		this();
		this.name = copy.name;
		this.copyAttributes(attributes);
	}
	
	
	//Accesseurs
	public  LinkedHashSet<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public  String getName(){
		return this.name;
	}
	
	
	//Méthodes
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + ":\n");
		result.append(this.cascade ? "cascade constraint\n" : "");
		for (Attribute a : this.attributes) {
			result.append(a.toString());
			result.append('\n');
		}
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de création de table.
	 * 
	 * @return String
	 */
	public String toCreate()
	{
		StringBuilder result = new StringBuilder();

		result.append(this.tableNameToSQL());
		result.append("\n(\n");
		result.append(this.attributesToSQL());
		if (this.hasPrimaryKey()) {
			result.append(",\n");
			result.append(this.primaryKeyToSQL());
		}
		result.append("\n)");
		return result.toString();
	}
	
	/**
	 * Retourne les requetes SQL qui modifient
	 * La table avec des ALTER TABLE
	 * @return String
	 */
	public ArrayList<String> toModify(Table tableSource) {
		ArrayList<String> results = new ArrayList<String>();
		
		
		addAttributes(tableSource, results);
		
		dropAttributes(tableSource, results);
		
		modifyAttributes(tableSource, results);
		
		modifyName(tableSource, results);
		

	 
		return results;
	}



	/**
	 * TODO uniquement valable sur Oracle
	 * @param tableSource
	 * @param results
	 */
	private void modifyName(Table tableSource, ArrayList<String> results) {
		if (!tableSource.name.equals(this.name)){
			results.add("RENAME "+tableSource.name+" TO "+this.name);	
		}		
	}



	/**
	 * TODO Disperser ce code dans des méthodes métier ou plus génériques
	 * @param tableSource
	 * @param results
	 */
	private void modifyAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute[] attribute : attributesToChange(tableSource)){
			Attribute attSrc = attribute[0];
			Attribute attDest = attribute[1];
			if (attSrc.foreignKey != attDest.foreignKey){
				StringBuilder sql = new StringBuilder();
				sql.append("ALTER TABLE ");
				sql.append(this.name);
				sql.append("\n");
				if (attSrc.foreignKey){
					sql.append("DROP ");
					sql.append(attSrc.toSQLConstraintName("fk"));

				}else{
					sql.append("ADD ");
					sql.append(attDest.toSQLConstraintName("fk"));
					sql.append(attDest.toSQLConstraintType(" FOREIGN KEY", ""));
					sql.append(" ");
					sql.append(attDest.toSQLReferences());
				}
				results.add(sql.toString());
			}
			if (attSrc.unique != attDest.unique){
				StringBuilder sql = new StringBuilder();
				sql.append("ALTER TABLE ");
				sql.append(this.name);
				sql.append("\n");
				if (attSrc.unique){
					sql.append("DROP ");
					sql.append(attSrc.toSQLConstraintName("un"));
				}
				else{
					sql.append("ADD ");
					sql.append(attSrc.toSQLConstraintName("un"));
					sql.append(attDest.toSQLConstraintType(" UNIQUE",""));
					sql.append(" ");
				}
				results.add(sql.toString());
			}
			if (attSrc.primaryKey != attDest.primaryKey){
				StringBuilder sql = new StringBuilder();
				if (this.hasPrimaryKey()){
					sql.append("ALTER TABLE ");
					sql.append(this.name);
					sql.append("\nDROP CONSTRAINT pk_");
					sql.append(this.name);
					results.add(sql.toString());
				}
				
				if (this.hasPrimaryKey()){
					sql = new StringBuilder();
					sql.append("ALTER TABLE ");
					sql.append(this.name);
					sql.append("\nADD ");
					sql.append(this.primaryKeyToSQL());
					results.add(sql.toString());
				}

				
			}
			if ((attSrc.size != attDest.size) || (!attSrc.type.equals(attDest.type))){
				StringBuilder sql = new StringBuilder();
				//ALTER TABLE this.name
				//ALTER COLUMN attSrc.name
				sql.append("ALTER TABLE ");
				sql.append(this.name);
				sql.append("\nMODIFY ");
				sql.append(attSrc.name);
				sql.append(" ");
				sql.append(attDest.type);
				if (!attDest.type.equals("DATE")){
					sql.append(" (");
					sql.append(attDest.size);
					sql.append(")");
				}

				results.add(sql.toString());
				
			}
		}
	}



	/**
	 * Ajoute les requettes SQL des tables à supprimer
	 * @param tableSource
	 * @param results
	 */
	private void dropAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToDrop(tableSource)){
			StringBuilder build = new StringBuilder();
			build.append("ALTER TABLE ");
			build.append(this.name);
			build.append("\n");
			build.append("DROP COLUMN ");
			build.append(attribute.name);
			results.add(build.toString());
		}
	}


	/**
	 * Ajoute les requettes SQL des tables à ajouter
	 * @param tableSource
	 * @param results
	 */
	private void addAttributes(Table tableSource, ArrayList<String> results) {
		for (Attribute attribute : attributesToAdd(tableSource)){
			for (String sql : attribute.toSQL(this.name)){
				StringBuilder build = new StringBuilder();
				build.append("ALTER TABLE ");
				build.append(this.name);
				build.append("\n");
				build.append("ADD ");
				build.append(sql);
				results.add(build.toString());//on ajoute plusieures requetes pour un seul attribut
			}
		}
	}
	
	/**
	 * Retourne une liste d'attributs 
	 * qui doivent etre supprimés
	 * @param tableSource
	 * @return
	 */
	private ArrayList<Attribute> attributesToDrop(Table tableSource) {
		ArrayList<Attribute> attributesToDrop = new ArrayList<Attribute>();
		for (Attribute att : tableSource.getAttributes()){
			if (!estContenu(this.attributes,att)){
				attributesToDrop.add(att);
			}
	
		}
		
		return attributesToDrop;
	}

	/**
	 * Retourne une liste d'attributs
	 * qui sont différents et qui doivent
	 * donc être modifié
	 * 
	 * Le Attribute est composé de attribut source et attribut cible
	 * Attribute[0] ==> attribut actuel
	 * Attribute[1] ==> attribut à obtenir
	 * @param tableSource
	 * @return
	 */
	private ArrayList<Attribute[]> attributesToChange(Table tableSource) {
		ArrayList<Attribute[]> attributesToModify = new ArrayList<Attribute[]>();
		for (Attribute attributeSrc : tableSource.getAttributes()){
			for (Attribute attributeDest : this.attributes){
				if (attributeSrc.name.equals(attributeDest.name)){
					if (!attributeSrc.toString().equals(attributeDest.toString())){// on cherche les memes attributs mais différents
						attributesToModify.add(new Attribute[]{
								attributeSrc,
								attributeDest
						});
					}
				}
			}
		}
		return attributesToModify;
	}


	/**
	 * Retourne une liste d'attributs à ajouter
	 * (attribtus qui n'existent pas dans une tableSource)
	 * @return
	 */
	private ArrayList<Attribute> attributesToAdd(Table tableSource) {
		ArrayList<Attribute> attributesToAdd = new ArrayList<Attribute>();
		for (Attribute att : this.attributes){
			if (!estContenu(tableSource.getAttributes(),att)){
				attributesToAdd.add(att);
			}
	
		}
		
		return attributesToAdd;
	}


	/**
	 * Retourne true si l'attribut est contenu dans 
	 * la liste passé en paramètres
	 * @param ListOfAtributes
	 * @param att
	 * @return boolean
	 */
	private boolean estContenu(LinkedHashSet<Attribute> listAtributes, Attribute att) {
		for (Attribute attribute : listAtributes){
			if (attribute.name.equals(att.name)){
				return true;
			}
		}
		return false;
	}



	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de suppression de table.
	 * 
	 * @return String
	 */
	public String toDrop()
	{
		return "DROP TABLE " + this.name + 
				(this.cascade ? " CASCADE CONSTRAINTS" : "");
	}
	
	
	/**
	 * Retourne le nombre d'attributs qui
	 * composent la clef primaire de $this.
	 * 
	 * @return int
	 */
	private int countPrimaryKey()
	{
		int result = 0;
		for (Attribute a : this.attributes) {
			if (a.primaryKey) result++;
		}
		return result;
	}
	
	
	/**
	 * Retourne vrai si et seulement si $this possède
	 * au moins un attribut de clef primaire.
	 * 
	 * @return boolean
	 */
	private boolean hasPrimaryKey()
	{
		return this.countPrimaryKey() != 0;
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui commence
	 * une requète SQL de création de table.
	 * 
	 * La chaîne retournée s'arrête après le nom de la table.
	 * 
	 * @return String
	 */
	private String tableNameToSQL()
	{
		return "CREATE TABLE " + this.name;
	}
	
	
	/**
	 * Retourne une chaine de caractères qui représente
	 * l'ensemble des attributs de $this sous la forme d'une 
	 * requête SQL. A l'exception des contraintes de clefs primaires,
	 * toutes les autres sont retournées.
	 * 
	 * @return String
	 */
	private String attributesToSQL()
	{
		StringBuilder result = new StringBuilder();
		for (Attribute a : this.attributes) {
			a.setTableName(this.name);
			result.append(a.toCreate());
			result.append(",\n");
		}
		int resultLength = result.length();
		result.deleteCharAt(resultLength-1);
		result.deleteCharAt(resultLength-2);
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne vide si et seulement si $this n'a pas de clée primaire.
	 * 
	 * Retourne une chaîne de caractères qui synthétise les attributs
	 * de la clée primaire de $this sous la forme d'une clause CONSTRAINT.
	 * CONTRAINT pk_table PRIMARY KEY (noms)
	 * @return String
	 */
	private String primaryKeyToSQL()
	{
		StringBuilder result = new StringBuilder();
		if (this.hasPrimaryKey()) {
			result.append("CONSTRAINT pk_");
			result.append(this.name);
			result.append(" PRIMARY KEY (");
			for (Attribute a : this.attributes) {
				if (a.primaryKey) {
					result.append(a.name);
					result.append(", ");
				}
			}
			result.deleteCharAt(result.length()-1);
			result.deleteCharAt(result.length()-1);
			result.append(")");
		}
		return result.toString();
	}
	
	
	/**
	 * Recopie chaque attribut contenu dans $attributes,
	 * et place la copie dans l'ensemble d'attributs de $this.
	 * 
	 * @param attributes : un ensemble d'attributs, peut être vide.
	 */
	private void copyAttributes(LinkedHashSet<Attribute> attributes)
	{
		for (Attribute a : attributes) {
			this.attributes.add(new Attribute(a));
		}
	}




}

