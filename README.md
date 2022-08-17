# Movies List App

Movie List, is an App  that provides list/detail view of movie items.

# App features

Showing list of movies with pagination using [discover/movie](https://api.themoviedb.org/3/discover/movie). <br />
Search with pagination using [search/movie](https://api.themoviedb.org/3/search/movie). <br />
Showing movies genre [genre/movie/list](https://api.themoviedb.org/3/genre/movie/list). <br />
Filtering movies by genre with pagination using [discover/?with_genres={genreId}](https://api.themoviedb.org/3/discover/movie?with_genres={genreId}). <br />
Showing details of the movie item in details screen using [movie/{movieId}](https://api.themoviedb.org/3/movie/{movieId}). <br />
Mark the movies as favorite and save the user mark to local DB. <br />

# technologies and libraries:
- Kotlin
- Kotlin-coroutines / Flow / StateFlow / SharedFlow
- Dagger Hilt for providing objects over the app
- Retrofit for API communication.
- RoomDatabase: caching user favorites
- Paging3
- Navigation component
- View Binding and Data Binding
- Glide for loading data from the URL

# App Demo Video

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/XkeJBbZclNU/0.jpg)](https://www.youtube.com/watch?v=XkeJBbZclNU)