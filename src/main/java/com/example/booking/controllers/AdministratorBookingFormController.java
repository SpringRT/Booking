package com.example.booking.controllers;

import com.example.booking.entities.Booking;
import com.example.booking.entities.Cottage;
import com.example.booking.repositories.BookingRepository;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.InitializingBean;

public class AdministratorBookingFormController implements Initializable, InitializingBean {

    private Stage stage;

    private Booking booking;

    private BookingRepository bookingRepository;

    private ObservableList<Cottage> cottages;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerPhone;

    @FXML
    private ChoiceBox<Cottage> cottage;

    @FXML
    private DatePicker arrivalDate;

    @FXML
    private DatePicker departureDate;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        customerPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cottage.setConverter(new StringConverter<Cottage>() {
            @Override
            public String toString(Cottage object) {
                return object.toString();
            }

            @Override
            public Cottage fromString(String string) {
                int id = Integer.parseInt(string.split("\\s")[0].replace("#", ""));
                return cottages.parallelStream()
                        .filter(cottage -> cottage.getId() == id)
                        .findAny()
                        .orElse(null);
            }
        });
    }

    public void save() {
        List<String> errors = validate();
        if (errors.isEmpty()) {
            bookingRepository.save(booking);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setCottages(ObservableList<Cottage> cottages) {
        this.cottages = cottages;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cottage.setItems(cottages);

        customerName.setText(booking.getCustomerName());
        customerPhone.setText(booking.getCustomerPhone() > 0 ? Long.toString(booking.getCustomerPhone()) : "");
        cottage.setValue(booking.getCottage());
        arrivalDate.setValue(booking.getArrivalDate() != null ? Instant.ofEpochMilli(booking.getArrivalDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null);
        departureDate.setValue(booking.getDepartureDate() != null ? Instant.ofEpochMilli(booking.getDepartureDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null);
    }

    public List<String> validate() {
        final ZoneId timeZone = ZoneId.systemDefault();
        List<String> errors = new ArrayList<>();

        if (org.apache.commons.lang3.StringUtils.isBlank(customerName.getText())) {
            errors.add("error.customer_name.empty");
        }

        try {
            if (Long.valueOf(customerPhone.getText()) == 0) {
                errors.add("error.customer_phone.empty");
            }
        } catch (NumberFormatException exception) {
            errors.add("error.customer_phone.empty");
        }

        if (cottage.getValue() == null) {
            errors.add("error.cottage.empty");
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
            booking.setArrivalDate(Date.from(arrivalDate.getValue().atStartOfDay(timeZone).toInstant()));
            booking.setDepartureDate(Date.from(departureDate.getValue().atStartOfDay(timeZone).toInstant()));
            booking.setCottage(cottage.getValue());
            booking.setCustomerName(customerName.getText());
            booking.setCustomerPhone(Long.valueOf(customerPhone.getText()));
        }

        return errors;
    }
}
