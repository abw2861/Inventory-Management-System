package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the Modify Product Class. */
public class ModifyProductForm implements Initializable {

    public Product productToModify;

    public TextField modifyProductSearchField;

    public TableView modifyProductAssPartsTable;
    public TableColumn associatedPartsCostCol;
    public TableColumn associatedPartsInvCol;
    public TableColumn associatedPartsNameCol;
    public TableColumn associatedPartsIdCol;

    public TableView modifyProductPartsTable;
    public TableColumn modifyPartCostCol;
    public TableColumn modifyPartInvCol;
    public TableColumn modifyPartNameCol;
    public TableColumn modifyPartIdCol;

    public TextField modifyProductMinField;
    public TextField modifyProductMaxField;
    public TextField modifyProductPriceField;
    public TextField modifyProductInvField;
    public TextField modifyProductNameField;
    public TextField modifyProductIdField;

    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public ObservableList<Part> possibleAssociatedParts = FXCollections.observableArrayList();



    /** This method sets the product to be modified.
     This method will take the product selected from the 'products' table on the 'Main Form' screen. Its values will be assigned to the corresponding fields on the 'Modify Product' screen.
     @param productToModify The product to be modified.
      */
    public void setProductToModify(Product productToModify) {
        this.productToModify = productToModify;

        modifyProductIdField.setText(Integer.toString(productToModify.getId()));
        modifyProductNameField.setText(productToModify.getName());
        modifyProductInvField.setText(Integer.toString(productToModify.getStock()));
        modifyProductPriceField.setText(Double.toString(productToModify.getPrice()));
        modifyProductMaxField.setText(Integer.toString(productToModify.getMax()));
        modifyProductMinField.setText(Integer.toString(productToModify.getMin()));
        associatedParts.addAll(productToModify.getAllAssociatedParts());
    }

