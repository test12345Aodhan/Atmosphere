WHat the app should do.

1) Venue side - sign in and register (This is done)
(ability to for venues to follow other venues is done)
(SIgn in and register is done)
(Edit profile was working but image does not change, name does sometimes)
(notifications need worked on)

(ability to post image and decription shows on firebase under Posts but cannot see these posts. may be issue with storage section of firebase)
2) home fragment - show all posts of venues who follow other venues (recycler adapter issue)
3) these posts should be able to be liked and saved by the venue (no ability to comment as i didnt want negativity)
4) add stories on home page - these stories should be video (if possible) but if not then just images
5) search - basic search for venues and once clicked on takes you to that venues page(not taking me to the venues profile page, just to the signed in persons)
6) when on their page it should be view only (so no saved images button, no edit button no happy notification button)
6) on the signed in Venues profile page - should be able to see info about the venue including their tags 
and add see saved posts and that venues photos and see the eidt profile and happy hour notif button
7) if you click on the photo it shows you the whole post details (likes amount)
8)edit profile of venue - adding location (if possible to get co-ordinates from string and store in firebase database)
9) happy hour button needs done - so where a venue clicks it and sets a time it notifies all followers be it user(customer)
or another venue
10) be able to see followers and following when clicked on profile page - just same layout as search
11) ability to edit your posts and delete
12) edit profile is where you upload profile image, add address and change venue tags


(Have not been able to even look at customer side until venue side is done, however it would be a lot of copy and pasting)
(want customer side UI to be more like uberEats etc)
12) customer side - can view stories and posts on home frag. 
13) Customer cannot create posts and stories
14) customer side is viewing only
15) so customer can follow venues and look at their profile
16) customer search frag - search for venues based on name or venue tag
17) tags frag needs added which shows all venues where their tags = to the category selected
18) get customer current location and search for venues within 5 miles etc
19) customer has no profile page
20) need to add a side bar when it just shows their image and logout or delte account
21) venues and customers to receiver any notifications
22) customers to receiver happy hour notification and turn it off is possible
23) issue with any images uploaded from emulator - on firebase storage they are ending with .null instead of jpg etc
24) then if possible add suggest venues based on following list - so suggest a random venue to follow based on this


24) need to do a couple of junit tests