package demo.devshield.statement;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Searches account statements by a text the customer types. */
@RestController
public class StatementController {

	private final JdbcTemplate jdbc;

	public StatementController(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@GetMapping("/statements")
	public List<Map<String, Object>> search(@RequestParam String text) {
		// New SQL injection, added by this branch: the text goes straight into the query.
		String sql = "SELECT id, owner, balance FROM accounts WHERE owner LIKE '%" + text + "%'";
		return this.jdbc.queryForList(sql);
	}

}
