package org.cifssynchronizer.mvc.view.configuration;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.cifssynchronizer.dao.controllers.DAOSynchronizer;
import org.cifssynchronizer.dao.models.Configuration;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
public class AddEditConfigurationPresenter {
    private final Configuration configuration;
    private final AddEditConfigurationView configurationView;
    private final SimpleBooleanProperty change;
    private final DAOSynchronizer daoSynchronizer;
    private final Dialog dialog;

    public AddEditConfigurationPresenter(Configuration configuration, AddEditConfigurationView configurationView) {
        this.configuration = configuration;
        this.configurationView = configurationView;
        change = new SimpleBooleanProperty(false);
        daoSynchronizer = DAOSynchronizer.getInstance();
        dialog = new Dialog();
        attachEvents();
    }

    private void attachEvents() {
        configurationView.lastSynchronizationText.setConverter(
                new StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate object) {
                        return object == null || object.equals(LocalDate.ofEpochDay(0l)) ?
                                "Never" : object.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        return LocalDate.parse(string);
                    }
                }
        );

        configurationView.lastSynchronizationText.addEventFilter(DatePicker.ON_SHOWN, e -> {
            if (configurationView.lastSynchronizationText.getValue().equals(LocalDate.ofEpochDay(0l))) {
                configurationView.lastSynchronizationText.setValue(LocalDate.now());
                change.set(true);
            }
        });

        configurationView.resetButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure to reset the last synchronization date?", ButtonType.OK, ButtonType.CANCEL);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setResizable(true);
            alert.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(x -> {
                configurationView.lastSynchronizationText.setValue(LocalDate.ofEpochDay(0l));
                change.set(true);
            });
        });

        configurationView.credentialComboBox.getItems().addAll(daoSynchronizer.getCredentialJpaController().findCredentialEntities());
        if (configuration != null && configuration.getCredential() != null)
            configurationView.credentialComboBox.setValue(configuration.getCredential());

        configurationView.dirChooser.setOnAction(e -> {
            DirectoryChooser dirDialog = new DirectoryChooser();

            dirDialog.setTitle("Select Downloads Directory");
            dirDialog.setInitialDirectory(new File(System.getProperty("user.home")));

            File dir = dirDialog.showDialog(dialog.getOwner());
            if (dir != null) {
                configurationView.downloadPathText.setText(dir.getAbsolutePath());
            } else {
                configurationView.downloadPathText.setText(getDownloadsUrl());
            }
        });

        configurationView.downloadPathText.setText(getDownloadsUrl());
    }

    public void show() {
        dialog.setTitle(configuration == null ? "Add Configuration" : "Edit Configuration");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        ButtonType addEditBT = new ButtonType(configuration != null ? "Edit" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addEditBT, ButtonType.CANCEL);

        dialog.setOnCloseRequest(e -> {
            if (change.getValue()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "The \"last synchronization\" has change, do you want to save it?", ButtonType.OK, ButtonType.CANCEL);
                Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                alert.getDialogPane().setPrefWidth(500);
                alert.setResizable(true);
                alert.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(x -> {
                            try {
                                if (configuration != null) {
                                    configuration.setCredential(configurationView.credentialComboBox.getValue());
                                    configuration.setDownloadPath(configurationView.downloadPathText.getText());
                                    configuration.setLastSynchronization(configurationView.lastSynchronizationText.getValue().toEpochDay());
                                    configuration.setSmbPath(configurationView.smbPathText.getText());

                                    daoSynchronizer.getConfigurationJpaController().edit(configuration);
                                    change.set(false);
                                    //todo alert the main view!!!!
                                    //  synchronizerView.updateConfigurationCBox();
                                }
                            } catch (Exception e1) {
                                Alert alert1 = new Alert(Alert.AlertType.ERROR, e1.getMessage(), ButtonType.OK);
                                Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
                                stage1.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                                alert1.setResizable(true);
                                alert1.showAndWait();
                            }
                        }
                );
            }
        });

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        final Button addEdit = (Button) dialog.getDialogPane().lookupButton(addEditBT);
        addEdit.addEventFilter(ActionEvent.ACTION, this::AddEditOnAction);
    }

    private void AddEditOnAction(ActionEvent event) {
        try {
            if (configuration != null) {
                configuration.setCredential(configurationView.credentialComboBox.getValue());
                configuration.setDownloadPath(configurationView.downloadPathText.getText());
                configuration.setLastSynchronization(configurationView.lastSynchronizationText.getValue().toEpochDay());
                if (!configuration.getSmbPath().equals(configurationView.smbPathText.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "The Smb Path has change, do you want to reset the Last Synchronization?", ButtonType.OK, ButtonType.CANCEL);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                    alert.getDialogPane().setPrefWidth(500);
                    alert.setResizable(true);
                    final Configuration finalConfiguration = configuration;
                    alert.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(x -> finalConfiguration.setLastSynchronization(0l));
                }
                configuration.setSmbPath(configurationView.smbPathText.getText());
                daoSynchronizer.getConfigurationJpaController().edit(configuration);
            } else {
                daoSynchronizer.getConfigurationJpaController()
                        .create(new Configuration(configurationView.smbPathText.getText(), new Date(0l).getTime(),
                                configurationView.downloadPathText.getText(), configurationView.credentialComboBox.getValue()));
            }
            change.setValue(false);
            //todo alert the main view!!!!
            //synchronizerView.updateConfigurationCBox();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setResizable(true);
            alert.showAndWait();
            event.consume();
        }
    }

    private String getDownloadsUrl() {
        return Paths.get(System.getProperty("user.home"),
                Locale.getDefault().getLanguage().startsWith("en") ? "Downloads" : "Descargas").toString();
    }
}
