package com.ecommerce.wehackbackend.bot;

import com.ecommerce.wehackbackend.config.TelegramBotProperties;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties telegramBotProperties;
    private final UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                String[] parts = messageText.split(" ");
                if (parts.length == 2) {
                    String token = parts[1];
                    handleToken(chatId, token);
                } else {
                    sendMessage(chatId, "👋 Hello! To link your account, simply visit our secure login page below:\n\n🔗 https://we-hack-front-self.vercel.app/auth/login\n\nClick the link or copy it into your browser to get started!");                }
            }
        }
    }

    private void handleToken(Long chatId, String token) {
        Optional<User> optionalUser = userRepository.findByTgLinkTokenAndIsActive(token);

        if (optionalUser.isEmpty()) {
            sendMessage(chatId, "❌ Invalid or outdated token.");
            return;
        }

        User user = optionalUser.get();

        // TTL = 15 минут
        if (user.getTgTokenCreatedAt() != null &&
                Duration.between(user.getTgTokenCreatedAt(), LocalDateTime.now()).toMinutes() > 15) {
            sendMessage(chatId, "⚠\uFE0F The token is out of date. Generate a new one on the website.");
            return;
        }

        if (user.getTgChatId() != null) {
            sendMessage(chatId, "✅ Your account is already linked.");
            return;
        }

        user.setTgChatId(chatId);
        user.setTgLinkToken(null); // Удаляем токен после привязки
        user.setTgTokenCreatedAt(null);
        userRepository.save(user);

        sendMessage(chatId, "\uD83C\uDF89 Linking was successful! Now you will receive notifications about events at the university.");
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getBotToken();
    }
}
