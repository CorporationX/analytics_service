package faang.school.analytics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestClassTest {
    @InjectMocks
    private TestService service;

    @Test
    public void test() {
        int a = 1;
        int b = 1;
        Assertions.assertEquals(a, b);
    }

    @Test
    public void testGetTest() {
        int a = service.doTest();
        Assertions.assertEquals(2, a);
    }
}
