package com.versionone.sdk.examples;

import java.util.Calendar;
import java.util.Date;

import com.versionone.apiclient.APIException;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.IAPIConnector;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IMetaModel;
import com.versionone.apiclient.IServices;
import com.versionone.apiclient.MetaModel;
import com.versionone.apiclient.OidException;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.QueryResult;
import com.versionone.apiclient.Services;
import com.versionone.sso.V1SsoConnector;

public class SSOExample extends SdkSampleBase {

	private String _idpUrl;
	
	@Override
	public void loadConfig()
	{		
		super.loadConfig();
		setIdpUrl(Configuration.instance().getIdpUrl());
	}
	
    @Override
	public void connect() {
    	
        Log("Creating Meta Connector");
        IAPIConnector metaConnector = new V1SsoConnector(Configuration.instance(), "/meta.v1/");
        Log("Create MetaModel");
        _meta = new MetaModel(metaConnector);

        Log("Creating Data Connector");
//        IAPIConnector dataConnector = new V1SsoConnector(Configuration.instance(), "/rest-1.v1/");
        V1SsoConnector dataConnector = new V1SsoConnector(Configuration.instance(), "/rest-1.v1/");
        
        try {
        	Log("Authenticate");
			dataConnector.authenticate();
		} catch (SecurityException e) {
			e.printStackTrace();
			return;
		} catch (ConnectionException e) {
			e.printStackTrace();
			return;
		}
        
        Log("Create Services");
        _services = new Services(_meta, dataConnector);
	}

    protected IMetaModel _meta; 
	public IMetaModel getMetaModel() {
		return _meta;
	}

	private IServices _services;
	public IServices getServices() {
		return _services;
	}
    
    public void run()
    {
    	try {
    		Log("Get Members");

            IAssetType memberType = getMetaModel().getAssetType("Member");
            IAttributeDefinition memberName = memberType.getAttributeDefinition("Name");

            Query query = new Query(memberType);
            query.getSelection().add(memberName);
            QueryResult result = getServices().retrieve(query);
            Log(String.format("Server returned %s members", result.getTotalAvaliable()));
    					
			Log("The End - Success");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (OidException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }

	private void Log(String message)
    {
        Date now = Calendar.getInstance().getTime();
        System.out.printf("%s - %s\n", now.toString(), message);
    }

	public String getIdpUrl() {
		return _idpUrl;
	}

	public void setIdpUrl(String _idpUrl) {
		this._idpUrl = _idpUrl;
	}
    
}
