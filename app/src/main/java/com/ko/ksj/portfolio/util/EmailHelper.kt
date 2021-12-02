package com.ko.ksj.portfolio.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ko.ksj.portfolio.interfaces.AsyncTaskResponse
import com.ko.ksj.portfolio.interfaces.ResponseStrData
import com.ko.ksj.portfolio.repository.task.UserTask
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailHelper(
    email_title: String,      // 메일 제목
    email_body: String,       // 메일 내용
    send_add: String,       // 받는 메일 주소
) {
//    val fileName = ""   // 첨부파일 이름
//    val filePath = ""    // 첨부파일 경로
    var username = ""
    var password = ""
    var result = ""

    fun sendEmail(mContext: Context) {
        UserTask(mContext).AES(response, json)
    }

    val response = AsyncTaskResponse {
        Log.e("EmailHelper", "AsyncTaskPesponse : $it")
        result = it
    }
    val json = ResponseStrData {
        Log.e("EmailHelper", "Json : $it")
        if (result.equals("success")) {
            val decode = AESHelper.AES_Decode(it)
            try {
                val string = decode.split("/")
                username = string[0].trim()
                password = string[1].trim()
                Log.e("이메일" ,"$email_title / $email_body / $send_add / $username / $password")
                val props = Properties()
                props.setProperty("mail.transport.protocol", "smtp")
//                props.setProperty("mail.host", "smtp.gmail.com")
                props.put("mail.smtp.ssl.enable", "true")
                props.put("mail.smtp.socketFactory.port", "465")
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")

                props.put("mail.smtp.auth", "true")
//                props.put("mail.smtp.starttls.enable", "true")
                props.put("mail.smtp.socketFactory.fallback", "false")
                props.put("mail.smtp.host", "smtp.gmail.com")
                props.put("mail.smtp.debug", "true")
                props.put("mail.smtp.port", "465")

                props.setProperty("mail.smtp.quitwait", "false")

                val session = Session.getInstance(props,
                    object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(username, password)
                        }
                    })


                // 메시지 객체 만들기

                val message = MimeMessage(session)
                message.setFrom(InternetAddress(username))

                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(send_add)
                )
                message.subject = email_title
                message.setText(email_body)

//                message.setHeader("content-Type","text/html")
//
//            val multipart = MimeMultipart()
//            val messageBodyPart = MimeBodyPart()
//            val source = FileDataSource(filePath)
//
//            messageBodyPart.dataHandler = DataHandler(source)
//            messageBodyPart.fileName = fileName
//            multipart.addBodyPart(messageBodyPart)

                // 메시지에 파일 담고
//            message.setContent(multipart)

                // 전송
                Transport.send(message)
            }catch (e: Exception){
                e.stackTrace
            }
        }
    }
}