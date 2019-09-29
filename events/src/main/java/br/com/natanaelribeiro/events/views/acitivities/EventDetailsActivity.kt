package br.com.natanaelribeiro.events.views.acitivities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.basefeature.utils.isEmail
import br.com.natanaelribeiro.basefeature.utils.toFormattedDate
import br.com.natanaelribeiro.basefeature.utils.toFormattedPrice
import br.com.natanaelribeiro.events.R
import br.com.natanaelribeiro.events.viewmodels.EventDetailsViewModel
import br.com.natanaelribeiro.eventsdata.models.Event
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {

        val EXTRA_EVENT_ID = "br.com.natanaelribeiro.EXTRA_EVENT_ID"
        val EXTRA_EVENT_TITLE = "br.com.natanaelribeiro.EXTRA_EVENT_TITLE"
    }

    private val eventDetailsViewModel by viewModel<EventDetailsViewModel>()

    private lateinit var eventId: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var rootLayout: ViewGroup
    private lateinit var checkInButton: Button
    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var eventDescriptionTextView: TextView
    private lateinit var eventPriceTextView: TextView
    private lateinit var eventDateTextView: TextView
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        prepareUI()
        prepareViewModel()
    }

    override fun onResume() {
        super.onResume()

        if (intent.hasExtra(EXTRA_EVENT_ID)) {

            eventId = intent.getStringExtra(EXTRA_EVENT_ID)
            eventDetailsViewModel.getEventDetails(eventId)
        } else {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap?) {

        val eventLocation = LatLng(latitude, longitude)

        map?.addMarker(MarkerOptions().position(eventLocation))

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 16f))
    }

    private fun prepareUI() {

        rootLayout = findViewById(R.id.rootLayout)
        checkInButton = findViewById(R.id.checkInButton)
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout)
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView)
        eventPriceTextView = findViewById(R.id.eventPriceTextView)
        eventDateTextView = findViewById(R.id.eventDateTextView)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        configureToolbar()

        TransitionManager.beginDelayedTransition(rootLayout)

        checkInButton.setOnClickListener {

            nameTextInputLayout.isErrorEnabled = false
            emailTextInputLayout.isErrorEnabled = false

            var isAbleToCheckIn = true
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (name.isEmpty()) {
                isAbleToCheckIn = false
                nameTextInputLayout.error = getString(R.string.event_details_name_error_message)
                nameTextInputLayout.isErrorEnabled = true
            }

            if (!email.isEmail()) {
                isAbleToCheckIn = false
                emailTextInputLayout.error = getString(R.string.event_details_email_error_message)
                emailTextInputLayout.isErrorEnabled = true
            }

            if (isAbleToCheckIn) {

                eventDetailsViewModel.checkIn(eventId, name, email)
            }
        }
    }

    private fun configureToolbar() {

        supportActionBar?.hide()

        collapsingToolbarLayout.title = intent.getStringExtra(EXTRA_EVENT_TITLE)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun prepareViewModel() {

        eventDetailsViewModel.apply {

            viewState.observe(this@EventDetailsActivity, Observer { vs ->

                vs?.let { render(it) }
            })

            command.observe(this@EventDetailsActivity, Observer { cmd ->

                cmd?.let { triggerCommand(it) }
            })
        }
    }

    private fun render(viewState: EventDetailsViewModel.ViewState) {

        when (viewState.isLoadingEvent) {

            true -> loadingLayout.visibility = View.VISIBLE

            false -> loadingLayout.visibility = View.GONE
        }

        when (viewState.isDoingCheckIn) {

            true -> loadingLayout.visibility = View.VISIBLE

            false -> loadingLayout.visibility = View.GONE
        }
    }

    private fun triggerCommand(command: GenericCommand) {

        when (command) {

            is EventDetailsViewModel.Command.ShowEventDetails -> {

                updateViewWithEvent(command.event)
            }

            is EventDetailsViewModel.Command.ShowSuccessCheckInMessage -> {

                Snackbar.make(rootLayout, getString(R.string.event_details_successful_checkin_message), Snackbar.LENGTH_SHORT).show()
            }

            is EventDetailsViewModel.Command.ShowErrorLoadingEvent -> {

                AlertDialog.Builder(this@EventDetailsActivity)
                    .setTitle(R.string.event_details_alert_title)
                    .setMessage(R.string.event_details_alert_message)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        finish()
                    }
                    .show()
            }

            is EventDetailsViewModel.Command.ShowErrorDoingCheckIn -> {

                Snackbar.make(rootLayout, getString(R.string.event_details_error_doing_checkin_message), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateViewWithEvent(event: Event) {

        latitude = event.latitude.toDouble()
        longitude = event.longitude.toDouble()

        mapFragment.getMapAsync(this)

        val httpsImageUrl = event.imageUrl.replace("http", "https")

        Picasso.get()
            .load(httpsImageUrl)
            .error(R.drawable.ic_unavailable_image_clean)
            .fit()
            .into(eventImageView)

        eventDescriptionTextView.text = event.description
        eventPriceTextView.text = event.price.toFormattedPrice()
        eventDateTextView.text = event.date.toFormattedDate("dd/MM/yyyy")


    }
}