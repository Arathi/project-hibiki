package com.undsf.macauol.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MacauolLongPollingBot extends TelegramLongPollingBot {
    private static Logger logger = LoggerFactory.getLogger(MacauolLongPollingBot.class);

    protected String botUsername;
    protected String botToken;

    public MacauolLongPollingBot(String name, String token, DefaultBotOptions options) {
        super(options);
        botUsername = name;
        botToken = token;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message reqMsg = update.getMessage();
            String respText = null;

            String fromId = null;
            String reqText = reqMsg.getText();
            if (reqText != null) {
                Chat chat = reqMsg.getChat();
                if (chat.isUserChat()) {
                    fromId = reqMsg.getChatId() + "";
                }
                else {
                    User user = reqMsg.getFrom();
                    fromId = user.getId() + "@" + reqMsg.getChatId();
                }

                logger.info(fromId + ": " + reqText);
                if (reqText.equals("/signin") || reqText.startsWith("/signin@")) {
                    respText = "签到成功";
                }
                else if (reqText.equals("/blackjack") || reqText.startsWith("/blackjack@")) {
                    respText = "21点，尚未实现";
                }
                else if (reqText.equals("/fivecardstud") || reqText.startsWith("/fivecardstud@")) {
                    respText = "五张梭哈，尚未实现";
                }
            }
            if (respText != null) {
                SendMessage respMsg = new SendMessage()
                        .setChatId(reqMsg.getChatId())
                        .setText(respText);
                try {
                    execute(respMsg);
                }
                catch (TelegramApiException ex) {
                    logger.warn(ex.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
