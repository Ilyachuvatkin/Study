package ex3;

public class Main {
    public static void main(String[] args) {
        Box<Orange> boxWithOrange = new Box<>();
        Box<Apple> boxWithApple = new Box<>();
        Box<Apple> boxWithApple2 = new Box<>();
        boxWithApple2.putOneFruit(new Apple());
        boxWithApple2.putOneFruit(new Apple());
        boxWithApple.putOneFruit(new Apple());
        boxWithApple.putOneFruit(new Apple());
        boxWithOrange.putOneFruit(new Orange());
        boxWithOrange.putOneFruit(new Orange());

        System.out.println(boxWithApple.getAllFruits());
        System.out.println(boxWithApple2.getAllFruits());

        System.out.println("Коробки равны по весу? - " + boxWithApple.compare(boxWithApple2));

        boxWithApple.moveTo(boxWithApple2);
        System.out.println(boxWithApple.getAllFruits());
        System.out.println(boxWithApple2.getAllFruits());
    }
}
