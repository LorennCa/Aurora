/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.teseo.restapi.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "auth")
public class AuthWrapper {
    private String user;
    private String password;

    public AuthWrapper(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public AuthWrapper() {
        
    }
    
    @XmlElement
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString()
    {
        return "AuthWrapper: user: " + user + " | password: " + password;
    }
}