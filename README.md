# "Core" a simple library to setup MVVM using ViewBinding and Koin <!-- omit in toc --> 

[ ![Download](https://api.bintray.com/packages/hirogakatageri/sandbox/com.hirogakatageri.core/images/download.svg) ](https://bintray.com/hirogakatageri/sandbox/com.hirogakatageri.core/_latestVersion)

## Table of Contents <!-- omit in toc -->
- [Features](#features)
- [Setup](#setup)
- [Components](#components)
  - [CoreActivity & CoreFragment](#coreactivity--corefragment)
    - [Activity](#activity)
    - [Fragment](#fragment)
  - [CoreActivityViewModel & CoreFragmentViewModel](#coreactivityviewmodel--corefragmentviewmodel)
  - [ThrottledOnClickListener](#throttledonclicklistener)
  - [NetworkLiveData](#networklivedata)
  - [SingleLiveEvent](#singleliveevent)
- [Extras](#extras)
  - [Quadrant (Optional)](#quadrant-optional)
  - [CoreQuadrantViewModel](#corequadrantviewmodel)

## Features

- MVVM
- [Koin](https://github.com/InsertKoinIO/koin)
- [Coil](https://github.com/coil-kt/coil)
- [TimberKt](https://github.com/ajalt/timberkt)

## Setup

*Core already exports Koin, Coil and TimberKt when added to your module.* 

##### build.gradle in Root Project <!-- omit in toc -->

```
repositories {
	maven {
		url  "https://dl.bintray.com/hirogakatageri/sandbox"
	}
}
```

##### build.gradle <!-- omit in toc -->

```
android {
    buildFeatures{
        viewBinding = true
    }
}

implementation 'com.hirogakatageri:core:0.0.1'
```

## Components

### CoreActivity & CoreFragment
##### To initialize ViewBinding just override createBinding() e.g. <!-- omit in toc -->
#### Activity

```
    override fun createBinding(): VB =
        VB.inflate(layoutInflater)
```

#### Fragment

```
    override fun createBinding(container: ViewGroup?): VB =
        VB.inflate(layoutInflate, container, false)
```

### CoreActivityViewModel & CoreFragmentViewModel
##### To initialize ViewModel using Koin e.g. (See more in Koin Documentation) <!-- omit in toc -->
```
    import org.koin.androidx.viewmodel.ext.android.viewModel

    override val viewModel: TheViewModel by viewModel()
```

### ThrottledOnClickListener
##### Creates a new instance of an OnClickListener which blocks input after the first input for a set amount of time (Default: 400ms). <!-- omit in toc -->

```
    ThrottledOnClickListener.Builder(
        lifecycleOwner = lifecycleOwner,
        delayMs = 400,
        views = listOfViews
        view = view,
        onClick = { view ->
            when (view?.id) {
                R.id.some_view -> {
                    // Do something
                }
            }
        }
    ).build()
```

### NetworkLiveData
##### A LiveData for that emits device connection status... <!-- omit in toc -->

### [SingleLiveEvent](https://github.com/android/architecture-samples/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java)
##### A MutableLiveData for handling UI stuff when needed... <!-- omit in toc -->

## Extras

### [Quadrant](https://github.com/gaelmarhic/Quadrant) (Optional) 
##### A Gradle plugin for Android that makes navigation easy in multi-module projects. <!-- omit in toc -->

```
buildscript {

    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }

    dependencies {
        classpath "gradle.plugin.com.gaelmarhic:quadrant:1.4"
    }
}
```

### CoreQuadrantViewModel
##### A ViewModel with a **quadrant** named field used to pass a Pair<String, Bundle>. <!-- omit in toc -->