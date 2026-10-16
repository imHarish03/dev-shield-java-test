package demo.devshield.account;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Random;

import org.springframework.stereotype.Service;

/** Session tokens, password hashes, and the signing key for API tokens. */
@Service
public class TokenService {

	// Predictable: java.util.Random for session tokens.
	private final Random random = new java.util.Random();

	public String newSessionToken() {
		return Long.toHexString(this.random.nextLong()) + Long.toHexString(this.random.nextLong());
	}

	/** Hashes a password with MD5, which is fast to brute-force. */
	public String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		return HexFormat.of().formatHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
	}

	/** The key that signs API tokens, written into the code. */
	public Key signingKey() {
		return io.jsonwebtoken.security.Keys.hmacShaKeyFor("accounts-api-signing-key-do-not-share-2026".getBytes());
	}

}
