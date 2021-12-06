<?php
    $nick_name = json_decode(file_get_contents("php://input"))->{"nick_name"};
    $email = json_decode(file_get_contents("php://input"))->{"email"};
    $password = json_decode(file_get_contents("php://input"))->{"password"};

    $con = mysqli_connect("localhost", "root", "", "portfolio");
    mysqli_query($con,'SET NAMES utf8');
    
    $statement = mysqli_prepare($con, "UPDATE user_info SET nick_name = ?, password = ? WHERE email = ?");
    mysqli_stmt_bind_param($statement, "sss", $nick_name, $password, $email);
    mysqli_stmt_execute($statement);
 
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $nick_name, $email, $password, $type);

    $error = mysqli_error($con);

    $response = array();
    $data = array();
    $response["result"] = "failed";
    $response["message"] = $error;
    $response["data"] = $data;

    while(mysqli_stmt_fetch($statement)) {
        $response["result"] = "success";
        $response["message"] = $error;
    
        $data["email"] = $email;
        $data["password"] = $password;
        $data["nick_name"] = $nick_name;      
        $data["type"] = $type;

        $response["data"] = $data;
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>