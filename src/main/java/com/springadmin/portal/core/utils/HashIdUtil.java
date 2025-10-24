package com.springadmin.portal.core.utils;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashIdUtil {

    private Hashids hashids;

    @Value("${hashid.salt}")
    private String salt;

    @Value("${hashid.min-length}")
    private int minLength;


    public String encodeId(long id) {
        if (hashids == null) {
            hashids = new Hashids(salt, minLength);
        }
        return hashids.encode(id);
    }

    public long decodeId(String hash) {
        if (hashids == null) {
            hashids = new Hashids(salt, minLength);
        }
        long[] decoded = hashids.decode(hash);
        if (decoded.length == 0) {
            throw new IllegalArgumentException("Invalid hash ID");
        }
        return decoded[0];
    }
}

