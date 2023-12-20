Functionalities: 
1. login/registration
2. view profile with username, followers, following and posts
3. creating a post - IMAGE: upload or select from gallery (he said either way is fine but it would be best if we could do both)
4. feed [IMPORTANT] - viewing posts from profiles you follow.
this could be done with an infinite scroll, we can load 10 by 10 (sql query would be paging/selecting 10 by 10 from posts (from profiles we follow) ordered by date); as we scroll down, load 10 more and unload the 10 previous, and as we scroll up, the opposite. he said we could also implement it like pages: load 10 and then get to the bottom of the page and then click page 2 etc. both are fine, whatever we prefer
5. searching for profiles (users) - definitely to be done in the database, not in java android
6. commenting, liking posts
7. following, unfollowing
6. "chats"/messaging with mqtt. recommended way: have one topic per user, and subscribe that user to the topic. when user A wants to message user B, user A sends a message to topic A (A's topic, this user is subscribed to it). in the message there is information on the sender. he said we don't have to store it in the database. 


Recommendations:
- use FIREBASE (firebase real-time database) insted of firestore. i'm not 100% sure but i believe he said it's because firestore has support for SQL and we would need it for the project.

Report:
1. Small desription of the project, briefly describe the functionalities
2. Database diagram and explanation to why we designed them the way we did
3. Protyping - basically screenshots and their descriptions
4. Architectural implementation decisions (this is really important). Why we did some things the way we did. Definitely explain why we didn't use internal database but rather firebase - because (his words, paraphrased) we believe information should be remotely accessible. Also, note that we aren't using threads implemented manually, because firestore already has that (something like that), and state a page or a quote from the developers to confirm that).
5. Testing (not mandatory). From what i got from him, this is pretty informal. He said we can get someone to use (test) our application, e.g. 5 features (do 5 tasks, like upload a post) and then see how much time it took that person to do each. We can also write their comments and say how their feedback made us improve some parts of the UI ets. If we do this. we need to describe the profile of the person (age, are they in the field, how comfortable they are with technology etc.)


Other:
- he said we can use both our own autentification, or firebase's autentification. we don't have to worry about autentification of each "request", the login/registration are basically there just to confirm the user's identity and retreive the data related to him
- we shouldn't focus as much on the UI, we should focus on the implementation
 	