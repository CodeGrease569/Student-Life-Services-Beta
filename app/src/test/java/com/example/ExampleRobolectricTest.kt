package com.example

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.example.studentlife.activities.LoginActivity
import com.google.android.material.button.MaterialButton
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Student Life", appName)
  }

  @Test
  fun `login activity contains required authentication components`() {
    val activity = Robolectric.buildActivity(LoginActivity::class.java).setup().get()
    assertNotNull(activity)

    val etIdOrEmail = activity.findViewById<EditText>(R.id.etLoginIdOrEmail)
    val etPassword = activity.findViewById<EditText>(R.id.etLoginPassword)
    val btnSignIn = activity.findViewById<MaterialButton>(R.id.btnLoginSubmit)
    val cbRememberMe = activity.findViewById<CheckBox>(R.id.cbRememberMe)
    val tvForgotPassword = activity.findViewById<TextView>(R.id.tvForgotPassword)
    val tvGoToSignUp = activity.findViewById<TextView>(R.id.tvGoToSignUp)
    val ivTogglePassword = activity.findViewById<ImageView>(R.id.ivTogglePassword)

    assertNotNull("Student ID or Email input must exist", etIdOrEmail)
    assertNotNull("Password input must exist", etPassword)
    assertNotNull("Sign in button must exist", btnSignIn)
    assertNotNull("Remember Me checkbox must exist", cbRememberMe)
    assertNotNull("Forgot Password link must exist", tvForgotPassword)
    assertNotNull("Sign Up link must exist", tvGoToSignUp)
    assertNotNull("Password toggle icon must exist", ivTogglePassword)

    // Verify initial password input type is password
    assertTrue(etPassword.transformationMethod is PasswordTransformationMethod)

    // Toggle to visible
    ivTogglePassword.performClick()
    assertTrue(etPassword.transformationMethod is HideReturnsTransformationMethod)

    // Toggle back to hidden
    ivTogglePassword.performClick()
    assertTrue(etPassword.transformationMethod is PasswordTransformationMethod)
  }
}

