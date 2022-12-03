package controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This is the Main Form Class. */
public class MainForm implements Initializable {
    public TableView partsTable;

    public TableColumn partsIdCol;
    public TableColumn partsNameCol;
    public TableColumn partsInventoryLevelCol;
    public TableColumn partsCostCol;

    public TableView productsTable;

    public TableColumn productsIdCol;
    public TableColumn productsNameCol;
    public TableColumn productsInventoryLevelCol;
    public TableColumn productsCostCol;

    public TextField searchPartsField;
    public TextField searchProductsField;

    /** This method opens the 'Add Part' screen.
     This method uses a button to open the 'Add Part' screen. This will allow the user to add parts to the inventory.
     @param actionEvent The add part button is clicked.
     */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 720);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /** This method selects a part to modify and opens the 'Modify Part' screen.
     This method uses the 'Modify' button to modify a part that the user has selected from the parts table. This will open the 'Modify Part' screen. If no part is selected, the user is alerted with an error dialog box.
     @param actionEvent The 'modify part' button is clicked.
     */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Part is selected.");
            alert.showAndWait();
        }

        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartForm.fxml"));
            Parent root = loader.load();

            ModifyPartForm modifyPartForm = loader.getController();
            modifyPartForm.setPartToModify(selectedPart);


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 720);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** This method will delete a part from the inventory system.
     This method deletes a selected part from the parts table. If no part is selected, the user is alerted with an error dialog box. The user must confirm the deletion.
     @param actionEvent The 'delete part' button is clicked.
     */
    public void toDeletePart(ActionEvent actionEvent) {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Part is selected.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete a part, do you want to continue?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                Inventory.deletePart(selectedPart);
            }
        }
    }

    /** This method will open the 'Add Product' screen.
     This method uses a button to open the 'Add Product' screen.
     @param actionEvent The add product button is clicked.
      */
    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 720);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /** This method selects a product to modify and opens the 'Modify Product' screen.
     This method uses the 'Modify' button to modify a product that the user has selected from the products table. This will open the 'Modify Product' screen. If no product is selected, the user is alerted with an error dialog box.
     @param actionEvent The 'modify product' button is clicked.
     */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Product is selected.");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductForm.fxml"));
            Parent root = loader.load();

            ModifyProductForm modifyProductForm = loader.getController();
            modifyProductForm.setProductToModify(selectedProduct);


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 720);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** This method deletes a product.
     This method will delete a product from the 'products' table when the delete button is clicked. It will not allow the user to delete a product with associated parts.
     @param actionEvent The delete product button is clicked.
     */
    public void toDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Product is selected.");
            alert.showAndWait();
        }
        else if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete a product, do you want to continue?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                Inventory.deleteProduct(selectedProduct);
            }
        }
            else {
                Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                secondAlert.setTitle("Error Dialog");
                secondAlert.setContentText("Unable to delete product - contains associated parts.");
                secondAlert.showAndWait();
            }
    }

        /** This method will search the parts table.
         This method will allow the user to search and filter the 'parts' table by part ID or part name. If the user is searching by part ID, the corresponding part will be highlighted. If the user is searching by part name, the corresponding part(s) will be filtered.
         @param actionEvent The search part field entered.
         */
        public void searchPartsTable (ActionEvent actionEvent){
            ObservableList<Part> searchedParts = FXCollections.observableArrayList();
            if (searchPartsField.getText().matches("-?\\d+")) {
                Part part = Inventory.lookupPart(Integer.parseInt(searchPartsField.getText()));
                if (part != null) {
                    searchedParts.add(part);
                    partsTable.setItems(Inventory.getAllParts());
                    partsTable.getSelectionModel().select(part);
                }
                else {
                    partsTable.setItems(searchedParts);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("No part found.");
                    alert.showAndWait();
                }
            }
            else {
                searchedParts = Inventory.lookupPart(searchPartsField.getText());
                partsTable.setItems(searchedParts);
                if (searchedParts.isEmpty()) {
                    partsTable.setItems(searchedParts);

                    Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                    secondAlert.setTitle("Error Dialog");
                    secondAlert.setContentText("No part found.");
                    secondAlert.showAndWait();
                }
            }
        }

        /** This method will search the products table.
         This method will allow the user to search and filter the 'products' table by product ID or product name. If the user is searching by part ID, the corresponding product will be highlighted. If the user is searching by product name, the corresponding product(s) will be filtered.
         @param actionEvent The search product field entered.
         */
        public void searchProductsTable(ActionEvent actionEvent) {
        ObservableList<Product> searchedProducts = FXCollections.observableArrayList();
        if (searchProductsField.getText().matches("-?\\d+")){
            Product product = Inventory.lookupProduct(Integer.parseInt(searchProductsField.getText()));
            if(product != null){
                searchedProducts.add(product);
                productsTable.setItems(Inventory.getAllProducts());
                productsTable.getSelectionModel().select(product);
            }
            else {
                productsTable.setItems(searchedProducts);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("No product found.");
                alert.showAndWait();
            }
        }
        else {
            searchedProducts = Inventory.lookupProduct(searchProductsField.getText());
            productsTable.setItems(searchedProducts);
            if (searchedProducts.isEmpty()) {
                productsTable.setItems(searchedProducts);

                Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                secondAlert.setTitle("Error Dialog");
                secondAlert.setContentText("No product found.");
                secondAlert.showAndWait();
            }
        }
    }

    /** This method will exit the application.
     @param actionEvent Exit button clicked.
     */
    public void exitButton(ActionEvent actionEvent) {
        Platform.exit();
    }

    /** This method initializes the Main Form class.
     The tables are initialized with all parts and all products when the Main Form is opened.
      */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



/*
        Product test = new Product(26, "car", 1.02, 5, 1, 4);
        InHouse test1 = new InHouse(14, "tire2", 5.02, 11, 1, 20, 508);
        test.addAssociatedParts(test1);
       Inventory.addPart(new InHouse(12, "tire", 5.02, 11, 1, 20, 508));
       Inventory.addPart(test1);
       Inventory.addPart(new OutSourced(13, "wheel", 5.02, 11, 1, 20, "tire place"));
       Inventory.addProduct(new Product(25, "Cleaner", 1.02, 5, 1, 4));
       Inventory.addProduct(new Product(3, "product", 1.02, 5, 1, 4));
       Inventory.addProduct(test);
*/


        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}

