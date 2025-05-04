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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
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
                handleStartCommand(chatId, messageText);
            }
        }
    }

    private void handleStartCommand(Long chatId, String messageText) {
        String[] parts = messageText.split(" ");
        if (parts.length == 2) {
            handleToken(chatId, parts[1]);
        } else {
            sendWelcomeMessage(chatId);
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        String message = """
                👋 *Welcome!* 
                
                To link your account, visit our secure login page:
                🔗 [Login Page](https://we-hack-front-self.vercel.app/auth/login)
                
                Click the link or copy it to your browser""";
        sendMarkdownV2Message(chatId, message);
    }

    private void handleToken(Long chatId, String token) {
        Optional<User> optionalUser = userRepository.findByTgLinkTokenAndIsActive(token);

        if (optionalUser.isEmpty()) {
            sendMarkdownV2Message(chatId, "❌ *Invalid or outdated token*");
            return;
        }

        User user = optionalUser.get();

        if (isTokenExpired(user)) {
            sendMarkdownV2Message(chatId, "⚠️ *Token expired*\\nGenerate a new one on the website");
            return;
        }

        if (user.getTgChatId() != null) {
            sendMarkdownV2Message(chatId, "✅ *Account already linked*");
            return;
        }

        linkUserAccount(user, chatId);
        sendMarkdownV2Message(chatId, "🎉 *Linking successful!*\\nYou'll now receive event notifications");
    }

    private boolean isTokenExpired(User user) {
        return user.getTgTokenCreatedAt() != null &&
                Duration.between(user.getTgTokenCreatedAt(), LocalDateTime.now()).toMinutes() > 15;
    }

    private void linkUserAccount(User user, Long chatId) {
        user.setTgChatId(chatId);
        user.setTgLinkToken(null);
        user.setTgTokenCreatedAt(null);
        userRepository.save(user);
    }

    /**
     * Sends a message with MarkdownV2 formatting
     * @param chatId Target chat ID
     * @param text Message text with MarkdownV2 formatting
     */
    public void sendMarkdownV2Message(Long chatId, String text) {
        sendMarkdownV2Message(chatId, text, true, null);
    }

    /**
     * Sends a message with MarkdownV2 formatting and custom keyboard
     * @param chatId Target chat ID
     * @param text Formatted message text
     * @param disableWebPagePreview Whether to disable link previews
     * @param replyKeyboard Custom keyboard markup (nullable)
     */
    public void sendMarkdownV2Message(Long chatId, String text, boolean disableWebPagePreview, ReplyKeyboard replyKeyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(escapeMarkdownV2(text))
                .parseMode("MarkdownV2")
                .disableWebPagePreview(disableWebPagePreview)
                .replyMarkup(replyKeyboard)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}\nOriginal message: {}",
                    chatId, e.getMessage(), text, e);
        }
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


    /**
     * Escapes special characters for MarkdownV2
     */
    private String escapeMarkdownV2(String text) {
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
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