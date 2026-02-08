erDiagram

    USERS {
        NUMBER id PK
        VARCHAR username
        VARCHAR password_hash
        VARCHAR email
        VARCHAR user_type
    }

    LISTENERS {
        NUMBER user_id PK
        VARCHAR subscription_type
    }

    ARTISTS {
        NUMBER user_id PK
        VARCHAR artist_name
        VARCHAR genre
    }

    SONGS {
        NUMBER id PK
        NUMBER artist_id FK
        VARCHAR title
        NUMBER duration_seconds
    }

    PLAYLISTS {
        NUMBER id PK
        NUMBER listener_id FK
        VARCHAR name
        NUMBER is_public
    }

    PLAYLIST_SONGS {
        NUMBER playlist_id PK
        NUMBER song_id PK
    }

    LISTENING_HISTORY {
        NUMBER id PK
        NUMBER listener_id FK
        NUMBER song_id FK
        TIMESTAMP played_at
    }

    USERS ||--|| LISTENERS : "is"
    USERS ||--|| ARTISTS : "is"

    ARTISTS ||--o{ SONGS : uploads
    LISTENERS ||--o{ PLAYLISTS : creates
    PLAYLISTS ||--o{ PLAYLIST_SONGS : contains
    SONGS ||--o{ PLAYLIST_SONGS : added_to
    LISTENERS ||--o{ LISTENING_HISTORY : plays
    SONGS ||--o{ LISTENING_HISTORY : tracked_in

    
    
    
    
    
    
   flowchart TB

    UI[MainApp\n(Console UI)]
    SERVICE[Service Layer\n(AuthService, MusicService, PlaylistService)]
    DAO[DAO Layer\n(UserDAO, SongDAO, PlaylistDAO)]
    DB[(Oracle Database)]

    UI --> SERVICE
    SERVICE --> DAO
    DAO --> DB
    