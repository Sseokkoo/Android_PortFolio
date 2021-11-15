<?php
    $email = $_POST["email"];
    $password = $_POST["password"];

    $con = mysqli_connect("localhost", "root", "0502", "test");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "SELECT * FROM user_info WHERE email = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $email, $password);
    mysqli_stmt_execute($statement);
 
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $email, $password, $nick_name, $type);
 

    $error = mysqli_connect_error();
    $errno = mysqli_connect_errno();

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