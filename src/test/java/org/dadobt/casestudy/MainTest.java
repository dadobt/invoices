package org.dadobt.casestudy;

import org.dadobt.casestudy.integrationtest.AppTestSuiteTest;
import org.junit.jupiter.api.Test;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite.SuiteClasses(AppTestSuiteTest.class)
public class MainTest {

    @Test
    void contextLoads() {
    }
}