# 📊 RevPlay – Entity Relationship Diagram

```mermaid
erDiagram

    USERS {
        NUMBER id PK
        VARCHAR username
        VARCHAR password_hash
        VARCHAR email
        VARCHAR full_name
        VARCHAR user_type
        VARCHAR security_question
        VARCHAR security_answer
    }

    LISTENERS {
        NUMBER user_id PK, FK
        VARCHAR subscription_type
        VARCHAR favorite_genre
    }

    ARTISTS {
        NUMBER user_id PK, FK
        VARCHAR artist_name
        VARCHAR genre
        NUMBER disclose_social
        VARCHAR instagram_id
        VARCHAR youtube_channel
    }

    ALBUMS {
        NUMBER id PK
        NUMBER artist_id FK
        VARCHAR title
        DATE release_date
    }

    SONGS {
        NUMBER id PK
        NUMBER artist_id FK
        NUMBER album_id FK
        VARCHAR title
        NUMBER duration_seconds
        VARCHAR genre
        NUMBER play_count
        NUMBER favorite_count
    }

    PLAYLISTS {
        NUMBER id PK
        NUMBER listener_id FK
        VARCHAR name
        VARCHAR description
        NUMBER is_public
        NUMBER song_count
    }

    PLAYLIST_SONGS {
        NUMBER playlist_id FK
        NUMBER song_id FK
    }

    FAVORITES {
        NUMBER listener_id FK
        NUMBER song_id FK
    }

    LISTENING_HISTORY {
        NUMBER id PK
        NUMBER listener_id FK
        NUMBER song_id FK
        TIMESTAMP played_at
    }

    PODCASTS {
        NUMBER id PK
        NUMBER artist_id FK
        VARCHAR title
        VARCHAR description
    }

    PODCAST_EPISODES {
        NUMBER id PK
        NUMBER podcast_id FK
        VARCHAR title
        NUMBER duration_seconds
        NUMBER play_count
    }

    PODCAST_HISTORY {
        NUMBER id PK
        NUMBER listener_id FK
        NUMBER episode_id FK
        TIMESTAMP played_at
    }

    USERS ||--|| LISTENERS : "is"
    USERS ||--|| ARTISTS : "is"

    ARTISTS ||--o{ SONGS : uploads
    ARTISTS ||--o{ ALBUMS : creates
    ARTISTS ||--o{ PODCASTS : creates

    ALBUMS ||--o{ SONGS : contains

    LISTENERS ||--o{ PLAYLISTS : creates
    PLAYLISTS ||--o{ PLAYLIST_SONGS : includes
    SONGS ||--o{ PLAYLIST_SONGS : part_of

    LISTENERS ||--o{ FAVORITES : marks
    SONGS ||--o{ FAVORITES : liked_by

    LISTENERS ||--o{ LISTENING_HISTORY : listens
    SONGS ||--o{ LISTENING_HISTORY : tracked_in

    PODCASTS ||--o{ PODCAST_EPISODES : contains
    LISTENERS ||--o{ PODCAST_HISTORY : listens
    PODCAST_EPISODES ||--o{ PODCAST_HISTORY : tracked_in
