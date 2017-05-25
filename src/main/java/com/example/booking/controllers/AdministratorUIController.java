package com.example.booking.controllers;

import com.example.booking.entities.Booking;
import com.example.booking.entities.Cottage;
import com.example.booking.repositories.BookingRepository;
import com.example.booking.repositories.CottageRepository;
import com.example.booking.utils.ViewAndController;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.InitializingBean;
import static org.springframework.util.Assert.notNull;

public class AdministratorUIController implements Initializable, InitializingBean {

    private final ObservableList<Cottage> cottages = FXCollections.observableArrayList();

    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    private CottageRepository cottageRepository;

    private BookingRepository bookingRepository;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> bookingClientColumn;

    @FXML
    private TableColumn<Booking, Long> bookingPhoneColumn;

    @FXML
    private TableColumn<Booking, String> bookingCottageColumn;

    @FXML
    private TableColumn<Booking, String> bookingArrivalDateColumn;

    @FXML
    private TableColumn<Booking, String> bookingDepartureDateColumn;

    @FXML
    private Button editBookingButton;

    @FXML
    private Button deleteBookingButton;

    @FXML
    private TableView<Cottage> cottagesTable;

    @FXML
    private TableColumn<Cottage, Integer> cottageIdColumn;

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

    @FXML
    private Button editCottageButton;

    @FXML
    private Button deleteCottageButton;

    private Stage stage;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        bookingIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        bookingClientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        bookingPhoneColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getCustomerPhone()).asObject());
        bookingCottageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCottage().toString()));
        bookingArrivalDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateFormat.format(cellData.getValue().getArrivalDate())));
        bookingDepartureDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateFormat.format(cellData.getValue().getDepartureDate())));

        cottageIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        cottageBedsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBeds()).asObject());
        cottageParkingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isParkingLot() ? resourceBundle.getString("option.yes") : "-"));
        cottagePlaygroundColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isPlayground() ? resourceBundle.getString("option.yes") : "-"));
        cottageArbourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isArbour() ? resourceBundle.getString("option.yes") : "-"));
        cottagePetsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isPets() ? resourceBundle.getString("option.allowed") : "-"));
        cottageRentColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getRent()).asObject());

        bookingsTable.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editBookingButton.setDisable(newValue == null || bookings.isEmpty());
                    deleteBookingButton.setDisable(newValue == null || bookings.isEmpty());
                });

        cottagesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editCottageButton.setDisable(newValue == null || cottages.isEmpty());
                    deleteCottageButton.setDisable(newValue == null || cottages.isEmpty());
                }
        );

        bookingsTable.setItems(bookings);
        cottagesTable.setItems(cottages);
    }

    public void createBooking() throws IOException, Exception {
        Booking booking = showBookingForm(new Booking(), resourceBundle.getString("title.booking_create"));

        bookings.setAll((Collection<Booking>) bookingRepository.findAll());

        bookingsTable.getSelectionModel()
                .select(booking);
    }

    public void editBooking() throws IOException, Exception {
        Booking booking = bookingsTable.getSelectionModel()
                .getSelectedItem();

        showBookingForm(booking, resourceBundle.getString("title.booking_edit") + booking.getId());

        bookings.setAll((Collection<Booking>) bookingRepository.findAll());

        bookingsTable.getSelectionModel()
                .select(booking);
    }

    public Booking showBookingForm(Booking booking, String title) throws IOException, Exception {
        Stage modalStage = new Stage();
        modalStage.initOwner(stage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setResizable(false);
        modalStage.centerOnScreen();
        modalStage.setTitle(title);

        ViewAndController<Parent, AdministratorBookingFormController> viewAndController = ViewAndController.load("administrator_booking_form");

        AdministratorBookingFormController bookingFormController = viewAndController.getController();
        bookingFormController.setBookingRepository(bookingRepository);
        bookingFormController.setStage(modalStage);
        bookingFormController.setBooking(booking);
        bookingFormController.setCottages(cottages);
        bookingFormController.afterPropertiesSet();

        modalStage.setScene(new Scene(viewAndController.getParentView()));
        modalStage.showAndWait();

        return bookingFormController.getBooking();
    }

    public void deleteBooking() {
        Booking booking = bookingsTable.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle(resourceBundle.getString("title.booking_delete") + booking.getId());
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("message.booking_delete_confirmation"));

        ButtonType buttonType = alert.showAndWait().get();
        if (buttonType == ButtonType.OK) {
            bookings.remove(booking);
            bookingRepository.delete(booking);
        }
    }

    public void createCottage() throws IOException {
        Cottage cottage = showCottageForm(new Cottage(), "title.cottage_create");

        cottages.setAll((Collection<Cottage>) cottageRepository.findAll());

        cottagesTable.getSelectionModel()
                .select(cottage);
    }

    public void editCottage() throws IOException {
        Cottage cottage = cottagesTable.getSelectionModel()
                .getSelectedItem();

        showCottageForm(cottage, resourceBundle.getString("title.cottage_edit") + cottage.getId());

        cottages.setAll((Collection<Cottage>) cottageRepository.findAll());

        cottagesTable.getSelectionModel()
                .select(cottage);
    }

    public Cottage showCottageForm(Cottage cottage, String title) throws IOException {
        Stage modalStage = new Stage();
        modalStage.initOwner(stage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setResizable(false);
        modalStage.centerOnScreen();
        modalStage.setTitle(title);

        ViewAndController<Parent, AdministratorCottageFormController> viewAndController = ViewAndController.load("administrator_cottage_form");

        AdministratorCottageFormController cottageFormController = viewAndController.getController();
        cottageFormController.setCottageRepository(cottageRepository);
        cottageFormController.setStage(modalStage);
        cottageFormController.setCottage(cottage);

        modalStage.setScene(new Scene(viewAndController.getParentView()));
        modalStage.showAndWait();

        return cottageFormController.getCottage();
    }

    public void deleteCottage() {
        Cottage cottage = cottagesTable.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle(resourceBundle.getString("title.cottage_delete") + cottage.getId());
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("message.cottage_delete_confirmation"));

        ButtonType buttonType = alert.showAndWait().get();
        if (buttonType == ButtonType.OK) {
            cottages.remove(cottage);
            cottageRepository.delete(cottage);
            bookings.setAll((Collection<Booking>) bookingRepository.findAll());
        }
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
        bookings.addAll((Collection<Booking>) bookingRepository.findAll());
        cottages.addAll((Collection<Cottage>) cottageRepository.findAll());
    }
}
