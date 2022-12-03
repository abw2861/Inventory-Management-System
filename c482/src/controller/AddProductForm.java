package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/** This is the Add Product Form class. */
public class AddProductForm implements Initializable {

    public TextField productIdField;
    public TextField productNameField;
    public TextField productInvField;
    public TextField productPriceField;
    public TextField productMaxField;
    public TextField productMinField;

    public TableView addProductTable;
    public TableView addProductBottomTable;

    public TextField searchAddProductField;

    private static int uniqueProductIds = 0;

    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partsInvCol;
    public TableColumn partsCostCol;
    public TableColumn addBottomPartIdCol;
    public TableColumn addBottomPartNameCol;
    public TableColumn addBottomPartInvCol;
    public TableColumn addBottomPartCostCol;

    public static ObservableList<Part> bottomTable = FXCollections.observableArrayList();

    /** This method assigns the ID field with an auto-generated ID.
     @return Returns a unique product ID.
     */
    public static int getUniqueProductIds() {
        uniqueProductIds = uniqueProductIds + 1;
        return uniqueProductIds;
    }

    /** This method will cancel adding a new product.
     This method will redirect the user back to the 'Main Form' screen.
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

    /** This method will save a new product to the inventory system.
     This method will save a new product, the inventory stock field must be between min and max, and max must be greater than min. It will not allow the user to save if any fields are empty or have incorrect input. The new product will be saved with any associated parts added to the 'associated parts' table.
     @param actionEvent The save button is clicked.
     */
    public void onProductsSave(ActionEvent actionEvent) throws IOException {
        try {
            Integer.parseInt(productInvField.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Double.parseDouble(productPriceField.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Price/Cost field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(productMaxField.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Max field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(productMinField.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Min field must contain a number value.");
            alert.showAndWait();
        }

            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();
            int productInv = Integer.parseInt(productInvField.getText());
            double productCost = Double.parseDouble(productPriceField.getText());
            int productMax = Integer.parseInt(productMaxField.getText());
            int productMin = Integer.parseInt(productMinField.getText());

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
                Product product = new Product(productId, productName, productCost, productInv, productMin, productMax);
                Inventory.addProduct(product);

                for (Part part : bottomTable) {
                    product.addAssociatedParts(part);
                }

                bottomTable.clear();

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1200, 720);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }

    }

    /** This method will search the 'parts' table on the 'Add Product' screen.
     This method will allow the user to search and filter the 'parts' table by part ID or part name. If the user is searching by part ID, the corresponding part will be highlighted. If the user is searching by part name, the corresponding part(s) will be filtered.
     @param actionEvent The search field is entered.
      */
    public void searchAddProductTable(ActionEvent actionEvent) {
        ObservableList<Part> searchedParts = FXCollections.observableArrayList();

        if (searchAddProductField.getText().matches("-?\\d+")){
            Part part = Inventory.lookupPart(Integer.parseInt(searchAddProductField.getText()));
            if (part != null){
                searchedParts.add(part);
                addProductTable.setItems(Inventory.getAllParts());
                addProductTable.getSelectionModel().select(part);
            }
            else {
                addProductTable.setItems(searchedParts);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("No part found matching ID entered.");
                alert.showAndWait();
            }
        }
        else {
            searchedParts = Inventory.lookupPart(searchAddProductField.getText());
            addProductTable.setItems(searchedParts);
            if (searchedParts.isEmpty()) {
                addProductTable.setItems(searchedParts);

                Alert secondAlert = new Alert(Alert.AlertType.ERROR);
                secondAlert.setTitle("Error Dialog");
                secondAlert.setContentText("No part found matching name entered.");
                secondAlert.showAndWait();
            }
        }
    }

    /** This method will add a selected part from the 'parts' table to the 'associated parts' table.
     @param actionEvent The add button is clicked.
     */
    public void toAddPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) addProductTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Part is selected.");
            alert.showAndWait();
        }
        else {
            bottomTable.add(selectedPart);
        }
        addProductBottomTable.setItems(bottomTable);
    }

    /** This method will remove an associated part from the 'associated parts' table.
     @param actionEvent The remove button is clicked.
      */
    public void toRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) addProductBottomTable.getSelectionModel().getSelectedItem();

        if (addProductBottomTable.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("There are no parts to remove.");
            alert.showAndWait();
        }
        else if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Part is selected.");
            alert.showAndWait();
        }
        else {
            bottomTable.remove(selectedPart);
        }
        addProductBottomTable.setItems(bottomTable);
    }

    /** This is the initialize method for the Add Product Form.
     The ID field will be initialized with an auto-generated ID number and the top part's table will contain the parts inventory.
      */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addBottomPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addBottomPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addBottomPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addBottomPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdField.setText(Integer.toString(getUniqueProductIds()));

        addProductTable.setItems(Inventory.getAllParts());
    }
}
