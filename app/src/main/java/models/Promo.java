package models;

import java.util.ArrayList;

public class Promo {
    public int id;
    public String name;

    public Promo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static int findIdIn(ArrayList<Promo> promosList, String promoName){
        for(Promo promo : promosList){
            if(promo.name.equals(promoName)){
                return  promo.id;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
