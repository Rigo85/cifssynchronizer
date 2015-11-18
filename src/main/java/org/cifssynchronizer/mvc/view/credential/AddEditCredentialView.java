package org.cifssynchronizer.mvc.view.credential;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.cifssynchronizer.core.Keys;
import org.cifssynchronizer.core.Security;
import org.cifssynchronizer.core.Utils;
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
public class AddEditCredentialView extends GridPane {
    TextField domainText;
    TextField usernameText;
    PasswordField passwordText;

    public AddEditCredentialView(Credential credential) {
        super();
        Label domainLabel = new Label("Domain:");
        domainText = new TextField();
        domainText.setPrefWidth(250);
        domainText.setText(credential != null ? credential.getDomainName() : "");
        Platform.runLater(domainText::requestFocus);

        Label usernameLabel = new Label("Username:");
        usernameText = new TextField();
        usernameText.setPrefWidth(250);
        usernameText.setText(credential != null ? credential.getUserName() : "");

        Label passwordLabel = new Label("Password:");
        passwordText = new PasswordField();
        passwordText.setPrefWidth(250);
        Keys keys = Utils.loadKeys();
        passwordText.setText(credential != null && keys != null ? Security.decrypt(keys.getKey1(), keys.getKey2(), credential.getPassword()) : "");

        setHgap(10);
        setVgap(10);

        setPadding(new Insets(8, 8, 8, 8));

        add(domainLabel, 0, 0);
        add(domainText, 1, 0);
        add(usernameLabel, 0, 1);
        add(usernameText, 1, 1);
        add(passwordLabel, 0, 2);
        add(passwordText, 1, 2);
        GridPane.setHgrow(domainText, Priority.ALWAYS);
        GridPane.setHgrow(usernameText, Priority.ALWAYS);
        GridPane.setHgrow(passwordText, Priority.ALWAYS);
    }
}
