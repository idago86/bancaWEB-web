/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dto.ClientDTO;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import service.ClientiServiceRemote;

/**
 *
 * @author Israel Dago
 */

@Named
@RequestScoped
public class ClientBean implements java.io.Serializable {

    private String nume, prenume, cnp;
    

    @EJB
    private ClientiServiceRemote serviceRemote;

    

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public void adaugaClient() {
        serviceRemote.adaugaClient(new ClientDTO(this.nume, this.prenume, this.cnp));
        reIntilializeCampuriText();
    }

    public void removeClient(Integer clientID) {
        ClientDTO clt = new ClientDTO(clientID);
        serviceRemote.removeClient(clt);  
        RequestContext.getCurrentInstance().execute("swal('Success!', 'Client removed Successfully', 'success')");
    }

    public List<ClientDTO> getAllClienti() {
        return serviceRemote.allClienti();
    }

//    public void onDelete(SelectEvent event) {
//        ClientDTO clt = (ClientDTO) event.getObject();
//
//        serviceRemote.removeClient(new ClientDTO(clt.getId(), clt.getNume(), clt.getPrenume(), clt.getCnp()));
//        System.out.println(clt.getNume() + " " + clt.getPrenume() + " a fost sters");
//
//    }


    private void reIntilializeCampuriText() {
        this.nume = null;
        this.prenume = null;
        this.cnp = null;
    }

 
}
