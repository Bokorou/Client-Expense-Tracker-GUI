package org.example.Models;

public class TransactionCategory {
    private int id;
    private String categoryName;
    private String CategoryColour;

    public TransactionCategory(int id, String categoryName, String categoryColour) {
        this.id = id;
        this.categoryName = categoryName;
        CategoryColour = categoryColour;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColour() {
        return CategoryColour;
    }

    public void setCategoryColour(String categoryColour) {
        CategoryColour = categoryColour;
    }
}
