package org.keycloak.examples.providers.credential.hash;

import java.util.Arrays;
import java.util.Collections;
import javax.ws.rs.core.Response;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.Base64;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class KeycloakAdminClientExample {
	
	public static void main(String[] args) {

		
		
		String serverUrl = "http://18.139.4.3:8080/auth";
		String realm = "demo";
		String clientId = "admin-api";
		String clientSecret = "b32080b3-d96c-4193-b76c-abfe56cf9848";


//		// Client "idm-client" needs service-account with at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
		Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(serverUrl) //
				.realm(realm) //
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
				.clientId(clientId) //
				.clientSecret(clientSecret).build();

		// User "javaland" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
		/*Keycloak keycloak = KeycloakBuilder.builder() //
				.serverUrl(serverUrl) //
				.realm(realm) //
				.grantType(OAuth2Constants.PASSWORD) //
				.clientId(clientId) //
				.clientSecret(clientSecret) //
				.username("idm-admin") //
				.password("admin") //
				.build();*/

		// Define user
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername("tester1@gmail.com");
		user.setFirstName("First");
		user.setLastName("Last");
		user.setEmail("tester1@gmail.com");
		user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

		// Define password credential
		CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setAlgorithm(CustomPasswordHashProviderFactory.ID);
		//passwordCred.setSalt(Base64.encodeBytes("theSalt".getBytes()));
		passwordCred.setSalt(Base64.encodeBytes("$2y$10$8OMo6giO/KV2.Cig2drVZuqigG3.h9dh6J8WV/6kRbe6GHQNNBmjq".getBytes()));
		passwordCred.setHashIterations(10);
		//passwordCred.setValue("password");
		
						
		//passwordCred.setHashedSaltedValue("ABC");
		passwordCred.setHashedSaltedValue("$2y$10$8OMo6giO/KV2.Cig2drVZuqigG3.h9dh6J8WV/6kRbe6GHQNNBmjq");


		user.setCredentials(Collections.singletonList(passwordCred));
		
		// Get realm
		RealmResource realmResource = keycloak.realm(realm);
		UsersResource userRessource = realmResource.users();

		// Create user (requires manage-users role)
		Response response = userRessource.create(user);
		System.out.println("Repsonse: " + response.getStatusInfo());
		System.out.println(response.getLocation());
		String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

		System.out.printf("User created with userId: %s%n", userId);

		// Get realm role "user" (requires view-realm role)
		RoleRepresentation testerRealmRole = realmResource.roles()//
				.get("user").toRepresentation();

		// Assign realm role "user" to user
		userRessource.get(userId).roles().realmLevel() //
				.add(Arrays.asList(testerRealmRole));

		// Get client
		ClientRepresentation app1Client = realmResource.clients() //
				.findByClientId("skillzstreet").get(0);

		// Get client level role (requires view-clients role)
		RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
				.roles().get("USER").toRepresentation();

		// Assign client level role to user
		userRessource.get(userId).roles() //
				.clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));

		
		// Define password credential
		/*CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setAlgorithm(CustomPasswordHashProviderFactory.ID);
		passwordCred.setSalt(BCrypt
                .gensalt(5)
                .getBytes().toString());
		passwordCred.setHashIterations(1);
		//passwordCred.setValue("password");
		passwordCred.setHashedSaltedValue("WwLz7syE5CmMiiSa7/kiGGJt+oUT2nk2kfzPytZnvLaDCrLjF5K2bijCNDWwwGOy3GjSXiOY7QDFi20lY59p8A==");

		// Set password credential
		userRessource.get(userId).resetPassword(passwordCred);*/
		
		

	}
}
