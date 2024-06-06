$(document).ready(function() {
    $('#dataForm').on('submit', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작 방지

        // 입력된 데이터 가져오기
        var sender = $('#sender').val();
        var receiver = $('#receiver').val();

        // 폼 데이터 형식으로 데이터 생성
        var requestData = {
            sender: sender,
            receiver: receiver,
        };

        // 서버로 POST 요청 보내기
        $.ajax({
            type: 'POST',
            url: '/SecurityProject/decryptedMsg', // 요청할 경로
            contentType: 'application/x-www-form-urlencoded', // 요청 데이터 형식
            data: $.param(requestData), // 요청 데이터
            success: function(response) {
                // 서버에서 받은 데이터를 JavaScript 변수에 할당하여 사용
                var message = response.message;
                var signature = response.signature;

                // 결과를 HTML에 출력
                $('#msg').html("<정산 요청 결과>" + "<br>" + message);
                $('#signature').html("<서명 결과>" + "<br>" + signature);
            },
            error: function(xhr, status, error) {
                $('#result').text('요청 실패: ' + error); // 오류 메시지를 result div에 출력
            }
        });
    });
});


function goToMainPage() {
    window.location.href = "main.html";
}