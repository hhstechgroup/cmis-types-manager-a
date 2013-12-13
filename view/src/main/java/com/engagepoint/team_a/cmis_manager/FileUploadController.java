package com.engagepoint.team_a.cmis_manager;

import java.io.IOException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.*;

@ManagedBean
@RequestScoped
public class FileUploadController {
    private String msgLbl = "----";
    private String show="false";

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getMsgLbl() {
        return msgLbl;
    }

    public void setMsgLbl(String msgLbl) {
        this.msgLbl = msgLbl;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            msgLbl=file.getFileName()+" content type: "+file.getContentType();
            show="true";
            create(file.getInputstream());
        } catch (IOException e) {
            msgLbl=e.getMessage();
        }
    }

    public void create(InputStream inputStream) {
        OutputStream outputStream = null;
        String pathStr = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/files");
        File path = new File(pathStr);

        if (!path.exists()) {
            path.mkdir();
        }
        try {
            File newFile = File.createTempFile("iadfasfd", "fasdfasdf", path);
//            msgLbl+=newFile.getAbsolutePath();
//            msgLbl+=" "+newFile.getPath()+"--"+newFile.getName();
            outputStream = new FileOutputStream(newFile);
            byte[] bytes = new byte[inputStream.available()];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

