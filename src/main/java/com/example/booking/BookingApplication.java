package com.example.booking;

import com.example.booking.controllers.AdministratorUIController;
import com.example.booking.controllers.StartController;
import com.example.booking.controllers.UserUIController;
import com.example.booking.repositories.ApplicationUserRepository;
import com.example.booking.repositories.BookingRepository;
import com.example.booking.repositories.CottageRepository;
import com.example.booking.utils.ViewAndController;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class BookingApplication extends Application {

    private static String[] args;

    private ConfigurableApplicationContext applicationContext;

    @Value("${ui.title:Сервис бронирования}")
    private String windowTitle;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CottageRepository cottageRepository;

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(getClass(), args);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewAndController<Parent, StartController> startViewAndController = signIn();

        if (startViewAndController.getController().getApplicationUser() != null) {
            ViewAndController viewAndController = getUserInterface(startViewAndController, stage);

            stage.setTitle(windowTitle);
            stage.setScene(new Scene(viewAndController.getParentView()));
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        }
    }

    private ViewAndController getUserInterface(ViewAndController<Parent, StartController> startViewAndController, Stage stage) throws IOException, Exception {
        ViewAndController<Parent, InitializingBean> viewAndController;
        if (startViewAndController.getController().getApplicationUser().isAdministrator()) {
            viewAndController = ViewAndController.load("administrator_ui");
            AdministratorUIController administratorController = (AdministratorUIController) viewAndController.getController();
            administratorController.setBookingRepository(bookingRepository);
            administratorController.setCottageRepository(cottageRepository);
            administratorController.setStage(stage);
        } else {
            viewAndController = ViewAndController.load("user_ui");
            UserUIController userController = (UserUIController) viewAndController.getController();
            userController.setBookingRepository(bookingRepository);
            userController.setCottageRepository(cottageRepository);
            userController.setStage(stage);
        }

        viewAndController.getController().afterPropertiesSet();

        return viewAndController;
    }

    private ViewAndController<Parent, StartController> signIn() throws IOException {
        Stage startStage = new Stage();
        ViewAndController<Parent, StartController> startViewAndController = ViewAndController.load("start");
        startViewAndController.getController().setApplicationUserRepository(applicationUserRepository);
        startViewAndController.getController().setStage(startStage);
        startStage.setTitle(windowTitle);
        startStage.setScene(new Scene(startViewAndController.getParentView()));
        startStage.setResizable(false);
        startStage.centerOnScreen();
        startStage.showAndWait();

        return startViewAndController;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.stop();
    }

    public static void main(String[] args) {
        BookingApplication.args = args;
        Application.launch(BookingApplication.class, args);
    }
}
