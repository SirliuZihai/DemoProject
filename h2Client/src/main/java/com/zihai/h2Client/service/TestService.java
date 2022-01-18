package com.zihai.h2Client.service;

import java.util.List;

public interface TestService {
    List doSomething();

    String testSql();

    void updateTest();

    String testCache(String id);

    void testCacheEvict(String id);
}
