package com.ko.ksj.portfolio.View.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.google.firebase.auth.UserInfo
import com.google.gson.Gson
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ko.ksj.portfolio.Interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.Interfaces.ResponseMessage
import com.ko.ksj.portfolio.Interfaces.ResponseObjData
import com.ko.ksj.portfolio.R
import com.ko.ksj.portfolio.Repository.Task.UserTask
import com.ko.ksj.portfolio.ViewModel.LoginViewModel
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.ko.ksj.portfolio.Repository.Task.NaverUserTask

class LoginFrag : Fragment() {
    val TAG = "LoginFrag"

    private lateinit var mContext: Context
    lateinit var navController: NavController

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private var authlevel: Int = 0
    val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login_login, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        navController = Navigation.findNavController(view)

        pref = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        editor = pref.edit()

//        BT_Login_Login_LoginBTN.isEnabled = false

        setting()
        errorcheck()
    }

    fun setting() {
        BT_Login_Login_LoginBTN.setOnClickListener {
            UserTask(mContext).login(
                EDT_Login_Login_Id.text.toString(),
                EDT_Login_Login_Pw.text.toString(),
                viewModel.infoGet("deviceType"),
                Config.getInstance().token,
                viewModel.infoGet("appVersion"),
                viewModel.infoGet("deviceModel"),
                viewModel.infoGet("deviceSerial"),
                viewModel.infoGet("osVersion"),
                response, message, json)
        }
        if (!Config.getInstance().langCode.equals("KR")) {
            TV_Login_Login_FindToId.text = ""
            TV_Login_Login_FindToId.isEnabled = false
            IV_Login_Login_Kakao.visibility = View.GONE
            IV_Login_Login_Kakao.isEnabled = false
            IV_Login_Login_Naver.visibility = View.GONE
            IV_Login_Login_Naver.isEnabled = false
        }else{
            TV_Login_Login_FindToId.text = resources.getString(R.string.cmmn_findid)
            TV_Login_Login_FindToId.isEnabled = true
            IV_Login_Login_Kakao.visibility = View.VISIBLE
            IV_Login_Login_Kakao.isEnabled = true
            IV_Login_Login_Naver.visibility = View.VISIBLE
            IV_Login_Login_Naver.isEnabled = true
        }
        TV_Login_Login_FindToId.setOnClickListener {
            navController.navigate(R.id.action_navigation_login_to_navigation_auth)
            LoginToId = true
        }
        TV_Login_Login_FindToPw.setOnClickListener {
            navController.navigate(R.id.action_navigation_login_to_navigation_auth)
            LoginToPw = true
        }

        IV_Login_Login_Naver.setOnClickListener {
            naver()
        }
        IV_Login_Login_Kakao.setOnClickListener {
            kakao()
        }
        IV_Login_Login_Google.setOnClickListener {
            google()
        }


    }

    fun errorcheck() {
        EDT_Login_Login_Id.addTextChangedListener {
            if (EDT_Login_Login_Id.text.length > 0) {
                TV_Login_Login_EnableId.setText(resources.getString(R.string.info_id_email))
                TV_Login_Login_EnableId.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_MediumGray))
                View_Login_Login_Id_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_DarkGray))
                IV_Login_Login_EmailError.setImageResource(0)
                btnChange()
            } else {
                if (EDT_Login_Login_Id.text.isEmpty())
                    TV_Login_Login_EnableId.setText("")
            }
        }
        EDT_Login_Login_Pw.addTextChangedListener {
            if (EDT_Login_Login_Pw.text.length > 0) {
                TV_Login_Login_EnablePw.setText(resources.getString(R.string.cmmn_pw))
                TV_Login_Login_EnablePw.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_MediumGray))
                View_Login_Login_Pw_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_DarkGray))
                IV_Login_Login_PwError.setImageResource(0)
                btnChange()
            } else {
                if (EDT_Login_Login_Pw.text.isEmpty())
                    TV_Login_Login_EnablePw.setText("")
            }
        }
    }

    fun btnChange() {
        if (true) { // EDT_Login_Login_Id.text.length > 2 && EDT_Login_Login_Pw.text.length > 7
            BT_Login_Login_LoginBTN.isEnabled = true
            BT_Login_Login_LoginBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_MediumGreen))
            BT_Login_Login_LoginBTN.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        } else {
            BT_Login_Login_LoginBTN.isEnabled = false
            BT_Login_Login_LoginBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_WhiteTwo))
            BT_Login_Login_LoginBTN.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_LightGray))
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
            val email = user.email
            EDT_Login_Login_Id.setText(email)
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
            EDT_Login_Login_Id.setText(user!!.email)
        }
    }

