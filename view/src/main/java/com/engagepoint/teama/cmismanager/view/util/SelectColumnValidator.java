package com.engagepoint.teama.cmismanager.view.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.List;

@FacesValidator("com.engagepoint.teama.cmismanager.view.util.SelectColumnValidator")
public class SelectColumnValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        List<String> columns = (List<String>) o;
        if (columns.size() > 4) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "too many columns selected. maximum - 4", "too many columns selected. maximum - 4"));

        }
    }
}
