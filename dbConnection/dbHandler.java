package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.AlarmData;

public class dbHandler {
	
	private static Connection con;
	private static boolean hasData = false;
	private static String url = "jdbc:sqlite:..\\";
	private static String fileName = "AlarmDB";
	
	public dbHandler() throws ClassNotFoundException, SQLException
	{
		if(con == null)
		{
			getConnection();
		}
	}
	
	//Get Connection
	private void getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection(url + fileName);
		initialize();
	}

	
	//Create Database
		private void initialize() throws SQLException
		{
			if(!hasData) {
				hasData = true;
				
				Statement state = con.createStatement();
				ResultSet res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='alarms'");
				
				if(!res.next()) {
					System.out.println("No Results: Building User table");
					
					//build table
					Statement state2 = con.createStatement();
					state2.execute("CREATE TABLE alarms(hour integer," + "minute integer," + "name varchar(60)," + "file varchar(60)," + "hasPlayed Boolean,"
					+ "Sunday boolean,"+" Monday boolean,"+" Tuesday boolean,"+" Wednesday boolean,"+" Thursday boolean,"+" Friday boolean, "+" Saturday boolean)");
					
					
					//Test data/////////////////////
					PreparedStatement prep = con.prepareStatement("INSERT INTO alarms values(?,?,?,?,?,?,?,?,?,?,?,?)");
					prep.setInt(1, 5);
					prep.setInt(2, 45);
					prep.setString(3,"Test");
					prep.setString(4,"C:\\Users\\qchoumont\\Music\\orchestrawav-26158.mp3");
					prep.setBoolean(5, false);
					prep.setBoolean(6, false);
					prep.setBoolean(7, false);
					prep.setBoolean(8, true);
					prep.setBoolean(9, false);
					prep.setBoolean(10, false);
					prep.setBoolean(11, false);
					prep.setBoolean(12, false);
					
					prep.execute();
					//////////////////////////////////
				}
			}
		}
		
		//Get Alarms
		public ResultSet getAll() throws ClassNotFoundException, SQLException
		{
			if(con==null) {
				getConnection();
			}
				Statement state = con.createStatement();
				ResultSet rs = state.executeQuery("SELECT rowid, * FROM alarms");

				return rs;
		}
		
	//Set Alarms
	public void AddAlarm(AlarmData ad) throws ClassNotFoundException, SQLException
	{
		if(con==null) {
			getConnection();
		}
		
			String sqlInsert = "INSERT INTO alarms VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement stmt;
			stmt = con.prepareStatement(sqlInsert);
			
			stmt.setInt(1,ad.getHour());
			stmt.setInt(2,ad.getMinute());
			stmt.setString(3,ad.getName());
			stmt.setString(4,ad.getFile());
			stmt.setBoolean(5,false);
			stmt.setBoolean(6, ad.getSun());
			stmt.setBoolean(7, ad.getMon());
			stmt.setBoolean(8, ad.getTue());
			stmt.setBoolean(9, ad.getWed());
			stmt.setBoolean(10, ad.getThr());
			stmt.setBoolean(11, ad.getFri());
			stmt.setBoolean(12, ad.getSat());
			stmt.execute();
	}
	
	public void UpdateAlarm(int id, boolean hasPlayed) throws SQLException, ClassNotFoundException
	{
		if(con==null) {
				getConnection();
		}
		String sqlInsert = "UPDATE alarms SET hasPlayed = ? WHERE rowid = ?";
		PreparedStatement prep = con.prepareStatement(sqlInsert);
		prep.setBoolean(1,hasPlayed);
		prep.setInt(2,id);
		prep.execute();
	}
	
	//Delete Alarm
	public void DeleteAlarm(int id) throws ClassNotFoundException, SQLException
	{
		if(con==null) {
			getConnection();
		}
		PreparedStatement prep = con.prepareStatement("DELETE FROM alarms WHERE rowid = " + id);
		prep.execute();
	}	
}
