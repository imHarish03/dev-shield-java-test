package demo.devshield.account;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	private final AccountRepository accounts;

	private final TokenService tokens;

	public AccountController(AccountRepository accounts, TokenService tokens) {
		this.accounts = accounts;
		this.tokens = tokens;
	}

	// Any website may call this API.
	@CrossOrigin(origins = "*")
	@GetMapping("/accounts")
	public List<Map<String, Object>> search(@RequestParam String owner) {
		return this.accounts.findByOwner(owner);
	}

	@GetMapping("/accounts/sorted")
	public List<Map<String, Object>> sorted(@RequestParam(defaultValue = "id") String by) {
		return this.accounts.listSortedBy(by);
	}

	@PostMapping("/accounts/{id}/deposit")
	public int deposit(@PathVariable long id, @RequestParam long amount) {
		return this.accounts.deposit(id, amount);
	}

	@PostMapping("/session")
	public String signIn(@RequestParam String owner, HttpServletResponse response) {
		Cookie session = new Cookie("SESSION", this.tokens.newSessionToken());
		// The session cookie is also sent over plain HTTP.
		session.setSecure(false);
		response.addCookie(session);
		return "signed in as " + owner;
	}

}
