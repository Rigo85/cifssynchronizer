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

package org.cifssynchronizer.dao.controllers;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DAOSynchronizer {
    private CredentialJpaController credentialJpaController;
    private ConfigurationJpaController configurationJpaController;
    private static DAOSynchronizer daoSynchronizer = null;

    private DAOSynchronizer() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SynchronizerDBPU");
        credentialJpaController = new CredentialJpaController(emf);
        configurationJpaController = new ConfigurationJpaController(emf);
    }

    public static DAOSynchronizer getInstance(){
        if(daoSynchronizer == null) daoSynchronizer = new DAOSynchronizer();
        return daoSynchronizer;
    }

    public CredentialJpaController getCredentialJpaController() {
        return credentialJpaController;
    }

    public ConfigurationJpaController getConfigurationJpaController() {
        return configurationJpaController;
    }
}
