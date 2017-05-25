package com.example.booking.controllers;

import com.example.booking.entities.ApplicationUser;
import com.example.booking.repositories.ApplicationUserRepository;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartController implements Initializable {
    
    private ApplicationUserRepository applicationUserRepository;
    
    private ApplicationUser applicationUser;
    
    @FXML
    private TextField signInUsername;
    
    @FXML
    private PasswordField signInPassword;
    
    @FXML
    private TextField signUpUsername;
    
    @FXML
    private PasswordField signUpPassword;
    
    private Stage stage;
    
    private ResourceBundle resourceBundle;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
    
    @FXML
    public void signIn() {
        applicationUser = applicationUserRepository.findOne(signInUsername.getText());
        if (applicationUser == null) {
            showError(resourceBundle.getString("title.error"), null, resourceBundle.getString("error.user_not_registered"), Alert.AlertType.ERROR);
        } else if (!signInPassword.getText().equals(applicationUser.getPassword())) {
            showError(resourceBundle.getString("title.error"), null, resourceBundle.getString("error.wrong_password"), Alert.AlertType.ERROR);
        } else {
            stage.hide();
            stage.close();
        }
    }
    
    @FXML
    public void signUp() {
        if (signUpUsername.getText().trim().isEmpty() || signUpPassword.getText().trim().isEmpty()) {
            showError(resourceBundle.getString("title.error"), null, resourceBundle.getString("error.empty_login_or_password"), Alert.AlertType.ERROR);
        } else {
            if (applicationUserRepository.exists(signUpUsername.getText())) {
                showError(resourceBundle.getString("title.error"), null, resourceBundle.getString("error.user_registered"), Alert.AlertType.ERROR);
            } else {
                applicationUserRepository.save(new ApplicationUser(signUpUsername.getText().trim(), signUpPassword.getText().trim()));
                showError(resourceBundle.getString("title.signup_successful"), null, resourceBundle.getString("message.signup_successful"), Alert.AlertType.INFORMATION);
                signUpUsername.setText("");
                signUpPassword.setText("");
            }
        }
    }
    
    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }
    
    public void setApplicationUserRepository(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }
    
    private void showError(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.show();
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
