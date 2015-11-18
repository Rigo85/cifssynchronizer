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

package org.cifssynchronizer.core;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.cifssynchronizer.dao.models.Configuration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SynchronizerCore {
    private Configuration configuration;
    private List<String> errorList;
    private NtlmPasswordAuthentication ntlmPasswordAuthentication;
    private String smbPath;

    public SynchronizerCore(Configuration configuration) {
        this.configuration = configuration;
        this.errorList = new LinkedList<>();
        Keys keys = Utils.loadKeys();

        String domain = configuration != null && configuration.getCredential() != null ? configuration.getCredential().getDomainName() : "";
        String userName = configuration != null && configuration.getCredential() != null ? configuration.getCredential().getUserName() : "";
        String password = configuration != null && configuration.getCredential() != null ?
                Security.decrypt(keys.getKey1(), keys.getKey2(), configuration.getCredential().getPassword()) : "";

        smbPath = configuration != null && configuration.getCredential() != null ? configuration.getSmbPath() : "";
        smbPath = smbPath + (!smbPath.isEmpty() && !smbPath.endsWith("/") ? "/" : "");
        this.ntlmPasswordAuthentication = new NtlmPasswordAuthentication(domain, userName, password);
    }

    private List<SmbFile> walk(SmbFile[] sfs, LocalDate lastSynchronization) {
        List<SmbFile> list = new LinkedList<>();

        Stream.of(sfs).forEach(sf -> {
            LocalDate input = new Date(sf.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!input.isBefore(lastSynchronization)) {
                try {
                    if (sf.isFile()) {
                        list.add(sf);
                    } else if (sf.isDirectory()) {
                        list.addAll(Stream.of(sf.listFiles())
                                .flatMap(x -> walk(new SmbFile[]{x}, lastSynchronization).stream()).collect(Collectors.toList()));
                    }
                } catch (SmbException e) {
                    this.errorList.add(e.getMessage());
                }
            }
        });

        return list;
    }

    public List<SmbFile> getFilesList() {
        List<SmbFile> list = new LinkedList<>();

        try {
            SmbFile smbFile = new SmbFile(smbPath, ntlmPasswordAuthentication);
            list.addAll(walk(smbFile.listFiles(), LocalDate.ofEpochDay(configuration.getLastSynchronization())));
        } catch (Exception ex) {
            errorList.add(ex.getMessage());
        }

        return list;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public NtlmPasswordAuthentication getNtlmPasswordAuthentication() {
        return ntlmPasswordAuthentication;
    }
}
