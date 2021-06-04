function deleteUser() {
    return (confirm('Czy na pewno chcesz usunąć użytkownika?'))
}

function deleteTask() {
    return (confirm('Czy na pewno chcesz usunąć zadanie z listy? \nJest to operacja nieodwracalna!'))
}

// $(document).ready(function() {
//
//     var data = {};
//     $("#assignedUser option").each(function(i,el) {
//         data[$(el).data("value")] = $(el).val();
//     });
//     console.log(data, $("#assignedUser option").val());
//
//     $('#submit').click(function()
//     {
//         var value = $('#selected').val();
//         alert($('#assignedUser [value="' + value + '"]').data('value'));
//     });
// });

function checkPasswordMatch() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        document.getElementById("passwordCheck").innerHTML = "Podane hasła muszą być identyczne!";
    }
    else {
        document.getElementById("passwordCheck").innerHTML = "";
    }
}
