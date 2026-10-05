package br.lawtrel.tecnomanager;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/** Reject external databases before Flyway or Hibernate is initialized. */
public class TestDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        String url = context.getEnvironment().getRequiredProperty("spring.datasource.url");
        String user = context.getEnvironment().getRequiredProperty("spring.datasource.username");
        boolean memory = url.equals("jdbc:h2:mem:tecnomanager-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE") && user.equals("sa");
        boolean postgres = url.equals("jdbc:postgresql://127.0.0.1:55435/tecnomanager_test") && user.equals("tecnomanager_test");
        if (!memory && !postgres) throw new IllegalStateException("Use somente o banco descartável de testes do TecnoManager.");
    }
}
