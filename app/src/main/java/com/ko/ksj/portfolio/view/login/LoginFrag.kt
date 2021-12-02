package com.ko.ksj.portfolio.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.databinding.FragmentLoginLoginBinding
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseArrayData
import com.ko.ksj.portfolio.interfaces.ResponseMessage
import com.ko.ksj.portfolio.interfaces.ResponseObjData
import com.ko.ksj.portfolio.model.Config
import com.ko.ksj.portfolio.model.DataInfo
import com.ko.ksj.portfolio.model.User
import com.ko.ksj.portfolio.repository.task.NaverUserTask
import com.ko.ksj.portfolio.repository.task.UserTask
import com.ko.ksj.portfolio.util.AESHelper
import com.ko.ksj.portfolio.util.AESHelper.AES_Decode
import com.ko.ksj.portfolio.util.EmailHelper
import com.ko.ksj.portfolio.viewmodel.LoginViewModel
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.fragment_login_login.*

class LoginFrag : Fragment() {
    val TAG = "LoginFrag"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentLoginLoginBinding

    var result = ""
    var btncheck = false
    var btncheck2 = false

    val dataInfo = DataInfo()

    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val root = inflater.inflate(R.layout.fragment_login_login, container, false)
        var time: Long = 0

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_login, container, false)
        val root = binding.root

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val now = System.currentTimeMillis()
            val result = now - time

            if (result < 2000) {
                activity?.finish()
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.common_backpress), Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.TitleIcon("", "")

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        pref = mContext.getSharedPreferences("Info", Context.MODE_PRIVATE)
        editor = pref.edit()

        setting()

//        EmailHelper(
//            resources.getString(R.string.findtopw_email_title),
//            "메일 본문입니다.",
////                binding.user?.email!!
//            "kangsukjoo1@naver.com"
//        ).sendEmail(mContext)

