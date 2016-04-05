package org.cifssynchronizer.mvc.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.cifssynchronizer.core.CIFSSynchronizerCore;

import java.util.concurrent.ConcurrentLinkedDeque;

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
public class UpdateService extends Service<ConcurrentLinkedDeque<SmbFile>> {
    public final SimpleBooleanProperty canContinue;
    private CIFSSynchronizerCore cifsSynchronizerCore;

    public UpdateService() {
        super();

        canContinue = new SimpleBooleanProperty(false);
        cifsSynchronizerCore = null;
    }

    @Override
    protected Task<ConcurrentLinkedDeque<SmbFile>> createTask() {
        return new Task<ConcurrentLinkedDeque<SmbFile>>() {
            @Override
            protected ConcurrentLinkedDeque<SmbFile> call() throws Exception {
                if (cifsSynchronizerCore != null) {
                    return cifsSynchronizerCore.produceFiles();
                }
                return null;
            }
        };
    }

    public void setCifsSynchronizerCore(CIFSSynchronizerCore cifsSynchronizerCore) {
        this.cifsSynchronizerCore = cifsSynchronizerCore;
    }
}
