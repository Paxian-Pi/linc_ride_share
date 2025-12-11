APK File Link (Downloadable): https://drive.google.com/file/d/1AqmQuzprJN2wHNjOFa1pEaCiiElihBK6/view?usp=sharing

Build Instructions:
1. Clone the repository
2. Add you valid Google API Key to the Androidmanifest and at line 196 of the GoogleMapView.kt file
3. Sync the Gradle
4. Connect you device and run the app

App Instructions:
1. Launch the app
2. Click the "Offer Ride" button (App pushes to "GetToPickup" screen)
3. Wait for "driver to pickup" simulation to complete (App automatically routes to the "RiderIsArriving" screen)
4. Notice the Countdown Timer
5. Now slide right to begin "pickup to destination" simulation
6. Wait for simulation to finish (App automatically routes to the "TripEnded" screen)
7. Click on close button on the to left corner of the screen to reset

The MVVM architecture was used for the following reasons:
1. Clear Separation of Concerns
2. Great for Reactive & Declarative UI Frameworks
3. Reusability of Logic
4. Better Scalability
5. Cleaner, More Maintainable Codebase
