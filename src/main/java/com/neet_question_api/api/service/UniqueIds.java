package com.neet_question_api.api.service;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class UniqueIds {
    private  final int API_KEY_LENGTH = 32;
    private  final SecureRandom secureRandom = new SecureRandom();

    public  String generateApiKey() {
        byte[] randomBytes = new byte[API_KEY_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public  String generateUserId() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        // Convert to Base64 and remove padding
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
        return base64.substring(0, 12); // Shorten to 8 characters
    }
}
