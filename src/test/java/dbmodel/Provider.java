package dbmodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
//import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.Random;

public class Provider {

//	public void main(String[] args) throws Exception {
//
//		 ExecuteScalar("select * from environments where rownum=1", "VTAS");
//		// ExecuteCommand("update KEYTORC_RESERVATION set gsm_no='4444'",
//		// "VTAS");
//		try {
//			GetDataTable("select * from environments where system='TRAFGEN'", "VTAS");
//			System.out.println("basarili");
//		} catch (Exception ex) {
//			System.out.println("hata!");
//		}
//
//	}

	public String[] MyConnectionString(String env ,String system) throws Exception {

		String[] DBInfo = new String[5];

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.1.220;databaseName=GunceTestOtomasyon",
				"guncetestuser", "Otomasyon2019");
		Statement st = con.createStatement();
		String sql = "exec GetEnvironmentInfo '" + env + "', '" + system + "'";
		ResultSet rs = st.executeQuery(sql);
		if (rs.next())
		DBInfo[0] = rs.getString("ip_address");
		DBInfo[1] = rs.getString("port");
		DBInfo[2] = rs.getString("dbname");
		DBInfo[3] = rs.getString("userid");
		DBInfo[4] = rs.getString("password");
		con.close();

		return DBInfo;
	}
	
	public String SetRandomString(int byteLength) {

		String text = "";
		for (int i = 0; i < byteLength; i++) {
			Random rnd = new Random();
			char c = (char) (rnd.nextInt(26) + 'a');
			text = text + c;
		}
		return text;
	}
	
	public String checkDBCell(String prefix, String value) {
if (value == null || value.isEmpty()) {
	value = prefix + SetRandomString(15);
	}
		return value;
	}
	
	public String ExecuteScalar(String query, String env ,String system) throws Exception {
		String[] DBInfo = MyConnectionString(env, system);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DriverManager.getConnection("jdbc:sqlserver://" + DBInfo[0] + ";databaseName=" + DBInfo[2],
				DBInfo[3], DBInfo[4]);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		String value = rs.getString(1);
		con.close();
		rs.close();
		st.close();
		return value;
	}

	public void ExecuteCommand(String query, String env, String system) throws Exception {
		String[] DBInfo = MyConnectionString(env, system);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DriverManager.getConnection("jdbc:sqlserver://" + DBInfo[0] + ";databaseName=" + DBInfo[2],
				DBInfo[3], DBInfo[4]);
		Statement st = con.createStatement();
		st.execute(query);
		st.close();
		con.close();

	}
	
	public String[][] GetDataTable(String query, String env, String system) throws Exception {

		try {
			int col = 0, row = 0;
			String[] DBInfo = MyConnectionString(env, system);
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection("jdbc:sqlserver://" + DBInfo[0] + ";databaseName=" + DBInfo[2],
					DBInfo[3], DBInfo[4]);
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rowdata = (ResultSetMetaData) rs.getMetaData();
			
			if (!rs.next()) {
				System.out.println("db'den geri değer dönmedi");
				return null;
			}
			
			col = rowdata.getColumnCount();
			rs.last();
			row = rs.getRow();
			
				
			
			String[][] datatable = new String[row + 1][col];

			for (int i = 0; i < col; i++) {
				datatable[0][i] = rowdata.getColumnName(i + 1);
			}
			
			rs.beforeFirst();
			while (rs.next()) {
				for (int i = 1; i < row + 1; i++) {
					for (int k = 0; k < col; k++)
						datatable[i][k] = rs.getString(k + 1);
				}
			}

			con.close();
			rs.close();
			st.close();
			return datatable;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}

	

	
	
//	public String[][] GetDataTable(String query, String env) throws Exception {
//		try {
//			int col = 0, row = 0;
//			String[] DBInfo = MyConnectionString(env);
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection con = DriverManager.getConnection("jdbc:sqlserver://"+ DBInfo[0] +";databaseName=" + DBInfo[2], DBInfo[3], DBInfo[4]);Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
//
//			ResultSetMetaData rowdata = (ResultSetMetaData) rs.getMetaData();
//			col = rowdata.getColumnCount();
//
//			while (rs.next())
//				row++;
//			rs.close();
//
//			String[][] datatable = new String[row][col];
//			ResultSet rs2 = st.executeQuery(query);
//
//			while (rs2.next()) {
//				for (int i = 0; i < row; i++) {
//					for(int k =0; k< col; k++)
//					datatable[i][k] = rs2.getString(k+1);
//				}
//			}
//			
//			con.close();
//			rs2.close();
//			st.close();
//			return datatable;
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//			return null;
//		}
//
//	}

}
