package org.keycloak.examples.providers.credential.hash;

import org.junit.Assert;
import org.junit.Test;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.UserCredentialModel;


public class CustomPasswordHashProviderTest  {
    @Test
    public void verify() throws Exception {
        String plainPrivateCredential = "0293LHWong@";
        
        
        
        CustomPasswordHashProviderFactory factory = new CustomPasswordHashProviderFactory();
        
        PasswordHashProvider hashProvider = factory.create(null);

        
        
        
        
        CredentialModel credentials = new CredentialModel();
        credentials.setAlgorithm(CustomPasswordHashProviderFactory.ID);
        credentials.setType(UserCredentialModel.PASSWORD);
        
        
        credentials.setSalt("$2y$10$8OMo6giO/KV2.Cig2drVZu".getBytes());
        credentials.setHashIterations(10);
        credentials.setValue("$2y$10$8OMo6giO/KV2.Cig2drVZuqigG3.h9dh6J8WV/6kRbe6GHQNNBmjq");
        //credentials.setValue("qigG3.h9dh6J8WV/6kRbe6GHQNNBmjq");
        
        //hashProvider.encode(plainPrivateCredential, 1, credentials);

        
        Assert.assertTrue(hashProvider.verify(plainPrivateCredential, credentials));
    }
}
