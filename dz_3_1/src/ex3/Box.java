package ex3;

import java.util.ArrayList;

public class Box <T extends Fruit> {
    private ArrayList<T> allFruits;

    public void setAllFruits(ArrayList<T> allFruits) {
        this.allFruits = allFruits;
    }

    public void putOneFruit(T fruit) {
        if (allFruits == null) {
            allFruits = new ArrayList<T>();
        }
        allFruits.add(fruit);
    }

    public ArrayList<T> getAllFruits() {
        return allFruits;
    }

    public boolean compare(Box <?> anotherBox) {
        return Math.abs(getWeight(this) - getWeight(anotherBox))<0.0001;
    }

    public float getWeight() {
        return getWeight(this);
    }

    private float getWeight (Box<?> box){
        float weight = 0;
        if (box.allFruits == null) {
            return 0;
        }
        Fruit someFruit = box.allFruits.get(0);
        if (someFruit instanceof Apple) {
            weight = box.allFruits.size() * Apple.WEIGHT;
        }
        if (someFruit instanceof Orange) {
            weight = box.allFruits.size() * Orange.WEIGHT;
        }
        return weight;
    }

    public void moveTo(Box<T> anotherBox) {
        if (anotherBox.allFruits == null) {
            anotherBox.allFruits = new ArrayList<T>();
        }
        anotherBox.allFruits.addAll(this.allFruits);
        setAllFruits(null);

    }
}
