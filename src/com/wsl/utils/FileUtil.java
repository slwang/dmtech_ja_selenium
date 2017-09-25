package com.wsl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.chanjet.csp.cmr.repository.RepositoryManager;

public class FileUtil {
    protected static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void mkdirs(final File f) {
        if (f.exists()) {
            if (f.isFile()) {
                logger.debug("$$ mkdirs() error: {}", f.getAbsolutePath());
            }
        } else {
            mkdirs(f.getParentFile());
            final boolean isSuccessful = f.mkdir();
            if (!isSuccessful) {
                logger.debug("$$ mkdirs() error:{}", f.getAbsolutePath());
            }
        }
    }

    public static void delDir(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!directory.isDirectory()) {
            return;
        }

        try {
            Thread.sleep(20L);// 暂停一下
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        java.nio.file.Path path = Paths.get(directory.getAbsolutePath());
        Files.walkFileTree(path, new SimpleFileVisitor<java.nio.file.Path>() {
            @Override
            public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(java.nio.file.Path file, IOException exc) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(java.nio.file.Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    try {
                        Thread.sleep(10L); // 暂停一下
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exc;
                }
            }
        });
    }

    public static void copyWar(String dalPath, String warPath) {
        ZipFile warFile = null;
        try {
            warFile = new ZipFile(dalPath.replace("dal-codegen", "webapp").replace(".jar", ".war"));
            Enumeration<? extends ZipEntry> _entry = warFile.entries();
            while (_entry.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) _entry.nextElement());
                String name = entry.getName();
                if (!name.startsWith("WEB-INF/classes/")) {
                    continue;
                }

                File file = new File(warPath + name.substring("WEB-INF/classes/".length()));
                if (entry.isDirectory()) {
                    FileUtil.mkdirs(file);
                } else {
                    InputStream in = warFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(file);
                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                    in.close();
                    out.close();
                }
            }

            try {
                File programRootDir = new File(warPath);
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
                add.setAccessible(true);
                add.invoke(classLoader, programRootDir.toURI().toURL());
            } catch (Exception e) {
                logger.error("add url error", e);
            }

        } catch (Exception e) {
            logger.error("read webapp.war error", e);
        } finally {
            if (warFile != null) {
                try {
                    warFile.close();
                } catch (IOException e) {
                    logger.error("close webapp.war error", e);
                }
            }
        }
    }

    public static void copyCfg(String classPath, String dalPath, String cfg, String hbCfg, String dbCfg) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(dalPath);
            Enumeration<JarEntry> _entry = jarFile.entries();
            while (_entry.hasMoreElements()) {
                JarEntry entry = (JarEntry) _entry.nextElement();
                String name = entry.getName();
                if (name.equals(hbCfg)) {
                    // mkdir first
                    FileUtil.mkdirs(new File(classPath + cfg));

                    // ------------------- hibernate.cfg.xml ------------//
                    InputStream input = jarFile.getInputStream(entry);
                    InputStreamReader isr = new InputStreamReader(input);
                    BufferedReader reader = new BufferedReader(isr);
                    File hcfg = new File(classPath + hbCfg);
                    if (hcfg.exists()) {
                        hcfg.delete();
                    }
                    OutputStream output = new FileOutputStream(hcfg);

                    String line;
                    String password = "", username = "", connurl = "";
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("hibernate.connection.password")) {
                            password = line.trim().replace("<property name=\"hibernate.connection.password\">", "")
                                    .replace("</property>", "");
                        } else if (line.contains("hibernate.connection.username")) {
                            username = line.trim().replace("<property name=\"hibernate.connection.username\">", "")
                                    .replace("</property>", "");
                        } else if (line.contains("hibernate.connection.url")) {
                            connurl = line.trim().replace("<property name=\"hibernate.connection.url\">", "")
                                    .replace("</property>", "").replace(":", "\\:");
                        }
                        output.write((line + "\n").getBytes());
                    }

                    reader.close();
                    output.close();

                    // ------------------- dbconfig.properties ------------//
                    File dcfg = new File(classPath + dbCfg);
                    if (dcfg.exists()) {
                        dcfg.delete();
                    }
                    OutputStream output1 = new FileOutputStream(dcfg);
                    output1.write(("csp.cspdbpool.maxidlesize=4\n").getBytes());
                    output1.write(("csp.dbpool.testonborrow=true\n").getBytes());
                    output1.write(("csp.dbpool.testwhileidle=true\n").getBytes());
                    output1.write(("csp.dbpool.validationquerytimeout=1\n").getBytes());
                    output1.write(("csp.cspdbpool.initialsize=5\n").getBytes());
                    output1.write(("csp.cspdbpool.maxtotalsize=40\n").getBytes());
                    output1.write(("csp.cspdbpool.minidlesize=2\n").getBytes());
                    output1.write(("csp.dbpool.validationquery=select 1\n").getBytes());
                    output1.write(("hibernate.connection.username=" + username + "\n").getBytes());
                    output1.write(("hibernate.connection.url=" + connurl + "\n").getBytes());
                    output1.write(("hibernate.connection.password=" + password + "\n").getBytes());
                    output1.close();

                    //System.setProperty(RepositoryManager.DB_REPOSITORY_DB_CONNECTION_FILE_SYS_PROP, classPath + dbCfg);

                    break;
                }
            }
        } catch (Exception e) {
            logger.error("read dal-codegen.jar error", e);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    logger.error("close dal-codegen.jar error", e);
                }
            }
        }
    }
}
