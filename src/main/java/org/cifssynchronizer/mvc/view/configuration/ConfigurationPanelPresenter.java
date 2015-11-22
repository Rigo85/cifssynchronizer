package org.cifssynchronizer.mvc.view.configuration;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.cifssynchronizer.dao.controllers.DAOSynchronizer;
import org.cifssynchronizer.dao.models.Configuration;

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
public class ConfigurationPanelPresenter {
    public final ConfigurationPanelView configurationPanelView;
    private final DAOSynchronizer daoSynchronizer;

    public ConfigurationPanelPresenter(ConfigurationPanelView configurationPanelView) {
        this.configurationPanelView = configurationPanelView;
        daoSynchronizer = DAOSynchronizer.getInstance();
        if (configurationPanelView != null) updateTableView();

        attachEvents();
    }

    private void attachEvents() {
        configurationPanelView.addButton.setOnAction(e -> addEditAction(null));

        configurationPanelView.editButton.setOnAction(e -> {
            final Configuration selectedItem = configurationPanelView.confTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                addEditAction(selectedItem);
            } else {
                String msg = "Please select a Configuration to edit!";
                if (configurationPanelView.confTableView.getItems().isEmpty()) {
                    msg = "You need to add first some Configurations";
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

                alert.showAndWait();
            }
        });

        configurationPanelView.removeButton.setOnAction(e -> {
            final Configuration selectedItem = configurationPanelView.confTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    daoSynchronizer.getConfigurationJpaController().destroy(selectedItem.getId());
                    updateTableView();
                } catch (Exception e1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, e1.getMessage(), ButtonType.OK);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

                    alert.showAndWait();
                }
            } else {
                String msg = "Please select a Configuration to remove!";
                if (configurationPanelView.confTableView.getItems().isEmpty()) {
                    msg = "You need to add first some Configurations";
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

                alert.showAndWait();
            }
        });

        configurationPanelView.confTableView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                final Configuration selectedItem = configurationPanelView.confTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    addEditAction(selectedItem);
                }
            }
        });
    }

    private void updateTableView() {
        configurationPanelView.confTableView.getItems().clear();
        configurationPanelView.confTableView.getItems()
                .addAll(daoSynchronizer.getConfigurationJpaController()
                        .findConfigurationEntities());
    }

    private void addEditAction(Configuration configuration) {
        AddEditConfigurationView addEditConfigurationView = new AddEditConfigurationView(configuration);
        AddEditConfigurationPresenter addEditConfigurationPresenter = new AddEditConfigurationPresenter(configuration, addEditConfigurationView);
        addEditConfigurationPresenter.show();
        updateTableView();
    }
}
