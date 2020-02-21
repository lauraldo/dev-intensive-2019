package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.getThemeColor
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_ABOUT
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_FIRST_NAME
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_LAST_NAME
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_NICKNAME
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_RANK
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_RATING
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_REPOSITORY
import ru.skillbranch.devintensive.models.Profile.Companion.FIELD_RESPECT
import ru.skillbranch.devintensive.ui.custom.TextDrawable
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel
    private var isEditMode = false

    private lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity", "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme")
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
            tv_nick_name.text = profile.nickName
            if (profile.firstName.isNotBlank() || profile.lastName.isNotBlank()) {
                iv_avatar.setImageDrawable(
                    TextDrawable(
                        Utils.toInitials(profile.firstName, profile.lastName)!!,
                        getThemeColor(R.attr.colorAccent)
                    )
                )
            } else {
                iv_avatar.setImageResource(R.drawable.avatar_default)
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
            FIELD_NICKNAME to tv_nick_name,
            FIELD_RANK to tv_rank,
            FIELD_RATING to tv_rating,
            FIELD_RESPECT to tv_respect,
            FIELD_FIRST_NAME to et_first_name,
            FIELD_LAST_NAME to et_last_name,
            FIELD_ABOUT to et_about,
            FIELD_REPOSITORY to et_repository
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) {
                if (wr_repository.error != null) {
                    et_repository.setText("")
                    et_repository.requestFocus()
                    return@setOnClickListener
                }
                saveProfileInfo()
            }
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        et_repository.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!Utils.isGithubUrlValid(p0.toString())) {
                    wr_repository.error = getString(R.string.invalid_repository_url)
                } else {
                    wr_repository.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun showCurrentMode(editMode: Boolean) {
        val infoItems = viewFields.filter {
                            setOf(FIELD_FIRST_NAME, FIELD_LAST_NAME, FIELD_ABOUT, FIELD_REPOSITORY)
                                .contains(it.key)
                        }

        for ((_, v) in infoItems) {
            v as EditText
            v.isFocusable = editMode
            v.isFocusableInTouchMode = editMode
            v.isEnabled = editMode
            v.background.alpha = if (editMode) 255 else 0
        }

        iv_preview_mode.visibility = if (editMode) View.GONE else View.VISIBLE

        wr_about.isCounterEnabled = editMode

        with(btn_edit) {
            val filter: ColorFilter? = if (editMode) {
                PorterDuffColorFilter(resources.getColor(R.color.color_accent, theme),PorterDuff.Mode.SRC_IN)
            } else {
                null
            }
            val icon = if (editMode) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else{
                resources.getDrawable(R.drawable.ic_edit, theme)
            }
            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile (
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }
}
