<?php
    $nick_name = $_POST["nick_name"]
    $email = $_POST["email"];
    $password = $_POST["password"];

    $con = mysqli_connect("localhost", "root", "0502", "portfolio");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "UPDATE user_info SET nick_name = ?, password = ? WHERE email = ?");
    mysqli_stmt_bind_param($statement, "sss", $nick_name, $password, $email);
    mysqli_stmt_execute($statement);
 
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $nick_name, $email, $password, $type);
 
    $error = mysqli_error($con);

    $response = array();
    $response["result"] = "failed";
    $response["message"] = $error;

    while(mysqli_stmt_fetch($statement)) {
        $response["result"] = "success";
        $response["email"] = $email;
        $response["password"] = $password;
        $response["nick_name"] = $nick_name;      
        $response["type"] = $type;
        $response["message"] = $error;
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>