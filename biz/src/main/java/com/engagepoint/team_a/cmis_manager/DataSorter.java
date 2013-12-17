package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.ValidationException;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class DataSorter {

    public static final String SAME_ID_ERROR = "Same ID";

    public static ArrayList<TypeDefinition> validateAndSort(HashMap<String, TypeDefinition> typeDefinitionMap, ArrayList<FileStatusReport> fileStatusList) {

        HashMap<String, ArrayList<TypeDefinition>> sortedByIdTypeMap = new HashMap<String, ArrayList<TypeDefinition>>();
        TreeSet<TypeDefinition> typeDefinitionTreeSet = new TreeSet<TypeDefinition>(new TypesIdentityComparator());

        for(String fileName : typeDefinitionMap.keySet()) {

            TypeDefinition type = typeDefinitionMap.get(fileName);

            try {
                validateTypeDefinition(type);
                if (! typeDefinitionTreeSet.add(type)) {
                    fileStatusList.add(new FileStatusReport(fileName, SAME_ID_ERROR));
                }
            } catch (ValidationException e) {
                fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
            }
        }

        for (TypeDefinition type : typeDefinitionTreeSet) {
            if (sortedByIdTypeMap.containsKey(type.getParentTypeId())) {

                ArrayList<TypeDefinition> children = sortedByIdTypeMap.get(type.getParentTypeId());
                children.add(type);

            } else {
                ArrayList<TypeDefinition> children = new ArrayList<TypeDefinition>();

                children.add(type);
                sortedByIdTypeMap.put(type.getParentTypeId(), children);
            }
        }

        for (TypeDefinition type : typeDefinitionTreeSet) {

            if( sortedByIdTypeMap.containsKey( type.getId() )) {
                ArrayList<TypeDefinition> temp = sortedByIdTypeMap.get( type.getParentTypeId() );

                if(!temp.isEmpty()) {
                    temp.addAll(sortedByIdTypeMap.get(type.getId()));
                    sortedByIdTypeMap.put(type.getId(), temp );
                }

            }

        }

        for (TypeDefinition type : typeDefinitionTreeSet) {
            if( sortedByIdTypeMap.containsKey( type.getId() )) {
                sortedByIdTypeMap.remove(type.getId());
            }
        }

        ArrayList<TypeDefinition> resultList = new ArrayList<TypeDefinition>();

        if(sortedByIdTypeMap.isEmpty()) {
            fileStatusList.add(new FileStatusReport("ID collision", resultList.size() + " files."));
        } else {

            for (Collection value : sortedByIdTypeMap.values()) {
                resultList.addAll(value);
            }

            fileStatusList.add(new FileStatusReport("Ready to upload", resultList.size() + " files."));

        }

        return resultList;
    }

    public static void validateTypeDefinition(TypeDefinition type) throws ValidationException {

        if (type == null) {
            throw new IllegalArgumentException("Type is null!");
        }

        if (type.getId().isEmpty()) {
            throw new ValidationException("Type id must be set.");
        }

        if (type.getLocalName().isEmpty()) {
            throw new ValidationException("Local name must be set.");
        }

        if (type.getQueryName() != null) {
            if (type.getQueryName().length() == 0) {
                throw new ValidationException("Query name must not be empty.");
            } else if (!checkQueryName(type.getQueryName())) {
                throw new ValidationException("Query name contains invalid characters.");
            }
        }

        if (type.isCreatable() == null) {
            throw new ValidationException("Creatable flag must be set.");
        }

        if (type.isFileable() == null) {
            throw new ValidationException("Fileable flag must be set.");
        }

        if (type.isQueryable() == null) {
            throw new ValidationException("Queryable flag must be set.");
        } else if (type.isQueryable().booleanValue()) {
            if (type.getQueryName() == null || type.getQueryName().isEmpty()) {
                throw new ValidationException("Queryable flag is set to TRUE, but the query name is not set.");
            }
        }

        if (type.isControllablePolicy() == null) {
            throw new ValidationException("ControllablePolicy flag must be set.");
        }

        if (type.isControllableAcl() == null) {
            throw new ValidationException("ControllableACL flag must be set.");
        }

        if (type.isFulltextIndexed() == null) {
            throw new ValidationException("FulltextIndexed flag must be set.");
        }

        if (type.isIncludedInSupertypeQuery() == null) {
            throw new ValidationException("IncludedInSupertypeQuery flag must be set.");
        }

        if (type.getBaseTypeId() == null) {
            throw new ValidationException("Base type id must be set.");
        } else if (!type.getBaseTypeId().value().equals(type.getParentTypeId())) {
            if (type.getParentTypeId() == null || type.getParentTypeId().isEmpty()) {
                throw new ValidationException("Parent type id must be set.");
            }
        }

    }

    private static boolean checkQueryName(String queryName) {
        return queryName != null && queryName.length() > 0 && queryName.indexOf(' ') < 0 && queryName.indexOf(',') < 0
                && queryName.indexOf('"') < 0 && queryName.indexOf('\'') < 0 && queryName.indexOf('\\') < 0
                && queryName.indexOf('.') < 0 && queryName.indexOf('(') < 0 && queryName.indexOf(')') < 0;
    }
}
