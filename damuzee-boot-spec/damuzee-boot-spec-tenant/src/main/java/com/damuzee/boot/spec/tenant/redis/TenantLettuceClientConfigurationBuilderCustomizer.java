package com.damuzee.boot.spec.tenant.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TenantLettuceClientConfigurationBuilderCustomizer implements LettuceClientConfigurationBuilderCustomizer {

    @Autowired
    private Optional<RedisProperties> redisProperties;
    @Autowired
    private Optional<RedisKeyProperties> redisKeyProperties;

    @Override
    public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigurationBuilder) {
        if (!redisProperties.isPresent() || !redisKeyProperties.isPresent()) {
            log.debug("redisProperties or redisKeyProperties is empty");
            return;
        }

        RedisProxy redisProxy = redisKeyProperties.map(RedisKeyProperties::getProxy)
            .orElse(RedisProxy.nonProxy);
        if (!Objects.equals(redisProxy, RedisProxy.twemproxy)) {
            log.debug("redisProxy is not twemproxy");
            return;
        }

        clientConfigurationBuilder.clientOptions(createClientOptions());
    }

    private ClientOptions createClientOptions() {
        ClientOptions.Builder builder = initializeClientOptionsBuilder();
        Duration connectTimeout = getProperties().getConnectTimeout();
        if (connectTimeout != null) {
            builder.socketOptions(SocketOptions.builder().connectTimeout(connectTimeout).build());
        }
        builder.protocolVersion(ProtocolVersion.RESP2);
        return builder.timeoutOptions(TimeoutOptions.enabled()).build();
    }

    private ClientOptions.Builder initializeClientOptionsBuilder() {
        if (getProperties().getCluster() != null) {
            ClusterClientOptions.Builder builder = ClusterClientOptions.builder();
            RedisProperties.Lettuce.Cluster.Refresh refreshProperties = getProperties().getLettuce().getCluster()
                .getRefresh();
            ClusterTopologyRefreshOptions.Builder refreshBuilder = ClusterTopologyRefreshOptions.builder()
                .dynamicRefreshSources(refreshProperties.isDynamicRefreshSources());
            if (refreshProperties.getPeriod() != null) {
                refreshBuilder.enablePeriodicRefresh(refreshProperties.getPeriod());
            }
            if (refreshProperties.isAdaptive()) {
                refreshBuilder.enableAllAdaptiveRefreshTriggers();
            }
            return builder.topologyRefreshOptions(refreshBuilder.build());
        }
        return ClientOptions.builder();
    }

    protected final RedisProperties getProperties() {
        return redisProperties.get();
    }

}
