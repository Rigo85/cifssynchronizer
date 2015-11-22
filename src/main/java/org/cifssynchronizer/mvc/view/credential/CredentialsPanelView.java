package org.cifssynchronizer.mvc.view.credential;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.cifssynchronizer.dao.models.Credential;

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
public class CredentialsPanelView extends BorderPane {
    Button addButton;
    Button editButton;
    TableView<Credential> credentialTableView;
    Button removeButton;

    public CredentialsPanelView() {
        super();

        createCredendialsTableView();
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

    private void createCredendialsTableView() {
        credentialTableView = new TableView<>();

        TableColumn<Credential, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setPrefWidth(250);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<Credential, String> domainCol = new TableColumn<>("Domain");
        domainCol.setPrefWidth(250);
        domainCol.setCellValueFactory(new PropertyValueFactory<>("domainName"));

        credentialTableView.getColumns().add(usernameCol);
        credentialTableView.getColumns().add(domainCol);
        credentialTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        BorderPane.setMargin(credentialTableView, new Insets(8, 8, 8, 8));

        setCenter(credentialTableView);
    }
}
