package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.exception.ResourceNotFoundException;
import com.ankit.BloodDonorFinder.model.Notification;
import com.ankit.BloodDonorFinder.model.User;
import com.ankit.BloodDonorFinder.repository.NotificationRepository;
import com.ankit.BloodDonorFinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    // save notification to database
    public void sendNotification(User user, String title, String message, Notification.Type type) {

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    // send mail to donor
    public void sendMail(String toEmail, String subject, String body) {

        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toEmail);
            mail.setSubject(subject);
            mail.setText(body);
            mailSender.send(mail);
        } catch(Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    // notify all nearby donors about blood request
    public void notifyNearbyDonors(List<User> donors, String bloodGroup, String hospitalName) {

        for(User donor : donors) {

            // save in app notification
            sendNotification(
                    donor, "Urgent! " + bloodGroup + " blood needed",
                    "Required at " + hospitalName + " - Please respond if  available",
                    Notification.Type.REQUEST);

            // send mail
            sendMail(
                    donor.getEmail(),
                    "Urgent blood request - " + bloodGroup,
                    "Dear " + donor.getName() + ",\n\n" +
                            "A Patient urgently needs " + bloodGroup + " blood at " + hospitalName + ".\n\n" +
                            "Please open the app and respond " + "if you are available. \n\n" +
                            "Thank You!\nBlood Donor Finder Team");
        }
    }

    // get all my notificaions
    public List<Notification> getMyNotification(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        return notificationRepository.findByUser(user);
    }

    // get unread notification only
    public List<Notification> getUnreadNotifications(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        return notificationRepository.findByUserAndIsRead(user, false);
    }

    // mark single notification as read
    public String markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));
        notification.setRead(true);

        notificationRepository.save(notification);

        return "Notification marked as read!";
    }

    // mark all notification as read
    public String markAllAsRead(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        List<Notification> unread = notificationRepository.findByUserAndIsRead(user, false);

        for(Notification notification: unread) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }

        return "All notification marked as read!";
    }

    // get unread count
    public long getUnreadCount(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        return notificationRepository.countByUserAndIsRead(user, false);
    }
}
