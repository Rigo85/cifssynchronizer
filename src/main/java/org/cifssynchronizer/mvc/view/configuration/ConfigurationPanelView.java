package org.cifssynchronizer.mvc.view.configuration;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.cifssynchronizer.dao.models.Configuration;
import org.cifssynchronizer.dao.models.Credential;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2015 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class ConfigurationPanelView extends BorderPane {

    public TableView<Configuration> confTableView;
    Button addButton;
    Button editButton;
    Button removeButton;

    public ConfigurationPanelView() {
        super();

        createConfTableView();
        createButtons();
    }

    private void createButtons() {
        HBox buttonBox = new HBox(8);

        Label expander = new Label();
        expander.setMaxWidth(Double.MAX_VALUE);

        addButton = new Button("Add");
        addButton.setPrefWidth(100);

        editButton = new Button("Edit");
        editButton.setPrefWidth(100);


        removeButton = new Button("Remove");
        removeButton.setPrefWidth(100);

        HBox.setHgrow(expander, Priority.ALWAYS);

        buttonBox.getChildren().addAll(expander, addButton, editButton, removeButton);
        BorderPane.setMargin(buttonBox, new Insets(8, 8, 8, 8));
        setBottom(buttonBox);
    }

    private void createConfTableView() {
        confTableView = new TableView<>();

        TableColumn<Configuration, String> smbPathCol = new TableColumn<>("Smb Path");
        smbPathCol.setPrefWidth(250);
        smbPathCol.setCellValueFactory(new PropertyValueFactory<>("smbPath"));

        TableColumn<Configuration, Long> synchronizationCol = new TableColumn<>("Last Synchronization");
        synchronizationCol.setPrefWidth(250);
        synchronizationCol.setCellValueFactory(new PropertyValueFactory<>("lastSynchronization"));

        synchronizationCol.setCellFactory(column -> new TableCell<Configuration, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText(null);
                else setText(item == 0l ?
                        "Never" : LocalDate.ofEpochDay(item).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

        TableColumn<Configuration, String> downloadCol = new TableColumn<>("Download Path");
        downloadCol.setPrefWidth(250);
        downloadCol.setCellValueFactory(new PropertyValueFactory<>("downloadPath"));

        TableColumn<Configuration, Credential> credentialCol = new TableColumn<>("Credential");
        credentialCol.setPrefWidth(250);
        credentialCol.setCellValueFactory(new PropertyValueFactory<>("credential"));

        confTableView.getColumns().add(smbPathCol);
        confTableView.getColumns().add(synchronizationCol);
        confTableView.getColumns().add(downloadCol);
        confTableView.getColumns().add(credentialCol);
        confTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        BorderPane.setMargin(confTableView, new Insets(8, 8, 8, 8));

        setCenter(confTableView);
    }
}