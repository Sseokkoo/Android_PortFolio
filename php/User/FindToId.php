<?php
    // $nick_name = $_POST["nick_name"]??'';
    $nick_name = json_decode(file_get_contents("php://input"))->{"nick_name"};

    $con = mysqli_connect("localhost", "root", "", "portfolio");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "SELECT * FROM user_info WHERE nick_name = ?");
    mysqli_stmt_bind_param($statement, "s", $nick_name);
    mysqli_stmt_execute($statement);
 
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $nick_name , $email , $password, $type);

    $error = mysqli_error($con);
    
    $response = array();
    // $data = array();
    $response["result"] = "failed";
    $response["message"] = $error;
    $response["data"] = "";

    while(mysqli_stmt_fetch($statement)) {
        $response["result"] = "success";
        $response["message"] = $error;
        // $data["nick_name"] = $nick_name;
        // $data["email"] = $email000;
        // $data["password"] = $password;
        // $data["type"] = $type;

        $response["data"] = $email;
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>