package me.moonways.bridgenet.mtp.message.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.config.descriptor.NetworkCipherSecurityDescriptor;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
@RequiredArgsConstructor
public final class MessageEncryption {

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String GENERATION_ALGORITHM = "RSA";

    private static final int KEY_SIZE = 2048;


    private final NetworkCipherSecurityDescriptor security;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private void generateKeyPair() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(GENERATION_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);

            final KeyPair pair = keyPairGenerator.generateKeyPair();

            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
        } catch (NoSuchAlgorithmException exception) {
            log.error("§4Cannot be generate private-key: §c{}", exception.toString());
        }
    }

    private PrivateKey generatePrivateKey(String key) {
        try {
            byte[] byteKey = BASE64_DECODER.decode(key.getBytes());

            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(GENERATION_ALGORITHM);

            return keyFactory.generatePrivate(encodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("§4Cannot be generate private-key: §c{}", exception.toString());
        }

        return null;
    }

    private PublicKey generatePublicKey(String key) {
        try {
            byte[] byteKey = BASE64_DECODER.decode(key.getBytes());

            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(GENERATION_ALGORITHM);

            return keyFactory.generatePublic(encodedKeySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("§4Cannot be generate public-key", exception);
        }

        return null;
    }

    public String toHexBase64(Key key) {
        return BASE64_ENCODER.encodeToString(key.getEncoded());
    }

    private boolean matchesKeysPair(PrivateKey privateKey, PublicKey publicKey) {
        String hexedPrivateKey = toHexBase64(privateKey);
        String hexedPublicKey = toHexBase64(publicKey);
        return security.getPrivateKey().equals(hexedPrivateKey) && security.getPublicKey().equals(hexedPublicKey);
    }

    public void generateKeys() {
        if (security == null || !security.isFilled()) {

            log.warn("§6Paired keys cannot be matches!");
            log.warn("§6Generating new security keys...");

            generateKeyPair();

            log.info("Generated private-key: §2" + toHexBase64(privateKey));
            log.info("Generated public-key: §2" + toHexBase64(publicKey));

            log.info("§nPlease, enter generated encryption keys to security config!");

        } else {
            publicKey = generatePublicKey(security.getPublicKey());
            privateKey = generatePrivateKey(security.getPrivateKey());

            log.debug("Encrypted keys was matches from security config");
        }
    }

    public ByteBuf decode(ByteBuf byteBuf) {
        byte[] bytes = ByteCodec.readBytesArray(byteBuf);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return Unpooled.buffer().writeBytes(cipher.doFinal(bytes));
        } catch (Exception exception) {
            log.error("§4Cannot be decode encrypted message: §c{}", exception.toString());
            return Unpooled.buffer().writeBytes(bytes);
        }
    }

    public ByteBuf encode(ByteBuf byteBuf) {
        byte[] bytes = ByteCodec.readBytesArray(byteBuf);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return Unpooled.buffer().writeBytes(cipher.doFinal(bytes));
        } catch (Exception exception) {
            log.error("§4Cannot be encode message with encryption: §c{}", exception.toString());
            return Unpooled.buffer().writeBytes(bytes);
        }
    }
}
