package br.com.natanaelribeiro.events.views.acitivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.events.R
import br.com.natanaelribeiro.events.viewmodels.ListOfEventsViewModel
import br.com.natanaelribeiro.events.views.adapters.EventAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list_of_events.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListOfEventsActivity : AppCompatActivity() {

    private val listOfEventsViewModel by viewModel<ListOfEventsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_events)

        prepareUI()
        prepareViewModel()
    }

    override fun onResume() {
        super.onResume()

        listOfEventsViewModel.getEvents()
    }

    private fun prepareUI() {

        TransitionManager.beginDelayedTransition(rootLayout)
    }

    private fun prepareViewModel() {

        listOfEventsViewModel.apply {

            viewState.observe(this@ListOfEventsActivity, Observer { vs ->

                vs?.let { render(it) }
            })

            command.observe(this@ListOfEventsActivity, Observer { cmd ->

                cmd?.let { triggerCommand(it) }
            })
        }
    }

    private fun render(viewState: ListOfEventsViewModel.ViewState) {

        when (viewState.isLoadingEvents) {

            true -> {

                loadingLayout.visibility = View.VISIBLE
            }

            false -> {

                loadingLayout.visibility = View.GONE
            }
        }
    }

    private fun triggerCommand(command: GenericCommand) {

        when (command) {

            is ListOfEventsViewModel.Command.ShowErrorLoadingList -> {

                AlertDialog.Builder(this@ListOfEventsActivity)
                    .setTitle(R.string.event_details_alert_title)
                    .setMessage(R.string.event_details_alert_message)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        listOfEventsViewModel.getEvents()
                    }
                    .setNegativeButton(android.R.string.no) { _, _ ->
                        finish()
                    }
                    .show()
            }

            is ListOfEventsViewModel.Command.ShowListOfEvents -> {

                val eventAdapter = EventAdapter(command.eventsList, this@ListOfEventsActivity) { clickedEvent ->

                    val intent = Intent(this@ListOfEventsActivity, EventDetailsActivity::class.java)
                    intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, clickedEvent.id)
                    intent.putExtra(EventDetailsActivity.EXTRA_EVENT_TITLE, clickedEvent.title)

                    startActivity(intent)
                }

                eventsRecyclerView.layoutManager = LinearLayoutManager(this@ListOfEventsActivity)
                eventsRecyclerView.adapter = eventAdapter
            }
        }
    }
}
