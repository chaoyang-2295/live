package com.huashengke.com.live.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.huashengke.com.live.AliClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@PropertySource("classpath:alilive.properties")
public class AliClientConfig {
    @Autowired
    private Environment env;
    @Bean
    public AliClient getAliClient() {
        IClientProfile profile = DefaultProfile.getProfile(
                env.getProperty("live.endpoint"),
                env.getProperty("live.accessKeyId"),
                env.getProperty("live.secretAccessKey"));

        return new AliClient(
                new DefaultAcsClient(profile),
                env.getProperty("live.domain"),
                env.getProperty("live.privateKey"),
                env.getProperty("live.courseCreator"),
                env.getProperty("live.bucket"),
                env.getProperty("live.videoEndPoint"),
                env.getProperty("live.videoName"));
    }
}