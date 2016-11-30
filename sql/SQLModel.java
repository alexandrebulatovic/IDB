package sql;

import java.sql.*;

import javax.swing.JOptionPane;

import connect.*;

public class SQLModel {
	
	public SQLModel(){
		try { 

			Connection conn = ConnectionManager.getConnection();
			Statement stat = conn.createStatement();
			
			// je recupere la requete
			SQLView wtf = new SQLView();
			String sql = wtf.getSqlArea().getText();
			
			//on execute sql
			StringBuilder stringBuilder = new StringBuilder(); // pour les requetes SELECT
			
			boolean res = stat.execute(sql);
			if (res) { // si c'est un select
				
				ResultSet rs = stat.getResultSet() ;
						
						while(rs.next()) {
							// TODO connaitre le nombre de colonnes puis le type de chaque colonne pour les afficher
							//stringBuilder.append("Some text")

							
						}
				String finalString = stringBuilder.toString();
				JOptionPane.showMessageDialog(null, finalString, null, JOptionPane.INFORMATION_MESSAGE);
			} else { //  INSERT, UPDATE, or DELETE ou definition de donnees
				if (sql.contains("INSERT")||sql.contains("UPDATE")||sql.contains("DELETE")){
					JOptionPane.showMessageDialog(null,stat.getUpdateCount()+" lignes affectées!", null, JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Réussite!", null, JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
			
			conn.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} 
	}


}
