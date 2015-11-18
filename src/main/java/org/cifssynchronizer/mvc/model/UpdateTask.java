package org.cifssynchronizer.mvc.model;

import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.cifssynchronizer.core.SynchronizerCore;

import java.util.Collections;
import java.util.List;

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
public class UpdateTask extends Task<List<SmbFile>> {
    private SynchronizerCore synchronizerCore;

    public UpdateTask() {
        super();

        synchronizerCore = null;
    }

    @Override
    protected List<SmbFile> call() throws Exception {
        return synchronizerCore != null ? synchronizerCore.getFilesList() : Collections.emptyList();
    }

    public void setSynchronizerCore(SynchronizerCore synchronizerCore) {
        this.synchronizerCore = synchronizerCore;
    }
}