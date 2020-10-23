package ex1_2;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String[] array = new String[10];      //любой массив ссылочного типа
        for (int i = 0; i <10; i++) {         // заполняем для проверки
            array[i] = Integer.toString(i);
            System.out.print(" "+array[i]);
        }
        System.out.println();
        int firstSwapElement = 3;     //указываем необходимое значение
        int secondSwapElement = 5;    //указываем необходимое значение
        swap(array,firstSwapElement,secondSwapElement);
        for (int i = 0; i <10; i++) {
            System.out.print(" "+array[i]);
        }
        System.out.println();
        ArrayList<String> arrayList= toArrayList(array);
        System.out.println(arrayList);
    }

    public static <T> T[] swap (T[] array, int a , int b) {
        T tempElement;
        tempElement = array[a];
        array[a] = array[b];
        array[b] = tempElement;
        return array;

    }

    public static <T> ArrayList<T> toArrayList(T[] array) {
        ArrayList<T> newArrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            newArrayList.add(array[i]);
        }
        return newArrayList;
    }
}
