package models;

import java.time.LocalDateTime;

/**
 * Represents a purchase made by a registered user for a song.
 */
public class Purchase {

    private int id;               // Auto-generated ID
    private String userId;        // RegisteredUser id
    private String songId;        // Song id
    private LocalDateTime purchasedAt;

    public Purchase(int id, String userId, String songId, LocalDateTime purchasedAt) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
        this.purchasedAt = purchasedAt;
    }

    public Purchase(String userId, String songId, LocalDateTime purchasedAt) {
        this.userId = userId;
        this.songId = songId;
        this.purchasedAt = purchasedAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", songId='" + songId + '\'' +
                ", purchasedAt=" + purchasedAt +
                '}';
    }
}
