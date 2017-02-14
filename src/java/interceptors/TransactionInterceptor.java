/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptors;

import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Israel Dago
 */
public class TransactionInterceptor implements java.io.Serializable{
    
    
    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception{
        HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String privilege = sessionFaces.getAttribute("username").toString().substring(0, 5);
        if("Admin".equalsIgnoreCase(privilege)){ 
            RequestContext.getCurrentInstance().execute("swal('Acces Autorized!!!', 'Operatiune effectuata cu sucess', 'success')");
            return ctx.proceed();
        }else{
            RequestContext.getCurrentInstance().execute("swal('Acces Denied!!!', 'Nu aveti privilegii de Admin pentru aceasta operatiune', 'error')");
            FacesContext.getCurrentInstance().getExternalContext().responseReset();
            return null;
        }
    }
    
    
}
