package idrobenya.baseline.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public class StatusSupport {
    private Label statusLabel;
    private ProgressBar progressBar;

    public StatusSupport(Label statusLabel, ProgressBar progressBar) {
        this.statusLabel = statusLabel;
        this.progressBar = progressBar;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void showStatus(String text) {
        statusLabel.setText(text);
        progressBar.setVisible(true);
    }

    public void endShowStatus() {
        statusLabel.setText("");
        progressBar.setVisible(false);
    }

}
