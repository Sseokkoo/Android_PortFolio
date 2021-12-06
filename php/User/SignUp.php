<?php 
    $con = mysqli_connect("localhost", "root", "", "portfolio");
    mysqli_query($con,'SET NAMES utf8');
 
    $nick_name = json_decode(file_get_contents("php://input"))->{"nick_name"};
    $email = json_decode(file_get_contents("php://input"))->{"email"};
    $password = json_decode(file_get_contents("php://input"))->{"password"};
    $type = json_decode(file_get_contents("php://input"))->{"type"};
 
    $statement = mysqli_prepare($con, "INSERT INTO user_info VALUES (?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssi", $nick_name, $email, $password, $type);
    mysqli_stmt_execute($statement);

    $response = array();
    $error = mysqli_error($con);
    $errno = mysqli_errno($con);
 
    if($error == null){
        // mysqli_stmt_store_result($statement)
        $response["result"] = "success";
        $response["message"] = $error;
        $response["data"] = "";
    }else{
        $response["result"] = "failed";
        $response["message"] = $error;
        $response["data"] = "";
    }
 
   
    echo json_encode($response);
?>