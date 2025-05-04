package com.ecommerce.wehackbackend.service;

import com.ecommerce.wehackbackend.bot.TelegramBot;
import com.ecommerce.wehackbackend.model.entity.Event;
import com.ecommerce.wehackbackend.model.entity.User;
import com.ecommerce.wehackbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService {
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    @Async
    public void sendEventNotifications(List<Long> userIds, Event event) {
        String message = buildEventMessage(event);

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user == null || user.getTgChatId() == null) {
                log.warn("User {} has no linked Telegram chat ID", userId);
                continue;
            }

            try {
                telegramBot.sendMessageWithMarkdown(user.getTgChatId(), message);
                Thread.sleep(40); // Интервал 40 мс между сообщениями
            } catch (InterruptedException e) {
                log.error("Message sending interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private String buildEventMessage(Event event) {
        return String.format(
                "🎉 *New event from %s Club!* 🎉\n\n" +
                        "🔹 *Title:* %s\n" +
                        "🔹 *Club:* %s\n" +
                        "📅 *Date:* %s\n" +
                        "⏰ *Time:* %s\n" +
                        "📍 *Location:* %s\n\n" +
                        "%s\n\n" +
                        "💰 *Price:* %s\n" +
                        "🎟 *Tickets remaining:* %s\n\n" +
                        "⚡ *Hurry up to book your spot!* ⚡\n" +
                        "🔗 Link: https://we-hack-front-self.vercel.app/events/%d",
                event.getClub().getName(),
                event.getTitle(),
                event.getClub().getName(),
                event.getDate(),
                event.getTime(),
                event.getIsOnline() ?
                        "Online: " + event.getStreamingUrl() :
                        (event.getVenue() != null ? event.getVenue().getName() : "To be announced"),
                event.getDescription(),
                event.getPrice() != null ? event.getPrice() + "KZT" : "Free",
                event.getCapacity() != null ? event.getCapacity() : "Unlimited",
                event.getId()
        );
    }
}