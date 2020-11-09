import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestCutArray {
    public int[] array;
    public int[] newArray;

    public TestCutArray(int[] array, int[] newArray) {
        this.array = array;
        this.newArray = newArray;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {(new int[]{4, 45, 45, 45, 56}), (new int[]{45, 45, 45, 56})},
                {new int[]{0, 8, 2, 6, 3, 4}, (new int[]{})},
                {(new int[]{4, 65, 7, 3, 0, 4, 5}), (new int[]{5})}
        });
    }

    @Test
    public void test() {
        Assert.assertArrayEquals(Main_cutArray.cutArray(array), newArray);
    }

    @Test(expected = RuntimeException.class)
    public void test1() {
        Assert.assertArrayEquals(Main_cutArray.cutArray(new int[]{1, 6, 9, 45, 8, 3}), new int[]{});
    }
}
