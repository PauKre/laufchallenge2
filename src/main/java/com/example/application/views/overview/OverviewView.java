package com.example.application.views.overview;


import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Overview")
@Route(value = "overview", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class OverviewView extends Main {

    public OverviewView() {
        addClassName("overview-view");

        Board board = new Board();
        board.addRow(createHighlight("Aktueller Kilometerstand", "0", 0.00), createHighlight("Anzahl Läufe", "0", 1.0),
                createHighlight("Letzter Lauf", "Heinz Dieter", 7.9));
        board.addRow(createViewEvents());
        board.addRow(createRunnerDetails(), createRunnerPortions());
        add(board);
    }

    private Component createHighlight(String title, String value, Double percentage) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "±";
        } else if (percentage > 0) {
            prefix = "+";
            theme += " success";
        } else if (percentage < 0) {
            icon = VaadinIcon.ARROW_DOWN;
            theme += " error";
        }

        H2 h2 = new H2(title);
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        Span span = new Span(value);
        span.addClassNames("font-semibold", "text-3xl");

        Icon i = icon.create();
        i.addClassNames("box-border", "p-xs");

        Span badge = new Span(i, new Span(prefix + percentage.toString()));
        badge.getElement().getThemeList().add(theme);

        VerticalLayout layout = new VerticalLayout(h2, span, badge);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private Component createViewEvents() {
        // Header
        Select year = new Select();
        year.setItems("insgesamt", "Felix", "Timon", "Stefan", "Axel");
        year.setValue("insgesamt");
        year.setWidth("100px");

        HorizontalLayout header = createHeader("Gelaufene Strecke", "Kumulative");
        header.add(year);

        // Chart
        Chart chart = new Chart(ChartType.AREA);
        Configuration conf = chart.getConfiguration();

        XAxis xAxis = new XAxis();
        xAxis.setCategories("9.1.", "10.1.", "11.1.", "12.1.", "13.1.");
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Kilometer");

        PlotOptionsArea plotOptions = new PlotOptionsArea();
        plotOptions.setPointPlacement(PointPlacement.ON);
        conf.addPlotOptions(plotOptions);

        conf.addSeries(new ListSeries("Strecke", 189, 191, 191, 196, 201, 203, 209, 212, 229, 242, 244, 247));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName("p-l");
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }

    private Component createRunnerDetails() {
        // Header
        HorizontalLayout header = createHeader("Interne Rangliste", "Läufe nach Läufer");

        // Grid
        Grid<RunnerDetails> grid = new Grid();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setAllRowsVisible(true);

        grid.addColumn(new ComponentRenderer<>(runnerDetails -> {
            Span status = new Span();
            String statusText = getStatusDisplayName(runnerDetails);
            status.getElement().setAttribute("aria-label", "Status: " + statusText);
            status.getElement().setAttribute("title", "Status: " + statusText);
            status.getElement().getThemeList().add(getStatusTheme(runnerDetails));
            return status;
        })).setHeader("").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(RunnerDetails::getRunnerName).setHeader("Läufer").setFlexGrow(1);
        grid.addColumn(RunnerDetails::getDistance).setHeader("Gesamtrecke").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        grid.addColumn(RunnerDetails::getNumberOfRuns).setHeader("Anzahl Läufe").setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.END);

        grid.setItems(new RunnerDetails( "Felix", 20, 3),
                new RunnerDetails("Timon", 31, 5),
                new RunnerDetails("Stefan", 15, 1));

        // Add it all together
        VerticalLayout runnerDetails = new VerticalLayout(header, grid);
        runnerDetails.addClassName("p-l");
        runnerDetails.setPadding(false);
        runnerDetails.setSpacing(false);
        runnerDetails.getElement().getThemeList().add("spacing-l");
        return runnerDetails;
    }

    private Component createRunnerPortions() {
        HorizontalLayout header = createHeader("Anteil pro Läufer", "");

        // Chart
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Felix", 20));
        series.add(new DataSeriesItem("Timon", 31));
        series.add(new DataSeriesItem("Stefan", 15));
        conf.addSeries(series);

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
    }

    private HorizontalLayout createHeader(String title, String subtitle) {
        H2 h2 = new H2(title);
        h2.addClassNames("text-xl", "m-0");

        Span span = new Span(subtitle);
        span.addClassNames("text-secondary", "text-xs");

        VerticalLayout column = new VerticalLayout(h2, span);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }

    private String getStatusDisplayName(RunnerDetails runnerDetails) {
        return "Test";
    }

    private String getStatusTheme(RunnerDetails serviceHealth) {
        return "badge primary small";

        //        Status status = serviceHealth.getStatus();
//        String theme = "badge primary small";
//        if (status == Status.EXCELLENT) {
//            theme += " success";
//        } else if (status == Status.FAILING) {
//            theme += " error";
//        }
//        return theme;
    }

}
