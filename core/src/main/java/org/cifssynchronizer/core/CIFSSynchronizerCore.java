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
import jcifs.smb.SmbFile;
import org.cifssynchronizer.dao.models.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class CIFSSynchronizerCore {
    final private String KILL = "KILL";
    final private List<String> errorList;
    final private NtlmPasswordAuthentication ntlmPasswordAuthentication;
    final private ConcurrentLinkedDeque<SmbFile> files = new ConcurrentLinkedDeque<>();
    final private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    final private ExecutorService threadPool = Executors.newFixedThreadPool(25);
    final private AtomicInteger counter = new AtomicInteger(0);
    private final Configuration configuration;
    private String smbPath;

    public CIFSSynchronizerCore(Configuration configuration) {
        this.configuration = configuration;
        errorList = new LinkedList<>();
        Keys keys = Utils.loadKeys();

        String domain = configuration != null && configuration.getCredential() != null ? configuration.getCredential().getDomainName() : "";
        String userName = configuration != null && configuration.getCredential() != null ? configuration.getCredential().getUserName() : "";
        String password = configuration != null && configuration.getCredential() != null ?
                Security.decrypt(keys.getKey1(), keys.getKey2(), configuration.getCredential().getPassword()) : "";

        smbPath = configuration != null && configuration.getCredential() != null ? configuration.getSmbPath() : "";
        smbPath = smbPath + (!smbPath.isEmpty() && !smbPath.endsWith("/") ? "/" : "");
        ntlmPasswordAuthentication = new NtlmPasswordAuthentication(domain, userName, password);
    }

    public ConcurrentLinkedDeque<SmbFile> produceFiles() {
        try {
            queue.put(smbPath);

            final Thread watcher = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        if (counter.get() == 0 && queue.isEmpty()) {
                            threadPool.shutdown();
                            queue.put(KILL);
                            return;
                        }
                    } catch (InterruptedException e) {
                        errorList.add(e.getMessage());
                    }
                }
            });
            watcher.start();

            threadPool.submit((Runnable) () -> {
                while (true) {
                    try {
                        String url = queue.take();
                        if (url.equals(KILL)) {
                            return;
                        }
                        threadPool.execute(new Worker(files, queue, ntlmPasswordAuthentication, url, counter, errorList,
                                configuration.getLastSynchronization()));
                    } catch (InterruptedException e) {
                        errorList.add(e.getMessage());
                    }
                }
            });

            watcher.join();
        } catch (InterruptedException e) {
            errorList.add(e.getMessage());
        }

        return files;
    }

//    public Observable<SmbFile> produceFiles() {
//        return Observable.create((Subscriber<? super SmbFile> observer) -> {
//            try {
//                SmbFile smbFile = new SmbFile(smbPath, ntlmPasswordAuthentication);
//                walk(observer, smbFile.listFiles(), LocalDate.ofEpochDay(configuration.getLastSynchronization()));
//            } catch (Exception ex) {
//                errorList.add(ex.getMessage());
//            }
//            observer.onCompleted();
//        });
//    }
//
//    private void walk(Subscriber<? super SmbFile> observer, SmbFile[] sfs, LocalDate lastSynchronization) {
//        Stream.of(sfs).forEach(sf -> {
//            LocalDate input = new Date(sf.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            if (!input.isBefore(lastSynchronization)) {
//                try {
//                    if (sf.isFile())
//                        observer.onNext(sf);
//                    else if (sf.isDirectory())
//                        walk(observer, sf.listFiles(), lastSynchronization);
//                } catch (Exception e) {
//                    this.errorList.add(e.getMessage());
//                }
//            }
//        });
//    }

    public List<String> getErrorList() {
        return errorList;
    }

    public NtlmPasswordAuthentication getNtlmPasswordAuthentication() {
        return ntlmPasswordAuthentication;
    }
}
