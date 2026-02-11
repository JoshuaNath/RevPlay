**RevPlay – Console-Based Music Streaming Application**
**Overview:**

RevPlay is a full-featured, console-based music streaming system built using Java 17, JDBC, Oracle PL/SQL, Maven, JUnit, Log4J, and Git.

The application simulates a real-world streaming platform with role-based access (Listener & Artist), music playback simulation, playlist management, analytics, and privacy controls — all implemented using a clean layered architecture.

This project demonstrates backend system design, relational database modeling, and production-style feature implementation within a compact codebase.

**Architecture:**
RevPlay follows a layered architecture:
Presentation Layer – Console UI (MainApp)
Service Layer – Business logic (AuthService, MusicService, PlaylistService)
DAO Layer – Database interaction via JDBC
Database Layer – Oracle SQL + PL/SQL procedures

This separation ensures:
Clean code organization
Maintainability
Scalability toward future web or microservice extension

**User Roles & Features:**
**Listener:**
Listeners can:
Register and login with security question validation
Search songs (partial & case-insensitive matching)
Browse songs, albums, and artists
Play songs with:
    Progress bar (MM:SS format)
    Pause, Skip, Repeat, Stop
    Add to Favorites while playing
View listening history (timestamped)
Create and manage playlists
View public playlists
View artist profiles
View favorite songs

**Artist:**
Artists can:
Register with optional social media disclosure
Create albums
Add songs to albums
Upload standalone songs
View their profile (visible to listeners)
View uploaded songs
View analytics per song including:
    Total play count
    Top listener
    Most played day
    Favorite count

Artist privacy is handled using a social media disclosure flag, allowing optional Instagram/YouTube visibility.

**Music Playback Simulation:**
The system includes a text-based music player with:
Real-time progress bar
Duration tracking
Skip to next song
Replay functionality
Favorite integration
Listening history tracking
Automatic analytics updates

Playback interactions update:
play_count
listening_history
Favorites table
Artist statistics

**Database Design:**
The system uses a normalized relational schema including:
USERS (base authentication table)
LISTENERS (role extension)
ARTISTS (role extension)
ALBUMS
SONGS
PLAYLISTS
PLAYLIST_SONGS (many-to-many)
FAVORITES (many-to-many)
LISTENING_HISTORY (analytics & tracking)

The design supports:
Role-based modeling
Album-song hierarchy
Playlist relationships
Real-time analytics
Privacy controls
Data integrity via primary & foreign keys

**Analytics Features:**
RevPlay computes real-time analytics using SQL aggregation:
Total plays per song
Most active listener
Most played day
Favorite count
Artist-level performance metrics

This demonstrates practical use of:
GROUP BY
COUNT
ORDER BY
PL/SQL procedures

**Technology Stack:**
Layer	            Technology
Language	        Java 17
Database	        Oracle SQL
Persistence	        JDBC
Procedures	        PL/SQL
Build Tool	        Maven
Testing	            JUnit 5
Logging	            Log4J
Version Control	    Git

**Key Design Principles Demonstrated:**
Layered architecture
Role-based system design
Database normalization
Many-to-many relationships
Analytics-driven queries
Privacy-aware feature modeling
Clean separation of concerns
Interview-ready system modeling

**Project Highlights:**
✔ Full authentication system
✔ Security question validation
✔ Album-based song organization
✔ Playlist management
✔ Search functionality
✔ Music playback simulation
✔ Favorites system
✔ Listening history tracking
✔ Song analytics
✔ Social media privacy control

**Why This Project Stands Out:**
RevPlay goes beyond simple CRUD operations and demonstrates:
Real-world relational database modeling
Feature-rich backend logic
Analytics computation
User interaction simulation
Extendable architecture
Production-style modular design

This project is designed to be:
Easily explainable in technical interviews
Extendable into a web or microservice architecture
A strong demonstration of backend engineering fundamentals

**Future Enhancements:**
Trending songs section
Artist follower system
Revenue simulation
Advanced search (genre, album, artist)
REST API conversion
Microservice architecture migration

 **Author:**
Developed as a backend-focused, system-design-oriented project to demonstrate practical database integration, Java application architecture, and feature-rich console interaction.