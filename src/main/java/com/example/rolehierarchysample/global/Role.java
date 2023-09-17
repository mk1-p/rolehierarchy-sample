package com.example.rolehierarchysample.global;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Role {

    GUEST("ROLE_GUEST","비 로그인 사용자"),
    COMMON("ROLE_USER","일반 사용자"),
    API("ROLE_API","API Key 사용자"),
    VIP("ROLE_VIP","특별 사용자"),
    ADMIN("ROLE_ADMIN","관리자");


    private String key;
    private String desc;

    Role(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }


    public String getSubKey() {
        return this.getKey().replace("ROLE_","");
    }


    private static final Map<String, Role> BY_KEY =
            Stream.of(values())
                    .collect(Collectors.toMap(Role::getKey, Function.identity()));

    public static Role valueOfKey(String insertKey) {
        return BY_KEY.get(insertKey);
    }

}
