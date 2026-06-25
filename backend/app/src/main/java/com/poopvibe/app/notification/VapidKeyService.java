package com.poopvibe.app.notification;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Base64;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.util.BigIntegers;
import org.springframework.stereotype.Service;

/**
 * Resolves configured VAPID keys or generates a dev-only in-memory pair.
 */
@Service
public class VapidKeyService {
    private final VapidKeyMaterial keyMaterial;

    /**
     * Creates the VAPID key service and ensures Bouncy Castle is available.
     *
     * @param properties push configuration properties
     */
    public VapidKeyService(PushProperties properties) {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        String publicKey = properties.configuredPublicKey().orElse(null);
        String privateKey = properties.configuredPrivateKey().orElse(null);
        if (publicKey == null || privateKey == null) {
            this.keyMaterial = generateEphemeralKeyPair();
        } else {
            this.keyMaterial = new VapidKeyMaterial(publicKey, privateKey);
        }
    }

    /**
     * Returns the base64url public VAPID key used by browser subscriptions.
     *
     * @return public VAPID key
     */
    public String publicKey() {
        return keyMaterial.publicKey();
    }

    /**
     * Returns both VAPID keys for server-side delivery.
     *
     * @return runtime key pair
     */
    public VapidKeyMaterial keyMaterial() {
        return keyMaterial;
    }

    private VapidKeyMaterial generateEphemeralKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(Utils.ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            generator.initialize(new ECNamedCurveGenParameterSpec(Utils.CURVE));
            KeyPair keyPair = generator.generateKeyPair();
            String publicKey = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(Utils.encode((ECPublicKey) keyPair.getPublic()));
            String privateKey = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(BigIntegers.asUnsignedByteArray(32, ((ECPrivateKey) keyPair.getPrivate()).getD()));
            return new VapidKeyMaterial(publicKey, privateKey);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Could not generate development VAPID keys.", ex);
        }
    }
}
