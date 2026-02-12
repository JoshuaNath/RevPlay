# 📊 RevPlay – Entity Relationship Diagram (ERD)

## 🎯 Overview

RevPlay is a console-based music and podcast streaming platform built using Java, JDBC, and Oracle PL/SQL.  
The database is designed using a relational model with normalized tables and proper foreign key constraints.

The ERD represents relationships between:

- Users (Listeners & Artists)
- Songs & Albums
- Playlists
- Favorites
- Listening History
- Podcasts & Podcast Episodes

---

## 🧑 USERS

The `users` table is the base entity for authentication and role management.

### Key Attributes:
- id (PK)
- username (Unique)
- password_hash
- email (Unique)
- full_name
- user_type (LISTENER / ARTIST)
- security_question
- security_answer

### Relationships:
- 1 → 1 with `listeners`
- 1 → 1 with `artists`

---

## 👂 LISTENERS

Stores listener-specific information.

### Key Attributes:
- user_id (PK, FK → users.id)
- subscription_type
- favorite_genre

### Relationships:
- 1 → Many Playlists
- 1 → Many Favorites
- 1 → Many Listening History
- 1 → Many Podcast History

---

## 🎤 ARTISTS

Stores artist-specific profile information.

### Key Attributes:
- user_id (PK, FK → users.id)
- artist_name
- genre
- disclose_social (0/1)
- instagram_id
- youtube_channel

### Relationships:
- 1 → Many Albums
- 1 → Many Songs
- 1 → Many Podcasts

---

## 💿 ALBUMS

Represents music albums created by artists.

### Key Attributes:
- id (PK)
- artist_id (FK → artists.user_id)
- title
- release_date

### Relationships:
- 1 Album → Many Songs
- Many Albums → 1 Artist

---

## 🎵 SONGS

Represents songs uploaded by artists.

### Key Attributes:
- id (PK)
- artist_id (FK → artists.user_id)
- album_id (FK → albums.id, nullable)
- title
- duration_seconds
- genre
- play_count
- favorite_count

### Relationships:
- Many Songs → 1 Artist
- Many Songs → 0/1 Album
- Many Songs ↔ Many Playlists
- Many Songs ↔ Many Listeners (Favorites)
- Many Songs → Many Listening History entries

---

## 📂 PLAYLISTS

Represents user-created playlists.

### Key Attributes:
- id (PK)
- listener_id (FK → listeners.user_id)
- name
- description
- is_public
- song_count

### Relationships:
- 1 Playlist → Many Songs (via playlist_songs)
- Many Playlists → 1 Listener

---

## 🔗 PLAYLIST_SONGS (Junction Table)

Handles Many-to-Many relationship between Playlists and Songs.

### Key Attributes:
- playlist_id (FK)
- song_id (FK)
- Composite Primary Key (playlist_id, song_id)

---

## ⭐ FAVORITES

Tracks songs marked as favorite by listeners.

### Key Attributes:
- listener_id (FK)
- song_id (FK)
- Composite Primary Key (listener_id, song_id)

---

## 🕒 LISTENING_HISTORY

Tracks song playback activity.

### Key Attributes:
- id (PK)
- listener_id (FK)
- song_id (FK)
- played_at (Timestamp)

---

## 🎙 PODCASTS

Represents podcasts created by artists.

### Key Attributes:
- id (PK)
- artist_id (FK → artists.user_id)
- title
- description

### Relationships:
- 1 Podcast → Many Podcast Episodes
- Many Podcasts → 1 Artist

---

## 🎧 PODCAST_EPISODES

Represents episodes under a podcast.

### Key Attributes:
- id (PK)
- podcast_id (FK → podcasts.id)
- title
- duration_seconds
- play_count

### Relationships:
- Many Episodes → 1 Podcast
- Many Episodes → Many Podcast History records

---

## 🕒 PODCAST_HISTORY

Tracks podcast episode playback.

### Key Attributes:
- id (PK)
- listener_id (FK)
- episode_id (FK)
- played_at (Timestamp)

---

## 🔁 Cardinality Summary

- User (1) → Listener (1)
- User (1) → Artist (1)
- Artist (1) → Songs (Many)
- Artist (1) → Albums (Many)
- Artist (1) → Podcasts (Many)
- Album (1) → Songs (Many)
- Playlist (Many) ↔ Songs (Many)
- Listener (1) → Playlists (Many)
- Listener (Many) ↔ Songs (Many via Favorites)
- Listener (1) → Listening History (Many)
- Podcast (1) → Episodes (Many)

---

## 🏗 Design Principles Used

- Normalized relational schema
- Foreign key constraints
- Junction tables for Many-to-Many relationships
- Separation of podcast and music analytics
- Role-based data segregation
- Scalable modular structure

---

## 🎯 Conclusion

The RevPlay ERD reflects a scalable streaming system architecture supporting:

- Role-based access (Listener / Artist)
- Music & Podcast streaming
- Playlists & Favorites
- Analytics & History tracking
- Album hierarchy
- Podcast episode management

This design ensures extensibility for future features such as:

- Subscriptions
- Recommendations
- Trending analytics
- Microservices migration
