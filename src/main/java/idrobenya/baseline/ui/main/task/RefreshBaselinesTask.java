package idrobenya.baseline.ui.main.task;

import idrobenya.baseline.model.Baseline;
import idrobenya.baseline.ui.StatusSupport;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public class RefreshBaselinesTask extends AbstractBaselineTask<Void> {
    private List<Baseline> baselines;
    private ListView<Baseline> baselinesListView;

    public RefreshBaselinesTask(ListView<Baseline> baselinesListView, StatusSupport statusBar) {
        super(statusBar, "Baselines Refreshing...");
        this.baselinesListView = baselinesListView;
    }

    @Override
    protected Void call() throws Exception {
        baselines = Baseline.list();
        return null;
    }

    @Override
    protected void succeeded() {
        baselinesListView.setItems(FXCollections.<Baseline>observableList(baselines));

        super.succeeded();
    }

}
