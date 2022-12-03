package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the Product class. */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** @return The id */
    public int getId() {
        return id;
    }

    /** @param id  The id to set */
    public void setId(int id) {
        this.id = id;
    }

    /** @return The name */
    public String getName() {
        return name;
    }

    /** @param name The name to set */
    public void setName(String name) {
        this.name = name;
    }

    /** @return The price */
    public double getPrice() {
        return price;
    }

    /** @param price The price to set */
    public void setPrice(double price) {
        this.price = price;
    }

    /** @return The stock */
    public int getStock() {
        return stock;
    }

    /** @param stock The stock to set */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** @return The min */
    public int getMin() {
        return min;
    }

    /** @param min  The min to set */
    public void setMin(int min) {
        this.min = min;
    }

    /** @return The max */
    public int getMax() {
        return max;
    }

    /** @param max The max to set */
    public void setMax(int max) {
        this.max = max;
    }

    /** @param part The part to add */
    public void addAssociatedParts(Part part){
        associatedParts.add(part);
    }

    /** @return The associated parts */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /** This method will delete an associated part from a selected product.
     If the associated parts list contains the part, it will be removed.
     @param selectedAssociatedPart The associated part to delete
     */
    public boolean deleteAssociatedPart (Part selectedAssociatedPart){
        boolean hasAssociatedPart = associatedParts.contains(selectedAssociatedPart);

        if(hasAssociatedPart){
            associatedParts.remove(selectedAssociatedPart);
        }
        return true;
    }

}
