# 🎵 RevPlay – Entity Relationship Diagram

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
        NUMBER user_id PK
        VARCHAR subscription_type
        VARCHAR favorite_genre
    }

    ARTISTS {
        NUMBER user_id PK
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
        NUMBER play_count
    }

    PLAYLISTS {
        NUMBER id PK
        NUMBER listener_id FK
        VARCHAR name
        VARCHAR description
        NUMBER is_public
    }

    PLAYLIST_SONGS {
        NUMBER playlist_id PK
        NUMBER song_id PK
        TIMESTAMP added_at
    }

    FAVORITES {
        NUMBER listener_id PK
        NUMBER song_id PK
        TIMESTAMP added_at
    }

    LISTENING_HISTORY {
        NUMBER id PK
        NUMBER listener_id FK
        NUMBER song_id FK
        TIMESTAMP played_at
    }

    USERS ||--|| LISTENERS : "is"
    USERS ||--|| ARTISTS : "is"

    ARTISTS ||--o{ ALBUMS : creates
    ARTISTS ||--o{ SONGS : uploads

    ALBUMS ||--o{ SONGS : contains

    LISTENERS ||--o{ PLAYLISTS : creates
    PLAYLISTS ||--o{ PLAYLIST_SONGS : contains
    SONGS ||--o{ PLAYLIST_SONGS : appears_in

    LISTENERS ||--o{ FAVORITES : adds
    SONGS ||--o{ FAVORITES : liked_by

    LISTENERS ||--o{ LISTENING_HISTORY : plays
    SONGS ||--o{ LISTENING_HISTORY : recorded_in
