package nagusia;

import java.sql.*;
import java.util.Date;

import eredu.domeinua.Erabiltzailea;

public class GertaerakBerreskuratuJDBC {
	public static void main(String[] args) {
		Connection c;
		Statement s;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost/gertaerak", "root", "");
			s = c.createStatement();
			rs = s.executeQuery("SELECT * FROM LOGINGERTAERA");
			System.out.println("LOGINGERTAERA (ID, DESKRIBAPENA, DATA)");
			while (rs.next()) {
				Long id = rs.getLong("ID");
				String deskribapena = rs.getString("DESKRIBAPENA");
				Date data = rs.getDate("DATA");
				System.out.println(id + " / " + deskribapena + " / " + data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getErabiltzaileaJDBC(Erabiltzailea e) {
		Connection c;
		PreparedStatement s;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost/gertaerak", "root", "");
			s = c.prepareStatement("SELECT * FROM ERABILTZAILEA WHERE izena=?");
			s.setString(1, e.getIzena());
			rs = s.executeQuery();
			if (rs.next())
				return (String) (rs.getString("IZENA") + "/" + rs.getString("PASAHITZA") + "/" + rs.getString("MOTA"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "--/--/--";
	}

}