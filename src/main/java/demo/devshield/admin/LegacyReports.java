package demo.devshield.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** The old reporting database, reached directly with JDBC. */
public class LegacyReports {

	private final String url = "jdbc:h2:mem:legacy";

	Connection connect() throws SQLException {
		// The database password is written into the code.
		return DriverManager.getConnection(url, "reports_admin", "R3ports!2026");
	}

	int countFor(String branch) throws SQLException {
		try (Connection connection = connect(); Statement statement = connection.createStatement()) {
			// SQL injection through a plain Statement.
			ResultSet rows = statement.executeQuery("SELECT COUNT(*) FROM reports WHERE branch = '" + branch + "'");
			return rows.next() ? rows.getInt(1) : 0;
		}
	}

}
