package util;

import java.io.*;
import java.util.zip.*;

public class ExtractZip {
    public ExtractZip() {
    }

    public boolean process(String zipfile, String extforder) throws FileNotFoundException,
        IOException, IllegalArgumentException {

        boolean status = false;

        try {
            System.out.println("file name==" + zipfile);

            FileInputStream fins = new FileInputStream(zipfile);

            BufferedInputStream bis = new BufferedInputStream(fins);
            ZipInputStream zins = new ZipInputStream(bis);

            if (zins == null) {
                System.out.println("file is null!");
            } else {
                if (zins.getNextEntry() == null) {
                    System.out.println("zins.getNextEntry is null! ");
                } else {
                    //System.out.println("do nothing");
                }
            }

            ZipEntry ze = null;            
            byte ch[] = new byte[256];
            int index = 0;
            while ((ze = zins.getNextEntry()) != null) {           	
                System.out.println("~~~~~index:"+index++);                
                //if (temp.indexOf("index.htm") > 0) {
                if (ze.getName().indexOf("index.htm") == 0) {
                    status = true;
                }
                String path = ze.getName();
                System.out.println("path==" + path);

                if (path.split("/").length >= 3) {
                    if (path.indexOf("css") > 0 || path.indexOf("images") > 0) {
                        path = path.substring((ze.getName().indexOf("/") + 1));
                    }
                } else if (path.split("/").length == 2) {
                    if (path.indexOf("index.htm") > 0 || ze.isDirectory()) {
                        path = path.substring((ze.getName().indexOf("/") + 1));
                    }
                } else if (path.split("/").length == 1) {
                    path = path.substring((ze.getName().indexOf("/") + 1));
                }

                System.out.println("path======" + path);
                File zfile = new File(extforder + path);
                if (zfile == null) {
                    System.out.println("zfile = null");
                } else {
                    System.out.println("zfile" + zfile.getName());
                }

                File fpath = new File(zfile.getParentFile().getPath());
                if (fpath == null) {
                    System.out.println("fpath = null");
                } else {
                    System.out.println("fpath" + fpath.getName());
                }

                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        zfile.mkdirs();
                    }
                    zins.closeEntry();
                } else {
                    if (!fpath.exists()) {
                        fpath.mkdirs();
                    }

                    FileOutputStream fouts = new FileOutputStream(zfile);
                    int i;
                    while ((i = zins.read(ch)) != -1) {
                        fouts.write(ch, 0, i);
                    }
                    zins.closeEntry();
                    fouts.close();
                }
            }
            fins.close();
            zins.close();

        } catch (FileNotFoundException e) {
                System.out.println("error msg=" + e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException ioe) {
                System.out.println("ioerror msg==" + ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (Exception iae) {
                System.out.println("ioerror msg==" + iae.getMessage());
            iae.printStackTrace(System.err);
            //throw new IllegalArgumentException(iae.getMessage());
        }
        return status;
    }

}
