package com.exampletest.testtt.Configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("file:foo.properties")
public class VariablesProperties {
    @Value("${tms.user}")
    private String user;
    @Value("${code.email.user}")
    private String code_email_user;
    @Value("${code.email.password}")
    private String code_email_password;
    @Value("${code.email.pop3.host}")
    private String code_email_pop3_host;
    @Value("${code.email.pop3.port}")
    private String code_email_pop3_port;
    @Value("${code.email.pop3.ssl.protocols}")
    private String code_email_pop3_ssl_protocols;
    @Value("${mail.smtp.host}")
    private String mailHost;
    @Value("${mail.from.Email}")
    private String mailFromEmail;
    @Value("${mail.to.Email}")
    private String mailToEmail;
    @Value("${mail.smtp.user}")
    private String mailUser;
    @Value("${mail.smtp.password}")
    private String mailPassword;
    @Value("${mail.smtp.port}")
    private String mailPort;
    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${proxies}")
    private String proxies;


}
