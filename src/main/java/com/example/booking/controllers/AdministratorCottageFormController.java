package com.example.booking.controllers;

import com.example.booking.entities.Cottage;
import com.example.booking.repositories.CottageRepository;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdministratorCottageFormController implements Initializable {

    @FXML
    private TextField beds;

    @FXML
    private CheckBox parking;

    @FXML
    private CheckBox playground;

    @FXML
    private CheckBox arbour;

    @FXML
    private CheckBox pets;

    @FXML
    private TextField rent;

    private Cottage cottage;

    private CottageRepository cottageRepository;

    private Stage stage;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        beds.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                beds.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        rent.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d\\.]*")) {
                rent.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
        });
    }

    public void save() {
        List<String> errors = validate();
        if (errors.isEmpty()) {
            cottageRepository.save(cottage);
            cancel();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle(resourceBundle.getString("title.error"));
            alert.setHeaderText(null);
            alert.setContentText(errors.stream().map(resourceBundle::getString).collect(Collectors.joining("\n")));

            alert.show();
        }
    }

    public void cancel() {
        stage.hide();
        stage.close();
    }

    public Cottage getCottage() {
        return cottage;
    }

    public void setCottage(Cottage cottage) {
        this.cottage = cottage;
        this.arbour.setSelected(cottage.isArbour());
        this.beds.setText(String.valueOf(cottage.getBeds()));
        this.parking.setSelected(cottage.isParkingLot());
        this.pets.setSelected(cottage.isPets());
        this.playground.setSelected(cottage.isPlayground());
        this.rent.setText(String.valueOf(cottage.getRent()));
    }

    public void setCottageRepository(CottageRepository cottageRepository) {
        this.cottageRepository = cottageRepository;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        try {
            Float.parseFloat(rent.getText());
        } catch (NumberFormatException exception) {
            errors.add("error.rent.invalid");
        }

        if (errors.isEmpty()) {
            cottage.setBeds(Integer.parseInt(beds.getText()));
            cottage.setParkingLot(parking.isSelected());
            cottage.setPlayground(playground.isSelected());
            cottage.setArbour(arbour.isSelected());
            cottage.setPets(pets.isSelected());
            cottage.setRent(Float.parseFloat(rent.getText()));
        }

        return errors;
    }
}