    /** This method will cancel modifying a product.
     This method will redirect user back to the 'Main Form' screen without making any changes to the selected product.
     @param actionEvent The cancel button is clicked.
     */
    public void onProductCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200,720);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** This method will search the 'parts' table on the 'Modify Product' screen.
     This method will allow the user to search and filter the 'parts' table by part ID or part name. If the user is searching by part ID, the corresponding part will be highlighted. If the user is searching by part name, the corresponding part(s) will be filtered.
     @param actionEvent The search part field is entered.
     */
    public void searchModifyProductTable(ActionEvent actionEvent) {
        ObservableList<Part> searchedParts = FXCollections.observableArrayList();
        if (modifyProductSearchField.getText().matches("-?\\d+")){
            Part part = Inventory.lookupPart(Integer.parseInt(modifyProductSearchField.getText()));
            if (part != null) {
                searchedParts.add(part);
                modifyProductPartsTable.setItems(Inventory.getAllParts());
                modifyProductPartsTable.getSelectionModel().select(part);
            }
            else {
                    modifyProductPartsTable.setItems(searchedParts);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("No part found matching ID entered.");
                    alert.showAndWait();
                }
        }
        else {
            searchedParts = Inventory.lookupPart(modifyProductSearchField.getText());
            modifyProductPartsTable.setItems(searchedParts);
            if (searchedParts.isEmpty()) {
                modifyProductPartsTable.setItems(searchedParts);

                Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                secondAlert.setTitle("Error Dialog");
                secondAlert.setContentText("No part found matching name entered.");
                secondAlert.showAndWait();
            }
        }
    }

    /** This method will add a part from the 'parts' table to the 'associated parts' table on the 'Modify Product' screen.
     @param actionEvent The add button is clicked.
      */
    public void toAddAssociatedParts(ActionEvent actionEvent) {
        Part selectedItem = (Part) modifyProductPartsTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Part is selected.");
            alert.showAndWait();
        }
        else if (possibleAssociatedParts.size() == 0){
            possibleAssociatedParts.addAll(associatedParts);
            possibleAssociatedParts.add(selectedItem);
        }
        else {
            possibleAssociatedParts.add(selectedItem);
        }
        modifyProductAssPartsTable.setItems(possibleAssociatedParts);
    }

    /** This method will remove an associated part from the 'associated parts' table.
     @param actionEvent The remove associated part button is clicked.

     RUNTIME_ERROR A logical error existed in this method, where the confirmation dialog box popped up even if the associated part's table was empty or if no associated part was selected. I corrected this by adding two logical checks. First, to check if the table is empty and let the user
     know there are no associated parts to delete. Second, to check if a part is selected and letting the user know that no part is selected. Now, the confirmation dialog box will only prompt the user if appropriate.
     */
    public void toRemoveAssociatedParts(ActionEvent actionEvent) {
        if (modifyProductAssPartsTable.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("There are no associated parts to delete.");
            alert.showAndWait();
        }
        else if (modifyProductAssPartsTable.getSelectionModel().getSelectedItem() == null ){
            Alert secondAlert = new Alert(Alert.AlertType.ERROR);
            secondAlert.setTitle("Error Dialog");
            secondAlert.setContentText("No Part is selected.");
            secondAlert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove this associated part?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {

                Part selectedAssociatedPart = (Part) modifyProductAssPartsTable.getSelectionModel().getSelectedItem();

                if (selectedAssociatedPart == null) {
                    Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                    secondAlert.setTitle("Error Dialog");
                    secondAlert.setContentText("No Part is selected.");
                    secondAlert.showAndWait();
                } else if (modifyProductAssPartsTable.getItems() == associatedParts) {
                    productToModify.deleteAssociatedPart(selectedAssociatedPart);
                    associatedParts.remove(selectedAssociatedPart);
                } else {
                    productToModify.deleteAssociatedPart(selectedAssociatedPart);
                    possibleAssociatedParts.remove(selectedAssociatedPart);
                }
            }
        }
    }

    /** This method will save a modified product to the product inventory.
     An existing product will be updated with any new input values, the inventory stock field must be between min and max, and max must be greater than min. It will not allow the user to save if any fields are empty or have incorrect input.
     @param actionEvent The save button is clicked on the Modify Product screen.
     */
    public void toSaveModifiedProduct(ActionEvent actionEvent) throws IOException {
        try {
            Integer.parseInt(modifyProductInvField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Double.parseDouble(modifyProductPriceField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Price/Cost field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(modifyProductMaxField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Max field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(modifyProductMinField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Min field must contain a number value.");
            alert.showAndWait();
        }

            int productId = Integer.parseInt(modifyProductIdField.getText());
            String productName = modifyProductNameField.getText();
            int productInv = Integer.parseInt(modifyProductInvField.getText());
            double productCost = Double.parseDouble(modifyProductPriceField.getText());
            int productMax = Integer.parseInt(modifyProductMaxField.getText());
            int productMin = Integer.parseInt(modifyProductMinField.getText());

            if (productMax <= productMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Min value must be less than Max value.");
                alert.showAndWait();
            }
            else if (productInv < productMin || productInv > productMax){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inventory must be between Min and Max.");
                alert.showAndWait();
            }
            else if (productName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Dialog");
                alert.setContentText("Name field is empty.");
                alert.showAndWait();
            }
            else {
                Product selectedProduct = new Product(productId, productName, productCost, productInv, productMin, productMax);
                Inventory.updateProduct(selectedProduct);


                if (modifyProductAssPartsTable.getItems() == possibleAssociatedParts) {
                    for (Part part : possibleAssociatedParts) {
                        selectedProduct.addAssociatedParts(part);
                    }
                } else {
                    for (Part part : associatedParts) {
                        selectedProduct.addAssociatedParts(part);
                    }
                }

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1200, 720);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }

    }

    /** This is the initialize method for the Modify Product Form screen.
     The 'parts' table will contain all inventory parts. The 'associated parts' table will contain any associated parts for the product selected.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductPartsTable.setItems(Inventory.getAllParts());
        modifyProductAssPartsTable.setItems(associatedParts);
    }
}
