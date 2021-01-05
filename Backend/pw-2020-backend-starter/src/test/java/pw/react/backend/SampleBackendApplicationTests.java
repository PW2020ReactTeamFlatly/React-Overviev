package pw.react.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pw.react.backend.service.HttpClient;
import pw.react.backend.web.Quote;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
//@ActiveProfiles(profiles = {"mysql-dev"})
class SampleBackendApplicationTests {

	@Autowired
	private HttpClient httpService;

	//@Test
	void contextLoads() {
	}

	//@Test
	void whenConsume_thenReturnQuote() {
		final Quote quote = httpService.consume("");
		assertThat(quote).isNotNull();
	}
}
