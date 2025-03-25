package com.digitalsanctuary.springaiclient.adapters.openai.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.digitalsanctuary.springaiclient.TestApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
class OpenAIConfigProxyTest {

    @Autowired
    private OpenAIConfigProperties properties;
    
    @Autowired
    private OpenAIConfig openAIConfig;

    @Test
    void testProxyConfigurationLoaded() {
        log.info("Testing proxy configuration loading");
        
        // Verify the basic properties are loaded
        assertNotNull(properties);
        assertNotNull(properties.getApiKey());
        assertEquals("gpt-4o", properties.getModel());
        
        // Verify the proxy configuration is loaded
        assertNotNull(properties.getProxy());
        assertFalse(properties.getProxy().isEnabled()); // Disabled by default in test config
        assertEquals("test-proxy.example.com", properties.getProxy().getHost());
        assertEquals(8080, properties.getProxy().getPort());
        assertEquals("testuser", properties.getProxy().getUsername());
        assertEquals("testpass", properties.getProxy().getPassword());
    }
    
    @Test
    void testCustomProxyConfiguration() {
        log.info("Testing custom proxy configuration");
        
        // Create a custom proxy configuration
        OpenAIConfigProperties.ProxyConfig proxyConfig = new OpenAIConfigProperties.ProxyConfig();
        proxyConfig.setEnabled(true);
        proxyConfig.setHost("custom-proxy.example.org");
        proxyConfig.setPort(3128);
        proxyConfig.setUsername("customuser");
        proxyConfig.setPassword("custompass");
        
        // Apply the custom configuration
        properties.setProxy(proxyConfig);
        
        // Verify the custom configuration
        assertEquals(true, properties.getProxy().isEnabled());
        assertEquals("custom-proxy.example.org", properties.getProxy().getHost());
        assertEquals(3128, properties.getProxy().getPort());
        assertEquals("customuser", properties.getProxy().getUsername());
        assertEquals("custompass", properties.getProxy().getPassword());
    }
    
    // We'll just directly test the proxy configuration values
    @Test
    void testExtraProxyConfigurationValues() {
        log.info("Testing additional proxy configuration values");
        
        // Create a custom proxy configuration with various values
        OpenAIConfigProperties.ProxyConfig proxyConfig = new OpenAIConfigProperties.ProxyConfig();
        
        // Test default values
        assertFalse(proxyConfig.isEnabled());
        
        // Test setting values
        proxyConfig.setEnabled(true);
        proxyConfig.setHost("custom-proxy.domain.com");
        proxyConfig.setPort(3128);
        proxyConfig.setUsername("proxyuser123");
        proxyConfig.setPassword("securepass456");
        
        assertEquals(true, proxyConfig.isEnabled());
        assertEquals("custom-proxy.domain.com", proxyConfig.getHost());
        assertEquals(3128, proxyConfig.getPort());
        assertEquals("proxyuser123", proxyConfig.getUsername());
        assertEquals("securepass456", proxyConfig.getPassword());
    }
}