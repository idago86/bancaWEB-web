/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Israel Dago
 */
@FacesValidator(value = "validators.ValidatorCNP")
public class ValidatorCNP implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String cnp = value.toString();

        if (cnp.length() != 13) {
                FacesMessage mesaj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input Error", "CNP incorrect..13 cifrii necesare pentru un CNP valid");
                throw new ValidatorException(mesaj);
        }
    }

}
