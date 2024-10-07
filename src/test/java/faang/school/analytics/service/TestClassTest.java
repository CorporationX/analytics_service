package faang.school.analytics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestClassTest {

    @Test
    public void test() {
        int a = 1;
        int b = 1;
        Assertions.assertEquals(a, b);
    }
}