//        val model: MainViewModel by viewModels()
//        model.getUser().observe(requireActivity(), Observer<List<User>> { users ->
//
//        })
        Log.e(TAG, "확인1 ${binding.EDTLoginId.text.toString()} / ${binding.user} / ${binding.user?.email}")

        binding.EDTLoginId.addTextChangedListener {
            binding.user = User("", binding.EDTLoginId.text.toString(), binding.EDTLoginPassword.text.toString(), 0)
            Log.e(TAG, "확인2 ${binding.EDTLoginId.text.toString()}  / ${binding.user?.email}")

            if (binding.user!!.email.contains(" ")) {
                binding.EDTLoginId.setText(binding.user!!.email.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.error_email_no_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (!viewModel.EmailError(binding.user!!.email, mContext)) {
                    btncheck = true
                    btnChange()
//                    viewModel.live_send_string.observe(viewLifecycleOwner, Observer {
//                        binding.TVLoginIdErr.text = it
//                    })
                    binding.TVLoginIdErr.text = viewModel.live_send_string.value
                    Log.e(TAG, "확인3 ${viewModel.live_send_string.value} / ${binding.user?.email}")
                } else {
                    btncheck = false
                    btnChange()
//                    viewModel.live_send_string.observe(viewLifecycleOwner, Observer {
//                        binding.TVLoginIdErr.text = it
//                        Log.e(TAG, "확인4 ${it}")
//                    })
                    binding.TVLoginIdErr.text = viewModel.live_send_string.value

                    Log.e(TAG, "확인3 ${viewModel.live_send_string.value} / ${binding.user?.email}")
                }
            }

        }
        binding.EDTLoginPassword.addTextChangedListener {
            binding.user = User("", binding.EDTLoginId.text.toString(), binding.EDTLoginPassword.text.toString(), 0)
            if (binding.user?.password!!.contains(" ")) {
                binding.EDTLoginPassword.setText(binding.user!!.password.replace(" ", ""))
                Toast.makeText(mContext, resources.getString(R.string.error_pass_no_empty), Toast.LENGTH_SHORT).show()
                binding.invalidateAll()
            } else {
                if (!viewModel.PwError(binding.user?.password!!, mContext)) {
                    btncheck2 = true
                    btnChange()
//                    viewModel.live_send_string2.observe(viewLifecycleOwner, Observer {
//                        TV_Login_Password_Err.text = it
//                    })
                    binding.TVLoginPasswordErr.text = viewModel.live_send_string2.value
                } else {
                    btncheck2 = false
                    btnChange()
//                    viewModel.live_send_string2.observe(viewLifecycleOwner, Observer {
//                        TV_Login_Password_Err.text = it
//                    })
                    binding.TVLoginPasswordErr.text = viewModel.live_send_string2.value
                }
                binding.invalidateAll()
            }
        }

        btnChange()
    }

    fun setting() {

        binding.BTNLoginLogin.setOnClickListener {
            UserTask(mContext).Login(
                binding.EDTLoginId.text.toString(),
                binding.EDTLoginPassword.text.toString(),
                response, message, json
            )
        }

        binding.BTNLoginLoginFindToId.setOnClickListener {
            navController.navigate(R.id.action_navigation_login_to_navigation_findtoid)
        }
        binding.BTNLoginLoginFindToPw.setOnClickListener {
            navController.navigate(R.id.action_navigation_login_to_navigation_findtopw)
        }
        binding.BTNLoginLoginSignup.setOnClickListener {
            navController.navigate(R.id.action_navigation_login_to_navigation_signup)
            dataInfo.infoSetInt("sns_type", 1)
            Config.getInstance().dataInfo = dataInfo
        }
        binding.BTNLoginNaver.setOnClickListener {
            naver()
        }
        binding.BTNLoginKakao.setOnClickListener {
            kakao()
        }
        binding.BTNLoginGoogle.setOnClickListener {
            google()
        }

    }

    fun btnChange() {
        if (btncheck && btncheck2) {
            binding.BTNLoginLogin.isEnabled = true
            binding.BTNLoginLogin.setBackgroundResource(R.drawable.shape_login_btn_sub)
        } else {
            binding.BTNLoginLogin.isEnabled = false
            binding.BTNLoginLogin.setBackgroundResource(R.drawable.shape_login_btn_error)
        }
    }
    // TODO: Google

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    fun google() {
        mAuth = FirebaseAuth.getInstance() // 인스턴스 초기화

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Name, email address, and profile photo Url
//            val email = user.email
            dataInfo.infoSet("sns_email", user.email.toString())
            dataInfo.infoSetInt("sns_type", 4)
            Config.getInstance().dataInfo = dataInfo
            UserTask(mContext).SNS_Login(
                user.email.toString(),
                4,
                response,
                message,
                json2
            )

        } else {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = mAuth?.currentUser
                updateUI(user)
                //                            googleloginOn = true;
            } else {
                // If sign in fails, display a message to the user.
                updateUI(null)
                //                            googleloginOn = false;
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            User("", user?.email.toString(), "", 0)

            dataInfo.infoSet("sns_email", user.email.toString())
            dataInfo.infoSetInt("sns_type", 4)
            Config.getInstance().dataInfo = dataInfo
            navController.navigate(R.id.action_navigation_login_to_navigation_signup)
        } else {
            Toast.makeText(mContext, resources.getString(R.string.error_login_failed), Toast.LENGTH_SHORT).show()
        }
    }

// TODO: Naver

    lateinit var mOAuthLoginInstance: OAuthLogin

    fun naver() {
        Log.e("네이버", "class실행")
//        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
        //초기화
        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.init(
            mContext,
            resources.getString(R.string.naver_client_id),
            resources.getString(R.string.naver_client_secret),
            resources.getString(R.string.naver_client_name)
        )

//        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.btnNaver);
//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        mOAuthLoginInstance.startOauthLoginActivity(requireActivity(), mOAuthLoginHandler)
    }

    private val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken = mOAuthLoginInstance.getAccessToken(mContext)
                val refreshToken = mOAuthLoginInstance.getRefreshToken(mContext)
                val expiresAt = mOAuthLoginInstance.getExpiresAt(mContext)
                val tokenType = mOAuthLoginInstance.getTokenType(mContext)

