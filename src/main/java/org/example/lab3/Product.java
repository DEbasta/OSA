package org.example.lab3;

import java.util.Random;

public class Product {
    int mat1, mat2, mat3, profit, revenue, costPrice;
    public int getCostPrice() {
        return costPrice;
    }

    public int getMat1() {
        return mat1;
    }

    public int getMat2() {
        return mat2;
    }

    public int getMat3() {
        return mat3;
    }

    public int getProfit() {
        return profit;
    }

    public int getRevenue() {
        return revenue;
    }

    public Product(){
        Random random = new Random();
        mat1 = random.nextInt(4)+2;
        mat2 = random.nextInt(4)+2;
        mat3 = random.nextInt(4)+2;
        profit = random.nextInt(201) + 50;
        revenue = random.nextInt(1201) + 200;
        costPrice = random.nextInt(231) + 70;
    }
}
