$(function() {
    // enable/disable Programs selector
    $("#category").change(function () {
        let category = $(this).val();
        if (category === 'BONUS') {
            $("#programs").attr("disabled", true);
            $('.selectpicker').selectpicker('refresh');
        } else {
            $("#programs").attr("disabled", false);
            $('.selectpicker').selectpicker('refresh');
        }
    });
    // update ordering for each item before form submit
    $("form").submit(function (event) {
        $(".sortable").find(".database-ordering").each(function (index) {
            $(this).val(index);
        });
    });
});
