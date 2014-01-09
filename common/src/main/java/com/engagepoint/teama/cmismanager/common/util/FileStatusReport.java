package com.engagepoint.teama.cmismanager.common.util;

import java.io.Serializable;

public class FileStatusReport implements Serializable {

    private String name;
    private String status;

    public FileStatusReport(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + "  " + status;
    }

    @Override
    public boolean equals(Object obj) {
        boolean boolValue;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FileStatusReport)) {
            return false;
        }
        FileStatusReport other = (FileStatusReport) obj;
        if (other.getName().equals(this.getName()) &&
                other.getStatus().equals(this.getStatus())) {
            boolValue = true;
        } else {
            boolValue = false;
        }
        return boolValue;

    }
}
