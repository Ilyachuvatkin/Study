import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCheckArray {
    int[] array;
    boolean check;

    public TestCheckArray(int[] array, boolean check) {
        this.array = array;
        this.check = check;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 1, 1, 1, 1}, false},
                {new int[]{4, 4, 4, 4, 4}, false},
                {new int[]{1, 4, 1, 4, 1, 1}, true},
                {new int[]{1, 4, 5, 1, 4}, false}
        });
    }
    @Test
    public void testCheck() {
        Assert.assertEquals(Main_checkArray.checkArray(array), check);
    }
}
