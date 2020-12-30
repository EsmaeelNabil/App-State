# App-State

**Network and Activity State Providers**

- Network State      : get the current state from anywhere without ~~Context~~  using a global variable `appHasNetwork`.
- Actvivty Provider  : get the current Actvivty with a global variable `currentActivity` 

## Including in your project
[![](https://jitpack.io/v/EsmaeelNabil/App-State.svg)](https://jitpack.io/#EsmaeelNabil/App-State)
### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
    	....
	maven { url 'https://jitpack.io' }
    }
}
```
And add a dependency code to your **APP**'s `build.gradle` file. [![](https://jitpack.io/v/EsmaeelNabil/App-State.svg)](https://jitpack.io/#EsmaeelNabil/App-State)
```gradle
dependencies {
	  implementation 'com.github.EsmaeelNabil:App-State:0.1'
}
```
## Usage - Features

##### [Network State Handler](https://github.com/EsmaeelNabil/App-State/blob/master/stateLib/src/main/java/com/esmaeel/statelib/NetworkStateHandler.kt)
``` kotlin
class AppClass : Application() {
   
    override fun onCreate() {
        super.onCreate()

        //use the Ktx function .
        initNetworkStateHandler()

    }
}
```
###### Unregistering the callback
 - if `initNetworkStateHandler` already initialized the callback,
 - the initialized callback will be returned and no overriding will take place.
``` kotlin
    unregisterNetworkStateHandlerCallback(initNetworkStateHandler())
```
    

###### Now you can Access the global realtime updated value, anywhere in your app
`val hasInternet = appHasNetwork`


##### [Activity Provider , Listener](https://github.com/EsmaeelNabil/App-State/blob/master/stateLib/src/main/java/com/esmaeel/statelib/ActivityProvider.kt)
``` kotlin
class AppClass : Application() {
   
    override fun onCreate() {
        super.onCreate()

        registerActivityTracker()
        
        // OR

        registerActivityTracker { activity ->
            /*
            onStateChanged gives you an instance of the current activity
            on every state `lifecycle` change.
            */
        }

    }
}
```

###### Or with optional Callbacks
``` kotlin

registerActivityTracker(
            onActivityCreatedCallback = { activity, savedInstanceState -> },

            onActivityStartedCallback = { activity -> },

            onActivityPausedCallback = { activity -> },

            onActivityResumedCallback = { activity -> },

            onActivityStoppedCallback = { activity -> },

            onActivitySaveInstanceStateCallback = { activity, outState -> },

            onActivityDestroyedCallback = { activity -> },

            onStateChanged = {
                // gives you an instance of the current activity on every state `lifecycle` change
            },
        )
```


###### Now you can Access the global realtime updated value anywhere in your app
`val current = currentActivity`

