public class Main_checkArray {
    public static void main(String[] args) {
        int[] array = {1, 1, 1, 1, 4};
        checkArray(array);
        System.out.println(checkArray(array));
    }

    static boolean checkArray(int[] array) {
        boolean haveOne = false;
        boolean haveFour = false;
        boolean haveAnotherNumber = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                haveOne = true;
            } else {
                if (array[i] == 4) {
                    haveFour = true;
                } else {
                    haveAnotherNumber = true;
                }
            }
        }
        if (haveOne && haveFour && !haveAnotherNumber) {
            return true;
        } else return false;
    }
}
