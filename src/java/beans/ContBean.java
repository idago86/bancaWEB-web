/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.ClientDTO;
import dto.ContDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import service.ConturiServiceRemote;


/**
 *
 * @author Israel Dago
 */

@Named
@RequestScoped
public class ContBean implements java.io.Serializable{
    private String clientID;
    private String accountDescription;
    
    
    @EJB private ConturiServiceRemote serviceRemote;
   

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

   
    
    public void adaugaAccount(){        
        ContDTO contDTO = new ContDTO();
        contDTO.setDescriere(this.accountDescription);
        contDTO.setClient(new ClientDTO(Integer.parseInt(this.clientID)));
        serviceRemote.addAccount(contDTO);
        reIntilializeCampuriText();
    }
    
    public void removeCont(Integer contID){
        ContDTO contDTO = new ContDTO();
        contDTO.setId(contID);
        serviceRemote.removeAccount(contDTO); 
        RequestContext.getCurrentInstance().execute("swal('Success!', 'Account removed Successfully', 'success')");
    }
    
    
    
    public List<ContDTO> getAllAccounts(){
        return serviceRemote.allAccounts();
    }
    
    
    
    private void reIntilializeCampuriText(){
        this.clientID = null;
        this.accountDescription = null;        
    }
  
 
}
