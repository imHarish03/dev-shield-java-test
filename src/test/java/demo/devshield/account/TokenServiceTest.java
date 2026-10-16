package demo.devshield.account;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

	private final TokenService tokens = new TokenService();

	@Test
	void sessionTokensAreLongAndDifferent() {
		String first = this.tokens.newSessionToken();
		String second = this.tokens.newSessionToken();
		assertThat(first).hasSizeGreaterThan(16);
		assertThat(first).isNotEqualTo(second);
	}

	@Test
	void theSamePasswordHashesTheSameWay() throws Exception {
		assertThat(this.tokens.hashPassword("secret")).isEqualTo(this.tokens.hashPassword("secret"));
		assertThat(this.tokens.hashPassword("secret")).isNotEqualTo(this.tokens.hashPassword("other"));
	}

}
