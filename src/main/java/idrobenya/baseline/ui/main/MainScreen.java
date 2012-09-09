/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idrobenya.baseline.ui.main;

import idrobenya.baseline.Main;
import idrobenya.baseline.model.Baseline;
import idrobenya.baseline.ui.StatusSupport;
import idrobenya.baseline.ui.dialog.BaselineInfoDialog;
import idrobenya.baseline.ui.main.task.AddNewBaselineTask;
import idrobenya.baseline.ui.main.task.RefreshBaselinesTask;
import idrobenya.baseline.ui.main.task.RemoveBaselineTask;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import name.antonsmirnov.javafx.dialog.Dialog;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author OS
 */
public class MainScreen implements Initializable {

    @FXML
    private ListView<Baseline> baselinesListView;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar statusProgressBar;

    private StatusSupport statusBar;

    public MainScreen(Stage owner) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            fxmlLoader.setController(this);

            owner.setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void onAddBaselineClicked(ActionEvent event) {
        BaselineInfoDialog dialog = new BaselineInfoDialog(Main.getMainWindow());
        dialog.show();
        final Baseline resultBaseline = dialog.getBaselineInfo();

        if (dialog.isDialogSuccess()) {
            new Thread(new AddNewBaselineTask(this, statusBar, resultBaseline)).start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusBar = new StatusSupport(statusLabel, statusProgressBar);
        refreshBaselinesList();
    }

    public void refreshBaselinesList() {
        new Thread(new RefreshBaselinesTask(baselinesListView, statusBar)).start();
    }

    @FXML
    public void onBaselineRemoveClicked() {
        final Baseline selectedBaseline = baselinesListView.getSelectionModel().getSelectedItem();

        if (selectedBaseline == null) {
            return;
        }

        Dialog.buildConfirmation("Remove Baseline", "Are you really want to remove baseline?",
                Main.getMainWindow()).addYesButton(new EventHandler() {

            @Override
            public void handle(Event event) {
                new Thread(new RemoveBaselineTask(MainScreen.this, statusBar, selectedBaseline)).start();
            }
        })
                .addNoButton(null).build().showAndWait();
    }

}
