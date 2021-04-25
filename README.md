# Core

Boilerplate Library for MVVM Architecture using Koin DI and ViewBinding

## Features

### CoreApp

Application with ready module inclusions.

```
val testMod = module {

    scope<TestActivity> {}
    scope<TestViewModelActivity> {}
    scope<TestFragment> {}
    scope<TestViewModelFragment> {}
    viewModel { TestViewModel() }
}

class TestApp : CoreApp() {
    override val moduleList: List<Module> = listOf(testMod)
}
```

### CoreActivity

Activity with ViewBinding included.

```
    override fun createBinding(): CoreTestLayoutBinding =
        CoreTestLayoutBinding.inflate(layoutInflater)

    override fun CoreTestLayoutBinding.bind() {
        textView.text = "CoreActivityTest"
    }
```

### CoreViewModelActivity

CoreActivity with ViewModel ready to be injected using Koin.

```
    override val vm: TestViewModel by viewModel()
```

### CoreFragment

Fragment with ViewBinding included.

```
    override fun createBinding(container: ViewGroup?): CoreFragmentTestLayoutBinding =
        CoreFragmentTestLayoutBinding.inflate(layoutInflater, container, false)

    override fun CoreFragmentTestLayoutBinding.bind() {
        fragmentTextView.text = "CoreFragment"
    }
```

### CoreViewModelFragment

CoreFragment with ViewModel ready to be injected using Koin.

```
    override val vm: TestViewModel by viewModel() // or sharedViewModel()
```

### CoreService

Based on ScopeService of Koin DI. This class implements LifecycleService.

## Gradle API Implementations

- Coroutines BOM (1.4.2)
- Koin DI (2.2.2)
- Android LiveData (2.3.0)
- Android ViewModel (2.3.0)
- Android State ViewModel (2.3.0)
- Android LifecycleService (2.3.0)

## Install

### Manual

- Clone the Repository.
- Copy the "core" module to your project.

### Jitpack

Add in Root build.gradle

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add in dependencies

```
implementation 'com.github.HirogaKatageri:core:<tag>'
```
