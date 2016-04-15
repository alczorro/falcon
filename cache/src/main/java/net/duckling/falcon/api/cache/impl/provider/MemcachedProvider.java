/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.falcon.api.cache.impl.provider;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import net.duckling.falcon.api.cache.impl.CacheServiceBrokenException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Locator;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedNode;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.SerializingTranscoder;

import org.apache.log4j.Logger;

/**
 * @title: MemcachedProvider.java
 * @package net.duckling.falcon.api.cache.impl.provider
 * @description: Memcached 客户端具体实现
 * @author clive
 * @date 2012-9-13 下午2:57:38
 */
public class MemcachedProvider implements ICacheProvider {

    private static final Logger LOG = Logger.getLogger(MemcachedProvider.class);
    private static final int COMPRESS_THRESHOLD = 1024;
    private static final int OP_TIMEOUT = 4000;
    private static final int MAX_RECONN_DELAY = 1800;
    private static final int TIMEOUT_EXCEPTION_THRESHOLD = 1998;
    private static final int EXPIRED = 3600;
    private String url;
    private MemcachedClient client;

    /**
     * @param url
     * @throws IOException
     */
    public MemcachedProvider(String url) throws IOException {
        this.url = url;
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
        builder.setProtocol(Protocol.BINARY);
        SerializingTranscoder transcoder = new SerializingTranscoder();
        transcoder.setCompressionThreshold(COMPRESS_THRESHOLD);
        builder.setTranscoder(transcoder);
        builder.setOpTimeout(OP_TIMEOUT);
        builder.setHashAlg(DefaultHashAlgorithm.KETAMA_HASH);
        builder.setFailureMode(FailureMode.Redistribute);
        builder.setUseNagleAlgorithm(false);
        builder.setLocatorType(Locator.CONSISTENT);
        builder.setMaxReconnectDelay(MAX_RECONN_DELAY);
        builder.setTimeoutExceptionThreshold(TIMEOUT_EXCEPTION_THRESHOLD);
        builder.setUseNagleAlgorithm(false);
        client = new MemcachedClient(builder.build(), AddrUtil.getAddresses(url));
    }

    int retryCount = 0;
    int maxRetryTime = 10;
    long maxReconnectTimestamp = 30000;
    long lastRightTimestamp;
    long firstErrorTimestamp;

    @Override
    public Object get(String key) {
        Object obj = null;
        try {
            obj = client.get(key);
            lastRightTimestamp = System.currentTimeMillis();
        } catch (RuntimeException e) {
            exceptionHandler(e, "get");
        }
        return obj;
    }

    @Override
    public void remove(String key) {
        try {
            OperationFuture<Boolean> flag = client.delete(key);
            if (flag.get()) {
                lastRightTimestamp = System.currentTimeMillis();
            }
        } catch (RuntimeException | InterruptedException | ExecutionException e) {
            exceptionHandler(e, "remove");
        }
    }

    @Override
    public void set(String key, Object value) {
    	set(key, value, EXPIRED);
    }

    private void exceptionHandler(Exception e, String method) {
        if (firstErrorTimestamp == 0 || firstErrorTimestamp < lastRightTimestamp) {
            firstErrorTimestamp = System.currentTimeMillis();
        } else {
            if (maxReconnectTimestamp + firstErrorTimestamp < System.currentTimeMillis()) {
                LOG.info("Switch to local cache mode.");
                String state = checkCacheState();
                if (ALL_DOWN.equals(state)) {
                    throw new CacheServiceBrokenException();
                }
            } else {
                LOG.info("Trying to reconnect memcache server:" + this.url);
            }
        }
        LOG.error("MemcachedProvider." + method + "() throws an exception: " + e.getCause());
    }

    @Override
    public void disconnect() {
        this.client.shutdown();
    }

    @Override
    public String checkCacheState() {
        Collection<SocketAddress> availableServers = client.getAvailableServers();
        Collection<SocketAddress> brokenServers = client.getUnavailableServers();
        Collection<MemcachedNode> nodeList = client.getNodeLocator().getAll();
        if (nodeList.size() == brokenServers.size()) {
            return ALL_DOWN;
        } else if (availableServers.size() == nodeList.size()) {
            return ALL_WORK;
        }
        for (SocketAddress s : brokenServers) {
            client.connectionLost(s);
            LOG.error("Memcache node:" + s.toString() + " has broken.");
        }
        return PART_DOWN;
    }

	@Override
	public void set(String key, Object value, int expireInSeconds) {
        try {
            OperationFuture<Boolean> flag = client.set(key, expireInSeconds, value);
            if (flag.get()) {
                lastRightTimestamp = System.currentTimeMillis();
            }
        } catch (RuntimeException | InterruptedException | ExecutionException e) {
            exceptionHandler(e, "set");
        }
		
	}

}
