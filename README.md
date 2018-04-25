# Warren Chat Demo app
This project is just a demonstration of [Warren](https://oiwarren.com/) app chat for android. It's accessing some available endpoints to provide the chat experience of discovering the user profile. 
The current project have support for Android Jelly Bean version (sdk version 16, version number 4.1).

----

### Setting up
To run the project, just clone the project using git and import into Android Studio. To install, just download the sdk available in [releases Github page](https://github.com/pablobaldez/WarrenChatDemo/releases).

----

### Implementation
It was implemented using MVP (a good article about it is available [here](https://antonioleiva.com/mvp-android/)) and concepts about [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html). The main libraries not available in android suppoprt packages used here was:

- [Retrofit 2](http://square.github.io/retrofit/): to make request calls to endpoints
- [RxJava 2](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid): to develop using the reactive programming paradigm
- [Dagger 2](https://github.com/google/dagger): to use dependency injection design pattern

Outside of the application code, we have also two unit test classes:
- MessageSplitterTest: A class responsible to test the splitting of the delays and texts to be shown
- ChatPresenterTest: A simple test class implemented to show the usage of [Mockito](https://github.com/mockito/mockito) and [Hamcrest](https://github.com/hamcrest/JavaHamcrest) libraries.

----

### Package structure
The project was structured to organize the layers of application following the Clean Architecture principles. You can find the following packages in this version:
- utils: just utilitary classes. Here i'm avoiding imports or references to another project packages.
- model: the lower representation of information in the system. Here you can find the simple POJO classes, there is no logic implementation and this package can't import or make some reference to another project package. To help me, there is an anti-pattern because of some imports of Gson annotations.
- presenters: all presenter classes, classes to help the presenters or classes to be sent to view layer. All classes in this package can import just from model or util package
- views: classes to represent and controls the UI of application. Can import and make reference of any package
- api: classes to access the api; Just retrofit imports
- injection: classes to handle the project dependency injection
