package org.cifssynchronizer.mvc.view.configuration;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.cifssynchronizer.dao.models.Configuration;
import org.cifssynchronizer.dao.models.Credential;

import java.time.LocalDate;

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
public class AddEditConfigurationView extends GridPane {
    TextField smbPathText;
    TextField downloadPathText;
    DatePicker lastSynchronizationText;
    ComboBox<Credential> credentialComboBox;
    Button resetButton;
    Button dirChooser;

    public AddEditConfigurationView(Configuration configuration) {
        super();
        Label pathLabel = new Label("Smb Path:");
        smbPathText = new TextField();
        smbPathText.setPrefWidth(250);
        smbPathText.setText(configuration != null ? configuration.getSmbPath() : "");
        Platform.runLater(smbPathText::requestFocus);

        Label lastSynchLabel = new Label("Last Synchronization:");
        lastSynchronizationText = new DatePicker(LocalDate.ofEpochDay(configuration == null ? 0l : configuration.getLastSynchronization()));
        lastSynchronizationText.setEditable(false);
        lastSynchronizationText.setPrefWidth(250);

        resetButton = new Button("Reset");

        Label credentialLabel = new Label("Credential:");
        credentialComboBox = new ComboBox<>();
        credentialComboBox.setPrefWidth(250);

        Label downloadPathLabel = new Label("Download smbPath:");
        downloadPathText = new TextField();
        downloadPathText.setEditable(false);

        dirChooser = new Button("...");

        setHgap(10);
        setVgap(10);

        setPadding(new Insets(8, 8, 8, 8));

        add(pathLabel, 0, 0);
        add(smbPathText, 1, 0);

        add(lastSynchLabel, 0, 1);
        add(lastSynchronizationText, 1, 1);
        add(resetButton, 2, 1);

        add(downloadPathLabel, 0, 2);
        add(downloadPathText, 1, 2);
        add(dirChooser, 2, 2);

        add(credentialLabel, 0, 3);
        add(credentialComboBox, 1, 3);

        GridPane.setHgrow(smbPathText, Priority.ALWAYS);
        GridPane.setHgrow(downloadPathText, Priority.ALWAYS);
        GridPane.setHgrow(credentialComboBox, Priority.ALWAYS);
        GridPane.setHgrow(lastSynchronizationText, Priority.ALWAYS);
    }
}
