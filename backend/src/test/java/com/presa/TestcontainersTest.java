package com.presa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest Trying to not use it for unit tests,
// only use for integration tests

public class TestcontainersTest extends AbstractTestContainersUnitTest {
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
