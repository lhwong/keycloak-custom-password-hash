package org.keycloak.examples.providers.credential.hash;

import org.keycloak.Config.Scope;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;

import org.apache.commons.codec.digest.DigestUtils;

public class CustomPasswordHashProviderFactory implements PasswordHashProviderFactory {

    public static final String ID = "bcrypt";
    
 
    public static final String ALGORITHM = "2y";

    public static final int DEFAULT_ITERATIONS = 10;


    

    
    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return new CustomPasswordHashProvider(ID, ALGORITHM, DEFAULT_ITERATIONS);
    }
	
	@Override
	public void init(Scope config) {
		
	}
	
	@Override
	public void postInit(KeycloakSessionFactory factory) {
		
		
	}
	
	@Override
	public String getId() {
		return ID;
	}









	@Override
	public void close() {
		
		
	}

    
}
