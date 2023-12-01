package org.example.lab3;

import java.util.Random;

public class PartOne {
    int amount1, amount2, amount3;
    public PartOne() {
        Random random = new Random();
        Product product1 = new Product(), product2 = new Product();
        amount1 = random.nextInt(501) + 200;
        amount2 = random.nextInt(501) + 200;
        amount3 = random.nextInt(501) + 200;
    }
}
