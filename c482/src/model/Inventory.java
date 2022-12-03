package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

/** This is the Inventory class. */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** This method will add a part to the allParts list.
     @param part The part to add
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /** This method will add a product to the allProducts list.
     @param product The product to add
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /** This method will search for a part in the allParts list by part ID.
     If the ID searched matches the partID, it will return the part. If there is no match, it will return null.
     @param partId The partId to search
     @return The part matching the partId searched
     */
    public static Part lookupPart(int partId){
        int index = 0;
        boolean hasPart = false;

        for (Part pt : allParts){
            if (pt.getId() == partId){
                index = allParts.indexOf(pt);
                hasPart = true;
            }
        }
        if (hasPart) {
            return allParts.get(index);
        }
        else {
            return null;
        }
    }

    /** This method will search for a product in the allParts list by product ID.
     If the ID searched matches the productID, it will return the product. If there is no match, it will return null.
     @param productId The productId to search
     @return The product matching the productId searched
     */
    public static Product lookupProduct(int productId){
        int index = 0;
        boolean hasProduct = false;

        for (Product pd : allProducts){
            if (pd.getId() == productId){
                index = allProducts.indexOf(pd);
                hasProduct = true;
            }
        }
        if (hasProduct){
            return allProducts.get(index);
        }
        else {
            return null;
        }
    }

    /** This method will search for a part in the allParts list by part name.
     The name does not need to be an exact match, if the searched string matches any letters in the part name, the part(s) will get returned.
     @param name The name searched
     @return The matching parts
     */
    public static ObservableList<Part> lookupPart (String name){
        ObservableList<Part> lookupPartList = FXCollections.observableArrayList();

        for (Part pt : allParts){
            if(pt.getName().toLowerCase().contains(name.toLowerCase())){
                lookupPartList.add(pt);
            }
        }
        return lookupPartList;
    }

    /** This method will search for a product in the allProducts list by product name.
     The name searched does not need to be an exact match. If the searched string matches any letters, the product(s) will get returned.
     @param name The name searched
     @return The matching products
     */
    public static ObservableList<Product> lookupProduct(String name){
        ObservableList<Product> lookupProductList = FXCollections.observableArrayList();

        for (Product pd : allProducts){
            if(pd.getName().toLowerCase().contains(name.toLowerCase())){
                lookupProductList.add(pd);
            }
        }
        return lookupProductList;
    }

    /** This method will delete a selected part from the allParts list.
     If allParts contains the selected part, it will be removed.
     @param selectedPart The part to delete
      */
    public static boolean deletePart(Part selectedPart){
        boolean hasPart = allParts.contains(selectedPart);

        if(hasPart){
            allParts.remove(selectedPart);
        }
        return true;
    }

    /** This method will update a part that has been modified.
     This method will increment through the allParts list, finding the part at the index that matches the part ID of the selected part. A new part will be inserted at that index with the new values.
     @param selectedPart The selected part to update
     */
    public static void updatePart (Part selectedPart){
        for (int index = 0; index < allParts.size(); index++){
            if(allParts.get(index).getId() == selectedPart.getId()){
                allParts.set(index, selectedPart);
            }
        }
    }

    /** This method will update a product that has been modified.
     This method will increment through the allProducts list, finding the product at the index that matches the product ID of the selected product. A new product will be inserted at that index with the new values.
     @param selectedProduct The selected product to update
     */
    public static void updateProduct (Product selectedProduct){
        for (int index = 0; index < allProducts.size(); index++){
            if(allProducts.get(index).getId() == selectedProduct.getId()){
                allProducts.set(index, selectedProduct);
            }
        }
    }

    /** This method will delete a product from the allProducts list.
     If allProducts contains the selected product, it will be removed.
     @param selectedProduct The product to delete
     */
    public static boolean deleteProduct (Product selectedProduct){
        boolean hasProduct = allProducts.contains(selectedProduct);

        if (hasProduct) {
            allProducts.remove(selectedProduct);
        }
        return true;
    }

    /** This method will return the allParts list.
      @return The all parts list */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /** This method will return the allProducts list.
      @return  The all products list*/
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}


