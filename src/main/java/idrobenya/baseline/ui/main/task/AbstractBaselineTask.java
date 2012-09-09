package idrobenya.baseline.ui.main.task;

import idrobenya.baseline.ui.StatusSupport;
import javafx.concurrent.Task;
import name.antonsmirnov.javafx.dialog.Dialog;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public abstract class AbstractBaselineTask<T> extends Task<T> {
    private StatusSupport statusBar;
    private String processingStatusMessage;

    protected AbstractBaselineTask(StatusSupport statusBar, String processingStatusMessage) {
        this.statusBar = statusBar;
        this.processingStatusMessage = processingStatusMessage;
    }

    public StatusSupport getStatusBar() {
        return statusBar;
    }

    public String getProcessingStatusMessage() {
        return processingStatusMessage;
    }

    @Override
    protected void scheduled() {
        statusBar.showStatus(processingStatusMessage);
    }

    @Override
    protected void succeeded() {
        statusBar.endShowStatus();
    }

    @Override
    protected void cancelled() {
        statusBar.endShowStatus();
    }

    @Override
    protected void failed() {
        statusBar.endShowStatus();

        Dialog.showThrowable("Occurs Error", "Occurs Error. Please contact system administrator.", getException());
    }

}
