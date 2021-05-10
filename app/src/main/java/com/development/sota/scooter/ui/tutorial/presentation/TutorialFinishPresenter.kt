package com.development.sota.scooter.ui.tutorial.presentation

import android.content.Context
import android.os.Bundle
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.tutorial.domain.TutorialInteractor
import com.development.sota.scooter.ui.tutorial.domain.TutorialInteractorImpl
import moxy.DefaultViewState
import moxy.MvpPresenter

class TutorialFinishPresenter(val context: Context) : MvpPresenter<TutorialFinishView>(), BasePresenter {


    fun onNextButtonClicked(index: Int) {

        if (index == 2) {
            closeTutorial()
        } else {
            viewState.nextPage(index + 1)
        }
    }

    fun onSkipButtonClicked() {
        closeTutorial()
    }

    private fun closeTutorial() {

        viewState.finishActivity()
    }

    override fun onDestroyCalled() {}

}