package com.bzh.dytt

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

open class BaseViewModel @Inject constructor(var dataRepository: DataRepository) : ViewModel()
