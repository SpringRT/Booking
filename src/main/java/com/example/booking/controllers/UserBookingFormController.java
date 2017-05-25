package com.example.booking.controllers;

import com.example.booking.entities.Booking;
import com.example.booking.repositories.BookingRepository;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class UserBookingFormController implements Initializable {

    private Booking booking;

    private BookingRepository bookingRepository;

    private Stage stage;

    private ResourceBundle resourceBundle;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerPhone;

    @FXML
    private DatePicker arrivalDate;

    @FXML
    private DatePicker departureDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        customerPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void save() {
        List<String> errors = validate();
        if (errors.isEmpty()) {
            bookingRepository.save(booking);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle(resourceBundle.getString("title.success"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("message.booking_created"));

            alert.showAndWait();
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

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Метод валидации формы. Валидирует введённые данные и заполняет свойства объекта, если ошибки отсутствуют.
     *
     * @return список ошибок
     */
    private List<String> validate() {
        final ZoneId timeZone = ZoneId.systemDefault();
        List<String> errors = new ArrayList<>();

        if (StringUtils.isBlank(customerName.getText())) {
            errors.add("error.customer_name.empty");
        }

        try {
            if (Long.valueOf(customerPhone.getText()) == 0) {
                errors.add("error.customer_phone.empty");
            }
        } catch (NumberFormatException exception) {
            errors.add("error.customer_phone.empty");
        }

        if (arrivalDate.getValue() == null) {
            errors.add("error.arrival_date.empty");
        } else if (arrivalDate.getValue().atStartOfDay(timeZone).isBefore(LocalDate.now().atStartOfDay().atZone(timeZone))) {
            errors.add("error.arrival_date.before_today");
        }

        if (departureDate.getValue() == null) {
            errors.add("error.departure_date.empty");
        } else if (arrivalDate.getValue().atStartOfDay(timeZone).isBefore(arrivalDate.getValue().atStartOfDay(timeZone))) {
            errors.add("error.departure_date.before_arrival_date");
        } else if (departureDate.getValue().atStartOfDay(timeZone).isBefore(LocalDate.now().atStartOfDay().atZone(timeZone))) {
            errors.add("error.departure_date.before_today");
        }

        if (errors.isEmpty()) {
            booking.setCustomerName(customerName.getText());
            booking.setCustomerPhone(Long.valueOf(customerPhone.getText()));
            booking.setArrivalDate(Date.from(arrivalDate.getValue().atStartOfDay(timeZone).toInstant()));
            booking.setDepartureDate(Date.from(departureDate.getValue().atStartOfDay(timeZone).toInstant()));
        }

        return errors;
    }
}
