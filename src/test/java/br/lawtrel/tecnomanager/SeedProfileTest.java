package br.lawtrel.tecnomanager;

import br.lawtrel.tecnomanager.controller.DBSeedingController;
import br.lawtrel.tecnomanager.service.DBSeedingService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SeedProfileTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withBean(DBSeedingService.class, () -> mock(DBSeedingService.class))
            .withUserConfiguration(DBSeedingController.class);

    @ParameterizedTest
    @ValueSource(strings = {"default", "prod", "test", "local,prod", "local,test"})
    void seedIsNotRegisteredOutsideLocalDevelopment(String profiles) {
        runner.withPropertyValues("spring.profiles.active=" + profiles)
                .run(context -> assertThat(context).doesNotHaveBean(DBSeedingController.class));
    }

    @Test
    void seedIsAvailableOnlyWhenLocalIsExplicitlySelected() {
        runner.withPropertyValues("spring.profiles.active=local")
                .run(context -> assertThat(context).hasSingleBean(DBSeedingController.class));
    }
}
