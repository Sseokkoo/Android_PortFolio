<?php
    $email = $_POST["email"];

    $con = mysqli_connect("localhost", "root", "0502", "test");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "DELETE * FROM user_info WHERE email = ?");
    mysqli_stmt_bind_param($statement, "s", $email);
    mysqli_stmt_execute($statement);
 
    $error = mysqli_connect_error();
    $errno = mysqli_connect_errno();

    $response = array();
    $response["result"] = "failed";
    $response["message"] = $error;

    while(mysqli_stmt_fetch($statement)) {
        $response["result"] = "success";
        $response["message"] = $error;
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>