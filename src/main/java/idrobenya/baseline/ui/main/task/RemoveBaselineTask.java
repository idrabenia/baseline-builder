package idrobenya.baseline.ui.main.task;

import idrobenya.baseline.model.Baseline;
import idrobenya.baseline.ui.StatusSupport;
import idrobenya.baseline.ui.main.MainScreen;
import javafx.concurrent.Task;
import name.antonsmirnov.javafx.dialog.Dialog;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public class RemoveBaselineTask extends AbstractBaselineTask<Void> {
    private Baseline baseline;
    private MainScreen mainScreen;

    public RemoveBaselineTask(MainScreen mainScreen, StatusSupport statusBar, Baseline baseline) {
        super(statusBar, "Baseline Deletion...");
        this.baseline = baseline;
        this.mainScreen = mainScreen;
    }

    @Override
    protected Void call() throws Exception {
        baseline.delete();
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        mainScreen.refreshBaselinesList();
    }

}
