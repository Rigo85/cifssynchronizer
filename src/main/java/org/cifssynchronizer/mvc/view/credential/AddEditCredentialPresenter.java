package org.cifssynchronizer.mvc.view.credential;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.cifssynchronizer.core.Keys;
import org.cifssynchronizer.core.Security;
import org.cifssynchronizer.core.Utils;
import org.cifssynchronizer.dao.controllers.DAOSynchronizer;
import org.cifssynchronizer.dao.models.Credential;

import javax.persistence.RollbackException;

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
public class AddEditCredentialPresenter {
    private final Credential credential;
    private final AddEditCredentialView credentialView;

    public AddEditCredentialPresenter(Credential credential, AddEditCredentialView credentialView) {
        this.credential = credential;
        this.credentialView = credentialView;
    }

    private void AddEditOnAction(ActionEvent event) {
        DAOSynchronizer daoSynchronizer = DAOSynchronizer.getInstance();
        Keys keys = Utils.loadKeys();
        try {
            if (credential != null) {
                credential.setDomainName(credentialView.domainText.getText());
                credential.setUserName(credentialView.usernameText.getText());
                credential.setPassword(Security.encrypt(keys.getKey1(), keys.getKey2(), credentialView.passwordText.getText()));
                daoSynchronizer.getCredentialJpaController().edit(credential);
            } else {
                daoSynchronizer.getCredentialJpaController()
                        .create(new Credential(credentialView.domainText.getText(), credentialView.usernameText.getText(),
                                Security.encrypt(keys.getKey1(), keys.getKey2(), credentialView.passwordText.getText())));
            }
        } catch (Exception e) {
            String msg = e instanceof RollbackException ? "The combination of Domain and Username need to be unique!" : e.getMessage();

            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.getDialogPane().setPrefWidth(450);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setResizable(true);
            alert.showAndWait();
            event.consume();
        }
    }

    public void show() {
        Dialog dialog = new Dialog();
        dialog.getDialogPane().setContent(this.credentialView);

        dialog.setTitle(credential == null ? "Add Credential" : "Edit Credential");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        ButtonType addEditBT = new ButtonType(credential != null ? "Edit" : "Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addEditBT, ButtonType.CANCEL);

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        final Button addEdit = (Button) dialog.getDialogPane().lookupButton(addEditBT);
        addEdit.addEventFilter(ActionEvent.ACTION, this::AddEditOnAction);

        dialog.showAndWait();
    }
}
