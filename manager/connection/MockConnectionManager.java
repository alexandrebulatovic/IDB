package manager.connection;

import java.sql.Connection;

import useful.ConnectionStrings;
import useful.Response;

public class MockConnectionManager 
implements I_ConnectionManager 
{
	//Attributs
	/** Nom d'utilisateur.*/
	private String user;
	
	
	//Méthodes
	@Override
	public String getUser() {return this.user;}

	
	@Override
	public Connection getConnection() {return null;}

	
	@Override
	public boolean isConnected() {return true;}

	
	@Override
	public Response connect(ConnectionStrings param) 
	{
		this.user = param.user;
		return new Response(true, "Connexion réussie.");
	}

	
	@Override
	public Response reconnect() 
	{
		return new Response(true, "Connexion réussie.");
	}

	@Override
	public void disconnect() {}
}
