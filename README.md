# Celo
Application for downloading and presenting random users

The following application was developed within 3 days to meet the following requirements
* Extract random user information from REST API
* Display information on users
* Implementation of pagination
* Filter/Search
* SQLite DB operations
* Detailed view

The application should meet each of these requirements.

Incomplete components due to time restriction
* Unit and UI testing
* UI design principles (information is presented in a bland way)
* No offline downloading of images
* ORM such as Room Persistence Library not implemented which would have helped in designing for MVP pattern
* Keys on database

###Recycler View of Users###
This shows the search bar as well as users loaded in currently with their corresponding thumbnail.
The search bar will only filter the list based on the first name at the moment.
![picture alt](https://raw.githubusercontent.com/sanathss/Celo/master/detailView.png)


###Detailed View of User###
As seen, the UI contains important information but does not have labels or a pleasing design yet.
![picture alt](https://raw.githubusercontent.com/sanathss/Celo/master/recyclerView.png)
