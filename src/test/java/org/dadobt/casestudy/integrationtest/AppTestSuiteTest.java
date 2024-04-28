package org.dadobt.casestudy.integrationtest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticateControllerTest.class,
        InvoiceControllerTest.class,
        SupplierControllerTest.class
})
public class AppTestSuiteTest {
}
