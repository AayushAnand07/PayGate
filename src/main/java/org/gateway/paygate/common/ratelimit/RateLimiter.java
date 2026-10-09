package org.gateway.paygate.common.ratelimit;

public interface RateLimiter {

    RateLimitResult check(String key,int maxRequestsAllowed,long windowSeconds);
}
