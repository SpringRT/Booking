package com.example.booking.controllers;

import com.example.booking.entities.Booking;
import com.example.booking.entities.Cottage;
import com.example.booking.repositories.BookingRepository;
import com.example.booking.repositories.CottageRepository;
import com.example.booking.utils.ViewAndController;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.InitializingBean;
import static org.springframework.util.Assert.notNull;

public class UserUIController implements Initializable, InitializingBean {

    private final ObservableList<Cottage> cottages = FXCollections.observableArrayList();

    @FXML
    private Button bookButton;

    @FXML
    private TableView<Cottage> cottagesTable;

    @FXML
    private TableColumn<Cottage, Integer> cottageNumberColumn;

    @FXML
    private TableColumn<Cottage, Integer> cottageBedsColumn;

    @FXML
    private TableColumn<Cottage, String> cottageParkingColumn;

    @FXML
    private TableColumn<Cottage, String> cottagePlaygroundColumn;

    @FXML
    private TableColumn<Cottage, String> cottageArbourColumn;

    @FXML
    private TableColumn<Cottage, String> cottagePetsColumn;

    @FXML
    private TableColumn<Cottage, Float> cottageRentColumn;

    private BookingRepository bookingRepository;

    private CottageRepository cottageRepository;

    private Stage stage;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        cottagesTable.setItems(cottages);

        cottageNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        cottageBedsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBeds()).asObject());
        cottageParkingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isParkingLot() ? "Есть" : "-"));
        cottagePlaygroundColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isPlayground() ? "Есть" : "-"));
        cottageArbourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isArbour() ? "Есть" : "-"));
        cottagePetsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isPets() ? "Можно" : "-"));
        cottageRentColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getRent()).asObject());

        cottagesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    bookButton.setDisable(newValue == null || cottages.isEmpty());
                }
        );
    }

    public void book() throws IOException {
        Stage modalStage = new Stage();
        modalStage.initOwner(stage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setResizable(false);
        modalStage.centerOnScreen();
        modalStage.setTitle(resourceBundle.getString("title.book"));

        Booking booking = new Booking();
        booking.setCottage(cottagesTable.getSelectionModel().getSelectedItem());

        ViewAndController<Parent, UserBookingFormController> viewAndController = ViewAndController.load("user_booking_form");
        viewAndController.getController().setBooking(booking);
        viewAndController.getController().setBookingRepository(bookingRepository);
        viewAndController.getController().setStage(modalStage);

        modalStage.setScene(new Scene(viewAndController.getParentView()));
        modalStage.showAndWait();
    }

    public void setCottageRepository(CottageRepository cottageRepository) {
        this.cottageRepository = cottageRepository;
    }

    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(cottageRepository, "cottageRepository cannot be null");
        notNull(bookingRepository, "bookingRepository cannot be null");
        notNull(stage, "stage cannot be null");
        cottages.addAll((Collection<Cottage>) cottageRepository.findAll());
    }
}
