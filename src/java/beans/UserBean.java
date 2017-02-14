/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.ContDTO;
import dto.TransactionDTO;
import dto.UserDTO;
import interceptors.TransactionInterceptor;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import service.UseriServiceRemote;
import utils.Cryptare;
import service.ConturiServiceRemote;


/**
 *
 * @author Israel Dago
 */
@Named
@SessionScoped
public class UserBean implements java.io.Serializable {

    private Integer id;
    private String username, parola;
    private boolean admin;
    private Integer contDeCreditatID;
    private Integer contDeDebitatID;
    private Double suma;

    @EJB
    private UseriServiceRemote service;
    @EJB
    private ConturiServiceRemote transactionService;
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

   
    

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Integer getContDeCreditatID() {
        return contDeCreditatID;
    }

    public void setContDeCreditatID(Integer contDeCreditatID) {
        this.contDeCreditatID = contDeCreditatID;
    }

    public Integer getContDeDebitatID() {
        return contDeDebitatID;
    }

    public void setContDeDebitatID(Integer contDeDebitatID) {
        this.contDeDebitatID = contDeDebitatID;
    }

    public Double getSuma() {
        return suma;
    }

    public void setSuma(Double suma) {
        this.suma = suma;
    }

    ////My Methods
    public void register() {
        UserDTO userDTO = new UserDTO(this.username, Cryptare.hidePassword(this.parola), this.admin);
        boolean rez = service.registerUser(userDTO);
        if (rez) {
            //register reusit
            if (this.admin) {
                RequestContext.getCurrentInstance().execute("swal('Congratulation!', 'New Admin registered with Success', 'success')");
            } else {
                RequestContext.getCurrentInstance().execute("swal('Congratulation!', 'Simple user registered with Success', 'success')");
            }
        } else {
            // register esuat
            RequestContext.getCurrentInstance().execute("swal('Register Failed!!!', 'You maybe registered already? Try to Log in', 'error')");
        }
        this.username = null;
        this.parola = null;
        this.admin = false;
    }

    public String loginCheck() {
        HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        UserDTO userDTO = new UserDTO(this.username, Cryptare.hidePassword(this.parola)); //loginReusit
        UserDTO rez = service.loginUser(userDTO);
        if (rez != null) {
            //login reusit          
            if (rez.isAdmin()) {    
                //FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("privilege", "Admin");
                sessionFaces.setAttribute("username", "Admin " + this.username);
                
            } else {
                //FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("privilege", "User");
                sessionFaces.setAttribute("username", "User " + this.username);
            }
          
            return "loginReusit";
        } else {
            // login esuat            
            RequestContext.getCurrentInstance().execute("swal('Login Failed!!!', 'Username or Parola Gresite!!! Try again', 'error')");
        }
        this.username = null;
        this.parola = null;
        this.admin = false;
        return "loginFailed";
    }

     
    
    public String getLogedPrivilege() {
        HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return sessionFaces.getAttribute("username").toString();
        //FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("privilege");

    }

    public void logout() {
        try {
            HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            // addWarningMessage("Deconectare", "La revedere");
            this.username = null;
            this.parola = null;
            this.admin = false;
            
            sessionFaces.setAttribute("username", null);            
            FacesContext.getCurrentInstance().getExternalContext().redirect("../faces/index.xhtml");

        } catch (IOException ex) {
            RequestContext.getCurrentInstance().execute("swal('Logout Failed!!!', 'Some errors happened', 'error')");

        }

    }

    private void addWarningMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    ////////Transferi Methods
    public List<ContDTO> getAllAccounts() {
        return transactionService.allAccounts();
    }

    @Interceptors(TransactionInterceptor.class)
    public void depunere() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSuma(this.suma);
        transactionDTO.setContDeCreditatID(this.contDeCreditatID);
        transactionService.depunereNumar(transactionDTO);
        reIntilializeCampuriTransfer();
    }

    @Interceptors(TransactionInterceptor.class)
    public void retragere() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSuma(this.suma);
        transactionDTO.setContDeDebitatID(this.contDeDebitatID);
        transactionService.retragereNumar(transactionDTO);
        reIntilializeCampuriTransfer();
    }

    @Interceptors(TransactionInterceptor.class)
    public void transfer() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSuma(this.suma);
        transactionDTO.setContDeCreditatID(this.contDeCreditatID);
        transactionDTO.setContDeDebitatID(this.contDeDebitatID);
        transactionService.transferNumar(transactionDTO);
        reIntilializeCampuriTransfer();
    }

    private void reIntilializeCampuriTransfer() {
        this.suma = null;
        this.contDeCreditatID = null;
        this.contDeDebitatID = null;
    }

}
