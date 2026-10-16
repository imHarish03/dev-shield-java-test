package demo.devshield.account;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/** Accounts in the database. */
@Repository
public class AccountRepository {

	private final JdbcTemplate jdbc;

	public AccountRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	/** Accounts whose owner's name matches. */
	public List<Map<String, Object>> findByOwner(String owner) {
		// SQL injection: the name goes straight into the statement.
		String sql = "SELECT id, owner, balance FROM accounts WHERE owner = '" + owner + "'";
		return this.jdbc.queryForList(sql);
	}

	/** Accounts sorted by a column the caller chooses. */
	public List<Map<String, Object>> listSortedBy(String column) {
		// SQL injection through String.format.
		return this.jdbc.queryForList(String.format("SELECT id, owner, balance FROM accounts ORDER BY %s", column));
	}

	public int deposit(long id, long amount) {
		return this.jdbc.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, id);
	}

}
