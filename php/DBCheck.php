<?php
    $sql_id = $_POST["sql_id"]
    $sql_pw = $_POST["sql_pw"]
    $sql_db = $_POST["sql_db"]

    $con = mysqli_connect("localhost", $sql_id, $sql_pw, $sql_db);
    mysqli_query($con,'SET NAMES utf8');
    
    $response = array();

    if (!$con){
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno();
        $response["result"] = "failed";
        $response["message"] = $error    
    }else{
        $response["result"] = "success";
        $response["message"] = $error
    }
 
    echo json_encode($response);
    mysqli_close($con);
?>