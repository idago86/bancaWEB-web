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
@FacesValidator(value = "validators.NumberValidator")
public class NumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        try {

            Integer.parseInt(value.toString());

            if (value.toString().length() != 13) {
                FacesMessage mesaj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input Error", "CNP incorrect..13 cifrii necesare pentru un CNP valid");
                throw new ValidatorException(mesaj);
            }

        } catch (NumberFormatException e) {
            FacesMessage mesaj = new FacesMessage(FacesMessage.SEVERITY_WARN, "Input Error", "Wrong Number Format");
            throw new ValidatorException(mesaj);
        }

    }

}
