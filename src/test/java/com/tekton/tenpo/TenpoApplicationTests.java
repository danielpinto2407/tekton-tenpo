package com.tekton.tenpo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestTenpoApplication.class)
@ActiveProfiles("test")
class TenpoApplicationTests {

    @Test
    void contextLoads() {
        assert true;
    }
}
