package com.engagepoint.team_a.cmis_manager;

import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import java.util.Comparator;

public class TypesIdentityComparator implements Comparator<TypeDefinition> {

    @Override
    public int compare(TypeDefinition o1, TypeDefinition o2) {
        int returned = 0;

        if (!o1.getId().equals(o2.getId()) && !o1.getQueryName().equals(o2.getQueryName())
                && !o1.getDisplayName().equals(o2.getDisplayName()) && !o1.getLocalName().equals(o2.getLocalName())) {
                    if (o1.getParentTypeId().equals(o2.getParentTypeId())) {
                        if (o1.getBaseTypeId().value().equals(o2.getBaseTypeId().value())) {
                            returned = o1.getId().compareTo(o2.getId());
                        }
                    } else {
                        returned = o1.getId().compareTo(o2.getId());
                    }
                }
        return returned;
    }

}