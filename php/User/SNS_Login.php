<?php
    $email = json_decode(file_get_contents("php://input"))->{"email"};
    $type = json_decode(file_get_contents("php://input"))->{"type"};

    $con = mysqli_connect("localhost", "root", "", "portfolio");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "SELECT * FROM user_info WHERE email = ? AND type = ?");
    mysqli_stmt_bind_param($statement, "si", $email,$type);
    mysqli_stmt_execute($statement);
 
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $nick_name, $email, $password, $type);
 

    $error = mysqli_error($con);
    $errno = mysqli_errno($con);
    
    $response = array();
    $data = array();

    $response["result"] = "failed";
    $response["message"] = $error;
    $data["result"] = "";
    $response["data"] = $data;

    while(mysqli_stmt_fetch($statement)) {
        $response["result"] = "success";
        $response["message"] = $error;
    
        $data['email'] = $email;
        $data['password'] = $password;

        $response["data"] = $data;
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>