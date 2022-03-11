package ru.rt.conf;

import java.util.Objects;

/**
 * Конфигурационные параметры bss сервисов (входит в BssConf)
 * @author Maksim.Filatov
 */
public class UrlConf {
    private String url;
    private String login;
    private String pwl;
    private Boolean encrypt = false;

    public UrlConf(String url, String pwl, String login, Boolean encrypt) {
	this.url = url;
	this.pwl = pwl;
	this.login = login;
	this.encrypt = encrypt;
    }

    public UrlConf() {
    }

    public String getUrl() {
	return url;
    }
    public void setUrl(String url) {
	this.url = url;
    }

    public String getPwl() {
	return pwl;
    }
    public void setPwl(String pwl) {
	this.pwl = pwl;
    }

    public String getLogin() {
	return login;
    }
    public void setLogin(String login) {
	this.login = login;
    }
    
    public Boolean getEncrypt() {
	return encrypt;
    }
    public void setEncrypt(Boolean encrypt) {
	this.encrypt = encrypt;
    }
    
    @Override
    public int hashCode() {
	int hash = 3;
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final UrlConf other = (UrlConf) obj;
	if (!Objects.equals(this.url, other.url)) {
	    return false;
	}
	if (!Objects.equals(this.pwl, other.pwl)) {
	    return false;
	}
	if (!Objects.equals(this.login, other.login)) {
	    return false;
	}
	return true;
    }
    
    @Override
    public String toString() {
	return "UrlConf{" + "url=" + url + '}';
    }
       
}
