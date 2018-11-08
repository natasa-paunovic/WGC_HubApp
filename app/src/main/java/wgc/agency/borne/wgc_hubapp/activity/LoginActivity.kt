package wgc.agency.borne.wgc_hubapp.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.text.Editable
import android.text.TextWatcher
import wgc.agency.borne.wgc_hubapp.R

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import wgc.agency.borne.wgc_hubapp.extension.setInvisible
import wgc.agency.borne.wgc_hubapp.extension.setVisible


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prepareOnClickListeners()
        checkIsFilledData()

    }

    private fun checkIsFilledData() {
           emailEdit.addTextChangedListener(object : TextWatcher {
               override fun afterTextChanged(s: Editable?) {
                   if(emailEdit.text.toString().trim().isNotEmpty() && passwordEdit.text.toString().trim().isNotEmpty()){
                     enableButton()
                   } else{
                       disableButton()
                   }

               }

               override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }

               override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }
           })

        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                    if(emailEdit.text.toString().trim().isNotEmpty() && passwordEdit.text.toString().trim().isNotEmpty()){
                        enableButton()
                    } else
                        disableButton()

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun disableButton() {
        loginButton.isEnabled=false
        loginButton.background= ContextCompat.getDrawable(this, R.drawable.signin_disable)
        forgotPasswordText.setInvisible()
    }

    private fun enableButton() {
        loginButton.isEnabled=true
        loginButton.background= ContextCompat.getDrawable(this, R.drawable.signin_enable)
        forgotPasswordText.setVisible()
    }


    private fun prepareOnClickListeners() {
//        signUpText.setOnClickListener {
//            startActivity(SignUpActivity.intent(this))
//        }

        forgotPasswordText.setOnClickListener {

        }

        loginButton.setOnClickListener {
            progressView.setVisible()
            loginCall()
        }


    }

    private fun loginCall() {
//        val request = LoginRequest(emailEdit.text.toString().trim(), passwordEdit.text.toString())
//        startService(ApiService.intent(this, ApiService.LOGIN_REQUEST, request))
    }

}
