package br.lawtrel.tecnomanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@org.springframework.test.context.ContextConfiguration(initializers = TestDatabaseInitializer.class)
@ActiveProfiles("test")
class TecnomanagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
