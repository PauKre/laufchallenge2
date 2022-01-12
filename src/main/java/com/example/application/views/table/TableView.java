package com.example.application.views.table;

import com.example.application.data.FirebaseDataProvider;
import com.example.application.data.db.Firebase;
import com.example.application.data.db.RunDB;
import com.example.application.data.entity.Run;
import com.example.application.data.service.SampleRunService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import com.vaadin.ui.DateField;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

@PageTitle("Table")
@Route(value = "table/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class TableView extends Div implements BeforeEnterObserver {

    private final String RUN_ID = "samplePersonID";
    private final String RUN_EDIT_ROUTE_TEMPLATE = "table/%d/edit";

    private Grid<Run> grid = new Grid<>(Run.class);

    private TextField name;
    private NumberField distance;
    private NumberField time;
    private DatePicker date;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");

    private BeanValidationBinder<Run> binder;

    private Run run;

    private SampleRunService sampleRunService;
    private FirebaseDataProvider<Run> dataProvider;

    public TableView(@Autowired SampleRunService sampleRunService) {
        try {
            Firebase.setup();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to setup Firebase connection. See the log for details");
        }
        dataProvider = new FirebaseDataProvider<>(Run.class, RunDB.getRunsDb());
        grid.setDataProvider(dataProvider);
        this.sampleRunService = sampleRunService;
        addClassNames("table-view", "flex", "flex-col", "h-full");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        grid.removeColumn(grid.getColumnByKey("key"));
        grid.removeColumn(grid.getColumnByKey("id"));
        grid.setColumnOrder(
                grid.getColumnByKey("name"),
                grid.getColumnByKey("distance"),
                grid.getColumnByKey("time"),
                grid.getColumnByKey("date")
        );
        grid.getColumnByKey("distance").setHeader("Distanz");
        grid.getColumnByKey("time").setHeader("Zeit");
        grid.getColumnByKey("date")
                .setHeader("Datum");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(RUN_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TableView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Run.class);

        binder.forField(date)
                .withConverter(new LocalDateLongConverter())
                .bind(Run::getDate, Run::setDate);
        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            date.setValue(LocalDate.now());
        });

        delete.addClickListener(e -> {
            clearForm();
            refreshGrid();
            date.setValue(LocalDate.now());
        });

        save.addClickListener(e -> {
            try {
                if (this.run == null) {
                    this.run = new Run();
                }
                binder.writeBean(this.run);

                sampleRunService.update(this.run);
                if (run.getKey() == null) {
                    RunDB.add(run);
                } else {
                    RunDB.update(run.getKey(), run);
                }


                clearForm();
                refreshGrid();
                date.setValue(LocalDate.now());
                Notification.show("SamplePerson details stored.");
                UI.getCurrent().navigate(TableView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the samplePerson details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(RUN_ID);
        if (samplePersonId.isPresent()) {
            Optional<Run> samplePersonFromBackend = sampleRunService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %d", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TableView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Name");
        distance = new NumberField("Distanz");
        time = new NumberField("Zeit");
        date = new DatePicker("Datum");
        date.setValue(LocalDate.now());
        date.setLocale(Locale.GERMANY);
        Component[] fields = new Component[]{name, distance, time, date};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Run value) {
        this.run = value;
        binder.readBean(this.run);

    }
}
