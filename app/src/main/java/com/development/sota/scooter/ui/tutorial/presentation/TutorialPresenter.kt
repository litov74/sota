package com.development.sota.scooter.ui.tutorial.presentation

import android.content.Context
import android.os.Bundle
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.tutorial.domain.TutorialInteractor
import com.development.sota.scooter.ui.tutorial.domain.TutorialInteractorImpl
import moxy.DefaultViewState
import moxy.MvpPresenter

class TutorialPresenter(val context: Context) : MvpPresenter<TutorialView>(), BasePresenter {
    private val interactor: TutorialInteractor = TutorialInteractorImpl(this)

    fun onNextButtonClicked(index: Int) {
        if (index == 5) {
            closeTutorial()
        } else {
            viewState.nextPage(index + 1)
        }
    }

    fun onBackClicked(index: Int) {
        if (index-1 >= 0) {
            viewState.nextPage(index - 1)
        }
    }

    fun onSkipButtonClicked() {
        closeTutorial()
    }

    private fun closeTutorial() {
        interactor.setSuccessfulFlag()
        viewState.finishActivity()
    }

    override fun onDestroyCalled() {}

}