package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

    /** This is the Modify Part Form class.*/
    public class ModifyPartForm implements Initializable {

        public RadioButton partsInHouse;
        public RadioButton partsOutSourced;

        public Label partsMachineId;

        public TextField partIdField;
        public TextField partNameField;
        public TextField partInvField;
        public TextField partCostField;
        public TextField partMaxField;
        public TextField partMachineIdCompanyField;
        public TextField partMinField;

        public Part partToModify;

    /** This method sets the text of the last field to "Company Name".
     @param actionEvent Outsourced radio button selected.
     */
    public void onPartsOutSourced(ActionEvent actionEvent) {
        partsMachineId.setText("Company Name");
    }

    /** This method sets the text of the last field to "Machine ID".
     @param actionEvent In-House radio button selected.
     */
    public void onPartsInHouse(ActionEvent actionEvent) {
        partsMachineId.setText("Machine ID");
    }

    /** This method cancels the modifying of a part.
     This method will cancel the 'Modify Part' action and redirect user back to the Main Form screen.
     @param actionEvent The cancel button is clicked.
     */
    public void onModifyPartsCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200,720);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** This method will save a part that has been modified.
     This method will take a selected part and allow the user to modify it. The inventory stock field must be between min and max, and max must be greater than min. It will not allow the user to save if any fields are empty or have invalid input.
     It will save the part as 'Outsourced' or 'In-House' based on which radio button is selected.
     @param actionEvent The save button is clicked.
     */
    public void onModifyPartsSave(ActionEvent actionEvent) throws IOException {
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
            }
            else if (partInv < partMin || partInv > partMax){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inventory must be between Min and Max.");
                alert.showAndWait();
            }
            else if(partName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Dialog");
                alert.setContentText("Name field is empty.");
                alert.showAndWait();
            }
            else {
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
                    InHouse selectedPart = new InHouse(partId, partName, partCost, partInv, partMin, partMax, partMachineId);
                    selectedPart.setMachineId(partMachineId);
                    model.Inventory.updatePart(selectedPart);
                }
                else if (partsOutSourced.isSelected()) {
                    String companyName = partMachineIdCompanyField.getText();
                    OutSourced selectedPart = new OutSourced(partId, partName, partCost, partInv, partMin, partMax, companyName);
                    selectedPart.setCompanyName(companyName);
                    model.Inventory.updatePart(selectedPart);
                }
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1200, 720);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }
    }

    /** This method takes the user selected part from the 'parts' table on the 'Main Form' screen and parses the values into the correct fields.
     The selected part from the 'parts' table will have its values parsed into the corresponding fields. It will select the radio button based on which model class the part is an instance of. It will set the text of the last field based on the selected radio button.
     @param partToModify The selected part.
      */
    public void setPartToModify(Part partToModify){
        this.partToModify = partToModify;

        partIdField.setText(Integer.toString(partToModify.getId()));
        partNameField.setText(partToModify.getName());
        partInvField.setText(Integer.toString(partToModify.getStock()));
        partCostField.setText(Double.toString(partToModify.getPrice()));
        partMaxField.setText(Integer.toString(partToModify.getMax()));
        partMinField.setText(Integer.toString(partToModify.getMin()));


        if(partToModify instanceof InHouse){
            partsInHouse.setSelected(true);
            partsMachineId.setText("Machine ID");
            partMachineIdCompanyField.setText(Integer.toString(((InHouse) partToModify).getMachineId()));
        }
        else if(partToModify instanceof OutSourced){
            partsOutSourced.setSelected(true);
            partsMachineId.setText("Company Name");
            partMachineIdCompanyField.setText((((OutSourced) partToModify).getCompanyName()));
        }
    }

    /** This is the initialize method for the Modify Part Form class. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
