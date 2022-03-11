package ru.rt.conf;

import java.util.List;

public class BssConf {
    private List<UrlConf> params;               
            
    public BssConf(List<UrlConf> params) {
	this.params = params;	
    }
    
    public BssConf() {
    }        

    public List<UrlConf> getParams() {
	return params;
    }
    public void setParams(List<UrlConf> params) {
	this.params = params;
    }        
          
}