// TODO: Naver

    var OAUTH_CLIENT_ID = "JV9TxKcClc3tDB2eOCHm"
    var OAUTH_CLIENT_SECRET = "dn0_WXAPqd"
    var OAUTH_CLIENT_NAME = "포트폴리오"

    lateinit var mOAuthLoginInstance: OAuthLogin

    fun naver() {
        Log.e("네이버", "class실행")
//        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
        //초기화
        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.init(
            mContext,
            OAUTH_CLIENT_ID,
            OAUTH_CLIENT_SECRET,
            OAUTH_CLIENT_NAME
        )

//        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.btnNaver);
//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        mOAuthLoginInstance.startOauthLoginActivity(requireActivity(), mOAuthLoginHandler);
    }

    private val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken = mOAuthLoginInstance.getAccessToken(mContext)
                val refreshToken = mOAuthLoginInstance.getRefreshToken(mContext)
                val expiresAt = mOAuthLoginInstance.getExpiresAt(mContext)
                val tokenType = mOAuthLoginInstance.getTokenType(mContext)

//              Toast.makeText(mContext, "success:" + accessToken, Toast.LENGTH_SHORT).show();
                Log.e(
                    "확인",
                    "" + accessToken + "   /   " + refreshToken + "   /   " + expiresAt + "   /   " + tokenType
                )

                NaverUserTask(requireContext()).login(accessToken, true, navController)

                NaverUserTask(requireContext()).resultEmail(EDT_Login_Login_Id)
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
                            } else if (user != null) {
                                Log.i(
                                    TAG, "사용자 정보 요청 성공" +
                                            "\n회원번호: ${user.id}" +
                                            "\n이메일: ${user.kakaoAccount?.email}" +
                                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                                )
                                if(user.kakaoAccount?.email != null){
                                    EDT_Login_Login_Id.setText(user.kakaoAccount?.email)
                                }else{
                                    Toast.makeText(mContext, resources.getString(R.string.cmmn_kakao_email_none), Toast.LENGTH_SHORT).show()
                                }

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
        if (it.equals("0")) {
        }
    }
    val message = ResponseMessage {
        Log.e(TAG, "message :$it")

        if (!it.equals("")) {
            TV_Login_LoginError.setText(it.toString())
            TV_Login_Login_EnableId.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            TV_Login_Login_EnablePw.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Id_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Pw_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            IV_Login_Login_EmailError.setImageResource(R.drawable.source_login_error)
            IV_Login_Login_PwError.setImageResource(R.drawable.source_login_error)
            BT_Login_Login_LoginBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_WhiteTwo))
            BT_Login_Login_LoginBTN.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_LightGray))

        }
    }
    val json = ResponseObjData {
        Log.e(TAG, "Json :$it authLevel = $authlevel , ${it!!["authLevel"].asInt}")

        Config.getInstance().userInfo = Gson().fromJson(it, UserInfo::class.java)
        Config.getInstance().farmInfo = Gson().fromJson(it, FarmInfo::class.java)

        authlevel = it!!.get("authLevel").asInt

        if (authlevel == 9) {
            TV_Login_LoginError.setText(resources.getString(R.string.err_verify_email))
            TV_Login_Login_EnableId.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            TV_Login_Login_EnablePw.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Id_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Pw_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            IV_Login_Login_EmailError.setImageResource(R.drawable.source_login_error)
            IV_Login_Login_PwError.setImageResource(R.drawable.source_login_error)
            BT_Login_Login_LoginBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_WhiteTwo))
            BT_Login_Login_LoginBTN.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_LightGray))
        }
        if (authlevel == 2) {
            TV_Login_LoginError.setText(resources.getString(R.string.TV_Login_error4))
            TV_Login_Login_EnableId.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            TV_Login_Login_EnablePw.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Id_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            View_Login_Login_Pw_Line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_error))
            IV_Login_Login_EmailError.setImageResource(R.drawable.source_login_error)
            IV_Login_Login_PwError.setImageResource(R.drawable.source_login_error)
            BT_Login_Login_LoginBTN.setBackgroundColor(ContextCompat.getColor(mContext, R.color.zeplin_WhiteTwo))
            BT_Login_Login_LoginBTN.setTextColor(ContextCompat.getColor(mContext, R.color.zeplin_LightGray))
        }
        if (authlevel == 1) {
            val dataInfo = DataInfo()
            dataInfo.infoSet("email",it.get("email").asString)
            dataInfo.infoSet("userName",it.get("userName").asString)
            dataInfo.infoSet("userKind",it.get("userKind").asString)
            dataInfo.infoSetInt("farmSeq",it.get("farmSeq").asInt)
            dataInfo.infoSet("farmName",it.get("farmName").asString)
            dataInfo.infoSet("appVersion",it.get("appVersion").asString)
            dataInfo.infoSet("profileUrl",it.get("profileUrl").asString)

            Config.getInstance().dataInfo = dataInfo

            editor.putString("Auto_email", EDT_Login_Login_Id.text.toString())
            editor.putString("Auto_password", EDT_Login_Login_Pw.text.toString())
            editor.putBoolean("autoLogin", true)
            editor.apply()

            navController.navigate(R.id.action_navigation_login_to_navigation_home)
            activity?.finish()
        }
    }
}