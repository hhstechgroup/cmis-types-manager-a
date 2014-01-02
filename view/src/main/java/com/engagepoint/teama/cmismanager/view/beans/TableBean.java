package com.engagepoint.teama.cmismanager.view.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@ViewScoped
public class TableBean {
    private String[] columnsArray = {"id","localName","localNamespace","displayName",
            "queryName",
            "description","inherited","required","queryable","orderable","openChoice","propertyType","cardinality",
            "updatability","choiseList","defaultValue","extensions"};
    private String[] defColumns = {"id","displayName","description"};
    private List<String> selectedColumns =  Arrays.asList(defColumns);
    List<String> columnsHeader = Arrays.asList(columnsArray);

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public List<String> getColumnsHeader() {
        return columnsHeader;
    }

    public void setColumnsHeader(List<String> columnsHeader) {
        this.columnsHeader = columnsHeader;
    }

}
