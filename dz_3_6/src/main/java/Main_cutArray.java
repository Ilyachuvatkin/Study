import java.util.Arrays;

public class Main_cutArray {
    public static void main(String[] args) {
        int[] array = {2, 4, 55, 8, 0, 4};
        int[] newArray = cutArray(array);
        for (int i : newArray) {
            System.out.println(i);
        }
    }

    static int[] cutArray(int[] array) {
        int position = 0;
        for (int j = 0; j < array.length; j++) {
            if (array[j] == 4) {
                position = j + 1;
            }
        }
        if (position != 0) {
            return Arrays.copyOfRange(array, position, array.length);
        } else {
            throw new RuntimeException("Array without '4'");
        }
    }
}
