package org.cifssynchronizer.mvc.view.credential;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.cifssynchronizer.dao.controllers.DAOSynchronizer;
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
public class CredentialsPanelPresenter {
    private final CredentialsPanelView credentialsPanelView;
    DAOSynchronizer daoSynchronizer;

    public CredentialsPanelPresenter(CredentialsPanelView credentialsPanelView) {
        this.credentialsPanelView = credentialsPanelView;
        daoSynchronizer = DAOSynchronizer.getInstance();
        if (credentialsPanelView != null) updateTableView();

        attachEvents();
    }

    private void attachEvents() {
        credentialsPanelView.addButton.setOnAction(e -> addEditAction(null));

        credentialsPanelView.editButton.setOnAction(e -> {
            final Credential selectedItem = credentialsPanelView.credentialTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                addEditAction(selectedItem);
            } else {
                String msg = "Please select a Credential to edit!";
                if (credentialsPanelView.credentialTableView.getItems().isEmpty()) {
                    msg = "You need to add first some Credentials";
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

                alert.showAndWait();
            }
        });

        credentialsPanelView.removeButton.setOnAction(e -> {
            final Credential selectedItem = credentialsPanelView.credentialTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    daoSynchronizer.getCredentialJpaController().destroy(selectedItem.getId());
                    updateTableView();
                } catch (Exception e1) {
                    String msg = e1.getMessage().contains("violation of foreign key") ?
                            "The selected credential it's being used by some configuration" : e1.getMessage();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                    alert.setResizable(true);
                    alert.showAndWait();
                }
            } else {
                String msg = "Please select a Credential to remove!";
                if (credentialsPanelView.credentialTableView.getItems().isEmpty()) {
                    msg = "You need to add first some Credentials";
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

                alert.showAndWait();
            }
        });

        credentialsPanelView.credentialTableView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                final Credential selectedItem = credentialsPanelView.credentialTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    addEditAction(selectedItem);
                }
            }
        });
    }

    private void addEditAction(Credential credential) {
        AddEditCredentialView addEditCredentialView = new AddEditCredentialView(credential);
        AddEditCredentialPresenter addEditCredentialPresenter = new AddEditCredentialPresenter(credential, addEditCredentialView);
        addEditCredentialPresenter.show();
        updateTableView();
    }

    void updateTableView() {
        credentialsPanelView.credentialTableView.getItems().clear();
        credentialsPanelView.credentialTableView.getItems().addAll(
                daoSynchronizer.getCredentialJpaController().findCredentialEntities());
    }
}