//              Toast.makeText(mContext, "success:" + accessToken, Toast.LENGTH_SHORT).show();
                Log.e("확인", "" + accessToken + "   /   " + refreshToken + "   /   " + expiresAt + "   /   " + tokenType)

                NaverUserTask(requireContext(), navController).login(accessToken)
            } else {
                val errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode()
                val errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext)
                Toast.makeText(
                    mContext,
                    "errorCode:" + errorCode + ", errorDesc:" + errorDesc,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    //TODO : KAKAO
    fun kakao() {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)

                Toast.makeText(mContext, "${error.toString()} / $token", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(TAG, "토큰 정보 보기 실패", error)
                    } else if (tokenInfo != null) {
                        Log.i(
                            TAG, "토큰 정보 보기 성공" +
                                    "\n회원번호: ${tokenInfo.id}" +
                                    "\n만료시간: ${tokenInfo.expiresIn} 초"
                        )

                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                                Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show()
                            } else if (user != null) {
                                Log.e(
                                    TAG, "사용자 정보 요청 성공" +
                                            "\n회원번호: ${user.id}" +
                                            "\n이메일: ${user.kakaoAccount?.email}" +
                                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                                )
                                if (user.kakaoAccount?.email != null) {
                                    UserTask(mContext).SNS_Login(
                                        user.kakaoAccount?.email!!.toString(),
                                        2,
                                        response,
                                        message,
                                        json2
                                    )

                                    dataInfo.infoSet("sns_email", user.kakaoAccount?.email.toString())
                                    dataInfo.infoSetInt("sns_type", 2)
                                    Config.getInstance().dataInfo = dataInfo
                                } else {

                                    dataInfo.infoSet("sns_email", resources.getString(R.string.login_kakao_no_email))
                                    dataInfo.infoSetInt("sns_type", 2)
                                    Config.getInstance().dataInfo = dataInfo
                                    navController.navigate(R.id.action_navigation_login_to_navigation_signup)
                                }
                            } else {
                                Toast.makeText(mContext, resources.getString(R.string.error_login_failed), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.loginWithKakaoTalk(mContext, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
        }
    }

    //    TODO: Retrofit
    val response = AsyncTaskResponse {
        Log.e(TAG, "AsyncTaskResponse :$it")
        result = it
    }
    val message = ResponseMessage {
        Log.e(TAG, "message :$it")
    }
    val json = ResponseObjData {
        Log.e(TAG, "Json :$it")

        if (result.equals("success")) {

            User(
                it["nick_name"].asString,
                it["email"].asString,
                it["password"].asString,
                it["type"].asInt
            )

            navController.navigate(R.id.action_navigation_login_to_navigation_home)

            binding.CBLoginAutoCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    editor.putBoolean("AutoCheck", true)
                    editor.putString("AutoEmail", it["email"].asString)
                    editor.putString("AutoPassword", it["password"].asString)
                    editor.apply()
                } else {
                    editor.putBoolean("AutoCheck", false)
                    editor.remove("AutoEmail")
                    editor.remove("AutoPassword")
                    editor.apply()
                }
            }
        }
    }
    val json2 = ResponseObjData {
        Log.e(TAG, "Json :$it / resuilt : $result")

        if (result.equals("success")) {
            UserTask(mContext).Login(
                it["email"].asString,
                it["password"].asString,
                response, message, json
            )
        } else {
            navController.navigate(R.id.action_navigation_login_to_navigation_signup)
        }
    }
}
//ToDo : 1:Email, 2:Kakao, 3:Naver, 4:Google