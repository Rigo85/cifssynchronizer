package org.cifssynchronizer.mvc.view;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.cifssynchronizer.core.SynchronizerCore;
import org.cifssynchronizer.dao.controllers.DAOSynchronizer;
import org.cifssynchronizer.dao.models.Configuration;
import org.cifssynchronizer.mvc.model.DownloadTask;
import org.cifssynchronizer.mvc.model.UpdateTask;
import org.cifssynchronizer.mvc.view.configuration.ConfigurationPanelPresenter;
import org.cifssynchronizer.mvc.view.configuration.ConfigurationPanelView;
import org.cifssynchronizer.mvc.view.credential.CredentialsPanelPresenter;
import org.cifssynchronizer.mvc.view.credential.CredentialsPanelView;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
public class CIFSSynchronizerPresenter {
    final CIFSSynchronizerView synchronizerView;
    DAOSynchronizer daoSynchronizer;
    UpdateTask updateTask;

    public CIFSSynchronizerPresenter(CIFSSynchronizerView synchronizerView) {
        this.synchronizerView = synchronizerView;

        updateTask = new UpdateTask();

        try {
            daoSynchronizer = DAOSynchronizer.getInstance();
        } catch (Exception e) {
            if (e.getMessage().contains("Failed to start database")) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        " Other software is using the database,\ncheck if you have another synchronizer open!", ButtonType.OK);
                final Stage window = (Stage) alert.getDialogPane().getScene().getWindow();
                window.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                alert.showAndWait();
                Platform.exit();
            } else {
                throw e;
            }
        }

        attachEvents();
    }

    private void attachEvents() {
        synchronizerView.configurationComboBox.getItems()
                .addAll(daoSynchronizer.getConfigurationJpaController().findConfigurationEntities());

        synchronizerView.updateButton.setOnAction(e -> updateAction());
        synchronizerView.updateButton.disableProperty().bind(updateTask.stateProperty().isEqualTo(Worker.State.RUNNING));

        synchronizerView.settings.setOnAction(e -> settingsAction());

        synchronizerView.exit.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setTitle("Confirmation Dialog");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) Platform.exit();
        });

        synchronizerView.about.setOnAction(e -> {
            //todo improve about dialog.
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setHeaderText("Rigoberto L. Salgado Reyes <rlsalgado2006@gmail.com>");
            alert.setTitle("About Dialog");
            alert.showAndWait();
        });
    }

    private void settingsAction() {
        TabPane SettingsPanel = new TabPane();
        Tab configurationTab = new Tab("Configurations");

        ImageView icon = new ImageView(new Image(getClass().getClassLoader().getResource("images/configurations.png").toExternalForm()));
        icon.setSmooth(true);
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        configurationTab.setGraphic(icon);
        configurationTab.setClosable(false);
        ConfigurationPanelView configurationPanelView = new ConfigurationPanelView();
        ConfigurationPanelPresenter configurationPanelPresenter = new ConfigurationPanelPresenter(configurationPanelView);
        configurationTab.setContent(configurationPanelView);

        Tab credentialsTab = new Tab("Credentials");
        icon = new ImageView(new Image(getClass().getClassLoader().getResource("images/credentials.png").toExternalForm()));
        icon.setSmooth(true);
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        credentialsTab.setGraphic(icon);
        credentialsTab.setClosable(false);
        CredentialsPanelView credentialsPanelView = new CredentialsPanelView();
        CredentialsPanelPresenter credentialsPanelPresenter = new CredentialsPanelPresenter(credentialsPanelView);
        credentialsTab.setContent(credentialsPanelView);

        SettingsPanel.getTabs().addAll(configurationTab, credentialsTab);

        Dialog dialog = new Dialog();
        dialog.setTitle("Settings");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.getDialogPane().setContent(SettingsPanel);
        dialog.getDialogPane().setPrefWidth(700);

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        dialog.showAndWait();
    }

    private void updateAction() {
        final Configuration configuration = synchronizerView.configurationComboBox.getValue();

        if (configuration != null) {
            SynchronizerCore synchronizerCore = new SynchronizerCore(configuration);
            updateTask.setSynchronizerCore(synchronizerCore);
            updateTask.setOnSucceeded(e -> {
                final List<SmbFile> filesList = (List<SmbFile>) e.getSource().getValue();
                onSucceedUpdate(filesList, configuration, synchronizerCore);
            });

            Thread backgroundThread = new Thread(updateTask);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        } else {
            synchronizerView.downloadsTableView.getItems().clear();
        }
    }

    private void onSucceedUpdate(List<SmbFile> filesList, Configuration configuration, SynchronizerCore synchronizerCore) {
        final List<String> errorList = synchronizerCore.getErrorList();
        if (!errorList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(String.format("The following errors was found when updating from %s", configuration));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            Label label = new Label("The errors was:");

            TextArea textArea = new TextArea(errorList.stream().collect(Collectors.joining(System.getProperty("line.separator"))));
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
            synchronizerView.downloadsTableView.getItems().clear();
        } else {
            AtomicInteger counter = new AtomicInteger(1);
            final List<DownloadTask> collect = filesList.stream().map(sf -> {
                try {
                    return new DownloadTask(synchronizerCore.getNtlmPasswordAuthentication(),
                            counter.getAndIncrement(), sf.getName(), sf.getCanonicalPath(), sf.length(),
                            new Date(sf.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (SmbException e) {
                    return null;
                }
            }).filter(d -> d != null).collect(Collectors.toList());

            synchronizerView.downloadsTableView.getItems().clear();
            synchronizerView.downloadsTableView.getItems().addAll(collect);
        }
    }
}
