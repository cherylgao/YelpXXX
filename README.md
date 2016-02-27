# Pley

The Pley is a restaurant/business search application similar to Yelp but offers more features such as dish recommendation and random restaurant select
we import Yelp API for general restaurant information database and Google API for location services. We also create our own server and database for customer review and related information services.

UI Design:
the app contains 5 main activities: activity_main, activity_detail, activity_lucky, activity_restaurant_list and activity_review. Main acitivity has two functions which links to a list of restaurant based on either user's current location or user's search. Moreover, if user not sure what to search, he can try the lottery option to get a randomly selected restraurant. In the listView of restaurant list, user can click on the item and jump to the restaurant detail activity that provides more informations. User can choose to write a review and submit to our server, then everyone use this app will be able to see the review content.

Backend:
-we initiate a YelpAPI by using apiFactory.createAPI().
Under the searching mechanism, we send get request which specifying some criterias(location, name, return list size) to YelpAPI and get a list of Business objects from the server. The Business class contains many useful information of each business(name, address, picture, location, etc) for the app to use.
-GoogleAPI is created thru GoogleAPIClient.Builder() and is connected to google server by overriding onStart().
LocationService.FusedLocationAPI.getLastLation(mGoogleApiClient) return the last known location(usually as known as the current location) of the user. The return object has the function getLatitude() and getLongitude() which return the Double of latitude and longitude. We send the coordinates to YelpAPI instead of a specific city name and will be able to get a list of Business objects. 
-communication between each activity is done by intent.putExtra()
