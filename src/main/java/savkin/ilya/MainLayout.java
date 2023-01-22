package savkin.ilya;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import savkin.ilya.view.account.AccountView;
import savkin.ilya.view.client.ClientsView;

import javax.annotation.PostConstruct;

public class MainLayout extends AppLayout {

    @PostConstruct
    public void init() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Тестовое задание");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Клиенты", ClientsView.class);
        RouterLink accountLink = new RouterLink("Аккаунты", AccountView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink, accountLink
        ));
    }
}
