# 🎵 RevPlay – Console-Based Music & Podcast Streaming Platform

## 🚀 Overview

RevPlay is a fully functional console-based music and podcast streaming application built using:

- Java 17
- JDBC
- Oracle PL/SQL
- Maven
- JUnit
- Log4J
- Git

The system follows a layered architecture and demonstrates real-world backend design concepts including authentication, role-based access control, relational database modeling, analytics tracking, and interactive playback simulation.

---

## 🎯 Core Features

### 👂 Listener Features
- User registration & login (with security question validation)
- Search songs dynamically (keyword-based)
- Browse songs, albums, artists
- Play music with:
    - Progress bar
    - Pause / Resume
    - Skip
    - Repeat
    - Stop
- Add songs to favorites
- View listening history (timestamped)
- Create & manage playlists (public/private)
- Explore and play podcasts
- View artist profiles

---

### 🎤 Artist Features
- Artist registration with optional social media disclosure
- Create albums
- Upload songs into albums
- Create podcasts
- Upload podcast episodes
- View profile with:
    - Total songs
    - Total duration
    - Genre
    - Social media info
- View detailed song analytics:
    - Play count
    - Most active listener
    - Most played day
    - Favorite count

---

## 🏗 Architecture

RevPlay follows a modular layered design:

- Presentation Layer (Console UI)
- Service Layer (Business Logic)
- DAO Layer (Database Access)
- Database Layer (Oracle SQL + PL/SQL)

The architecture ensures separation of concerns and future extensibility to web or microservices architecture.

---

## 🗄 Database Design

The system uses a normalized relational database with:

- Users (Listener / Artist roles)
- Songs & Albums
- Playlists
- Favorites
- Listening History
- Podcasts & Episodes
- Podcast History

Foreign key constraints and junction tables are used to maintain data integrity.

---

## 🎵 Playback Engine

RevPlay includes a fully simulated playback engine featuring:

- Real-time progress bar (MM:SS format)
- Interactive pause menu
- Skip to next track
- Repeat
- Add to favorites while playing
- Automatic next track playback

The same engine is reused for podcast episodes.

---

## 📊 Analytics

Artists can view advanced analytics for their songs:

- Total plays
- Most active listener
- Most played day
- Favorite count

This demonstrates practical database aggregation queries and reporting.

---

## 🧪 Testing

JUnit test cases validate:

- User registration
- Artist registration
- Security question storage
- Authentication logic
- Database consistency

---

## 💡 Design Highlights

- Clean layered architecture
- Proper DAO separation
- Role-based menu flow
- Scalable ER design
- Extensible podcast module
- Minimal Java file structure (≤ 20 files)
- Professional CLI UX

---

## 🔮 Future Improvements

- Recommendation engine
- Subscription tiers
- Trending charts
- Microservices refactor
- REST API version
- Web UI integration

---

## 🎓 Project Purpose

RevPlay demonstrates backend system design principles in a real-world streaming application scenario.  
It showcases database modeling, layered architecture, analytics implementation, and interactive CLI UX simulation.

---

## 🏁 Conclusion

RevPlay is a complete, scalable, and extensible streaming backend simulation that mirrors core functionality found in real-world platforms like Spotify and Apple Music — built entirely as a console-based Java application.
