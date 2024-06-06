$(document).ready(function() {
    $('#dataForm').on('submit', function(event) {
        event.preventDefault();

        var sender = $('#sender').val();
        var receiver = $('#receiver').val();
        var message = $('#message').val();

        // 폼 데이터 형식으로 데이터 생성
        var requestData = {
            sender: sender,
            receiver: receiver,
            message: message
        };

        $.ajax({
            type: 'POST',
            url: '/SecurityProject/encryptedMsg', // 요청할 경로
            contentType: 'application/x-www-form-urlencoded', // 요청 데이터 형식
            data: $.param(requestData), // 폼 데이터로 직렬화
            success: function(response) {
                $('#result').text(response);
            },
            error: function(xhr, status, error) {
                $('#result').text('키 생성 중 오류가 발생하였습니다.');
            }
        });
    });
});

function goToMainPage() {
    window.location.href = "main.html";
}
