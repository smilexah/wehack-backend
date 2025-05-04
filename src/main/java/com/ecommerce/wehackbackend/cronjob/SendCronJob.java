package com.ecommerce.wehackbackend.cronjob;

import com.ecommerce.wehackbackend.model.entity.Event;
import com.ecommerce.wehackbackend.model.entity.Subscription;
import com.ecommerce.wehackbackend.repository.EventRepository;
import com.ecommerce.wehackbackend.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendCronJob {

    private final EventRepository eventRepository;
    private final TelegramBot telegramBot;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    // Запускаем каждые 5 минут
    @Scheduled(cron = "0 */5 * * * *")
    public void checkUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Checking upcoming events at {}", now);

        List<Event> upcomingEvents = eventRepository.findUnnotifiedEvents(now.toLocalDate());

        upcomingEvents.forEach(event ->
                executorService.execute(() -> processEvent(event, now))
        );
    }

    private void processEvent(Event event, LocalDateTime now) {
        try {
            LocalTime eventTime = parseTime(event.getTime());
            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), eventTime);

            if (eventDateTime.isBefore(now)) {
                return; // Событие уже прошло
            }

            Duration timeLeft = Duration.between(now, eventDateTime);
            checkAndSendNotifications(event, timeLeft);

        } catch (DateTimeParseException e) {
            log.error("Failed to parse time for event {}: {}", event.getId(), event.getTime());
        } catch (Exception e) {
            log.error("Error processing event {}: {}", event.getId(), e.getMessage());
        }
    }

    private void checkAndSendNotifications(Event event, Duration timeLeft) {
        long hoursLeft = timeLeft.toHours();
        long minutesLeft = timeLeft.toMinutes();

        // Уведомление за 24 часа
        if (!event.isDayBeforeNotified() && hoursLeft <= 24 && hoursLeft > 23) {
            sendDayBeforeNotification(event);
            event.setDayBeforeNotified(true);
            eventRepository.save(event);
        }

        // Уведомление за 1 час
        if (!event.isHourBeforeNotified() && minutesLeft <= 60 && minutesLeft > 59) {
            sendHourBeforeNotification(event);
            event.setHourBeforeNotified(true);
            eventRepository.save(event);
        }
    }

    private LocalTime parseTime(String timeStr) throws DateTimeParseException {
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            // Пробуем альтернативный формат, если основной не сработал
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }

    private void sendDayBeforeNotification(Event event) {
        String message = String.format(
                "⏰ 24-Hour Reminder! ⏰%n%n" +
                        "🎤 Event: %s%n" +
                        "🏛 Club: %s%n" +
                        "📅 Date: %s%n" +
                        "⏰ Time: %s%n" +
                        "📍 Location: %s%n%n" +
                        "🔗 Details: https://we-hack-front-self.vercel.app/events/%d",
                event.getTitle(),
                event.getClub().getName(),
                event.getDate(),
                event.getTime(),
                event.getIsOnline() ? "Online" : event.getVenue().getName(),
                event.getId()
        );

        sendNotifications(event, message);
        log.info("Sent 24-hour notification for event: {}", event.getId());
    }

    private void sendHourBeforeNotification(Event event) {
        String message = String.format(
                "⏰ Starting Soon! ⏰%n%n" +
                        "🎤 Event: %s%n" +
                        "⏰ Starts in 1 hour at %s%n" +
                        "📍 Where: %s%n%n" +
                        "%s%n%n" +
                        "🔗 Join: https://we-hack-front-self.vercel.app/events/%d",
                event.getTitle(),
                event.getTime(),
                event.getIsOnline() ? "Online" : event.getVenue().getName(),
                event.getIsOnline() ? "Stream link: " + event.getStreamingUrl() : "",
                event.getId()
        );

        sendNotifications(event, message);
        log.info("Sent 1-hour notification for event: {}", event.getId());
    }

    private void sendNotifications(Event event, String message) {
        List<Subscription> subscriptions = event.getClub().getSubscriptions();

        subscriptions.stream()
                .filter(sub -> sub.getUser().getTgChatId() != null)
                .forEach(sub -> {
                    try {
                        telegramBot.sendMessage(
                                sub.getUser().getTgChatId(),
                                message
                        );
                        Thread.sleep(100); // Задержка между сообщениями
                    } catch (Exception e) {
                        log.error("Failed to send to user {}: {}",
                                sub.getUser().getId(), e.getMessage());
                    }
                });
    }
}