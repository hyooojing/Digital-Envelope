$(document).ready(function() {
    $('#confirmationForm').on('submit', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작 방지

        var userId = $('#userId').val();

        $.ajax({
            type: 'POST',
            url: '/SecurityProject/generateKey',
            data: { userId: userId }, // JSON.stringify() 없이 데이터를 폼 데이터 형식으로 전송
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
