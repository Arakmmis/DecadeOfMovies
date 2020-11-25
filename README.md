# Decade Of Movies

A movie details viewing app developed using the MVVM architecture, LiveData, Room, and Koin.

## Installation

There are no special or additional requirements to run this project. Simply clone and import it into Android Studio and Run.

## Basic Structure of the App

```markdown
- com.movies.decade
  - businesslogic
    - models
  - di
  - statemodels
  - utils
  - splash
  - movieslist
    - adapter
  - moviedetails
    - adapter
```

## Breakdown of the Structure

```markdown
  - businesslogic
    - models
```
Contains all things that concern the data layer. Both repositories and POJOs.

```markdown
  - statemodels
```
Dependency Injection Module for Koin.

```markdown
  - utils
```
Helper classes and files

```markdown
  - splash
  - movieslist
    - adapter
  - moviedetails
    - adapter
```
Activities, Adapters, and ViewModels that concern the view layer.

## Development of the App

> An MVVM approach was used to structure the classes inside the app.
  - The view layer is composed of Activities and RecyclerView Adapters.
  - A ViewModel handles the communication between the view and the data source.
  - The data layer is hidden behind a Manager class which takes care of retrieval of data from either the offline database Room or from the provided JSON file, as well as using retrofit to request each movie's images.
  
> The app structure conforms to MVVM architecture norms in terms of data flow.
  - Every outer layer can only be updated by subscribing to a state object of the subsequent inner layer,
  - Every layer is only concerned with what it should do to update its state properly so that subscribers can receive their updates.
  
> The database is being treated as the single source of truth, so every request for data goes through it first to check if it's populated or not.
  - The Manager class when queried for data checks first if there is a list to be returned from the database.
  - If there isn't, the movies list is retrieved from the JSON file.
  - A request is then fired to fetch 10 images for every movie.
  - Finally, the list is inserted into the database.
  
> Searching and Sorting
  - The first fetching of the movies list is a request from the database which returns the list sorted by year, then rating (both descending), then alphabetically (ascending).
  - The retrieved list is stored in a member field in the ViewModel and all subsequent searches are done on this field.
  - The results of the search are sorted in the same order as the database.
  - I chose this approach to minimize the number of I/O operations that would have to be done on the database to query the user's searches. Also, I concluded that this would be much faster as the data I'm dealing with is capped at 2k entries.
  
## Issues Faced

> LiveData
  - A huge problem with LiveData was its inability to be observed without passing a LifecycleOwner to it. So I had to use the MediatorLiveData and Transformations to be able to work around that issue.
  - One other issue was the non-availability of a CallAdapter for LiveData in Retrofit, so I had to resort to using RxJava instead.
  
> Initial Load Time
  - On first launch, the app takes around 10-15s to load, which puts if the user continuously tries to use it by clicking on the screen, an ANR dialog is displayed by the system.
  - I didn't manage to get to find a way to solve it in the remaining time that I had, but if I could have another go at it, I would start by tackling the most important bottleneck I see, which is; the requests to Flickr API to get the images for every movie upon launch.
  - I would probably hold off on firing the requests and risk displaying to the user the list of movies with placeholder images to not stall the user on startup.
  
## Overall Impressions

This was frankly a very interesting challenge, and I've learned a lot by doing it as it was a chance for me to use all the components in it together for the first time.

Thank you for your time and sorry if this was too long.
