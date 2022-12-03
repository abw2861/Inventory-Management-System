package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This is the Add Part Form Class. */
public class AddPartForm implements Initializable {

    public Label partsMachineId;

    public RadioButton partsOutSourced;
    public RadioButton partsInHouse;

    public TextField partIdField;
    public TextField partNameField;
    public TextField partInvField;
    public TextField partCostField;
    public TextField partMaxField;
    public TextField partMinField;
    public TextField partMachineIdCompanyField;

    private static int uniquePartIds = 0;

    /** This method assigns the ID field with an auto-generated ID.
     @return Returns a unique Part ID.
     */
    public static int getUniquePartIds() {
        uniquePartIds = uniquePartIds + 1;
        return uniquePartIds;
    }

    /** This method sets the text of the last field to "Machine ID".
     @param actionEvent In-House radio button selected.
     */
    public void onInHouse(ActionEvent actionEvent) {
        partsMachineId.setText("Machine ID");
    }

    /** This method sets the text of the last field to "Company Name".
     @param actionEvent Outsourced radio button selected.
     */
    public void onOutSourced(ActionEvent actionEvent) {
        partsMachineId.setText("Company Name");
    }

    /** This method saves a new part to the inventory system.
     This method will save a new part, the inventory stock field must be between min and max, and max must be greater than min. It will not allow the user to save if any fields are empty or have incorrect input.
     It will save the part as 'Outsourced' or 'In-House' based on which radio button is selected.
     @param actionEvent The save button is clicked.
     */
    public void onPartsSave(ActionEvent actionEvent) throws IOException {
        try {
            Integer.parseInt(partInvField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Double.parseDouble(partCostField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Price/Cost field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(partMaxField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Max field must contain a number value.");
            alert.showAndWait();
        }
        try {
            Integer.parseInt(partMinField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Min field must contain a number value.");
            alert.showAndWait();
        }

        int partId = Integer.parseInt(partIdField.getText());
        String partName = partNameField.getText();
        int partInv = Integer.parseInt(partInvField.getText());
        double partCost = Double.parseDouble(partCostField.getText());
        int partMax = Integer.parseInt(partMaxField.getText());
        int partMin = Integer.parseInt(partMinField.getText());


        if (partMax <= partMin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Min value must be less than Max value.");
            alert.showAndWait();
        } else if (partInv < partMin || partInv > partMax) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory must be between Min and Max.");
            alert.showAndWait();
        } else if (partName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Dialog");
            alert.setContentText("Name field is empty.");
            alert.showAndWait();
        } else {
            if (partsInHouse.isSelected()) {
                try {
                    Integer.parseInt(partMachineIdCompanyField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Machine ID field must contain a number value.");
                    alert.showAndWait();
                }
                int partMachineId = Integer.parseInt(partMachineIdCompanyField.getText());
                InHouse newInHousePart = new InHouse(partId, partName, partCost, partInv, partMin, partMax, partMachineId);
                newInHousePart.setMachineId(partMachineId);
                Inventory.addPart(newInHousePart);
            } else if (partsOutSourced.isSelected()) {
                String partMachineCompanyId = partMachineIdCompanyField.getText();
                OutSourced newOutsourcedPart = new OutSourced(partId, partName, partCost, partInv, partMin, partMax, partMachineCompanyId);
                newOutsourcedPart.setCompanyName(partMachineCompanyId);
                Inventory.addPart(newOutsourcedPart);
            }

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 720);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }


    /** This method will cancel saving a new part.
     This method will redirect user to the Main Form page when the cancel button is clicked.
     @param actionEvent The cancel button is clicked.
     */
    public void onPartsCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200,720);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** This method initializes the Add Part form.
     The Add Part form is initialized and sets the ID field to an auto-generated ID number.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdField.setText(Integer.toString(getUniquePartIds()));
    }
}
