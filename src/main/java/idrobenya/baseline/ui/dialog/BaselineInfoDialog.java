package idrobenya.baseline.ui.dialog;

import idrobenya.baseline.model.Baseline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.stage.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ilya Drabenia
 * @since 01.09.12
 */
public class BaselineInfoDialog implements Initializable {
    private Stage dialogStage;
    private boolean dialogSuccess = false;
    private final Effect ERROR_EFFECT = new InnerShadow(BlurType.THREE_PASS_BOX, Color.RED, 9d, 0.4d, 0d, 0d);

    @FXML
    private TextField baselineNameField;

    @FXML
    private TextField wireframesDirectoryPathField;

    public BaselineInfoDialog(Window owner) {
        Validate.notNull(owner);

        try {
            dialogStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BaselineInfoDialog.fxml"));
            fxmlLoader.setController(this);

            dialogStage.setScene(new Scene((Parent) fxmlLoader.load()));
            dialogStage.setTitle("Baseline Info");
            dialogStage.initOwner(owner);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            baselineNameField.requestFocus();
            baselineNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean newValue) {
                    if (Boolean.FALSE.equals(newValue)) {
                        BaselineInfoDialog.this.validateNameField();
                    }
                }
            });

            wireframesDirectoryPathField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean o, Boolean newValue) {
                    if (Boolean.FALSE.equals(newValue)) {
                        BaselineInfoDialog.this.validatePathField();
                    }
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isDialogSuccess() {
        return dialogSuccess;
    }

    public void show() {
        dialogStage.showAndWait();
    }

    public Baseline getBaselineInfo() {
        Baseline baseline = new Baseline();
        baseline.setName(baselineNameField.getText());
        baseline.setPathToDirectory(wireframesDirectoryPathField.getText());
        return baseline;
    }

    @FXML
    public void onCancelButtonClicked(ActionEvent event) {
        dialogStage.hide();
        dialogSuccess = false;
    }

    @FXML
    public void onOkButtonClicked(ActionEvent event) {
        if (validateNameField() & validatePathField()) {
            dialogStage.hide();
            dialogSuccess = true;
        }
    }

    @FXML
    public void onSelectDirectoryButtonClicked(ActionEvent event) throws Exception {
        File wireframesDirectory = DirectoryChooserBuilder.create()
                .title("Open wireframes directory")
                .build().showDialog(this.dialogStage);

        if (wireframesDirectory != null) {
            wireframesDirectoryPathField.setText(wireframesDirectory.getCanonicalPath());
        }
    }

    private boolean validateNameField() {
        if (StringUtils.isBlank(baselineNameField.getText())) {
            Tooltip errorTooltip = new Tooltip("Field is required");

            baselineNameField.setTooltip(errorTooltip);
            baselineNameField.setEffect(ERROR_EFFECT);

            return false;
        } else {
            baselineNameField.setTooltip(null);
            baselineNameField.setEffect(null);

            return true;
        }
    }

    private boolean validatePathField() {
        if (StringUtils.isBlank(wireframesDirectoryPathField.getText())) {
            Tooltip errorTooltip = new Tooltip("Path is required");

            wireframesDirectoryPathField.setTooltip(errorTooltip);
            wireframesDirectoryPathField.setEffect(ERROR_EFFECT);

            return false;
        } else {
            wireframesDirectoryPathField.setTooltip(null);
            wireframesDirectoryPathField.setEffect(null);

            return true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
