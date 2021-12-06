<?php
// $plainText = '\/Tfg8dfGr7sWVjyqTXuJv5qOGvfFjiihBsRKY0l\/L7M=';
$plainText = 'kangsukjoo1@gmail.com/vyhfqykbwapmkaiv';
// vyhfqykbwapmkaiv
// lenfvsxtmbnhtvsw

$key = 'portfolio-aes256';



// 256 bit 키를 만들기 위해서 비밀번호를 해시해서 첫 32바이트를 사용합니다.

// $key = substr(hash('sha256', $key, true), 0, 32);

// echo "비밀번호 바이너리:" . $password . "<br/>";



// Initial Vector(IV)는 128 bit(16 byte)입니다.

// $iv = chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0) . chr(0x0);



// 암호화

// $encrypted = base64_encode(openssl_encrypt($plainText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, str_repeat(chr(0x00),16)));
$encrypted = base64_encode(openssl_encrypt($plainText, 'aes-128-cbc', $key, true, str_repeat(chr(0),16)));

// 복호화

// $decrypted = openssl_decrypt(base64_decode($encrypted), 'aes-256-cbc', $password, OPENSSL_RAW_DATA, $iv);

// $decrypted = openssl_decrypt(base64_decode($plainText), 'aes-256-cbc', $password, OPENSSL_RAW_DATA, $iv);




// echo 'plainText : ' . $plainText . "<br/>";
$response = array();
    $response["result"] = "success";
    $response["message"] = "";
    $response["data"] = $encrypted;
echo json_encode($response);

// echo '암호화 : ' . $encrypted . "<br/>";
// echo '복호화 : ' . $decrypted . "<br/>";
?>