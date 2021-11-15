<?php 
    $con = mysqli_connect("localhost", "root", "0502", "test");
    mysqli_query($con,'SET NAMES utf8');
 
    $nick_name = $_POST["nick_name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $type = $_POST["type"];
 
    $statement = mysqli_prepare($con, "INSERT INTO user_info VALUES (?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssi", $nick_name, $email, $password, $type);
    mysqli_stmt_execute($statement);

    $response = array();
    $error = mysqli_connect_error();
    $errno = mysqli_connect_errno();
 
    if(mysqli_stmt_store_result($statement)){
        $response["result"] = "success";
        $response["message"] = $error;
    }else{
        $response["result"] = "failed";
        $response["message"] = $error;
    }
 
   
    echo json_encode($response);
?>