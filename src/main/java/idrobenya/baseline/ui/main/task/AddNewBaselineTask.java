package idrobenya.baseline.ui.main.task;

import idrobenya.baseline.Main;
import idrobenya.baseline.model.Baseline;
import idrobenya.baseline.ui.StatusSupport;
import idrobenya.baseline.ui.main.MainScreen;
import javafx.concurrent.Task;
import name.antonsmirnov.javafx.dialog.Dialog;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public class AddNewBaselineTask extends AbstractBaselineTask<Void> {
    private MainScreen mainScreen;
    private Baseline newBaseline;

    public AddNewBaselineTask(MainScreen mainScreen, StatusSupport statusBar, Baseline newBaseline) {
        super(statusBar, "Baseline Deploying...");
        this.mainScreen = mainScreen;
        this.newBaseline = newBaseline;
    }

    @Override
    protected Void call() throws Exception {
        newBaseline.makeBaseline();
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        mainScreen.refreshBaselinesList();
    }

}
