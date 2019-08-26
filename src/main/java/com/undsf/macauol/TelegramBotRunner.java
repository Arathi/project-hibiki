package com.undsf.macauol;

import com.undsf.macauol.bots.MacauolLongPollingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

@Component
@Order(1)
public class TelegramBotRunner implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(TelegramBotRunner.class);

    @Value("${macauolbot.name}")
    protected String botUsername;

    @Value("${macauolbot.token}")
    protected String botToken;

    @Value("${proxy.host}")
    protected String proxyHost;

    @Value("${proxy.port}")
    protected int proxyPort;

    @Value("${proxy.type}")
    protected String proxyType;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        startTelegramBotServer();
    }

    public void startTelegramBotServer() {
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            DefaultBotOptions options = new DefaultBotOptions();
            if (!proxyType.equals("NONE")) {
                options.setProxyHost(proxyHost);
                options.setProxyPort(proxyPort);
            }
            if (proxyType.equals("SOCKS5")) {
                options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            }
            else if (proxyType.equals("HTTP")) {
                options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }

            LongPollingBot macauolBot = new MacauolLongPollingBot(botUsername, botToken, options);
            api.registerBot(macauolBot);
        }
        catch (TelegramApiException ex) {
            logger.warn(ex.getMessage());
        }
    }
}
