package org.keycloak.examples.providers.credential.hash;

import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;




public class CustomPasswordHashProvider implements PasswordHashProvider {

	private final String providerId;


    private final int defaultIterations;
    
 



    
    public CustomPasswordHashProvider(String providerId, String algorithm, int defaultIterations) {
    	this.providerId = providerId;
        this.defaultIterations = defaultIterations;

    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, CredentialModel credential) {
    	
    	return credential.getHashIterations() == policy.getHashIterations() && providerId.equals(credential.getAlgorithm());
    }

    @Override
    public void encode(String rawPassword, int iterations, CredentialModel credential) {
        if (iterations == -1) {
            iterations = defaultIterations;
        }

        byte[] salt = getSalt();
        String encodedPassword = encode(rawPassword, iterations, salt);

        credential.setAlgorithm(providerId);
        credential.setType(UserCredentialModel.PASSWORD);
        credential.setSalt(salt);
        credential.setHashIterations(iterations);
        credential.setValue(encodedPassword);
    }

    @Override
    public String encode(String rawPassword, int iterations) {
        if (iterations == -1) {
            iterations = defaultIterations;
        }

        byte[] salt = getSalt();
        return encode(rawPassword, iterations, salt);
    }

    @Override
    public boolean verify(String rawPassword, CredentialModel credential) {
        return encode(rawPassword, credential.getHashIterations(), credential.getSalt()).equals(credential.getValue());
    	
    	//return BCrypt.checkpw(rawPassword, credential.getValue());
    }
    
    
    
    public void close() {
    }
    
    private String encode(String rawPassword, int iterations, byte[] salt) {
        
    	try {
            //String hexPrivateCreditional = DigestUtils.sha1Hex(rawPassword.getBytes("UTF-8"));
            String stringSalt = new String(salt);
            
            return BCrypt.hashpw(rawPassword, stringSalt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    	
    	
    }
    
    
    private byte[] getSalt() {
        
        
        return BCrypt
        .gensalt(10)
        .getBytes();
    }
    

	
	

    
}
