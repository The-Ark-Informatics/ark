package au.org.theark.core.web.component.settings;

import au.org.theark.core.model.config.entity.Setting;
import au.org.theark.core.service.IArkSettingService;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Created by george on 1/2/17.
 */
public class ArkSettingsDataViewPanel<T extends Setting> extends Panel {

    public ArkSettingsDataViewPanel(String id) {
        super(id);
    }

    public ArkSettingsDataViewPanel(String id, IDataProvider<Setting> dataProvider, Class<T> teir, FeedbackPanel feedbackPanel) {
        super(id);
        ArkSettingDataView<T> arkSettingDataView = new ArkSettingDataView<T>("settingsView", dataProvider, teir, feedbackPanel);
        PagingNavigator pageNavigator = new PagingNavigator("navigator", arkSettingDataView);
        this.add(pageNavigator);
        this.add(arkSettingDataView);
    }
}
