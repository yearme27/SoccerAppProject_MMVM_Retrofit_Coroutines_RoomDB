# Soccer App

An Android app (Kotlin, MVVM, Navigation component) for browsing soccer leagues, their seasons and the standings for a season. Data comes from the free `api-football-standings.azharimm.site` API.

## Flow

1. **Start page**: list all leagues, or type a league abbreviation (for example `eng.1`) to jump straight to its seasons.
2. **Leagues**: tap a league to see its seasons.
3. **Seasons**: tap a season to see its standings (wins, losses, draws, games played, points).

## How it is built

- `SoccerViewModel` exposes one `LiveData<UIState>` per screen (`Loading`, `Success`, `Error`). The fragments call `loadLeagues()`, `loadSeasons(id)` and `loadStandings(season, id)`, which skip the request when that data is already loaded.
- `SoccerRepositoryImpl` wraps the Retrofit calls in `SoccerService` and turns them into a `Flow<UIState>`.
- `DI` builds Retrofit and OkHttp by hand. HTTP body logging is only on in debug builds.
- Standings stats are matched by name (`wins`, `losses`, `ties`, `gamesPlayed`, `points`), not by position.

## Setup

Open the project in Android Studio, let Gradle sync, and run it on a device or emulator (`minSdk` 26). The app needs internet access.

If every screen shows an error, check that the API host is still up. It is a free community API.
