package org.cifssynchronizer.core;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
class Worker implements Runnable {

    final private List<String> errorList;
    private final long lastSynchronization;
    private ConcurrentLinkedDeque<SmbFile> files;
    private LinkedBlockingQueue<String> queue;
    private NtlmPasswordAuthentication auth;
    private String url;
    private AtomicInteger counter;


    Worker(ConcurrentLinkedDeque<SmbFile> files, LinkedBlockingQueue<String> queue,
           NtlmPasswordAuthentication auth, String url, AtomicInteger counter,
           List<String> errorList, long lastSynchronization) {
        this.files = files;
        this.queue = queue;
        this.auth = auth;
        this.url = url;
        this.counter = counter;
        this.errorList = errorList;
        this.lastSynchronization = lastSynchronization;
    }

    private boolean isDirectory(SmbFile smbFile) {
        try {
            return smbFile.isDirectory();
        } catch (SmbException e) {
            errorList.add(e.getMessage());
            return false;
        }
    }

    private void spooling(SmbFile smbFile) {
        try {
            queue.put(smbFile.toString());
        } catch (InterruptedException e) {
            errorList.add(e.getMessage());
        }
    }

    private boolean filterDate(SmbFile smbFile){
        LocalDate input = new Date(smbFile.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !input.isBefore(LocalDate.ofEpochDay(lastSynchronization));
    }

    @Override
    public void run() {
        counter.incrementAndGet();
        try {
            final Map<Boolean, List<SmbFile>> groups = Arrays.stream(new SmbFile(url, auth).listFiles())
                    .filter(this::filterDate)
                    .collect(Collectors.partitioningBy(this::isDirectory));

            files.addAll(groups.get(Boolean.FALSE));

            groups.get(Boolean.TRUE).stream().forEach(this::spooling);
        } catch (MalformedURLException | SmbException e) {
            errorList.add(e.getMessage());
        }
        counter.decrementAndGet();
    }
}
