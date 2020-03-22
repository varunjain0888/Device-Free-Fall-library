# Device-Free-Fall

# Steps to use this library
1. Extends your activity with "Detection Activity"

2. Add below dependency in your app level build.gradle:- 
    implementation 'com.varunjain.android:devicefreefall:1.0.0'
    
3. Use the below snipet to get the free fall count in your activity:-
    
    countViewModel.countRepo.observe(this, Observer {
    textView.text = "Total Fall Count ${it}"
    })
    
# Other library used in this are:-     
1. Room Database
2. GreenDao Event Bus
3. Kotlin lifecycle extention

# Next feature in development:-
1. Calculating distance from what height the device falls down.
